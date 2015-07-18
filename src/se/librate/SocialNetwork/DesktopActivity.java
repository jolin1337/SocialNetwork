package se.librate.SocialNetwork;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;

public class DesktopActivity extends Activity {
    DesktopView dv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Contact.defaultProfileImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile_image);
        dv = new DesktopView(this);
        //setContentView(dv);
        setContentView(R.layout.main);
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }
}
