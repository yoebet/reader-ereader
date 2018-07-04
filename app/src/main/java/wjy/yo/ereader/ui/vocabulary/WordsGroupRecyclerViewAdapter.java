package wjy.yo.ereader.ui.vocabulary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.VocabularyGroupedBinding;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.common.ImmutableFlowLayout;
import wjy.yo.ereader.vo.GroupedUserWords;
import wjy.yo.ereader.vo.GroupedUserWords.Group;

public class WordsGroupRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<GroupedUserWords, VocabularyGroupedBinding> {

    private Context context;

    private Map<Group, Boolean> wordsCollapsedMap = new HashMap<>();
    private Map<Group, Integer> wordsHeightMap = new HashMap<>();

    WordsGroupRecyclerViewAdapter(Context context) {
        super(R.layout.vocabulary_grouped, VocabularyGroupedBinding::setGrouped);
        this.context = context;
    }


    private TextView currentWordText;

    private View.OnClickListener wordClickListener = (View v) -> {
        if (!(v instanceof TextView)) {
            return;
        }
        TextView tv = (TextView) v;
        UserWord uw = (UserWord) v.getTag();
        String word = uw.getWord();
        System.out.println(word);

        String currentWord = null;
        if (currentWordText != null) {
            UserWord currentUserWord = (UserWord) currentWordText.getTag();
            currentWord = currentUserWord.getWord();
        }

        if (word.equals(currentWord)) {
//            tv.setBackgroundResource(R.drawable.unselected_tag);
            tv.setTextColor(Color.parseColor("#3F51B5"));
            currentWordText = null;
        } else {
            tv.setTextColor(Color.GREEN);
//            tv.setBackgroundResource(R.drawable.selected_tag);
            if (currentWordText != null) {
                currentWordText.setTextColor(Color.parseColor("#3F51B5"));
            }
            currentWordText = tv;
        }
    };

    public boolean areItemsTheSame(GroupedUserWords oo, GroupedUserWords no) {
        if (oo == null || no == null) {
            return oo == no;
        }
        return Objects.equals(oo.getGroup(), no.getGroup());
    }

    private void bindGroupHeaderListener(VocabularyGroupedBinding binding) {

        binding.groupHeader.setOnClickListener((View view) -> {

            ImmutableFlowLayout wordsFlow = binding.wordsFlow;
            GroupedUserWords groupedWords = binding.getGrouped();
            Group group = groupedWords.getGroup();
            Integer height = wordsHeightMap.get(group);
            if (height == null) {
                height = wordsFlow.getMeasuredHeight();
                if (height > 0) {
                    wordsHeightMap.put(group, height);
                }
            }

            Boolean wordsCollapsed = wordsCollapsedMap.get(group);
            if (wordsCollapsed == null) {
                wordsCollapsed = false;
            }
            final boolean nextCollapsed = !wordsCollapsed;

            int fromHeight = wordsCollapsed ? 0 : height;
            int toHeight = wordsCollapsed ? height : 0;
//            System.out.println("slide: " + fromHeight + " -> " + toHeight);
            ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
            wordsFlow.setPivotY(0);
            anim.addUpdateListener(valueAnimator -> {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = wordsFlow.getLayoutParams();
                layoutParams.height = val;
                wordsFlow.setLayoutParams(layoutParams);
            });
            anim.setDuration(500);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    wordsCollapsedMap.put(group, nextCollapsed);
                }
            });
            anim.start();
        });
    }

    protected void doOnBindViewHolder(VocabularyGroupedBinding binding) {
        GroupedUserWords groupedWords = binding.getGrouped();
        List<UserWord> userWords = groupedWords.getUserWords();

        ImmutableFlowLayout flowLayout = binding.wordsFlow;
        flowLayout.removeAllViewsInLayout();

        LayoutInflater inflater = LayoutInflater.from(context);

        for (UserWord userWord : userWords) {
            TextView tv = (TextView) inflater.inflate(R.layout.vocabulary_word, null);
            tv.setText(userWord.getWord());
            tv.setTag(userWord);

            flowLayout.addView(tv);
            tv.setOnClickListener(wordClickListener);
        }

        ViewGroup.LayoutParams layoutParams = flowLayout.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        flowLayout.setLayoutParams(layoutParams);

        Group group = groupedWords.getGroup();
        wordsCollapsedMap.remove(group);
        wordsHeightMap.remove(group);

        bindGroupHeaderListener(binding);
    }

}
