package lab.kultida.utility;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDPClient_Unicast extends AsyncTask<String, Void, String> {
    protected InetAddress serverIP;
    protected int serverPort;
    protected int clientPort;

    protected DatagramSocket socket;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        byte[] data_byte;

        try {
            JSONObject condition = new JSONObject(arg0[0]);
            this.serverIP = InetAddress.getByName(condition.getString("serverIP"));
            this.serverPort = Integer.parseInt(condition.getString("serverPort"));
            this.clientPort = Integer.parseInt(condition.getString("clientPort"));
            data_byte = condition.getString("data").getBytes("UTF-8");

            socket = new DatagramSocket(clientPort);
            socket.setSoTimeout(300);

            DatagramPacket sendPacket = new DatagramPacket(data_byte,data_byte.length,serverIP,serverPort);

        } catch (JSONException | UnknownHostException | UnsupportedEncodingException | SocketException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
