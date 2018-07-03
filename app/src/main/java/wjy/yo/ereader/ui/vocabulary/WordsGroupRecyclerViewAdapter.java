package wjy.yo.ereader.ui.vocabulary;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.VocabularyGroupedBinding;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.common.ImmutableFlowLayout;
import wjy.yo.ereader.vo.GroupedUserWords;

public class WordsGroupRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<GroupedUserWords, VocabularyGroupedBinding> {

    private Context context;

    @Override
    protected void doOnCreateViewHolder(VocabularyGroupedBinding binding) {
        binding.getRoot().setOnClickListener((View view) -> {
            GroupedUserWords grouped = (GroupedUserWords) view.getTag();
            if (grouped == null) {
                return;
            }
            //
        });
    }

    WordsGroupRecyclerViewAdapter(Context context) {
        super(R.layout.vocabulary_grouped, VocabularyGroupedBinding::setGrouped);
        this.context = context;
    }

    private View.OnClickListener wordClickListener = (View v) -> {
        System.out.println(v.getTag());
//        if (v instanceof TextView) {
//            TextView tv = (TextView) v;
//            System.out.println(tv.getText());
//        }
    };

    protected void doOnBindViewHolder(VocabularyGroupedBinding binding) {
        GroupedUserWords groupedUserWords = binding.getGrouped();
        ImmutableFlowLayout flowLayout = binding.wordsFlow;
        flowLayout.removeAllViews();


        for (UserWord userWord : groupedUserWords.getUserWords()) {
            TextView textView = buildWordText(userWord);
            flowLayout.addView(textView);
            textView.setOnClickListener(wordClickListener);
        }
    }

    private TextView buildWordText(UserWord userWord) {
        TextView tv = new TextView(context);
        tv.setText(userWord.getWord());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setPadding((int) dpToPx(8), (int) dpToPx(4), (int) dpToPx(8), (int) dpToPx(4));
        tv.setBackgroundResource(R.drawable.label_bg);
        tv.setTag(userWord);
        return tv;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
