package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Boardcast_Send extends AsyncTask<String, Void, String> {
    protected InetAddress broadcastIP;
    protected int serverPort;
    protected int clientPort;
    protected JSONObject condition;
    protected DatagramSocket socket;

    @Override
    protected void onPreExecute() { super.onPreExecute(); }

    @Override
    protected String doInBackground(String... arg0) {
	    Log.d("sending message", "do in background");

	    byte[] data_byte;
        try {
	        broadcastIP = InetAddress.getByName("192.168.42.255");
	        Log.d("send broadcast","start");
	        Log.d("broadcast IP",broadcastIP.getHostAddress());
	        Log.d("--","------------------------------------------");

            condition = new JSONObject(arg0[0]);
	        Log.d("condition", condition.toString());
	        Log.d("--","------------------------------------------");
            serverPort = condition.getInt("clientPort");
            clientPort = condition.getInt("clientPort");
            data_byte = condition.getJSONObject("data").toString().getBytes("UTF-8");

            socket = new DatagramSocket(22220);
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(data_byte, data_byte.length, broadcastIP, 22220);
            socket.send(packet);
	        Log.d("send broadcast","finish");
	        socket.close();
        } catch (Exception e) {
            e.printStackTrace();
	        Log.d("send broadcast","Exception Error");
	        if(socket != null) {
		        socket.close();
	        }
        }
        return "ok";
    }

    @Override
    protected void onPostExecute(String result) {
    }
}