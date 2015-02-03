package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by F0l2g3tm3n0t on 17-Jan-15.
 */
public class TCP_Unicast_Receive extends AsyncTask<String, Void, String> {

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
		ServerSocket serverSocket = null;
		Socket socket = null;
		DataInputStream input = null;
		try{
			// Initial condition
			JSONObject data_frame = new JSONObject(arg0[0]);
			Log.d(log_Head + " - doInBackground", "data_frame : " + data_frame);
			int serverPort = data_frame.getInt("serverPort");

			// open socket
			Log.d(log_Head + " - doInBackground","open socket");
			serverSocket = new ServerSocket(serverPort);
			socket = new Socket();

			while(true){
				// socket connected
				socket = serverSocket.accept();
				Log.d(log_Head + " - doInBackground","socket connected");
				// receive
				input = new DataInputStream(socket.getInputStream());
				msg = input.readUTF();

				//Log.d(log_Head + " - doInBackground","packet address : " + address.getHostAddress());
				Log.d(log_Head + " - doInBackground","my address : " + myAddress);
				Log.d(log_Head + " - doInBackground","message : " + msg);
				input.close();
				socket.close();
				serverSocket.close();
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
			try {
				if(input != null){
					input.close();
				}
				if(socket != null){
					socket.close();
				}
				if(serverSocket != null){
					serverSocket.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return "Fail : No Connection";
	}
}
