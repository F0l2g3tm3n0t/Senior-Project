package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by F0l2g3tm3n0t on 16-Dec-14.
 */
public class UDP_Boardcast_Receive extends AsyncTask<String, Void, String> {
    protected DatagramSocket socket;
    protected int clientPort;
    protected JSONObject condition;


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... arg0) {
        byte[] buf = new byte[1024];

        try {
            condition = new JSONObject(arg0[0]);
            clientPort = condition.getInt("clientPort");

            socket = new DatagramSocket(clientPort);
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            String msg = new String(packet.getData());
            Log.d("Message",msg);
	        socket.close();
            return msg;
        }catch (Exception e){
            Log.d("Exception Error : ",e.getMessage());
            e.printStackTrace();
	        if(socket != null){
		        socket.close();
	        }
        }
        return "Fail!!";
}

    @Override
    protected void onPostExecute(String result) {}

}