package lab.kultida.udpManager;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import lab.kultida.seniorproject.R;

public class DummyPacketSimulator extends ActionBarActivity {

	protected EditText editText_IpAddress;
	protected EditText editText_Port;
	protected EditText editText_MessageSize;
	protected EditText editText_LoopCount;
	protected EditText editText_Delay;
	protected TextView textView_DeviceInfo;
	protected Button button_SendMessage;
	protected TextView textView_Status;
	protected int port_Client = 33193;

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dummy_packet_simulator);
		getComponent();
		
		//test
		editText_IpAddress.setText("192.168.248.1");
		editText_Port.setText("6664");
		editText_MessageSize.setText("");
		editText_LoopCount.setText("10");
		editText_Delay.setText("2000");
	}
	
	protected void getComponent(){
		editText_IpAddress = (EditText) findViewById(R.id.editText_IpAddress);
		editText_Port = (EditText) findViewById(R.id.editText_Port);
		editText_MessageSize = (EditText) findViewById(R.id.editText_MessageSize);
		editText_LoopCount = (EditText) findViewById(R.id.editText_LoopCount);
		editText_Delay = (EditText) findViewById(R.id.editText_Delay);
		textView_DeviceInfo = (TextView) findViewById(R.id.textView_DeviceInfo);
		button_SendMessage = (Button) findViewById(R.id.button_SendMessage);
		textView_Status = (TextView) findViewById(R.id.textView_Status);
		textView_Status.setMovementMethod(new ScrollingMovementMethod());
	}

	public void sendMessage(View view){
		clearTextfield();
		setProgressBarIndeterminateVisibility(true);
		Log.d("sendMessage", "ready to send Message");
		Toast.makeText(DummyPacketSimulator.this, "Ready to send Message", Toast.LENGTH_SHORT).show();
		
		try {
			//dont forget check parameter is correct
			int loop = 1;
			if(!editText_LoopCount.getText().toString().matches("")) loop = Integer.parseInt(editText_LoopCount.getText().toString());

            textView_DeviceInfo.setText("Device Information \nIP Address : " + getIPAddress(true) + "\nClient Port : " + port_Client);
			
			for(int i = 0;i < loop;i++){
				JSONObject connectionInfo = getJSONValue(i + 1);
				UDPClient udpclient = new UDPClient(connectionInfo,textView_DeviceInfo,textView_Status);
				if(editText_MessageSize.getText().toString().matches("")) {
					udpclient.sendMessage("test");
				}else{
					udpclient.sendMessage(Integer.parseInt(editText_MessageSize.getText().toString()));
				}
			}
			textView_DeviceInfo.append("Finished");
			
		} catch (Exception e) {
			Log.d("DummyPacketSimulator", "Error to Execute");
			e.printStackTrace();
		}

		setProgressBarIndeterminateVisibility(false);
	}
	
	public JSONObject getJSONValue(int loop){
		JSONObject connectionInfo = new JSONObject();
		
		if(!editText_IpAddress.getText().toString().matches(""))
			try {
				connectionInfo.put("serverIp", editText_IpAddress.getText().toString());
				if(!editText_Port.getText().toString().matches("")) connectionInfo.put("clientPort", editText_Port.getText().toString());
				connectionInfo.put("clientPort", port_Client);
				if(!editText_MessageSize.getText().toString().matches("")) connectionInfo.put("size", editText_MessageSize.getText().toString());
				connectionInfo.put("loop", loop);
				if(!editText_Delay.getText().toString().matches("")) connectionInfo.put("delay", editText_Delay.getText().toString());
					
				//check ip has . 3 times
				//check port size loop delay is + integer only 
			} catch (JSONException e) {
				Log.d("DummyPacketSimulator - getJSONValue", "Input Data Invalid");
				e.printStackTrace();
			}
		
		return connectionInfo;
	}
	
	public static String getIPAddress(boolean useIPv4) {
        // useIPv4 = true  >> IPv4
		//  	   = false >> IPv6
		try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }

	public void clearTextfield(){
		textView_DeviceInfo.setText("");
		textView_Status.setText("");
	}
	
	public void clearEditText(){
		editText_IpAddress.setText("");
		editText_Port.setText("");
		editText_MessageSize.setText("");
		editText_LoopCount.setText("");
		editText_Delay.setText("");
	}

	public void clearScreen(View view){
		clearEditText();
		clearTextfield();
	}
}
