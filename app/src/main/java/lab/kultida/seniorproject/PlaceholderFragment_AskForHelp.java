package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import lab.kultida.utility.UDP_Unicast_Send;

public class PlaceholderFragment_AskForHelp extends PlaveholderFragment_Prototype {

    protected EditText editText_Annotation;
    protected Spinner spinner_Signal;
    protected Button button_AskForHelp;
    protected TextView textView_Output;
    protected int serverPort = 9998;
    protected int clientPort = 45808;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_AskForHelp:
                JSONObject condition = new JSONObject();
                try {
                    condition.put("serverIP", serverIP);
                    condition.put("clientPort",serverPort);
                    condition.put("clientPort",clientPort);
                    JSONObject data = new JSONObject();
                    data.put("annotation", editText_Annotation.getText());
                    data.put("signal",spinner_Signal.getSelectedItem().toString());
                    condition.put("data",data);
                    textView_Output.setText(condition.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UDP_Unicast_Send_AskForHelp().execute(condition.toString());
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
            socket.close();
        }
    }
}


