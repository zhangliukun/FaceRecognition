package zalezone.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import zalezone.facerecognition.R;

/**
 * Created by zale on 16/4/15.
 */
public class ListDialog extends Dialog
{
    private String mList[];

    private AdapterView.OnItemClickListener mListener;

    private Context context;

    private View bgView;

    private ListView itemListView;

    private TextView dialogTitle;

    public ListDialog(Context context,String[] list,AdapterView.OnItemClickListener listener) {
        super(context, R.style.share_dialog);
        this.context = context;
        this.mListener = listener;
        this.mList = list;
        initView();
    }

    private void initView() {
        setContentView(R.layout.list_dialog);
        setCanceledOnTouchOutside(true);
        bgView = findViewById(R.id.dialog_bg);
        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        itemListView = (ListView) findViewById(R.id.dialog_list);
        itemListView.setAdapter(new ArrayAdapter<String>(context,R.layout.item_dialog_list,mList));
        if (mListener!=null){
            itemListView.setOnItemClickListener(mListener);
        }
    }

}
