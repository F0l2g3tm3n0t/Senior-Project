package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Broadcast_Send extends AsyncTask<String, Void, String> {

    protected String log_Head;

    @Override
    protected void onPreExecute() {
        // log_head
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        DatagramSocket socket = null;
	    byte[] data_byte;
        try {
            // Initial condition
            JSONObject data_frame = new JSONObject(arg0[0]);
            Log.d(log_Head + " - doInBackground","data_frame : " + data_frame);
            int serverPort = data_frame.getInt("serverPort");
            data_byte = data_frame.getJSONObject("data").toString().getBytes("UTF-8");
            Log.d(log_Head + " - doInBackground","new String(data_byte,\"UTF-8\") : " + new String(data_byte,"UTF-8"));
            InetAddress broadcastIP = InetAddress.getByName("192.168.42.255");
//            InetAddress broadcastIP = InetAddress.getByName("10.0.3.255");
            Log.d(log_Head + " - doInBackground","broadcast IP : 192.168.42.255");

            // open socket and packet
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(data_byte, data_byte.length, broadcastIP, serverPort);
            socket.send(packet);
	        socket.close();
            return "Success";
        } catch (Exception e) {
            Log.d("Exception",log_Head);
            e.printStackTrace();
	        if(socket != null) socket.close();
            return "Fail";
        }
    }

    @Override
    protected void onPostExecute(String result) {
    }
}