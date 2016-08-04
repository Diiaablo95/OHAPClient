package fi.oulu.tol.esde23.ohap;

import com.opimobi.ohap.CentralUnit;
import com.opimobi.ohap.Container;
import com.opimobi.ohap.Device;

import java.net.URL;

/**
 * Created by backd00red on 22/03/16.
 */
public class CentralUnitConnection extends CentralUnit {

    private int nListeners;

    //Creates two containers with 4 nodes (for each possible combination of type and valueType) each.
    public CentralUnitConnection(URL url) {
        super(url);

        Container container1 = new Container(this, 100);
        container1.setName("Container1");
        Container container2 = new Container(this, 200);
        container2.setName("Container2");

        Device[] dummyDevices = {new Device(container1, 1, Device.Type.ACTUATOR, Device.ValueType.BINARY),
                new Device(container1, 2, Device.Type.SENSOR, Device.ValueType.BINARY),
                new Device(container1, 3, Device.Type.ACTUATOR, Device.ValueType.DECIMAL),
                new Device(container1, 4, Device.Type.SENSOR, Device.ValueType.DECIMAL),

                new Device(container2, 5, Device.Type.ACTUATOR, Device.ValueType.BINARY),
                new Device(container2, 6, Device.Type.SENSOR, Device.ValueType.BINARY),
                new Device(container2, 7, Device.Type.ACTUATOR, Device.ValueType.DECIMAL),
                new Device(container2, 8, Device.Type.SENSOR, Device.ValueType.DECIMAL)};

        for (Device dev : dummyDevices) {
            dev.setName("Device n: " + dev.getId());
            dev.setDescription("Description for device n: " + dev.getId());
            if (dev.getValueType() == Device.ValueType.DECIMAL) {
                dev.setMinMaxValues(0, 10);
                dev.setUnit("meters", "m");
                dev.setDecimalValue(3.5);
            } else {
                dev.setBinaryValue(true);
            }
        }
    }

    @Override
    protected void changeBinaryValue(Device device, boolean value) {}

    @Override
    protected void changeDecimalValue(Device device, double value) {}

    @Override
    protected void listeningStateChanged(Container container, boolean listening) {
        if (container.isListening()) {
            if (++nListeners == 1) {
                this.startNetworking();
            }
            this.sendListeningStart(container);
        } else {
            this.sendListeningStop(container);
            if (--nListeners == 0) {
                this.stopNetworking();
            }
        }
    }

    private void startNetworking() {}

    private void stopNetworking() {}

    private void sendListeningStart(Container container) {}

    private void sendListeningStop(Container container) {}
}
