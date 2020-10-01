package com.example.sqldistantlecture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
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
    String urlWebService, urlWebServicebase;
    //ipconfig/all
    HttpURLConnection co;
    URL url;
    // quand il y a un thread multitache
    //StrictMode.ThreadPolicy policy;
    InputStream inputStream = null;
    BufferedReader br;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = (TextView) findViewById(R.id.TV_Results);
        town = (EditText) findViewById(R.id.ET_town);
        urlWebServicebase = "http://192.168.43.182/accessrvtowns/accesSrvTowns.php?beginning=";
        //urlWebService = "http://172.16.47.51/Communes/svc_communes.php?debut=";
    }


    @SuppressLint("SetTextI18n")
    public void Search(View view){
        //quand il n'y a qu'un seul thread multitache
        //policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        urlWebService = urlWebServicebase + town.getText();
        results.setText("Towns beginning with " + town.getText() + " : " );
        AsyncTasks asyncTasks = new AsyncTasks();
        asyncTasks.execute();
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
    private class AsyncTasks extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return (getServerDataJSON(urlWebService));
        }

        @Override
        protected void onPostExecute(String res) {
            results.append(res);
        }

    }
}
