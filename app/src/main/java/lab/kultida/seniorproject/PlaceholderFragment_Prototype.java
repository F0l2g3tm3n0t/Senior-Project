package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PlaceholderFragment_Prototype extends Fragment implements View.OnClickListener{
    protected Activity activity;
    protected boolean debugging_mode = true;
    protected View rootView;
    protected String serverIP = "1.1.1.99";

	protected String getMacAddress(){
		WifiManager manager = (WifiManager)(activity.getSystemService(Context.WIFI_SERVICE));
		WifiInfo info = manager.getConnectionInfo();
		String macAddress = info.getMacAddress();
		return macAddress;
	}

    protected void defaultOperation(){
        setRetainInstance(true);
        activity = getActivity();
    }

	protected InetAddress getPIAddress(){
		InetAddress address = null;
		WifiManager manager = (WifiManager)(activity.getSystemService(Context.WIFI_SERVICE));
		WifiInfo info = manager.getConnectionInfo();
		try {
			address = InetAddress.getByName(info.getIpAddress() + "");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return address;
	}

    @Override
    public void onClick(View v) {

    }
}


