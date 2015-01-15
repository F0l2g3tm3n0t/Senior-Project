package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.view.View;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lab.kultida.utility.DataBase;

public class PlaceholderFragment_Prototype extends Fragment implements View.OnClickListener{
    protected MainActivity activity;
    protected boolean debugging_mode = true;
    protected View rootView;
    protected String serverIP;
    protected DataBase database;

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
        database = activity.database;
    }

	InetAddress getBroadcastAddress() {
		WifiManager wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		try {
			return InetAddress.getByAddress(quads);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

    @Override
    public void onClick(View v) {

    }
}


