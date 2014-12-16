package lab.kultida.utility;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDP_Unicast_Send extends AsyncTask<String, Void, String> {
    protected InetAddress serverIP;
    protected JSONObject condition;
    protected DatagramSocket socket;
    protected int serverPort = 9998;
    protected int clientPort = 45808;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        byte[] data_byte;

        try {
            condition = new JSONObject(arg0[0]);
            this.serverIP = InetAddress.getByName(condition.getString("serverIP"));
            this.serverPort = Integer.parseInt(condition.getString("clientPort"));
            this.clientPort = Integer.parseInt(condition.getString("clientPort"));
            data_byte = condition.getJSONObject("data").toString().getBytes("UTF-8");

            socket = new DatagramSocket(clientPort);
            //socket.setSoTimeout(300);
            DatagramPacket sendPacket = new DatagramPacket(data_byte,data_byte.length,serverIP,serverPort);
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiveMessage();
    }
    public String receiveMessage(){
        byte[] buffer = new byte[3000];

        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            return new String(packet.getData(), 0, packet.getLength()) + ", from address: " + packet.getAddress().toString().substring(1) + ", port: " + packet.getPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Fail!!";
    }
    @Override
    protected void onPostExecute(String result) {

    }
}
