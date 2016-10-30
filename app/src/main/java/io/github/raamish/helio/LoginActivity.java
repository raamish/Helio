package io.github.raamish.helio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button button = (Button) findViewById(R.id.btn_login);
        TextView txtview = (TextView) findViewById(R.id.link_signup);
        txtview.setText("No Account yet ? Create One.");


        button.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {

                EditText email = (EditText)findViewById(R.id.input_email);
                EditText password = (EditText)findViewById(R.id.input_password);
                TextView textbox = (TextView)findViewById(R.id.link_signup);

                if((email.getText().toString().equals("raamish")) && (password.getText().toString().equals("raamish")))
                    {
                        Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, Main_Menu.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
                    }
                }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu_drawer, menu);
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