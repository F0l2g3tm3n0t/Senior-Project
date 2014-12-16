package lab.kultida.udpManager;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient implements Runnable {
	public final int DEFAULT_SERVER_PORT = 50003; 
	public final int DEFAULT_CLIENT_PORT = 33193;
	public final int DEFAULT_BCAST_PORT = 8779;

	private InetAddress serverIp;
	private int serverPort;
	private int clientPort;
	private int loop;
	private int delay;
	
	private String messageShow;
	private byte[] bytes;
	private DatagramSocket socket;
	
	private TextView textView_Status;
	
	private boolean isRunning;
	private Thread thread;
	
	public UDPClient(JSONObject connectionInfo,TextView textView_DeviceInfo,TextView textView_Status) {
		try {
			if(connectionInfo.has("serverIp")) this.serverIp = InetAddress.getByName(connectionInfo.getString("serverIp"));
			else this.serverIp = InetAddress.getByName("127.0.0.1");
			
			if(connectionInfo.has("clientPort")) this.serverPort = Integer.parseInt(connectionInfo.getString("clientPort"));
			else this.serverPort = DEFAULT_SERVER_PORT;
			
			if(connectionInfo.has("clientPort")) this.clientPort = Integer.parseInt(connectionInfo.getString("clientPort"));
			else this.clientPort = DEFAULT_CLIENT_PORT;
			
			if(connectionInfo.has("loop")) this.loop = Integer.parseInt(connectionInfo.getString("loop"));
			else this.loop = 1;
			
			if(connectionInfo.has("delay")) this.delay = Integer.parseInt(connectionInfo.getString("delay"));
			else this.delay = 0;
			
			this.textView_Status = textView_Status;
			
			Log.d("UDPClient - Constructor", "serverIp = " + serverIp.toString());
			Log.d("UDPClient - Constructor", "clientPort = " + serverPort);
			Log.d("UDPClient - Constructor", "clientPort = " + clientPort);
			Log.d("UDPClient - Constructor", "loop = " + loop);
			Log.d("UDPClient - Constructor", "delay = " + delay);
			
		} catch (JSONException e1) {
			Log.d("UDPClient - Constructor", "Error - JSONObject ");
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			Log.d("UDPClient - Constructor", "Error Unknow Host Object");
			e.printStackTrace();
		}
		this.isRunning = false;
		
		try {
//			socket = new DatagramSocket(null); 
//			SocketAddress socketAddr=new InetSocketAddress(DEFAULT_BCAST_PORT); 
//			socket.setReuseAddress(true); 
//			socket.setBroadcast(true); 
//			socket.bind(socketAddr); 
			
//			socket = new DatagramSocket(clientPort);
			socket = new DatagramSocket();
			socket.setSoTimeout(300);
//			socket.setBroadcast(true); //why set broadcast
		} catch (SocketException e) {
			Log.d("UDPClient - Constructor", "Error Socket: " + e.getMessage());
			return; 
		}
		Log.d("UDPClient - Constructor", "Client created");
	}

	public void run() { 
		Log.d("UDPClient - run", "Run");
		this.isRunning = true;
//		Log.d("UDPClient - run", "Ready to access loop while");
//		Log.d("UDPClient - run", "Server ip = " + serverIp);
//		Log.d("UDPClient - run", "Loop");
		byte[] sendData = bytes;
		try {
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIp, serverPort);
			socket.send(sendPacket);
			Log.d("UDPClient - run", "Package Sent Complete");
			textView_Status.append("\nloop : " + loop + "\nPort : " + clientPort + "\nMessage : " + messageShow + "\nByte : " + bytes + "\n");
		} catch (Exception e) {
			Log.d("UDPClient - run", "Exception While Thread");
		}	
//		receiveMessage();
		if (socket != null) socket.close();
		this.isRunning = false;
	}
 
	public void sendMessage(byte[] bytes) {
		Log.d("UDPClient - sendMessage", "BYTE = " + bytes);
		try {
			if(this.isRunning) {
				while(this.thread.isAlive());	
				this.isRunning = false;
			}
			this.bytes = bytes;
			thread = new Thread(this, "Client Thread");
			thread.start();
		} catch (Exception e) {
			Log.d("UDPClient - sendMessage", "Error New Thread");
			return; 
		}
	}
	
	public String sendMessage(String message) {
		messageShow = message;	
		try {
			bytes = message.getBytes("UTF-8");
//				bytes = Serializer.serialize(message);
		} catch (IOException e) {
			Log.d("UDPClient - sendMessage", "Error serializer message");
			e.printStackTrace();
		}
		Log.d("UDPClient - sendMessage - String", "message = " + message);
		sendMessage(bytes);
		return message;
	}
	
	public void sendMessage(JSONObject message){
		byte[] bytes = null;
		try {
			bytes = Serializer.serialize(message);
		} catch (IOException e) {
			Log.d("UDPClient - sendMessage", "Error serializer JSONObject");
			e.printStackTrace();
		}
		sendMessage(bytes);
	}
	
	public String sendMessage(int length){
		String message = "";
		for(int i = 0;i < length;i++) message += "0";
		Log.d("UDPClient - sendMessage", "Message = " + message);
		return sendMessage(message);
	}

	public void receiveMessage(){
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
			msgReply = (String) Serializer.deserialize(replyPacket.getData()); // change reply message to be byte string
		} catch (ClassNotFoundException e) {
			Log.d("UDPClient - receiveMessage", "Error serializer replyPacket"); // print reply message
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("UDPClient - receiveMessage", "Error IO : " + e.getMessage());
			e.printStackTrace();
		}
		Log.d("UDPClient - receiveMessage", "Reply Packet: " + msgReply); // print reply message
	}
} 