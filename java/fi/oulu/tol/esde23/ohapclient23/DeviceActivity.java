package fi.oulu.tol.esde23.ohapclient23;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.opimobi.ohap.CentralUnit;
import com.opimobi.ohap.Device;

import java.net.MalformedURLException;
import java.net.URL;

import fi.oulu.tol.esde23.ohap.CentralUnitConnection;

public class DeviceActivity extends OptionsMenuActivity {

    public static final String EXTRA_CENTRAL_UNIT_URL = "fi.oulu.tol.esde.esde23.CENTRAL_UNIT_URL";
    public static final String EXTRA_DEVICE_ID = "fi.oulu.tol.esde23.DEVICE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CentralUnit centralUnit;

        //No need to check for intent content since activity is always lunched within an Intent
        URL intentURL;
        try {
            intentURL = new URL(getIntent().getStringExtra(EXTRA_CENTRAL_UNIT_URL));
            long deviceID = getIntent().getLongExtra(EXTRA_DEVICE_ID, 0);

            centralUnit = new CentralUnitConnection(intentURL);
            centralUnit.setName("OHAP Test Server");
            Device deviceToShow = (Device) centralUnit.getItemById(deviceID);
            initializeUI(deviceToShow);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void initializeUI(final Device device) {
        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        TextView textView_id = (TextView) findViewById(R.id.textView_id);
        TextView textView_description = (TextView) findViewById(R.id.textView_description);
        TextView textView_path = (TextView) findViewById(R.id.textView_path);
        Switch switch_status = (Switch) findViewById(R.id.switch_status);
        LinearLayout seekBar_layout = (LinearLayout) findViewById(R.id.seekBar_layout);
        SeekBar seekBar_value = (SeekBar) findViewById(R.id.seekBar_value);
        final TextView textField_measure_unit = (TextView) findViewById(R.id.textField_measure_unit);

        if (textView_name != null) {
            textView_name.setText(device.getName());
        }
        if (textView_id != null) {
            textView_id.setText(String.format("%d", device.getId()));
        }
        if (textView_description != null) {
            textView_description.setText(device.getDescription());
        }
        if (textView_path != null) {
            textView_path.setText(ItemUtility.getPath(device));
        }

        if (seekBar_layout != null && seekBar_value != null && switch_status != null && textField_measure_unit != null) {
            if (device.getValueType() == Device.ValueType.BINARY) {
                seekBar_layout.setVisibility(View.GONE);
                switch_status.setChecked(device.getBinaryValue());
                switch_status.setEnabled(device.getType() == Device.Type.ACTUATOR);
            } else if (device.getValueType() == Device.ValueType.DECIMAL) {
                switch_status.setVisibility(View.GONE);
                seekBar_value.setMax((int) device.getMaxValue());
                seekBar_value.setProgress((int) device.getDecimalValue());
                textField_measure_unit.setText(String.format("%.2f %s", device.getDecimalValue(), device.getUnitAbbreviation()));
                seekBar_value.setEnabled(device.getType() == Device.Type.ACTUATOR);
            }
        }
        ActionBar actionBar;
        if ((actionBar = getSupportActionBar()) != null) {
            actionBar.setTitle(String.format("Device type : %s", device.getType()));
        }
    }
}