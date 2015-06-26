package greentoad.com.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener,View.OnFocusChangeListener {

    ArrayList<String> Names;
    ListView lstNames;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EnlazarControlesXML();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GenerateList(JSONArray Array){

        lstNames = (ListView) findViewById(R.id.lstNames);
        Names= new ArrayList<>();

        for(int i = 0 ; i < Array.length(); i++){

            try {

                JSONObject obj=Array.getJSONObject(i);
                if (obj!=null) {

                    Names.add(obj.getString("DNI") + " " + obj.getString("nombre") + " " + obj.getString("apellido1") );

                }

            } catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "No se ha podido realizar la consulta", Toast.LENGTH_LONG).show();

            }

        }

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Names);
        lstNames.setAdapter(adapter);

    }

    private void Clean(){

       /* nombre.setText("");
        apellido.setText("");
        SIP.setText("");
        telefono.setText("");*/
        //JsonText.setText("");
    }

    private void EnlazarControlesXML(){
        Button b1=(Button)findViewById(R.id.button1);
        Button btnConsultar=(Button)findViewById(R.id.btnConsultar);
        //JsonText=(EditText)findViewById(R.id.jsonText);

        //Valores por defecto
        //DNI.setText("123456");



        b1.setOnClickListener(this);
        btnConsultar.setOnClickListener(this);
        //DNI.setOnFocusChangeListener(this);

    }

    // Eventos

    public void onClick(View view) {
        try{
            switch (view.getId()) {
                case R.id.button1:
                    //InsertarUsuario(DNI.getText().toString(), nombre.getText().toString(), apellido.getText().toString(), telefono.getText().toString(), SIP.getText().toString());
                    break;
                case R.id.btnConsultar:
                    //GetUsers();
                    break;
            }
        }catch(Exception e){

            Toast.makeText(getApplicationContext(), "Error en el envio de la informacion, verifique su conexion a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show();

        }
    }

    public void onFocusChange(View view,boolean hasFocus){
        if(hasFocus==false){

            //GetUserByDNI(DNI.getText().toString());

        }

    }






}
