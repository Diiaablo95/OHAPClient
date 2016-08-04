package fi.oulu.tol.esde23.ohapclient23;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by backd00red on 22/03/16.
 */
public class SettingsFragment extends PreferenceFragment {

    //Key to access to preference value
    public static final String URL_EDIT_TEXT_PREFERENCE_KEY = "preferred_URL";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from the XML resource
        addPreferencesFromResource(R.xml.preferences);

        final EditTextPreference urlPreference = (EditTextPreference) findPreference(URL_EDIT_TEXT_PREFERENCE_KEY);
        urlPreference.setSummary(urlPreference.getText());

        urlPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean shouldChange = true;

                try {
                    URL url = new URL((String) newValue);
                    urlPreference.setSummary(url.toString());
                } catch (MalformedURLException e) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("URL wrong.")
                            .setMessage("URL filled in the field is not correctly formatted.")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                    shouldChange = false;
                }
                return shouldChange;
            }
        });
    }
}
