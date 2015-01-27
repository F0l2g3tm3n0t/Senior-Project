package lab.kultida.seniorproject;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lab.kultida.utility.TCP_Unicast_Receive;
import lab.kultida.utility.TCP_Unicast_Send;

public class PlaceholderFragment_ChatRoom extends PlaceholderFragment_Prototype {
    protected ListView listView_Chatroom;
    protected ChatListView adapter;
    protected TextView textView_Info;
    protected EditText editText_ChatRoom;
    protected Button button_ChatRoom;
    protected Calendar calendar;
    protected SimpleDateFormat time;
    protected SimpleDateFormat date;
	protected int clientPort = 20394;
	protected int serverPort = 22220;
    protected boolean chatroom_alreadyopen = false;
	protected String piIP = "192.168.42.1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);

        defaultOperation();
        getComponent();
        createChat();
        createTime();
        pullDataFromDatabase();
//        receiveBroadcast_Chatroom();
        chatroom_alreadyopen = true;

        return rootView;
    }

    protected void pullDataFromDatabase(){
        JSONArray data_frame_array = database.selectAllDataChatroom(new String[]{database.getTABLE_ChatRoom_Date(),database.getTABLE_ChatRoom_Time()});
        Log.d("pullDataFromDatabase()","data_frame_array : " + data_frame_array.toString());
        try {
            for(int i = 0;i < data_frame_array.length();i++){
                JSONObject data_frame = data_frame_array.getJSONObject(i);
                adapter.addChatMessage(data_frame);
                adapter.notifyDataSetChanged();
                listView_Chatroom.setSelection(adapter.getCount() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void receiveBroadcast_Chatroom(){
        JSONObject data = new JSONObject();
        try {
            data.put("serverPort",serverPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    new TCP_Unicast_Received_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data.toString());
	    } else {
		    new TCP_Unicast_Received_ChatRoom().execute(data.toString());
	    }
    }

    protected void getComponent(){
        listView_Chatroom = (ListView)rootView.findViewById(R.id.listView_ChatArea);
        textView_Info = (TextView)rootView.findViewById(R.id.textView_Info);
        editText_ChatRoom = (EditText)rootView.findViewById(R.id.editText_ChatArea);
        button_ChatRoom = (Button)rootView.findViewById(R.id.button_ChatArea);
        button_ChatRoom.setOnClickListener(this);
    }

    public void createChat(){
        ArrayList<String> user = new ArrayList<>();
        ArrayList<String> message = new ArrayList<>();
        ArrayList<String> time = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        ArrayList<Boolean> fromMe = new ArrayList<>();
        adapter = new ChatListView(activity,user,message,time,date,fromMe);
        listView_Chatroom.setAdapter(adapter);
    }

    public void createTime(){
        calendar = Calendar.getInstance();
        time = new SimpleDateFormat("HH:mm");
        date = new SimpleDateFormat("dd-MM-yyyy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_ChatArea:
                /* JSON Format
                    data_frame
                        fromMe
                        clientPort
                        serverPort
                        data
                            user
                            message
                            time
                            date
                */

                JSONObject data = new JSONObject();
                JSONObject data_frame = new JSONObject();
	            try {
		            data.put("user",activity.myUser);
		            data.put("message",editText_ChatRoom.getText().toString());
		            data.put("time",time.format(calendar.getTime()));
		            data.put("date",date.format(calendar.getTime()));
		            data.put("flag","chatroom");
		            data_frame.put("fromMe", true);
                    data_frame.put("clientPort", clientPort);
                    data_frame.put("serverPort", serverPort);
		            data_frame.put("serverIP", piIP);
		            data_frame.put("data", data);

                    String value[] = {data.getString("user"),data.getString("message"),data.getString("date"),data.getString("time"),data_frame.getString("fromMe")};
                    database.insertData(database.getTABLE_ChatRoom(),database.getTable_ChatRoom_Column(),value);
	            } catch (JSONException e) {
		            e.printStackTrace();
	            }
	            addChatMessage(data_frame);
                break;
        }
    }

    public void addChatMessage(JSONObject data_frame){
//	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
//		    new UDP_Broadcast_Send_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_frame.toString());
//	    else
//		    new UDP_Broadcast_Send_ChatRoom().execute(data_frame.toString());

	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		    new TCP_Unicast_send_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_frame.toString());
	    } else {
		    new TCP_Unicast_send_ChatRoom().execute(data_frame.toString());
	    }

        adapter.addChatMessage(data_frame);
        adapter.notifyDataSetChanged();
        listView_Chatroom.setSelection(adapter.getCount() - 1);
    }



//  <<--------------------------  ASYNCTASK OPERATION  ------------------------->>

	private class TCP_Unicast_send_ChatRoom extends TCP_Unicast_Send{
		@Override
		protected void onPreExecute() {
			log_Head = "TCP_Unicast_Send_ChatRoom";
			super.onPreExecute();
		}
	}

	private class TCP_Unicast_Received_ChatRoom extends TCP_Unicast_Receive {
		@Override
		protected void onPreExecute() {
			log_Head = "TCP_Unicast_Receive_ChatRoom";
			myAddress = activity.getIPAddress(true);
			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(String result) {
			if(!result.contains("Fail")){
				JSONObject data_frame = null;
				try {
					JSONObject data = new JSONObject(result);
					data_frame = new JSONObject();
					data_frame.put("data",data);
					data_frame.put("fromMe",false);

					String value[] = {data.getString("user"),data.getString("message"),data.getString("date"),data.getString("time"),data_frame.getString("fromMe")};
					database.insertData(database.getTABLE_ChatRoom(),database.getTable_ChatRoom_Column(),value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if(chatroom_alreadyopen){
					adapter.addChatMessage(data_frame);
					adapter.notifyDataSetChanged();
					listView_Chatroom.setSelection(adapter.getCount() - 1);
				}
			}

			//Start Server Again
			receiveBroadcast_Chatroom();
		}
	}
}
