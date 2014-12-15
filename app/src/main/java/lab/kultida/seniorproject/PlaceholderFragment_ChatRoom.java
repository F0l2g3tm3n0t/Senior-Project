package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lab.kultida.utility.UDPClient_Boardcast;

public class PlaceholderFragment_ChatRoom extends PlaveholderFragment_Prototype {
    ListView listView_Chatroom;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        defaultOperation();
        getComponent();
        addChatRoom();
        return rootView;
    }

    protected void getComponent(){
        listView_Chatroom = (ListView)rootView.findViewById(R.id.listView_ChatRoom);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void addChatRoom(){
        ArrayList<String> user = new ArrayList<>();
        user.add("ekapop");
        ArrayList<String> message = new ArrayList<>();
        message.add("Help me");
        ArrayList<String> time = new ArrayList<>();
        time.add("15.26");
        ChatListView adapter = new ChatListView(activity,user,message,time);
        listView_Chatroom.setAdapter(adapter);
        JSONObject data = new JSONObject();
        try {
            data.put("user","eka");
            data.put("message","sawasdee krub");
            data.put("time","14.14");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.addChatRoom(data);
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
