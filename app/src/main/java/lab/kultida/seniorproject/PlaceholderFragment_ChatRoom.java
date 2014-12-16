package lab.kultida.seniorproject;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import lab.kultida.utility.UDP_Boardcast_Receive;
import lab.kultida.utility.UDP_Boardcast_Send;

public class PlaceholderFragment_ChatRoom extends PlaceholderFragment_Prototype {
    protected ListView listView_Chatroom;
    protected ChatListView adapter;
    protected TextView textView_Info;
    protected EditText editText_ChatRoom;
    protected Button button_ChatRoom;
    protected Calendar calendar;
    protected SimpleDateFormat time;
    protected SimpleDateFormat date;
    protected String myUser = "Anonymous";
	protected int clientPort = 20394;
	protected int serverPort = 22220;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);

        defaultOperation();
        welcomUser();
        getComponent();
        createChat();
        createTime();
//        addChatMessageTest();

        JSONObject data = new JSONObject();
        try {
            data.put("clientPort",clientPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
		    new UDP_Boardcast_Receive_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.toString());
	    else
		    new UDP_Boardcast_Receive_ChatRoom().execute(data.toString());


        return rootView;
    }

    protected void welcomUser(){
        final EditText input = new EditText(activity);
        AlertDialog.Builder adb_getUser = new AlertDialog.Builder(activity);
        adb_getUser.setTitle("Create User");
        adb_getUser.setMessage("Please Enter Your Name");
        adb_getUser.setView(input);
        adb_getUser.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!input.getText().toString().matches("")) myUser = input.getText().toString();
                Toast.makeText(activity,"Welcome " + myUser,Toast.LENGTH_SHORT).show();
            }
        });
        adb_getUser.show();
    }

    protected void getComponent(){
        listView_Chatroom = (ListView)rootView.findViewById(R.id.listView_ChatRoom);
        textView_Info = (TextView)rootView.findViewById(R.id.textView_Info);
        editText_ChatRoom = (EditText)rootView.findViewById(R.id.editText_ChatRoom);
        button_ChatRoom = (Button)rootView.findViewById(R.id.button_ChatRoom);
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
            case R.id.button_ChatRoom:
	            Log.d("Button Click", "o.");
                addChatMessage(myUser, editText_ChatRoom.getText().toString(), time.format(calendar.getTime()), date.format(calendar.getTime()), true);
                Log.d("sendMessage", myUser + " : " + editText_ChatRoom.getText().toString() + " : " + time.format(calendar.getTime()) + " : " + date.format(calendar.getTime()));
                break;
        }
    }

    public void addChatMessageTest(){
        JSONObject data = new JSONObject();
        try {
            data.put("user","eka");
            data.put("message","Sawasdee Krub");
            data.put("time","14.14");
            data.put("date",date.format(calendar.getTime()));
            data.put("fromMe",false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);adapter.addChatMessage(data);
        adapter.notifyDataSetChanged();
    }

    public void addChatMessage(JSONObject data){
        try {
            data.put("clientPort",clientPort);
	        data.put("serverPort",serverPort);
        } catch (JSONException e) {
            e.printStackTrace();
        }

	    Log.d("sending broadcast", data.toString());
        //new UDP_Boardcast_Send_ChatRoom().execute(data.toString());
	    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
		    new UDP_Boardcast_Send_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data.toString());
	    else
		    new UDP_Boardcast_Send_ChatRoom().execute(data.toString());
	    Log.d("sending broadcast", "Finished");
        adapter.addChatMessage(data);
        adapter.notifyDataSetChanged();
        listView_Chatroom.setSelection(adapter.getCount() - 1);
    }

    public void addChatMessage(JSONObject data,boolean fromMe){
        try {
	        String check = fromMe + "";
	        if(check == null){
		        fromMe = false;
	        }
            data.put("fromMe",fromMe);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	    addChatMessage(data);
    }

    public void addChatMessage(String user,String message,String time,String date,boolean fromMe){
	    Log.d("change to json" , "data to json");
        JSONObject data_frame = new JSONObject();
	    JSONObject data = new JSONObject();
        try {
            data.put("user",user);
            data.put("message",message);
            data.put("time",time);
            data.put("date",date);
            data_frame.put("fromMe", fromMe);
	        data_frame.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
	    if(!user.equals("system")) {
		    addChatMessage(data_frame);
	    }
    }

//    public void checkInfo(){
//        //TODO
//    }





//  <<--------------------------  ASYNTASK OPERATION  ------------------------->>
    protected class UDP_Boardcast_Send_ChatRoom extends UDP_Boardcast_Send {
        @Override
        protected void onPreExecute() {
	        Log.d("init", "Start sending broadcast");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
			Log.d("sending broadcast", "finished");
	        JSONObject data = new JSONObject();
	        JSONObject data_frame = new JSONObject();
	        try {
		        data.put("user", "system");
		        data.put("message", result);
		        data.put("time", time.format(calendar.getTime()));
		        data.put("date", date.format(calendar.getTime()));
		        data_frame.put("fromMe", false);
		        data_frame.put("data",data);
	        } catch (JSONException e) {
		        e.printStackTrace();
	        }
	        adapter.addChatMessage(data_frame);
	        adapter.notifyDataSetChanged();
	        listView_Chatroom.setSelection(adapter.getCount() - 1);
        }
    }



    protected class UDP_Boardcast_Receive_ChatRoom extends UDP_Boardcast_Receive {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
	        JSONObject data_frame = new JSONObject();
	        if(!result.contains("Fail")){
		        JSONObject data = null;
		        try {
			        data = new JSONObject(result);
			        data_frame.put("data",data);
			        data_frame.put("fromMe",false);
		        } catch (JSONException e) {
			        e.printStackTrace();
		        }
		        if(data != null) {
			        adapter.addChatMessage(data);
		        }
	        }

	        /*
	            ------------------
	            Start Server Again
	            ------------------
	         */
	        JSONObject newData = new JSONObject();
	        try {
		        newData.put("clientPort",clientPort);
	        } catch (JSONException e) {
		        e.printStackTrace();
	        }
	        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
		        new UDP_Boardcast_Receive_ChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,newData.toString());
	        else
		        new UDP_Boardcast_Receive_ChatRoom().execute(newData.toString());
        }
    }
}
