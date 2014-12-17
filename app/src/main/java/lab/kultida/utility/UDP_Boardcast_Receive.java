package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by F0l2g3tm3n0t on 16-Dec-14.
 */
public class UDP_Boardcast_Receive extends AsyncTask<String, Void, String> {
    protected DatagramSocket socket;
    protected int serverPort;
    protected JSONObject condition;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        byte[] buf = new byte[3000];
		Log.d("Receive","start");
        try {
            condition = new JSONObject(arg0[0]);
	        serverPort = condition.getInt("serverPort");
	        Log.d("Receive", serverPort + "");
            socket = new DatagramSocket(serverPort);
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
	        if(!socket.isConnected()) {
		        Log.d("Receive", "Socket connected");
		        socket.receive(packet);
		        String msg = new String(packet.getData());
		        InetAddress address = packet.getAddress();
		        InetAddress myAddress = InetAddress.getByName(getIPAddress(true));
		        if(myAddress == address) {
			        Log.d("Receive - message", msg);
			        socket.close();
			        Log.d("Receive", "socket closed");
			        return msg;
		        } else{
			        socket.close();
			        Log.d("Receive", "socket closed");
			        return "Fail!!";
		        }
	        }
        }catch (Exception e){
            Log.d("Exception Error : ",e.getMessage());
            e.printStackTrace();
	        if(socket != null){
		        socket.close();
	        }
        }
	    if(socket != null){
		    socket.close();
	    }
        return "Fail!!";
}

    @Override
    protected void onPostExecute(String result) {}

	protected String getIPAddress(boolean useIPv4) {
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