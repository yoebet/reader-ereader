package wjy.yo.ereader.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjy.yo.ereader.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DictEntryFragment extends Fragment {

    public DictEntryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dict_entry, container, false);
    }
}
