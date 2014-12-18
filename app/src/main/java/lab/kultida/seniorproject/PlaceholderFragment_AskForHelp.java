package lab.kultida.seniorproject;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import lab.kultida.utility.TCP_Unicast_Send;
import lab.kultida.utility.UDP_Unicast_Send;

public class PlaceholderFragment_AskForHelp extends PlaceholderFragment_Prototype {

    protected EditText editText_Annotation;
    protected Spinner spinner_Signal;
    protected Button button_AskForHelp;
    protected TextView textView_Output;
    protected int serverPort = 9998;
    protected int clientPort = 45808;
	protected InetAddress clientIP;
	//protected String serverIP;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ask_for_help, container, false);
        defaultOperation();
        getComponent();
        return rootView;
    }

    protected void getComponent(){
        editText_Annotation = (EditText)rootView.findViewById(R.id.editText_Annotation);
        spinner_Signal = (Spinner)rootView.findViewById(R.id.spinner_Signal);
        button_AskForHelp = (Button)rootView.findViewById(R.id.button_AskForHelp);
        button_AskForHelp.setOnClickListener(this);
        textView_Output = (TextView)rootView.findViewById(R.id.textView_Output);
    }
	protected String getIPAddress(boolean useIPv4) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_AskForHelp:
                JSONObject condition = new JSONObject();
                try {
	                clientIP = InetAddress.getByName(getIPAddress(true));
	                //serverIP = "192.168.42.15";
                    condition.put("serverIP", serverIP);
                    condition.put("serverPort",serverPort);
                    condition.put("clientPort",clientPort);
                    JSONObject data = new JSONObject();
                    data.put("annotation", editText_Annotation.getText());
                    data.put("signal",spinner_Signal.getSelectedItem().toString());
	                data.put("clientIP", clientIP);
	                data.put("clientPort", clientPort);
	                data.put("macaddress", getMacAddress());
                    condition.put("data",data);
	                Log.d("Ask For Help", "data before call method = " + condition.toString());
	                textView_Output.setText(condition.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //new UDP_Unicast_Send_AskForHelp().execute(condition.toString());
	            new TCP_Unicast_Send_AskForHelp().execute(condition.toString());
                break;
        }
    }





//  <<--------------------------  ASYNTASK OPERATION  ------------------------->>
    protected class UDP_Unicast_Send_AskForHelp extends UDP_Unicast_Send {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            textView_Output.append("\n\n" + condition.toString());
            textView_Output.append("\n" + result);
	        if(socket!=null){
		        socket.close();
	        }
        }
    }

	protected class TCP_Unicast_Send_AskForHelp extends TCP_Unicast_Send {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			//textView_Output.append("\n\n" + condition.toString());
			textView_Output.append("\n" + result);
		}
	}
}


