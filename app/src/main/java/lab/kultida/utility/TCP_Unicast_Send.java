package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class TCP_Unicast_Send extends AsyncTask<String, Void, String> {

    protected String log_Head = "";
    protected String data_receive = "";
    protected boolean receiveData = false;

    @Override
    protected void onPreExecute() {
        // log_Head
        super.onPreExecute();
    }

    protected void sleep(){

    }

    @Override
    protected String doInBackground(String... arg0) {
        Socket socket = null;
        DataOutputStream output = null;
        try{
            // Initial condition
            JSONObject data_frame = new JSONObject(arg0[0]);
            Log.d(log_Head + " - doInBackground","data_frame : " + data_frame);
            InetAddress serverIP = InetAddress.getByName(data_frame.getString("serverIP"));
            int serverPort = Integer.parseInt(data_frame.getString("serverPort_CheckHotspotInformation"));
            String data = data_frame.getJSONObject("data").toString();
            Log.d(log_Head + " - doInBackground","serverIP : " + data_frame.getString("serverIP"));
            Log.d(log_Head + " - doInBackground","serverPort_CheckHotspotInformation : " + serverPort);
            Log.d(log_Head + " - doInBackground","send data : " + data);

            // open socket
            Log.d(log_Head + " - doInBackground","open socket");
            socket = new Socket(serverIP,serverPort);
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(data);
            Log.d(log_Head + " - doInBackground", "receive data : " + data);
            output.flush();
            output.close();
            socket.close();

            if(!receiveData){
                DataInputStream input = new DataInputStream(socket.getInputStream());
                data_receive = input.readUTF();
            }

            sleep();
            return "Success : " + data;
        }catch (Exception e){
            e.printStackTrace();
            try {
                if(output != null) output.close();
                if(socket != null) socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            sleep();
            return "Fail";
        }
    }

    @Override
    protected void onPostExecute(String result) {}

}
