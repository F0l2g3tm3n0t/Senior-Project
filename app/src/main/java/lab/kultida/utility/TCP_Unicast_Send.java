package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TCP_Unicast_Send extends AsyncTask<String, Void, String> {

	protected JSONObject condition;
	protected InetAddress serverIP;
	protected int serverPort;
	protected String data_byte;
	protected Socket socket = null;
	protected DataOutputStream output = null;
	protected String result = "";

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... arg0) {
		Log.d("Ask For help", "Reach Do in background");
		try{
			/*
				-------------------
				 Initial condition
				-------------------
			 */
			Log.d("Ask For Help", "start init condition");
			condition = new JSONObject(arg0[0]);
			this.serverIP = InetAddress.getByName(condition.getString("serverIP"));
			this.serverPort = Integer.parseInt(condition.getString("serverPort"));
			this.data_byte = condition.getJSONObject("data").toString();
			Log.d("TCP_Unicast_Send", "initial condition: " + condition.toString());

			/*
				-------------
				 open socket
				-------------
			 */
			socket = new Socket(serverIP,serverPort);
			output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(data_byte);
			Log.d("TCP_Unicast_Send", "data : " + data_byte);
			output.flush();
			output.close();
			socket.close();
			return "Success";
		}catch (Exception e){
			Log.d("TCP_Unicast_Send", "Error" + e);
		}
		return "Fail";
	}

	@Override
	protected void onPostExecute(String result) {}

}
