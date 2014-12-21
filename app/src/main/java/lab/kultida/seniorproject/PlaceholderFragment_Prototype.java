package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.view.View;

public class PlaceholderFragment_Prototype extends Fragment implements View.OnClickListener{
    protected MainActivity activity;
    protected boolean debugging_mode = true;
    protected View rootView;
    protected String serverIP;

	protected String getMacAddress(){
		WifiManager manager = (WifiManager)(activity.getSystemService(Context.WIFI_SERVICE));
		WifiInfo info = manager.getConnectionInfo();
		String macAddress = info.getMacAddress();
		return macAddress;
	}

    protected void defaultOperation(){
        setRetainInstance(true);
        activity = (MainActivity)getActivity();
        serverIP = activity.serverIP;
    }

    @Override
    public void onClick(View v) {

    }
}


