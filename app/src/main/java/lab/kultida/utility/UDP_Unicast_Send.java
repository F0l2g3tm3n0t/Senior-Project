package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDP_Unicast_Send extends AsyncTask<String, Void, String> {

	protected String log_Head;

    @Override
    protected void onPreExecute() {
        // log_Head
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        DatagramSocket socket = null;
        byte[] data_byte;
        try {
            // Initial condition
            JSONObject data_frame = new JSONObject(arg0[0]);
            Log.d(log_Head + " - doInBackground", "data_frame : " + data_frame);
            InetAddress serverIP = InetAddress.getByName(data_frame.getString("serverIP"));
            int serverPort = Integer.parseInt(data_frame.getString("serverPort"));
            data_byte = data_frame.getJSONObject("data").toString().getBytes("UTF-8");

            // open socket
            socket = new DatagramSocket();
            DatagramPacket sendPacket = new DatagramPacket(data_byte,data_byte.length,serverIP,serverPort);
            socket.send(sendPacket);
            socket.close();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            if(socket != null) socket.close();
            return "Fail";
        }
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
