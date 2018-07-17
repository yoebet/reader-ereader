package wjy.yo.ereader.serviceimpl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.TextSearchService;

@Singleton
public class TextSearchServiceImpl implements TextSearchService {

    @Inject
    BookAPI bookAPI;

    private ParaDao paraDao;

    @Inject
    public TextSearchServiceImpl(DB db) {
        this.paraDao = db.paraDao();
    }


    private Single<List<Para>> searchLocally(String word) {

        return null;
    }

    @Override
    public Single<List<Para>> search(String word) {


        Single<List<Para>> netSource = bookAPI.textSearch(word, 5);
        return netSource;
    }
}
