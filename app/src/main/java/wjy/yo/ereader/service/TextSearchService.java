package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Single;
import wjy.yo.ereader.entity.book.Para;

public interface TextSearchService {

    Single<List<Para>> search(String word);
}
