package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import lab.kultida.utility.UDPClient_Unicast;

public class PlaceholderFragment_AskForHelp extends Prototype {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ask_for_help, container, false);
        defaultOperation();
        getComponent();
        return rootView;
    }

    protected void getComponent(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                JSONObject condition = new JSONObject();
                try {
                    condition.put("serverIP","");
                    condition.put("serverPort","");
                    condition.put("clientPort","");
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


