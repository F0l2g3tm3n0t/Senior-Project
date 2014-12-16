package lab.kultida.utility;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDP_Boardcast_Send extends AsyncTask<String, Void, String> {
    protected InetAddress broadcastIP;
    protected int serverPort;
    protected int clientPort;
    protected JSONObject condition;
    protected DatagramSocket socket;
    protected WifiManager wifiManager;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        /*
            --------------------------------
                 find broadcast address
            --------------------------------
         */


        DhcpInfo dhcpinfo = wifiManager.getDhcpInfo();
        //handle null
        byte[] data_byte;
        int broadcast = (dhcpinfo.ipAddress & dhcpinfo.netmask ) | ~dhcpinfo.netmask;
        byte[] quads = new byte[4];
        for(int k = 0; k < 4; k++){
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }

        try {
            broadcastIP = InetAddress.getByAddress(quads);
            condition = new JSONObject(arg0[0]);
            serverPort = condition.getInt("clientPort");
            clientPort = condition.getInt("clientPort");
            data_byte = condition.getJSONObject("data").toString().getBytes("UTF-8");

            socket = new DatagramSocket(clientPort);
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(data_byte, data_byte.length, broadcastIP, serverPort);
            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }

    @Override
    protected void onPostExecute(String result) {
    }
}