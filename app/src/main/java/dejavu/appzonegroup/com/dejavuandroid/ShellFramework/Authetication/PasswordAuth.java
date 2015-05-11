package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Authetication;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import dejavu.appzonegroup.com.dejavuandroid.Activities.CategoryFragment;
import dejavu.appzonegroup.com.dejavuandroid.Activities.MainActivity;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.SharePreferences.UserDetailsSharePreferences;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

public class PasswordAuth extends ActionBarActivity {
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_auth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        passwordEditText = (EditText) findViewById(R.id.password_field);
        findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify();
            }
        });
    }

    public void verify() {
        if (passwordEditText.getText().toString().equalsIgnoreCase(new UserDetailsSharePreferences(this).getPassword())) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            new ShowMessage(this, "Invalid password", Toast.LENGTH_LONG);
        }
    }
}
