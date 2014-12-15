package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class Prototype extends Fragment implements View.OnClickListener{
    protected Activity activity;
    protected boolean debugging_mode = true;
    protected View rootView;

    protected String serverIP;
    protected int serverPort;
    protected int clientPort;


    protected void defaultOperation(){
        setRetainInstance(true);
        activity = getActivity();
    }

    protected void getComponent(){
        activity = getActivity();
    }

    @Override
    public void onClick(View v) {

    }

    public static String getIPAddress(boolean useIPv4) {
        // useIPv4 = true  >> IPv4
        //  	   = false >> IPv6
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
}


