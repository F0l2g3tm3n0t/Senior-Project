package lab.kultida.seniorproject;

/**
 * Created by ekapop on 14/12/2557.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PlaceholderFragment_Home extends Prototype{
    protected TextView textView;
    protected Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        defaultOperation();
        getComponent();
        return rootView;
    }

    protected void getComponent(){
        textView = (TextView)rootView.findViewById(R.id.section_label);
        button = (Button)rootView.findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                textView.setText(":P");
                break;
        }
    }
}
