package com.example.sqldistantlecture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    TextView results;
    EditText town;
    // lien avec le PHP qui procède à la requête
    //String urlWebService = "http://172.16.47.51/Communes/svc_communes.php?debut=";
    String urlWebService = "http://172.16.47.101/accessrvtowns/accesSrvTowns.php?beginning=";
    //ipconfig/all
    HttpURLConnection co;
    URL url;
    StrictMode.ThreadPolicy policy;
    InputStream inputStream = null;
    BufferedReader br;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        results = (TextView) findViewById(R.id.TV_Results);
        town = (EditText) findViewById(R.id.ET_town);
    }


    @SuppressLint("SetTextI18n")
    public void Search(View view){
        urlWebService += town.getText();
        results.setText("Towns beginning with " + town.getText() + " : " );
        results.append(getServerDataJSON(urlWebService));
    }


    private String getServerDataRawText(String urlWebService){
        StringBuilder str = new StringBuilder();
        String line;

        try {
            // échange http avec le serveur
            url  = new URL(urlWebService);
            co = (HttpURLConnection)url.openConnection();
            co.connect();
            inputStream = co.getInputStream(); // réponse HTTP

            // exploitation/analyse de la réponse
            br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) !=null) {
                str.append(" - ").append(line).append("\n"); // concatenation
            }
        } catch (Exception exception){
            Log.e("log_tag", "Error during data reading :" + exception.toString());
        }
        return str.toString();
    }


    private String getServerDataJSON(String urlWebService){
        String str = "";
        String line;

        try {
            // échange http avec le serveur
            url  = new URL(urlWebService);
            co = (HttpURLConnection)url.openConnection();
            co.connect();
            inputStream = co.getInputStream(); // réponse HTTP

            // exploitation/analyse de la réponse
            br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) !=null) {
                str+= line + "\n"; // concatenation
            }
        } catch (Exception exception){
            Log.e("log_tag", "Error during data reading :" + exception.toString());
        }

        try {
            jsonArray = new JSONArray(str);
            str = "\n";
            for (int i = 0; i<jsonArray.length(); i++ ) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                str+= " - " + jsonObject.getString("townName") + "\n";
            }
        } catch (JSONException exept){
            Log.e("log_tag", "Erreur pdt ana data" + exept.toString());
        }

        return str;
    }
}