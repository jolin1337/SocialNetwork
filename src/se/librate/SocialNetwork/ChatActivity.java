package se.librate.SocialNetwork;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 *
 * @author Johannes
 */
public class ChatActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chat);
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
