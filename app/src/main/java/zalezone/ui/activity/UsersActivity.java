package zalezone.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import zalezone.ui.adapter.UserInfo;
import zalezone.ui.adapter.UserSwipeAdapter;
import zalezone.facerecognition.R;
import zalezone.util.CommonUtil;
import zalezone.util.FileUtils;
import zalezone.util.ToastUtil;
import zalezone.ui.widget.ListDialog;

/**
 * user list
 *
 * @author zale
 */
public class UsersActivity extends Activity {

    private List<UserInfo> data = new ArrayList<UserInfo>();
    // private SwipeListView mListView;
    private ListView mListView;
    private  Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        data = CommonUtil.getAllUsers(true);

        mListView = (ListView) findViewById(R.id.lv_users_userlist);//

        final UserSwipeAdapter mAdapter = new UserSwipeAdapter(getApplicationContext(), data);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toast.makeText(UsersActivity.this, "item onclick " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UsersActivity.this, UserpicsActivity.class);
                intent.putExtra("userid", data.get(position).getUserid());
                intent.putExtra("username", data.get(position).getName());
                startActivity(intent);
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int listPosition, final long listId) {
                dialog = new ListDialog(UsersActivity.this, new String[]{"删除"}, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                        String sTotal = CommonUtil.userProps.getProperty("total");
                        int total = 0;
                        if (sTotal!=null){
                            total = Integer.valueOf(sTotal);
                            if (total>0){
                                CommonUtil.userProps.setProperty("total", String.valueOf(total-1));
                                CommonUtil.userProps.remove(String.valueOf(data.get((int) listId).getUserid()));
                                try {
                                    CommonUtil.saveUserProperties(CommonUtil.userProps);
                                    ToastUtil.showShortToast(getApplicationContext(), "删除成功,总的照片数:"+String.valueOf(total-1));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        FileUtils.deleteFile(CommonUtil.USERFOLDER.getAbsolutePath() + File.separator
                                + String.valueOf(data.get((int) listId).getUserid()));
                        data.remove((int)listId);
                        mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    public void btn_users_back(View view) {
        UsersActivity.this.finish();
    }

}
