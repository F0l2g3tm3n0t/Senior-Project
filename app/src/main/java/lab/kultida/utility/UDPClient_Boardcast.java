package lab.kultida.utility;

import android.os.AsyncTask;

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
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
    }
}