package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by F0l2g3tm3n0t on 17-Jan-15.
 */
public class TCP_Multicast_Send extends AsyncTask<String, Void, String> {

	protected String log_Head = "";

	protected void onPreExecute() {
		// log_Head
		super.onPreExecute();
	}

	protected String doInBackground(String... arg0) {
		MulticastSocket msocket = null;
		String group = "224.0.0.1";
		try{
			JSONObject data_frame = new JSONObject(arg0[0]);
			Log.d(log_Head + " - doInBackground","data_frame : " + data_frame);
			int serverPort = Integer.parseInt(data_frame.getString("serverPort"));
			String data = data_frame.getJSONObject("data").toString();
			Log.d(log_Head + " - doInBackground","serverPort : " + serverPort);

			//open socket
			msocket = new MulticastSocket();

			//send data
			byte[] buf = data.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(group),serverPort);
			msocket.send(packet);

		}catch (Exception e){
			Log.d("Exception" , e.toString());
		}

		return null;
	}

	@Override
	protected void onPostExecute(String result) {}
}
