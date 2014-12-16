package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

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
		        Log.d("Receive - message", msg);
		        socket.close();
		        Log.d("Receive", "socket closed");
		        return msg;
	        }
        }catch (Exception e){
            Log.d("Exception Error : ",e.getMessage());
            e.printStackTrace();
	        if(socket != null){
		        socket.close();
	        }
        }
        return "Fail!!";
}

    @Override
    protected void onPostExecute(String result) {}

}