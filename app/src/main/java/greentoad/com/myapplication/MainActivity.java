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

    private EditText DNI=null;
    private EditText nombre=null;
    private EditText apellido=null;
    private EditText telefono=null;
    private EditText SIP=null;

    final String LocalCall= "http://10.0.2.2/ToadService/GetUserByDNI.php?DNI=";
    final String RemoteCall= "http://greentoad.esy.es/Prueba/ConsultarUsuario.php?DNI=";

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

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void GenerateList(){

        //lstNames = (ListView) findViewById(R.id.lstNames);
        Names= new ArrayList<>();
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


   //Métodos Privados

    private void InsertarUsuario(String numDNI,String nomb,String ape,String telef ,String numSIP){
        boolean Conected=NetworkIsConnected();
        if (Conected) {

            httpSendDataTask Request=new httpSendDataTask();

            Request.execute("http://10.0.2.2/prueba/registrarUsuario.php?DNI=" + numDNI +
                    "&Nombre=" + nomb + "&Apellido=" + ape +
                    "&Telefono=" + telef + "&SIP=" + numSIP);

        }
    }

    private void EnlazarControlesXML(){
        Button b1=(Button)findViewById(R.id.button1);
        DNI=(EditText)findViewById(R.id.editTextCedula);
        nombre=(EditText)findViewById(R.id.editTextNombre);
        apellido=(EditText)findViewById(R.id.editTextApellido);
        telefono=(EditText)findViewById(R.id.editTextTelefono);
        SIP=(EditText)findViewById(R.id.editTextSIP);

        //Valores por defecto
        DNI.setText("24354676Q");
        nombre.setText("PEPE");
        apellido.setText("LEAL");
        telefono.setText("965874125");
        SIP.setText("00125478");


        b1.setOnClickListener(this);
        DNI.setOnFocusChangeListener(this);

    }


    // Eventos

    public void onClick(View view) {
        try{

           InsertarUsuario(DNI.getText().toString(),nombre.getText().toString(), apellido.getText().toString(), telefono.getText().toString(),SIP.getText().toString());

        }catch(Exception e){

            Toast.makeText(getApplicationContext(), "Error en el envio de la informacion, verifique su conexion a internet y vuelva a intentarlo.", Toast.LENGTH_LONG).show();

        }
    }

    public void onFocusChange(View view,boolean hasFocus){
        if(hasFocus==false){

            GetUserByDNI(DNI.getText().toString());

        }

    }


    private void GetUserByDNI(String DNI) {

        boolean Conected = NetworkIsConnected();
        if (Conected ) {

            httpGetDataTask Request = new httpGetDataTask();
            httpGetDataTask.Result Res=null;
            //http://localhost/ToadService/GetUserByDNI.php?DNI
            //http://greentoad.esy.es/Prueba/ConsultarUsuario.php?DNI
            Request.execute(RemoteCall + DNI);

        }
    }


    private class httpGetDataTask extends AsyncTask<String,Void,httpGetDataTask.Result> {

        @Override
        protected Result doInBackground(String... url) {
            Result output;
            output = httpGetData(url[0]);
            return output;
        }

        public class Result {

            public JSONArray Response;
            public int ResultCode;
            public String Message;

            public Result(JSONArray Resp, int code, String Mess) {
                Response = Resp;
                ResultCode = code;
                Message = Mess;
            }

        }

        @Override
        protected void onPostExecute(Result R) {
            if (R.ResultCode == HttpURLConnection.HTTP_OK) {
                if ( R!= null) {
                    try {
                        JSONObject obj=R.Response.getJSONObject(0);
                        if (obj!=null) {

                            nombre.setText(obj.getString("nombre"));
                            apellido.setText(obj.getString("apellido1"));
                            telefono.setText(obj.getString("DNI"));
                            SIP.setText(obj.getString("permisos"));
                        }

                    } catch (JSONException e) {

                        nombre.setText("");
                        apellido.setText("");
                        SIP.setText("");
                        telefono.setText("");
                    }
                }
                Toast.makeText(getApplicationContext(), "El dato ha sido traido correctamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido realizarla consulta", Toast.LENGTH_LONG).show();
            }
        }


        private Result httpGetData(String mURL) {

            HttpURLConnection urlConnection = null;
            JSONArray ja = null;
            Result Res=null;

            try {

                URL url = new URL(mURL);
                urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();


                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                    String line;


                    while ((line = in.readLine()) != null) {
                        try {
                            ja = new JSONArray(line);
                            Res= new Result(ja, HttpURLConnection.HTTP_OK, null);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error recuperando la respuesta del servidor.", Toast.LENGTH_LONG).show();
                            Res= new Result(null, HttpURLConnection.HTTP_BAD_REQUEST, ex.getMessage());
                        }
                    }
                }
                //return new Result(urlConnection.getResponseCode(),"");
                catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error accediendo al servidor.", Toast.LENGTH_LONG).show();
                    Res= new Result(null, HttpURLConnection.HTTP_BAD_REQUEST, ex.getMessage());
                }

            } catch (MalformedURLException ex) {
                Log.e("httptest", Log.getStackTraceString(ex));
                Res=new Result(null, HttpURLConnection.HTTP_NOT_ACCEPTABLE, ex.getMessage());
                //return new Result(HttpURLConnection.HTTP_BAD_REQUEST,ex.getMessage());
            } catch (IOException ex) {
                Log.e("httptest", Log.getStackTraceString(ex));
                Res= new Result(null, HttpURLConnection.HTTP_NOT_ACCEPTABLE, ex.getMessage());
                //return new Result(HttpURLConnection.HTTP_FORBIDDEN,ex.getMessage());

            } finally {

                urlConnection.disconnect();
                return Res;
            }
        }

    }




    private class httpSendDataTask extends AsyncTask<String, Void, httpSendDataTask.Result> {
        @Override
        protected Result doInBackground(String... url) {
            Result output;
            output=httpSendData(url[0]);
            return output;
        }

        public class Result {

            public int ResultCode;
            public String Message;

            public Result (int code,String Mess){
                ResultCode=code;
                Message=Mess;
                            }

        }

        @Override
        protected void onPostExecute(Result R){
            if (R.ResultCode==HttpURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "El dato ha sido enviado correctamente", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "No se ha podido insertar el usuario", Toast.LENGTH_LONG).show();
            }
        }

        // Métodos de invocacion de Servicio WEB

        public Result httpSendData(String mURL) {
            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(mURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    return new Result(urlConnection.getResponseCode(),"");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    return new Result(HttpURLConnection.HTTP_INTERNAL_ERROR,ex.getMessage());
                }
            }
            catch (MalformedURLException ex) {
                Log.e("httptest", Log.getStackTraceString(ex));
                return new Result(HttpURLConnection.HTTP_BAD_REQUEST,ex.getMessage());
            }
            catch (IOException ex) {
                Log.e("httptest", Log.getStackTraceString(ex));
                return new Result(HttpURLConnection.HTTP_FORBIDDEN,ex.getMessage());
            }
            finally {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace(); //If you want further info on failure...
                }
            }
        }

    }




}
