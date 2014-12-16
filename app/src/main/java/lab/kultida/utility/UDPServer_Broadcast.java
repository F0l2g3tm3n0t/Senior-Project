package lab.kultida.utility;

import android.os.AsyncTask;

import java.net.DatagramSocket;

/**
 * Created by F0l2g3tm3n0t on 16-Dec-14.
 */
public class UDPServer_Broadcast extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
         /*
            --------------------------------
             receiving udp broadcast packet
            --------------------------------
         */
        byte[] buf = new byte[1024];
        DatagramSocket socket = new DatagramSocket(20394);
        socket.setBroadcast(true);
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try(){
            socket.receive(packet);
            String msg = new String(packet.getData(), 0, packet.getLength()) + ", from address: " + packet.getAddress().toString().substring(1) + ", port: " + packet.getPort();
            return msg;
            Log.d("Message" + msg);
        } catch (Exception e){
            Log.d("Exception Error : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
}

}

    @Override
    protected void onPostExecute(String result) {}

}