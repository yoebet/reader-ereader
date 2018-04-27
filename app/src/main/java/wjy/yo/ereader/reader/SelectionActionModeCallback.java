package wjy.yo.ereader.reader;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import wjy.yo.ereader.R;

public class SelectionActionModeCallback implements ActionMode.Callback {

    private int option3Id;
    private TextView tv;

    SelectionActionModeCallback(TextView tv) {
        this.tv = tv;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        System.out.println("onCreateActionMode");
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.text_context, menu);

        MenuItem m = menu.add("Option 3");
        option3Id = m.getItemId();
        System.out.println("added MenuItem: " + option3Id);
        System.out.println("MenuItem Size: " + menu.size());
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
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        System.out.println("onActionItemClicked");
        Context context = tv.getContext();
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
                if (itemId == option3Id) {
                    CharSequence selected = "";
                    int start = tv.getSelectionStart();
                    if (start >= 0) {
                        int end = tv.getSelectionEnd();
                        if (end >= 0) {
                            selected = tv.getText().subSequence(start, end);
                        }
                    }
                    Toast.makeText(context, "Option 3: " + selected, Toast.LENGTH_SHORT).show();
//                        mode.getMenu().close();
                    mode.finish();
//                        ((Activity)context).closeOptionsMenu();
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
