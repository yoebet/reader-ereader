package wjy.yo.ereader.serviceimpl;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.TextSearchService;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.TextSearchResult.ResultItem;

import static wjy.yo.ereader.util.Constants.FTS_TABLE_PARA_CONTENT;

@Singleton
public class TextSearchServiceImpl implements TextSearchService {

    @Inject
    BookAPI bookAPI;

    @Inject
    LocalSettingService settingService;

    private DB db;
//    private ParaDao paraDao;

    @Inject
    TextSearchServiceImpl(DB db) {
        this.db = db;
//        this.paraDao = db.paraDao();
    }


    private Single<TextSearchResult> searchLocally(String word, int limit) {

        TextSearchResult searchResult = new TextSearchResult();
        searchResult.setKeyword(word);
        searchResult.setResultFrom(TextSearchResult.RESULT_FROM_FTS);
        searchResult.setSearchField(TextSearchResult.SEARCH_FIELD_PARA_CONTENT);

        final List<ResultItem> resultItems = new ArrayList<>();
        searchResult.setResultItems(resultItems);

        return Single.create(emitter -> {

            SupportSQLiteOpenHelper openHelper = db.getOpenHelper();
            SupportSQLiteDatabase database = openHelper.getReadableDatabase();

            Cursor cursor = database.query(
                    "SELECT content, paraId, chapId, bookId FROM " + FTS_TABLE_PARA_CONTENT +
                            " WHERE " + FTS_TABLE_PARA_CONTENT + " MATCH ? LIMIT " + limit,
                    new Object[]{word});

            if (cursor == null) {
                emitter.onSuccess(searchResult);
                return;
            }

            try {
                while (cursor.moveToNext()) {
                    String content = cursor.getString(0);
                    String paraId = cursor.getString(1);
                    String chapId = cursor.getString(2);
                    String bookId = cursor.getString(3);

                    Para para = new Para();
                    para.setId(paraId);
                    para.setChapId(chapId);
                    para.setBookId(bookId);
                    para.setContent(content);

                    ResultItem item = new ResultItem();
                    item.setPara(para);
                    item.setParaLoaded(false);
                    resultItems.add(item);
                }
            } finally {
                cursor.close();
            }

            emitter.onSuccess(searchResult);
        });

    }

    private Single<TextSearchResult> searchFromServer(String word, int limit) {
        return bookAPI.textSearch(word, limit)
                .map((List<Para> paras) -> {
                    TextSearchResult searchResult = new TextSearchResult();
                    searchResult.setKeyword(word);
                    searchResult.setResultFrom(TextSearchResult.RESULT_FROM_SERVER);
                    searchResult.setSearchField(TextSearchResult.SEARCH_FIELD_PARA_CONTENT);
                    List<ResultItem> resultItems = new ArrayList<>();
                    searchResult.setResultItems(resultItems);

                    for (Para para : paras) {
                        ResultItem item = new ResultItem();
                        item.setPara(para);
                        item.setParaLoaded(true);
                        resultItems.add(item);
                    }

                    return searchResult;
                });
    }

    @Override
    public Single<TextSearchResult> search(String word) {
        int limit = 8;
        return searchLocally(word, limit);
    }
}
