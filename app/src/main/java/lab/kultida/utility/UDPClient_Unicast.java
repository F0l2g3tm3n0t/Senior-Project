package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import lab.kultida.udpManager.Serializer;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDPClient_Unicast extends AsyncTask<String, Void, String> {
    protected InetAddress serverIP;
    protected int serverPort;
    protected int clientPort;
    protected JSONObject condition;
    protected DatagramSocket socket;

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
            this.serverPort = Integer.parseInt(condition.getString("serverPort"));
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
        byte[] buffer = new byte[3000]; // set window size
        Log.d("UDPClient - receiveMessage", "set replyPacket");
        DatagramPacket replyPacket = new DatagramPacket(buffer, buffer.length); // create buffer for receiving reply
        Log.d("UDPClient - receiveMessage", "set receive replyPacket");
        try {
            socket.receive(replyPacket);
        } catch (IOException e1) {
            Log.d("UDPClient - ReceiveMessage", "Error IO");
            e1.printStackTrace();
        } // set reply to receive data from aSocket
        Log.d("UDPClient - receiveMessage", "receive Socket");
        String msgReply = null;
        try {
            //msgReply = (String) Serializer.deserialize(replyPacket.getData()); // change reply message to be byte string
            msgReply = new String(replyPacket.getData(), 0, replyPacket.getLength()) + ", from address: " + replyPacket.getAddress().toString().substring(1) + ", port: " + replyPacket.getPort();
        } catch (Exception e) {
            Log.d("UDPClient - receiveMessage", "Error IO : " + e.getMessage());
            e.printStackTrace();
            return "Fail!!";
        }
        Log.d("UDPClient - receiveMessage", "Reply Packet: " + msgReply); // print reply message
        return msgReply;
    }
    @Override
    protected void onPostExecute(String result) {

    }
}
