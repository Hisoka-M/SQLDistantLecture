package com.example.sqldistantlecture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {
    TextView results;
    EditText town;
    InputStream is;
    // lien avec le PHP qui procède à la requête
    String urlWebService = "http://172.16.47.101/accessrvtowns/accesSrvTowns.php?beginning=";
    //ipconfig car l'adresse local ne fonctionne pas quand il y a deux appareils différents
    HttpURLConnection co;
    URL url;
    BufferedReader br;
    StrictMode.ThreadPolicy policy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = (TextView) findViewById(R.id.TV_Results);
        town = (EditText) findViewById(R.id.ET_town);
        is = null;
        policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

    }
    private String getServerDataRawText(String urlWebService){
        StringBuilder str = new StringBuilder();
        String line;

        StrictMode.setThreadPolicy(policy);
        try {

            // échange http avec le serveur
            url = new URL(urlWebService);
            co.connect();
            is = co.getInputStream(); // réponse HTTP
            co = (HttpURLConnection)url.openConnection();
            // exploitation/analyse de la réponse
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) !=null) {
                str.append(" - ").append(line).append("\n");
            }
        } catch (Exception exception){
            Log.e("log_tag", "Error during data reading :" + exception.toString());
        }
        return str.toString();

        // must have a return statement
    }
    @SuppressLint("SetTextI18n")
    public void Search(View view){
        urlWebService += town.getText();
        results.setText("Towns beginning with " + town.getText() + " : " );
        results.append(getServerDataRawText(urlWebService));



        //if (town.getText().toString().matches("")) {
          //  Toast.makeText(this, "No town searched", Toast.LENGTH_LONG).show();
           // return;
        //}
        // results.append("\n Towns beginning with " + town.getText()  + ":\n");
        // show off the result : results.append(mySQLiteDB.townBeginningWith(town.getText().toString()));
    }
}