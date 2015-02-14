package lab.kultida.seniorproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.http.conn.util.InetAddressUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lab.kultida.utility.BestLocationListener;
import lab.kultida.utility.BestLocationProvider;
import lab.kultida.utility.DataBase;
import lab.kultida.utility.JSON_Parser;
import lab.kultida.utility.TCP_Unicast_Auto_Send;
import lab.kultida.utility.TCP_Unicast_Send;
import lab.kultida.utility.UDP_Broadcast_Receive;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    protected String lastTag = "";
    protected int selected = 0;
    protected Ringtone ringtone;
    protected AudioManager audioManager;
    protected int streamMaxVolume;
    protected String serverIP = "10.0.0.99";
    protected String serverPort_AlarmSignal = "21234";
    protected String serverPort_UpdateLocate = "9998";
    protected String PIIP = "192.168.42.1";
    protected String PIPort_JSON = "9090";
    public DataBase database;
    protected PlaceholderFragment_Home fragment_home;
    protected PlaceholderFragment_AskForHelp fragment_ask_for_help;
    protected PlaceholderFragment_ChatRoom fragment_chatRoom;
	protected PlaceholderFragment_ChatArea fragment_chatArea;
    protected String myUser = "Anonymous";
    protected String myPhone = "";

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, GPSTracker);

    protected BestLocationProvider mBestLocationProvider;
    protected BestLocationListener mBestLocationListener;
    protected Calendar calendar;
    protected SimpleDateFormat time;
    protected SimpleDateFormat date;
    protected Double latitude,longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        createFragment();
        setContentView(R.layout.activity_main);

        defaultOperation();
        //updateLocate();
        createDatabase();
        setUpAlarm();
        //receiveBroadcast_AlarmSignal();
        welcomeUser();

        calendar = Calendar.getInstance();
        time = new SimpleDateFormat("HH:mm");
        date = new SimpleDateFormat("dd-MM-yyyy");

        fragment_chatRoom.activity = this;
        fragment_chatRoom.database = database;
        fragment_chatRoom.receiveBroadcast_Chatroom();

	    fragment_chatArea.activity = this;
	    fragment_chatArea.database = database;
	    fragment_chatArea.receiveBroadcast_Chatarea();
    }

    public void initLocation(){
        if(mBestLocationListener == null){
            mBestLocationListener = new BestLocationListener() {
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                public void onProviderEnabled(String provider) { }
                public void onProviderDisabled(String provider) { }
                public void onLocationUpdateTimeoutExceeded(BestLocationProvider.LocationType type) { }
                public void onLocationUpdate(Location location, BestLocationProvider.LocationType type, boolean isFresh) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("GPSHelper-onLocationChg","\nDate : " + date.format(calendar.getTime())
                            + "\nTime : " + time.format(calendar.getTime())
                            + "\nLatitude : " + latitude
                            + "\nLongitude : " + longitude
                            + "\nAltitude : " + location.getAltitude()
                            + "\nBearing : " + location.getBearing()
                            + "\nProvider : " + location.getProvider()
                            + "\nSpeed : " + location.getSpeed());
                }
            };

            if(mBestLocationProvider == null){
                mBestLocationProvider = new BestLocationProvider(this, true, true, 10000, 1000, 15000, 0);
            }
        }
    }

    public void onResume() {
        super.onResume();
        initLocation();
        mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);
    }

    public void onStop() {
        super.onStop();
        mBestLocationProvider.stopLocationUpdates();
    }

