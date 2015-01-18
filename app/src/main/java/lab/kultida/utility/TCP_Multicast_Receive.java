package lab.kultida.utility;

import android.os.AsyncTask;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Created by F0l2g3tm3n0t on 17-Jan-15.
 */
public class TCP_Multicast_Receive extends AsyncTask<String, Void, String> {

	protected String log_Head;
	protected MulticastSocket socket;
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	protected String doInBackground(String... arg0) {

		try {

			DatagramPacket packet;
			while (true) {
				byte[] buf = new byte[256];
				packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);

				String received = new String(packet.getData());
				Log.d(log_Head + " - doInBackground", received);

				//return received;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Fail : No Connection";
	}
}
