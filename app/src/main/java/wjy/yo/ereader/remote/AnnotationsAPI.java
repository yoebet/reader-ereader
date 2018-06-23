package wjy.yo.ereader.remote;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Path;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;

public interface AnnotationsAPI {

    @GET("annotation_families/{id}/detail")
    Maybe<AnnotationFamily> getAnnotations(@Path("id") String id);
}
