package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by F0l2g3tm3n0t on 16-Dec-14.
 */
public class UDP_Broadcast_Receive extends AsyncTask<String, Void, String> {

    protected String log_Head;
    protected String myAddress;
    protected InetAddress address;
    protected String msg;

    @Override
    protected void onPreExecute() {
        // log_Head
        // myAddress
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        DatagramSocket socket = null;
        try {
            // Initial condition
            JSONObject data_frame = new JSONObject(arg0[0]);
            Log.d(log_Head + " - doInBackground","data_frame : " + data_frame);
            int serverPort = data_frame.getInt("serverPort");

            // open socket
            Log.d(log_Head + " - doInBackground","open socket");
            socket = new DatagramSocket(22220);
            socket.setBroadcast(true);
			while(true){
				// receive packet
				Log.d(log_Head + " - doInBackground","open packet");
				byte[] buf = new byte[3000];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);

				Log.d(log_Head + " - doInBackground","socket connected");
				socket.receive(packet);

				msg = new String(buf, 0, packet.getLength());
				address = packet.getAddress();
				Log.d(log_Head + " - doInBackground","packet address : " + address.getHostAddress());
				Log.d(log_Head + " - doInBackground","my address : " + myAddress);
				Log.d(log_Head + " - doInBackground","message : " + msg);
				socket.close();
				if(myAddress.matches(address.getHostAddress())) {
					Log.d(log_Head + " - doInBackground", "receive from myself");
					return "Fail : MySelf";
				} else{
					Log.d(log_Head + " - doInBackground","receive from other");
					return msg;
				}

			}
        }catch (Exception e){
            Log.d("Exception",log_Head);
            e.printStackTrace();
	        if(socket != null) socket.close();
        }
        return "Fail : No Connection";
    }

    @Override
    protected void onPostExecute(String result) {}
}