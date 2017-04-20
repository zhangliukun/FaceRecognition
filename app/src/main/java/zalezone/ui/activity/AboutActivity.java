package zalezone.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import zalezone.facerecognition.R;

/**
 * about
 *
 * @author zale
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void btn_about_back(View view) {
        AboutActivity.this.finish();
    }

}
