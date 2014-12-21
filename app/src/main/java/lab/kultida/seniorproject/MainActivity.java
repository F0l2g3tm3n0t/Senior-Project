package lab.kultida.seniorproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lab.kultida.utility.JSON_Parser;
import lab.kultida.utility.TCP_Unicast_Send;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    protected String lastTag = "";
    protected int selected = 0;
    protected Ringtone ringtone;
    protected AudioManager audioManager;
    protected int streamMaxVolume;
    protected String serverIP = "1.1.1.99";
    protected String serverPort_UpdateLocate = "9998";
    protected String PIIP = "192.168.42.1";
    protected String PIPort_JSON = "9090";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultOperation();
        updateLocate();
        setUpAlarm();
    }

	protected void updateLocate(){
        JSONObject data = new JSONObject();
		JSONObject data_frame = new JSONObject();

        try {
            /* JSON Format
                data_frame
                    serverIP
                    serverPort
                    data
                        annotation
                        signal
                        clientIP
                        macaddress
                        fromPI
             */

            data.put("annotation", "");
			data.put("signal", "updateLocate");
			data.put("clientIP", InetAddress.getByName(getIPAddress(true)));
			data.put("macaddress", getMacAddress());
            data.put("fromPi", getPIAddress());

            data_frame.put("serverIP", serverIP);
            data_frame.put("serverPort", serverPort_UpdateLocate);
			data_frame.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}

        Log.d("MainActivity - updateLocate()","TCP_Unicast_Send_UpdateLocation().execute(data_frame.toString()");
		new TCP_Unicast_Send_UpdateLocation().execute(data_frame.toString());

	}

    protected void defaultOperation(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    protected void setUpAlarm(){
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
        audioManager = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    protected void alarm() {
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
        if(ringtone != null){
            if(ringtone.isPlaying()) {
                Log.d("MainActivity - alarm","alarm stop");
                ringtone.stop();
            }
            else {
                Log.d("MainActivity - alarm","alarm play");
                ringtone.play();
            }
        }
    }

    protected void getJSONData(){
        new JSON_Parser_MainActivity().execute(PIIP, PIPort_JSON);
    }

    protected void checkIPAndServerConnection(){
        setProgressBarIndeterminateVisibility(true);
        AlertDialog.Builder adb_CheckIp = new AlertDialog.Builder(this);
        adb_CheckIp.setTitle("Check IP Address");
        adb_CheckIp.setMessage("IP Address : " + getIPAddress(true) + "\nServer Connection : " + checkServerConnection());
        adb_CheckIp.setPositiveButton("OK", null);
        adb_CheckIp.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setProgressBarIndeterminateVisibility(false);
                checkIPAndServerConnection();
            }
        });
        adb_CheckIp.show();
        setProgressBarIndeterminateVisibility(false);
    }

    protected String checkServerConnection(){
        //TODO
        return "connected";
    }

    public String getIPAddress(boolean useIPv4) {
        // useIPv4 = true  >> IPv4
        //  	   = false >> IPv6
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    protected String getPIAddress(){
        WifiManager manager = (WifiManager)(getSystemService(Context.WIFI_SERVICE));
        WifiInfo wifiInfo = manager.getConnectionInfo();
        try {
            int ip = wifiInfo.getIpAddress();
            String ipString = String.format(
                    "%d.%d.%d.%d",
                    (ip & 0xff),
                    (ip >> 8 & 0xff),
                    (ip >> 16 & 0xff),
                    (ip >> 24 & 0xff));
            Log.d("MainAcitivity - getIPAddress","IP Address : " + ipString);
            return ipString;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    protected String getMacAddress(){
        WifiManager manager = (WifiManager)(getSystemService(Context.WIFI_SERVICE));
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    /*Connect To WIFI*/
    //TODO What r u doing
    protected void connectToWifi(){
        setProgressBarIndeterminateVisibility(true);
        String networkSSID = "My_AP";
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

        //Check current WIFI
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "wifiManager.isWifiEnabled() : " + wifiManager.isWifiEnabled(), Toast.LENGTH_SHORT).show();
            WifiInfo info = wifiManager.getConnectionInfo ();
            if(info.getSSID().contains(networkSSID)){
                Toast.makeText(this,"still connected : " + info.getSSID(),Toast.LENGTH_SHORT).show();
                return;
            }
        }

//	    List<ScanResult> temp = wifiManager.getScanResults();
//		for(int z = 0; z < temp.size(); z++){
//			Log.d("Wifi List" + z, temp.get(z).SSID.toString() + ", " + temp.get(z).level);
//		}

        //Add config to Wifi Manager
	    wifiManager.addNetwork(conf);

        //enable Wifi
        while(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }

        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.contains("" + networkSSID + "")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                while(!wifiManager.getConnectionInfo().getSSID().contains(networkSSID)){}
                AlertDialog.Builder adb_ConnectWIFI = new AlertDialog.Builder(this);
                adb_ConnectWIFI.setTitle("Connect WIFI");
                adb_ConnectWIFI.setMessage("Connect WIFI : " + networkSSID + " complete" + "\nThis device will connect to Rescue's WIFI in few second");
                adb_ConnectWIFI.setPositiveButton("Ok", null);
                adb_ConnectWIFI.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connectToWifi();
                    }
                });
                adb_ConnectWIFI.show();
                setProgressBarIndeterminateVisibility(false);
                return;
            }
        }

        AlertDialog.Builder adb_ConnectWIFI = new AlertDialog.Builder(this);
        adb_ConnectWIFI.setTitle("Connect WIFI");
        adb_ConnectWIFI.setMessage("Connect WIFI : " + networkSSID + " fail");
        adb_ConnectWIFI.setPositiveButton("Ok", null);
        adb_ConnectWIFI.show();
        setProgressBarIndeterminateVisibility(false);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the menu_main content by replacing fragments
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
                }else{
                    transaction.add(R.id.container, new PlaceholderFragment_Home(), lastTag);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                mTitle = getString(R.string.title_section1);
                break;

            case 1:
                lastTag = "askForHelp";
                temp = fragmentManager.findFragmentByTag(lastTag);
                if(temp != null){
                    transaction.show(temp);
                }else{
                    transaction.add(R.id.container, new PlaceholderFragment_AskForHelp(), lastTag);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                mTitle = getString(R.string.title_section2);
                break;

            case 2:
                lastTag = "chatRoom";
                temp = fragmentManager.findFragmentByTag(lastTag);
                if(temp != null){
                    transaction.show(temp);
                }else{
                    transaction.add(R.id.container, new PlaceholderFragment_ChatRoom(), lastTag);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
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
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.checkIp :
                checkIPAndServerConnection();
                break;
            case R.id.connectWifi :
                connectToWifi();
                break;
            case R.id.action_getJSONData:
                getJSONData();
                break;
            case R.id.action_testAlarm:
                alarm();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //  <<--------------------------  ASYNCTASK OPERATION  ------------------------->>
    protected class JSON_Parser_MainActivity extends JSON_Parser {
        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final String result) {
            setProgressBarIndeterminateVisibility(false);
            if(!result.contains("fail")){
                ArrayList<String> arrayList = new ArrayList<>();
                JSONObject data= null;
                try {
                    data = new JSONObject(result);
                    Iterator keys = data.keys();
                    while(keys.hasNext()) {
                        String currentDynamicKey = (String)keys.next();
                        arrayList.add(currentDynamicKey);
//                        Log.d("currentDynamicKey",currentDynamicKey);
//                        Log.d("data.get(currentDynamicKey).toString()",data.get(currentDynamicKey).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final String[] data_list = arrayList.toArray(new String[arrayList.size()]);
                selected = 0;
                final Button button = new Button(MainActivity.this);
                button.setText("Filter JSON Data");
                AlertDialog.Builder adb_GetJSONData = new AlertDialog.Builder(MainActivity.this);
                adb_GetJSONData.setTitle("Get JSON Data From PI "+ PIIP + ":" + PIPort_JSON +" Complete");
                adb_GetJSONData.setSingleChoiceItems(data_list, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int item) {
                        selected = item;
                    }
                });
                adb_GetJSONData.setView(button);
                final JSONObject finalData = data;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder adb_GetJSONData = new AlertDialog.Builder(MainActivity.this);
                        adb_GetJSONData.setTitle("Get JSON Data : " + data_list[selected]);
                        if (finalData != null) {
                            try {
                                String temp = finalData.get(data_list[selected]).toString();
                                String[] temp_Split = temp.split(",");
                                String result = "";
                                for(int i = 0;i < temp_Split.length;i++){
                                    result = result + "\n" + temp_Split[i];
                                }
                                adb_GetJSONData.setMessage(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adb_GetJSONData.setPositiveButton("OK", null);
                        adb_GetJSONData.show();
                    }
                });
                adb_GetJSONData.setPositiveButton("OK", null);
                adb_GetJSONData.setNegativeButton("Open File", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(android.content.Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "text/plain");
                        startActivity(intent);
                    }
                });
                adb_GetJSONData.show();
            }else{
                AlertDialog.Builder adb_GetJSONData = new AlertDialog.Builder(MainActivity.this);
                adb_GetJSONData.setTitle("Get JSON Data");
                adb_GetJSONData.setMessage("Get JSON Data From PI "+ PIIP + ":" + PIPort_JSON +" Failed");
                adb_GetJSONData.setPositiveButton("OK", null);
                adb_GetJSONData.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getJSONData();
                    }
                });
                adb_GetJSONData.show();
            }
        }
    }

	protected class TCP_Unicast_Send_UpdateLocation extends TCP_Unicast_Send {

        @Override
        protected void onPreExecute() {
            log_Head = "TCP_Unicast_Send_UpdateLocation";
            super.onPreExecute();
        }

        protected void sleep(){
//            try {
//                Log.d(log_Head + " - onPostExecute","Thread sleep");
//                Thread.sleep(60000); // wait for 1 minute
//                Log.d(log_Head + " - onPostExecute","Thread wake up");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        @Override
		protected void onPostExecute(String result) {
//            updateLocate();
		}
	}
}
