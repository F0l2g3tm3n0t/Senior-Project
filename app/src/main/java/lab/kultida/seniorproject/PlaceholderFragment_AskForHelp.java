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

import lab.kultida.utility.UDPClient_Unicast;

public class PlaceholderFragment_AskForHelp extends Prototype {

    EditText editText_Annotation;
    Spinner spinner_Signal;
    Button button_AskForHelp;
    TextView textView_Output;

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
                    condition.put("serverIP",serverIP);
                    condition.put("serverPort",serverPort);
                    condition.put("clientPort",clientPort);
                    JSONObject data = new JSONObject();
                    data.put("annotation", editText_Annotation);
                    data.put("signal",spinner_Signal.getSelectedItem().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new UDPClient_Unicast_AskForHelp().execute(condition.toString());
                break;
        }
    }





//  <<--------------------------  ASYNTASK OPERATION  ------------------------->>
    protected class UDPClient_Unicast_AskForHelp extends UDPClient_Unicast{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}


