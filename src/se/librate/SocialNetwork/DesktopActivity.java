package se.librate.SocialNetwork;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
        //EditText et = new EditText(this);
        //dv = new DesktopView(this, et);
        //Button btn = new Button(this);
        //btn.setVisibility(View.GONE);
        //setContentView(dv);
        //addContentView(et, new ViewGroup.LayoutParams(300, 100));
        //addContentView(btn, new ViewGroup.LayoutParams(300, 100));
        //btn.requestFocus();
        //et.clearFocus();
        setContentView(R.layout.main);
        
        EditText et = (EditText) findViewById(R.id.search_bar);
        DesktopView dv = (DesktopView) findViewById(R.id.desktop);
        dv.setSearchBar(et);
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
