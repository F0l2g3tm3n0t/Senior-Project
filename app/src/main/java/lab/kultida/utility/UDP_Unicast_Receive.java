package lab.kultida.utility;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDP_Unicast_Receive extends AsyncTask<String, Void, String> {
    protected DatagramSocket socket;
    protected String result = null;
    protected int clientPort;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        byte[] buffer = new byte[3000];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            JSONObject condition = new JSONObject(arg0[0]);
            this.clientPort = Integer.parseInt(condition.getString("clientPort"));

            socket = new DatagramSocket(clientPort);
            socket.setSoTimeout(300);
            socket.receive(receivePacket);

//            result = String.valueOf(replyPacket.getData());
            result = new String(receivePacket.getData(),"UTF-8");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
    }
}
