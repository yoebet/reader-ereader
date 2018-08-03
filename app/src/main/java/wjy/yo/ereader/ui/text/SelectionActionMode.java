package wjy.yo.ereader.ui.text;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import wjy.yo.ereader.R;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.textview.ParaContentTextView;
import wjy.yo.ereader.vo.WordContext;

public class SelectionActionMode implements ActionMode.Callback {

    private int optionDictId;
    private ParaContentTextView textView;

    private DictAgent dictAgent;

    public SelectionActionMode(ParaContentTextView textView, DictAgent dictAgent) {
        this.textView = textView;
        this.dictAgent = dictAgent;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        System.out.println("onCreateActionMode");
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.text_context, menu);

        MenuItem m = menu.add("查词");
        optionDictId = m.getItemId();

//        System.out.println("getActionView: " + m.getActionView());
//
//        LayoutInflater li = (LayoutInflater) textView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View actionView = li.inflate(R.layout.dict_action_view, null);
//        m.setActionView(actionView);
//
//        m.setActionProvider(new ActionProvider(textView.getContext()) {
//            @Override
//            public View onCreateActionView() {
//
//                return actionView;
//            }
//        });

//            m.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
//
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    System.out.println("MenuItem clicked: " + item.getItemId());
//                    return true;
//                }
//            });

//            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                System.out.println("Type : " + mode.getType());
//            }

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        System.out.println("onPrepareActionMode");
//        mode.setType(ActionMode.TYPE_FLOATING);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        System.out.println("onActionItemClicked");
        Context context = textView.getContext();
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.option1:
                Toast.makeText(context, "Option 1", Toast.LENGTH_SHORT).show();
                mode.getMenu().close();
//                        mode.finish();
                return true;
            case R.id.option2:
                Toast.makeText(context, "Option 2", Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            default:
                if (itemId == optionDictId) {
                    int start = textView.getSelectionStart();
                    int end = textView.getSelectionEnd();
                    if (textView.hasSelection() && end > 0) {
                        CharSequence selected = textView.getText().subSequence(start, end);
                        WordContext wc = null;
                        Para para = textView.getPara();
                        if (para != null) {
                            wc = para.getWordContext();
                        }
                        dictAgent.requestDict(selected.toString(), wc);
                    }
                    mode.finish();
                    return true;
                }
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        System.out.println("onDestroyActionMode");
//            mode.finish();

    }
}
