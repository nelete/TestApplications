package greentoad.com.myapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class DetailsActivity extends ActionBarActivity {

    private EditText DNI=null;
    private EditText nombre=null;
    private EditText apellido=null;
    private EditText telefono=null;
    private EditText SIP=null;
    private EditText JsonText=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        DNI=(EditText)findViewById(R.id.editTextDNI);
        nombre=(EditText)findViewById(R.id.editTextNombre);
        apellido=(EditText)findViewById(R.id.editTextApellido);
        telefono=(EditText)findViewById(R.id.editTextTelefono);
        SIP=(EditText)findViewById(R.id.editTextSIP);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
