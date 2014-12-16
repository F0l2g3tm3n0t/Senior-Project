package lab.kultida.utility;

import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ekapop on 16/12/2557.
 */
public class JSON_Parser_NotAsyncTask {
    protected String ipv4;
    protected String port;

    public String run(String... arg0) {
        String result = null;
        try {
            ipv4 = arg0[0];
            port = arg0[1];
            result = readJsonFromIpv4(ipv4,port).toString();
            saveJSONToFile(result);
            return "completed";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "failed";
    }

    protected void saveJSONToFile(String result){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/SeniorProject");
        myDir.mkdirs();
        Calendar calendar = Calendar.getInstance();
        DateFormat time = new SimpleDateFormat("dd-MM-yyyy   HH:mm.txt");
        String fname = "JSON data from PI " + ipv4 + ":" + port + " : " + time.format(calendar.getTime()) + ".txt";
        File file = new File (myDir, fname);
        try {
            FileOutputStream out = new FileOutputStream(file, true);
            String temp = result;
            out.write(temp.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readAll(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while ((cp = reader.read()) != -1) {
            stringBuilder.append((char) cp);
        }
        return stringBuilder.toString();
    }

    public JSONObject readJsonFromIpv4(String ipv4,String port) throws IOException, JSONException {
        String url = "http://" + ipv4 + ":" + port;
        Log.d("url",url);
        InputStream inputStream = new URL(url).openStream();
        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String result = readAll(bufferedReader);
            JSONObject json = new JSONObject(result);
            return json;
        } finally {
            inputStream.close();
        }
    }

}