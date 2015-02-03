package lab.kultida.seniorproject;



import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.InetAddress;

import lab.kultida.utility.TCP_Unicast_Send;

public class PlaceholderFragment_AskForHelp extends PlaceholderFragment_Prototype {

    protected EditText editText_Annotation;
    protected Spinner spinner_Signal;
    protected Button button_AskForHelp;
    protected TextView textView_Output;
    protected int serverPort = 9998;
    protected int clientPort = 45808;
	protected InetAddress clientIP;


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
        textView_Output.setMovementMethod(new ScrollingMovementMethod());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_AskForHelp:
                 /* JSON Format
                    data_frame
                        serverIP
                        serverPort
                        clientPort
                        data
                            annotation
                            signal
                            clientIP
                            clientPort
                            macAddress
                            fromPI
                */

                JSONObject data = new JSONObject();
                JSONObject data_frame = new JSONObject();
                try {
                    data.put("annotation", editText_Annotation.getText());
                    data.put("signal", spinner_Signal.getSelectedItem().toString());
                    data.put("clientIP", InetAddress.getByName(activity.getIPAddress(true)));
                    data.put("macaddress", getMacAddress());
                    data.put("fromPi", activity.getNetworkName());
                    data.put("user", activity.myUser);
                    data.put("phone", activity.myPhone);
					data.put("lat", activity.latitude);
	                data.put("long", activity.longitude);
                    data_frame.put("serverIP", serverIP);
                    data_frame.put("serverPort", serverPort);
                    data_frame.put("data", data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("Placeholder_AskForHelp - Click()", "TCP_Unicast_Send_AskForHelp()execute(data_frame.toString()");
	            new TCP_Unicast_Send_AskForHelp().execute(data_frame.toString());
                break;
        }
    }

//  <<--------------------------  ASYNCTASK OPERATION  ------------------------->>
	protected class TCP_Unicast_Send_AskForHelp extends TCP_Unicast_Send {

    @Override
    protected void onPreExecute() {
        log_Head = "TCP_Unicast_Send_AskForHelp";        super.onPreExecute();
    }

    @Override
		protected void onPostExecute(String result) {
			textView_Output.append("Result : " + result + "\n");
		}
	}
}


