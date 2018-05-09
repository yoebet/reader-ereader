package wjy.yo.ereader.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import java.util.List;

import wjy.yo.ereader.livedata.JsonLiveData;

public class JsonViewModel extends AndroidViewModel {
    private final JsonLiveData data;

    public JsonViewModel(Application application) {
        super(application);
        data = new JsonLiveData(application);
    }

    public LiveData<List<String>> getData() {
        return data;
    }
}
