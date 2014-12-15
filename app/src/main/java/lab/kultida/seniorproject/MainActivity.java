package lab.kultida.seniorproject;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.*;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    protected String lastTag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultOperation();


    }

    public void defaultOperation(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(!lastTag.matches("")){
            Fragment lastFragment = fragmentManager.findFragmentByTag( lastTag );
            if ( lastFragment != null ) {
                transaction.hide( lastFragment );
            }
            Log.d("hide last tag",lastTag);
        }

        Fragment temp;
        switch (position){
            case 0:
                lastTag = "home";
                temp = fragmentManager.findFragmentByTag(lastTag);
                if(temp != null){
                    transaction.show(temp);
                    Log.d(lastTag + " switch","temp != null");
                }else{
                    transaction.add(R.id.container,new PlaceholderFragment_Home(),lastTag);
                    transaction.addToBackStack(null);
                    Log.d(lastTag + "switch","temp == null");
                }
                transaction.commit();
                mTitle = getString(R.string.title_section1);
                break;

            case 1:
                lastTag = "askForHelp";
                temp = fragmentManager.findFragmentByTag(lastTag);
                if(temp != null){
                    transaction.show(temp);
                    Log.d(lastTag + " switch","temp != null");
                }else{
                    transaction.add(R.id.container,new PlaceholderFragment_AskForHelp(),lastTag);
                    transaction.addToBackStack(null);
                    Log.d(lastTag + "switch","temp == null");
                }
                transaction.commit();
                mTitle = getString(R.string.title_section2);
                break;

            case 2:
                lastTag = "chatRoom";
                temp = fragmentManager.findFragmentByTag(lastTag);
                if(temp != null){
                    transaction.show(temp);
                    Log.d(lastTag + " switch","temp != null");
                }else{
                    transaction.add(R.id.container,new PlaceholderFragment_ChatRoom(),lastTag);
                    transaction.addToBackStack(null);
                    Log.d(lastTag + "switch","temp == null");
                }
                transaction.commit();
                mTitle = getString(R.string.title_section3);
                break;
        }

//        Log.d("onNavigationDrawerItemSelected","position + 1 : " + (position + 1));
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.connectWifi) {
            try {
                connectToWifi();
            } catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*Connect To WIFI*/
    public void connectToWifi(){
        String networkSSID = "My_AP_Pi2";
        String networkPass = "";
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";

        //For WEP authen
		/*
		conf.wepKeys[0] = "\"" + networkPass + "\"";
		conf.wepTxKeyIndex = 0;
		conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
		conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		*/

        //For WPA authen
		/*
		conf.preSharedKey = "\""+ networkPass +"\"";
		*/

        //For Open network
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        //Add config to Wifi Manager

        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        wifiManager.addNetwork(conf);

        //enable Wifi

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();

                break;
            }
        }
    }
}
