package lab.kultida.utility;

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by ekapop on 15/12/2557.
 */
public class UDPClient_Boardcast extends AsyncTask<String, Void, String> {
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


        /*
            --------------------------------
                 find broadcast address
            --------------------------------
         */
        WifiManager wifi = (WifiManager)getSystemService(WIFI_SERVICE);
        DhcpInfo dhcpinfo = wifi.getDhcpInfo();
        //handle null

        int broadcast = (dhcpinfo.ipAddress & dhcpinfo.netmask ) | ~dhcpinfo.netmask;
        byte[] quads = new byte[4];
        for(int k = 0; k < 4; k++){
            quad[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        InetAddress broadcastAddress = InetAddress.getByAddress(quads);
        /*
            --------------------------------
              manage message before send
            --------------------------------
         */

        JSONObject condition = new JSONObject(arg[0]);
        byte[] data = condition.getJSONObject("data").toString().getBytes("UTF-8");

        /*
            --------------------------------
              sending udp broadcast packet
            --------------------------------
         */
        DatagramSocket socket = new DatagramSocket(20394);
        socket.setBroadcast(true);
        DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), getBroadcastAddress(), DISCOVERY_PORT);
        socket.send(packet);
        return "ok";

    }

    @Override
    protected void onPostExecute(String result) {
    }
}