//	protected MulticastSocket multicastSocket(){
//		MulticastSocket msocket = null;
//		try {
//			msocket = new MulticastSocket(22220);
//			System.out.println("ChatRoom - (Multicast) " + "Receiving");
//			InetAddress group = InetAddress.getByName("224.0.0.1");
//			msocket.joinGroup(group);
//		} catch(Exception e){}
//		return msocket;
//	}

    protected void createFragment(){
        this.fragment_home = new PlaceholderFragment_Home();
        this.fragment_ask_for_help = new PlaceholderFragment_AskForHelp();
        this.fragment_chatRoom = new PlaceholderFragment_ChatRoom();
	    this.fragment_chatArea = new PlaceholderFragment_ChatArea();
    }

    protected void welcomeUser(){
        final AutoCompleteTextView input_user = new AutoCompleteTextView(this);
        input_user.setHint("First Name");
        ArrayAdapter<String> adapter_user = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,database.selectAllDataUser(null));
        input_user.setThreshold(1);
        input_user.setAdapter(adapter_user);

        final AutoCompleteTextView input_phone = new AutoCompleteTextView(this);
        input_phone.setHint("Phone Number");
        ArrayAdapter<String> adapter_phone = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,database.selectAllDataPhone(null));
        input_phone.setThreshold(1);
        input_phone.setAdapter(adapter_phone);

        LinearLayout layout = new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(input_user);
        layout.addView(input_phone);


        AlertDialog.Builder adb_getUser = new AlertDialog.Builder(this);
        adb_getUser.setTitle("Create User");
        adb_getUser.setMessage("Please Enter Your Name and Phone Number\n");
        adb_getUser.setView(layout);
        adb_getUser.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input_user.getText().toString().matches("")) {
                    myUser = input_user.getText().toString();
                }

                if (!input_phone.getText().toString().matches("")) {
                    myPhone = input_phone.getText().toString();
                }

                database.insertData(database.getTABLE_User(), database.getTable_User_Column(), new String[]{myUser});
                database.insertData(database.getTABLE_Phone(), database.getTable_Phone_Column(), new String[]{myPhone});

                Toast.makeText(MainActivity.this, "Welcome " + myUser + " : " + myPhone, Toast.LENGTH_SHORT).show();
            }

        });
        adb_getUser.show();
    }

    protected void createDatabase(){
        database = new DataBase(this); //start class DB
        database.getWritableDatabase(); // start create database and table


    }

    protected void receiveBroadcast_AlarmSignal(){
        JSONObject data = new JSONObject();
        try {
            data.put("serverPort",serverPort_AlarmSignal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new UDP_Broadcast_Receive_AlarmSignal().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data.toString());
        } else {
            new UDP_Broadcast_Receive_AlarmSignal().execute(data.toString());
        }
    }

	protected void updateLocate(){
        JSONObject data = new JSONObject();
		JSONObject data_frame = new JSONObject();
        try {
            data.put("annotation", "");
			data.put("signal", "updateLocate");
			data.put("clientIP", InetAddress.getByName(getIPAddress(true)));
			data.put("macaddress", getMacAddress());
            data.put("fromPi", getNetworkName());
            data.put("user", myUser);
            data.put("phone", myPhone);
	        data.put("lat", latitude);
	        data.put("long", longitude);

            data_frame.put("serverIP", serverIP);
            data_frame.put("serverPort", serverPort_UpdateLocate);
			data_frame.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}

        Log.d("MainActivity-updateLoc","TCP_Unicast_Send_UpdateLocation().execute(data_frame.toString()");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			new TCP_Unicast_Send_UpdateLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data_frame.toString());
		} else {
			new TCP_Unicast_Send_UpdateLocation().execute(data_frame.toString());
		}
		//new TCP_Unicast_Send_UpdateLocation().execute(data_frame.toString());

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
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
    }

    protected void alarmPlay(){
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
        if(ringtone != null){
            ringtone.play();
        }
    }

    protected void alarmStop(){
        audioManager.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume, AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
        if(ringtone != null){
            ringtone.stop();
        }
    }

    protected void getJSONData(){
        new JSON_Parser_MainActivity().execute(PIIP, PIPort_JSON);
    }

    protected void checkIP(){
        AlertDialog.Builder adb_CheckIp = new AlertDialog.Builder(MainActivity.this);
        adb_CheckIp.setTitle("Check IP Address");
        adb_CheckIp.setMessage("Network : " + ((WifiManager)getSystemService(WIFI_SERVICE)).getConnectionInfo().getSSID() + "\nIP Address : " + getIPAddress(true));
        adb_CheckIp.setPositiveButton("OK", null);
        adb_CheckIp.show();
    }

    protected void checkServerConnection(){
        setSupportProgressBarIndeterminateVisibility(true);
        JSONObject data = new JSONObject();
        JSONObject data_frame = new JSONObject();

        try {
            data.put("annotation", "");
            data.put("signal", "checkServerConnection");
            data.put("clientIP", InetAddress.getByName(getIPAddress(true)));
            data.put("macaddress", getMacAddress());
            data.put("fromPi", getNetworkName());

            data_frame.put("serverIP", serverIP);
            data_frame.put("serverPort", serverPort_UpdateLocate);
            data_frame.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }



        Log.d("MainActivity-checkIP+Sr", "TCP_Unicast_Send_CheckServerConnection().execute(data_frame.toString()");
        new TCP_Unicast_Send_CheckServerConnection().execute(data_frame.toString());

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

    protected String getNetworkAddress(){
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
            Log.d("MainAcitivity-getIPAddr","IP Address : " + ipString);
            return ipString;
        } catch (Exception e) {
            e.printStackTrace();
            return "Fail";
        }
    }

    protected String getNetworkName(){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()) {
            WifiInfo info = wifiManager.getConnectionInfo ();
            Log.d("networkName",info.getSSID());
            return info.getSSID().substring(1,info.getSSID().length() -1 );
        }
        return "error";
    }

    protected String getMacAddress(){
        WifiManager manager = (WifiManager)(getSystemService(Context.WIFI_SERVICE));
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    protected String getPhoneNumber(){
//        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
//        String phoneNumber = tm.getLine1Number();
//        Log.d("phoneNumber2",phoneNumber);
//        return phoneNumber;
        return "phone number";
    }

    /*Connect To WIFI*/
    //TODO What r u doing
    protected void connectToWifi(final String networkSSID){
        setSupportProgressBarIndeterminateVisibility(true);
//        String networkPass = "";
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
                while(true){
                    if (wifiManager.getConnectionInfo().getSSID().contains(networkSSID)) break;
                }
                AlertDialog.Builder adb_ConnectWIFI = new AlertDialog.Builder(this);
                adb_ConnectWIFI.setTitle("Connect WIFI");
                adb_ConnectWIFI.setMessage("Connect WIFI : " + networkSSID + " complete" + "\nThis device will connect to Rescue's WIFI in few second");
                adb_ConnectWIFI.setPositiveButton("Ok", null);
                adb_ConnectWIFI.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        connectToWifi(networkSSID);
                    }
                });
                adb_ConnectWIFI.show();
                setSupportProgressBarIndeterminateVisibility(false);
                return;
            }
        }

        AlertDialog.Builder adb_ConnectWIFI = new AlertDialog.Builder(this);
        adb_ConnectWIFI.setTitle("Connect WIFI");
        adb_ConnectWIFI.setMessage("Connect WIFI : " + networkSSID + " fail");
        adb_ConnectWIFI.setPositiveButton("Ok", null);
        adb_ConnectWIFI.show();
        setSupportProgressBarIndeterminateVisibility(false);
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
                    transaction.add(R.id.container, fragment_home, lastTag);
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
                    transaction.add(R.id.container, fragment_ask_for_help, lastTag);
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
                    transaction.add(R.id.container, fragment_chatRoom, lastTag);
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                mTitle = getString(R.string.title_section3);
                break;

	        case 3:
		        lastTag = "chatArea";
		        temp = fragmentManager.findFragmentByTag(lastTag);
		        if(temp != null){
			        transaction.show(temp);
		        }else{
			        transaction.add(R.id.container, fragment_chatArea, lastTag);
			        transaction.addToBackStack(null);
		        }
		        transaction.commit();
		        mTitle = getString(R.string.title_section4);
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
                checkIP();
                break;
            case R.id.checkServerConnection :
                checkServerConnection();
                break;
            case R.id.connectWifi :
                connectToWifi("My_AP_Pi");
                break;
            case R.id.action_getJSONData :
                getJSONData();
                break;
            case R.id.action_testAlarm :
                if(ringtone.isPlaying()) {
                    alarmStop();
                }else{
                    alarmPlay();
                }
                break;
            case R.id.action_clearChat :
                if(mTitle.toString().matches("Chat Room")) {
                    Toast.makeText(this,"Clear Chat",Toast.LENGTH_SHORT).show();
                    database.delelteAllData(database.getTABLE_ChatRoom());
                    fragment_chatRoom.adapter.clear();
                    fragment_chatRoom.adapter.notifyDataSetChanged();
                }
                else Toast.makeText(this,"Please switch page to chat room before use this operation",Toast.LENGTH_SHORT).show();
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
                                for (String aTemp_Split : temp_Split) {
                                    result = result + "\n" + aTemp_Split;
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

	protected class TCP_Unicast_Send_UpdateLocation extends TCP_Unicast_Auto_Send {

        @Override
        protected void onPreExecute() {
            log_Head = "TCP_Unicast_Send_UpdateLocation";
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
                updateLocate();
        }
    }

    protected class TCP_Unicast_Send_CheckServerConnection extends TCP_Unicast_Send {

        @Override
        protected void onPreExecute() {
            log_Head = "TCP_Unicast_Send_CheckServerConnection";
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder adb_CheckIp = new AlertDialog.Builder(MainActivity.this);
            adb_CheckIp.setTitle("Check Server Connection");
            adb_CheckIp.setMessage("Server Connection : " + result);
            adb_CheckIp.setPositiveButton("OK", null);
            adb_CheckIp.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setSupportProgressBarIndeterminateVisibility(false);
                    checkServerConnection();
                }
            });
            adb_CheckIp.show();
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }

    protected class UDP_Broadcast_Receive_AlarmSignal extends UDP_Broadcast_Receive {

        @Override
        protected void onPreExecute() {
            log_Head = "UDP_Broadcast_Receive_AlarmSignal";
            myAddress = getIPAddress(true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if(!result.contains("Fail")){
                try {
                    JSONObject data = new JSONObject(result);
                    if(data.getString("alarm").matches("alarm signal")){
                        if(!ringtone.isPlaying()){
                            AlertDialog.Builder adb_Alarm = new AlertDialog.Builder(MainActivity.this);
                            adb_Alarm.setTitle("Rescue Team are here");
                            adb_Alarm.setMessage("Rescue Team are here \nWe are searching you so do not turn off alarm");
                            adb_Alarm.setPositiveButton("OK", null);
                            adb_Alarm.show();
                        }
                        alarmPlay();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            receiveBroadcast_AlarmSignal();
        }
    }
}
