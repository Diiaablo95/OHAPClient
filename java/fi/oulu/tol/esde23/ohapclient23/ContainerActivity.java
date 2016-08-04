package fi.oulu.tol.esde23.ohapclient23;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.opimobi.ohap.CentralUnit;
import com.opimobi.ohap.Container;
import com.opimobi.ohap.Device;
import com.opimobi.ohap.Item;

import java.net.MalformedURLException;
import java.net.URL;

import fi.oulu.tol.esde23.ohap.CentralUnitConnection;

public class ContainerActivity extends OptionsMenuActivity {

    //Keys to access data from the intent for successive calls of the same activity.
    public static final String EXTRA_CENTRAL_UNIT_URL = "fi.oulu.tol.esde.esde23.CENTRAL_UNIT_URL";
    public static final String EXTRA_CONTAINER_ID = "fi.oulu.tol.esde23.CONTAINER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CentralUnit centralUnit = null;
        Container container = null;
        Intent startingIntent = getIntent();
        if (startingIntent.getStringExtra(EXTRA_CENTRAL_UNIT_URL) != null) {            //If this is not the main launch of this activity
            try {
                URL centralUnit_URL = new URL(startingIntent.getStringExtra(EXTRA_CENTRAL_UNIT_URL));
                centralUnit = new CentralUnitConnection(centralUnit_URL);
                container = (Container) centralUnit.getItemById(startingIntent.getLongExtra(EXTRA_CONTAINER_ID, 0));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            try {       //Never caught because control is done by the preference when trying to edit it.
                URL storedURL = new URL(PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsFragment.URL_EDIT_TEXT_PREFERENCE_KEY, ""));
                container = new CentralUnitConnection(storedURL);
                centralUnit = (CentralUnit) container;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        if (listView != null && container != null) {
            listView.setAdapter(new ContainerListAdapter(container)); //Set the adapter for the listView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //set the eventListener for the listView
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Item selectedItem = (Item) parent.getAdapter().getItem(position);
                    Intent startingIntent = null;

                    //Put as parameters URL of central unit & id for selected item
                    if (selectedItem instanceof Container) {
                        startingIntent = new Intent(ContainerActivity.this, ContainerActivity.class);
                        startingIntent.putExtra(EXTRA_CENTRAL_UNIT_URL, selectedItem.getCentralUnit().getURL().toString());
                        startingIntent.putExtra(EXTRA_CONTAINER_ID, selectedItem.getId());
                    } else if (selectedItem instanceof Device) {
                        startingIntent = new Intent(ContainerActivity.this, DeviceActivity.class);
                        startingIntent.putExtra(DeviceActivity.EXTRA_DEVICE_ID, selectedItem.getId());
                        startingIntent.putExtra(DeviceActivity.EXTRA_CENTRAL_UNIT_URL, selectedItem.getCentralUnit().getURL().toString());
                    }
                    startActivity(startingIntent);
                }
            });
        }

        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null && centralUnit != null) {
            actionBar.setTitle(String.format("CU URL: %s", centralUnit.getURL().toString()));
        }
    }
}