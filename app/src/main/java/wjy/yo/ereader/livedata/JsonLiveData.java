package wjy.yo.ereader.livedata;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonLiveData extends LiveData<List<String>> {
    private final Context context;
    private final FileObserver fileObserver;


    public JsonLiveData(Context context) {
        this.context = context;
        String path = new File(context.getFilesDir(),
                "downloaded.json").getPath();
        fileObserver = new FileObserver(path) {
            @Override
            public void onEvent(int event, String path) {
                // The file has changed, so let’s reload the data
                loadData();
            }
        };
        loadData();
    }

    @Override
    protected void onActive() {
        fileObserver.startWatching();
    }

    @Override
    protected void onInactive() {
        fileObserver.stopWatching();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                File jsonFile = new File(context.getFilesDir(),
                        "downloaded.json");
                List<String> data = new ArrayList<>();
                // Parse the JSON using the library of your choice
                return data;
            }

            @Override
            protected void onPostExecute(List<String> data) {
                setValue(data);
            }
        }.execute();
    }
}
