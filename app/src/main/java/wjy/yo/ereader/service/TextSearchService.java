package wjy.yo.ereader.service;

import io.reactivex.Single;
import wjy.yo.ereader.vo.TextSearchResult;

public interface TextSearchService {

    Single<TextSearchResult> search(String word);
}
