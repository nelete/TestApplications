package greentoad.com.myapplication;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ArrayList<String> Names;
    ListView lstNames;



    @Override
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



    private boolean NetworkIsConnected(){

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void GenerateList(){

        //lstNames = (ListView) findViewById(R.id.lstNames);
        Names=new ArrayList<String>();
        Names.add ("Nelo");
        Names.add("Ana");
        Names.add("Alfredo");
        Names.add("Olga");
        Names.add("Juan");
        Names.add("Santi");

        ArrayAdapter<String> adapter;
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Names);
        //lstNames.setAdapter(adapter);

    }


    public void httpSendData(String mURL) {
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(mURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.disconnect();

        }
        catch (MalformedURLException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
        }
        catch (IOException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
        }
        finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }
    }


    public String httpGetData(String mURL) {
        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(mURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.disconnect();
            return("Hello");
        }
        catch (MalformedURLException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
            return(ex.getMessage());
        }
        catch (IOException ex) {
            Log.e("httptest", Log.getStackTraceString(ex));
            return(ex.getMessage());
        }
        finally {
            try {
                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace(); //If you want further info on failure...
            }
        }
    }


    private void EnlazarControlesXML(){
        Button b1=(Button)findViewById(R.id.button1);
        final EditText DNI=(EditText)findViewById(R.id.editTextCedula);
        final EditText nombre=(EditText)findViewById(R.id.editTextNombre);
        final EditText apellido=(EditText)findViewById(R.id.editTextApellido);
        final EditText telefono=(EditText)findViewById(R.id.editTextTelefono);
        final EditText SIP=(EditText)findViewById(R.id.editTextSIP);

        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    httpSendData("http://10.0.2.2/prueba/registrarUsuario.php?DNI=" + DNI.getText() +
                            "&Nombre=" + nombre.getText() + "&Apellido=" + apellido.getText() +
                            "&Telefono=" + telefono.getText() + "&SIP=" + SIP.getText() );
                            Toast.makeText(getApplicationContext(), "El dato ha sido enviado correctamente", Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error en el envio de la informacion, verifique su conexion a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show();

                }

            }
        });

        DNI.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus==false){
                    JSONArray ja=null;
                    try {
                        String data;
                        data=httpGetData("http://10.0.2.2/prueba/consultarUsuario.php?cc="+DNI.getText());
                        if(data.length()>1)
                            ja=new JSONArray(data);


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error recuperando la informacion del servidor, verifique su conexion a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show();

                    }
                    try{

                        nombre.setText(ja.getString(1));
                        apellido.setText(ja.getString(2));
                        telefono.setText(ja.getString(3));
                        SIP.setText(ja.getString(4));
                    } catch (Exception e) {

                        nombre.setText("");
                        apellido.setText("");
                        SIP.setText("");
                        telefono.setText("");
                    }
                }
            }
        });
    }

}
