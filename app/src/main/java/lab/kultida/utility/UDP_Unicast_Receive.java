package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDP_Unicast_Receive extends AsyncTask<String, Void, String> {

    protected String log_Head;

    @Override
    protected void onPreExecute() {
        // log_Head
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        DatagramSocket socket = null;
        byte[] buffer = new byte[3000];
        try {
            // Initial condition
            JSONObject data_frame = new JSONObject(arg0[0]);
            Log.d(log_Head + " - doInBackground", "data_frame : " + data_frame);

            // open socket
            socket = new DatagramSocket();
            socket.setSoTimeout(300);
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);

            String result = new String(receivePacket.getData(),"UTF-8");
            socket.close();
            return result;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            if(socket != null) socket.close();
            return "Fail";
        }
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
