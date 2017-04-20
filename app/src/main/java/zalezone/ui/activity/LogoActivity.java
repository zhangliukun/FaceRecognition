package zalezone.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.io.File;

import zalezone.facerecognition.R;
import zalezone.libs.XFaceLibrary;
import zalezone.util.CommonUtil;
import zalezone.util.ToastUtil;

/**
 * 
 * logo index
 * 
 * @author zale
 * 
 */
public class LogoActivity extends Activity {

	private static final String TAG = "LogoActivity";

	private Button btn_logo_signin;
	private HTextView hTextView;
	private Handler mHandler = new Handler();
	private static int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logo);
		btn_logo_signin = (Button) findViewById(R.id.btn_logo_signin);
		hTextView = (HTextView) findViewById(R.id.text);
		hTextView.setAnimateType(HTextViewType.RAINBOW);
		hTextView.animateText("Face Recognition"); // animate

		if (!CommonUtil.isAppInit) {
			// async do init app
			new AsyncTask<Void, Void, Boolean>() {

				@Override
				protected Boolean doInBackground(Void... params) {
					CommonUtil.initApp(getApplicationContext());
					// whether or not to re-calculate the model?
					File file = new File(CommonUtil.FACERECMODEL_FILEPATH);
					if (!file.exists()) {
						new XFaceLibrary().trainModel();// takes time! 44 seconds
					}
					return true;
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if (result) {
						ToastUtil.showShortToast(getApplicationContext(), "应用程序初始化成功咯!");
						btn_logo_signin.setEnabled(true);// model can be used
					} else {
						ToastUtil.showShortToast(getApplicationContext(), "应用程序初始化失败啦!");
					}
				}

				@Override
				protected void onPreExecute() {
					ToastUtil.showShortToast(getApplicationContext(), "应用程序初始化中...请稍候...");
					btn_logo_signin.setEnabled(false);
				}

			}.execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//android 4.4.4 显示不出来
		menu.add("Setting");
		menu.add("Exit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e(TAG, "called onOptionsItemSelected; selected item: " + item);
		if (item.getTitle().equals("Setting")) {
			Intent intent = new Intent(LogoActivity.this, SettingActivity.class);
			startActivity(intent);
		} else {
			if (CommonUtil.xFaceLibrary != null && CommonUtil.xFaceLibrary.xfacerec != 0) {
				CommonUtil.xFaceLibrary.destoryFacerec();//it is time to destory it!
			}
			LogoActivity.this.finish();
			System.exit(0);
		}
		return true;
	}

	public void btn_logo_signup(View view) {
		Intent intent = new Intent(LogoActivity.this, SignupActivity.class);
		startActivity(intent);
	}

	public void btn_logo_signin(View view) {
		Intent intent = new Intent(LogoActivity.this, FacerecCameraActivity.class);
		startActivity(intent);
	}

    public void btn_logo_settings(View view) {
        Intent intent = new Intent(LogoActivity.this, SettingActivity.class);
        startActivity(intent);
    }

}
