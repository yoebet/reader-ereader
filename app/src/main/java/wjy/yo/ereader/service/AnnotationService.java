package wjy.yo.ereader.service;

import io.reactivex.Maybe;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;

public interface AnnotationService {

    Maybe<AnnotationFamily> loadAnnotations(String id);
}
