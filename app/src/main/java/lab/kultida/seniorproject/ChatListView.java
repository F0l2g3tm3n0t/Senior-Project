package lab.kultida.seniorproject;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChatListView extends ArrayAdapter<String>{

    private final Activity context;
    protected ArrayList<String> user;
    protected ArrayList<String> message;
    protected ArrayList<String> time;
    protected ArrayList<String> date;
    protected ArrayList<Boolean> fromMe;

    public ChatListView(Activity context,ArrayList<String> user,ArrayList<String> message,ArrayList<String> time,ArrayList<String> date,ArrayList<Boolean> fromMe){
        super(context, R.layout.chat_list,user);
        this.user = user;
        this.message = message;
        this.time = time;
        this.date = date;
        this.fromMe = fromMe;
        this.context = context;
    }

    public void addChatMessage(JSONObject data_frame){
        try {
            /* JSON Format
                data_frame
                    fromMe
                    data
                        user
                        message
                        time
                        date
            */

            fromMe.add(data_frame.getBoolean("fromMe"));

            JSONObject data = data_frame.getJSONObject("data");
            user.add(data.getString("user"));
            message.add(data.getString("message"));
            time.add(data.getString("time"));
            date.add(data.getString("date"));
            Log.d("ChatList-addChatMessage","data_frame = " + data_frame.toString());
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
        TextView textView_Date = (TextView)rowView.findViewById(R.id.textView_Date);

        textView_User.setText(user.get(position) + " : " + time.get(position));
        textView_Message.setText(message.get(position));

        if(fromMe.get(position)){
            textView_User.setGravity(Gravity.RIGHT);
            textView_Message.setGravity(Gravity.RIGHT);
            LinearLayout linearLayout_Main = (LinearLayout)rowView.findViewById(R.id.linearLayout_Main);
            linearLayout_Main.setGravity(Gravity.RIGHT);
        }
        if(position == 0 || !date.get(position).matches(date.get(position - 1))){
            textView_Date.setText(date.get(position));
        }else{
            textView_Date.setVisibility(View.INVISIBLE);
            textView_Date.setHeight(0);
        }
        return rowView;
    }
}