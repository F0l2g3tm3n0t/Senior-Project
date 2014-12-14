package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lab.kultida.utility.UDPClient_Boardcast;

public class PlaceholderFragment_ChatRoom extends Prototype {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        defaultOperation();
        getComponent();
        return rootView;
    }

    protected void getComponent(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }





//  <<--------------------------  ASYNTASK OPERATION  ------------------------->>
    protected class UDPClient_Boardcast_ChatRoom extends UDPClient_Boardcast {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
