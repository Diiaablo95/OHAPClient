package fi.oulu.tol.esde23.ohapclient23;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by backd00red on 22/03/16.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Displays the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
