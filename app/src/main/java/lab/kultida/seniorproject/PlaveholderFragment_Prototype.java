package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

public class PlaveholderFragment_Prototype extends Fragment implements View.OnClickListener{
    protected Activity activity;
    protected boolean debugging_mode = true;
    protected View rootView;

    protected String serverIP = "192.168.42.11";
    protected int serverPort = 9998;
    protected int clientPort = 45808;


    protected void defaultOperation(){
        setRetainInstance(true);
        activity = getActivity();
    }
    
    @Override
    public void onClick(View v) {

    }
}


