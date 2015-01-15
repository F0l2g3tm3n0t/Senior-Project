package lab.kultida.utility;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by ekapop on 26/12/2557.
 */
public class UDP_Broadcast_Receive_Thread implements Runnable{
    protected String log_Head;
    protected String msg;
    protected int serverPort;
    protected DatagramSocket socket;
    protected byte[] buf;
    protected DatagramPacket packet;

    public UDP_Broadcast_Receive_Thread(String log_Head, int serverPort){
        this.log_Head = log_Head;
        this.serverPort = serverPort;
        try {
            socket = new DatagramSocket(22220);
            socket.setBroadcast(true);
            buf = new byte[2048];
            packet = new DatagramPacket(buf, buf.length);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(this, "Server Thread");
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Log.d("run","T^T");
                socket.receive(packet);
//                socket.setReuseAddress(true);
                msg = new String(buf, 0, packet.getLength());
                Log.d(log_Head + " : msg", msg);
//                packet.setLength(buf.length);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
