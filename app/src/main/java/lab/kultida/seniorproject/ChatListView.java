package lab.kultida.seniorproject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatListView extends ArrayAdapter<String>{
    private final Activity context;
    protected ArrayList<String> user;
    protected ArrayList<String> message;
    protected ArrayList<String> time;

    public ChatListView(Activity context,ArrayList<String> user,ArrayList<String> message,ArrayList<String> time){
        super(context, R.layout.chat_list,user);

        this.user = user;
        this.message = message;
        this.time = time;

        Log.d("user", user.toString());
        Log.d("message",message.toString());
        Log.d("time",time.toString());

//        user = new ArrayList<>();
//        message = new ArrayList<>();
//        time = new ArrayList<>();
        this.context = context;
    }

    public void addChatRoom(JSONObject data){
        try {
            user.add(data.getString("user"));
            message.add(data.getString("message"));
            time.add(data.getString("time"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.chat_list, null, true);
        TextView textView_User = (TextView) rowView.findViewById(R.id.textView_User);
        TextView textView_Message = (TextView) rowView.findViewById(R.id.textView_Message);
        TextView textView_Time = (TextView) rowView.findViewById(R.id.textView_Time);

        Log.d("user getView " + position + " :",user.toString());
        Log.d("message getView " + position + " :",message.toString());
        Log.d("time getView " + position + " :",time.toString());

        textView_User.setText(user.get(position));
        textView_Message.setText(message.get(position));
        textView_Time.setText(time.get(position));

        return rowView;
    }
}