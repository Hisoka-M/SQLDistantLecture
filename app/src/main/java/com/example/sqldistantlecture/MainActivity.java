package com.example.sqldistantlecture;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextView results;
    EditText town;
    String urlWebService = "http://192.168.43.182/accessrvtowns/accesSrvTowns.php?beginning=";
    //ipconfig car l'adresse local ne fonctionne pas quand il y a deux appareils différents


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = (TextView) findViewById(R.id.TV_Results);
        town = (EditText) findViewById(R.id.ET_town);
    }
    private String getServerDataRawText(String urlServiceWeb){
        return "a faire";

        // must have a return statement
    }
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