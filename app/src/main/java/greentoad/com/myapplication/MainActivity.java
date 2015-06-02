package greentoad.com.myapplication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.client.HttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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


        ExecuteRestService();

        lstNames = (ListView) findViewById(R.id.lstNames);
        Names=new ArrayList<String>();
        Names.add ("Nelo");
        Names.add("Ana");
        Names.add("Alfredo");
        Names.add("Olga");
        Names.add("Juan");
        Names.add("Santi");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Names);
        lstNames.setAdapter(adapter);

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

    private void ExecuteRestService() {
        HttpURLConnection urlConnection;

        try {
            URL myurl = new URL("http://www.android.com/");

        try {
            urlConnection = (HttpURLConnection) myurl.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //readStream(in);
        }
        catch (IOException Ex){

        }

        } catch (MalformedURLException Ex) {

        }

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



}
