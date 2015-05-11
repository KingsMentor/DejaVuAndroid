package dejavu.appzonegroup.com.dejavuandroid.ShellFramework;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PinRequest;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.VerifyPin;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.BroadcastReceiver.PinReceiver;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.UserPhoneDetails.UserDetailsFromPhone;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

public class GenericFlow extends ActionBarActivity implements PinRequest.onPinRequest, PinReceiver.onPinReceivedListener, VerifyPin.pinVerificationListener {
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_flow);
        phoneEditText = (EditText) findViewById(R.id.phone_number_edit_view);
        phoneEditText.setText(new UserDetailsFromPhone(this).getPhoneNumber());
        ((Button) findViewById(R.id.verify_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PinRequest(GenericFlow.this, GenericFlow.this, phoneEditText.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_generic_flow, menu);
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

    @Override
    public void onPinRequested() {
        new PinReceiver(GenericFlow.this);
    }

    @Override
    public void onPinRequestDenied() {
        // do something
    }

    @Override
    public void onRequestFailed() {
        new VerifyPin("", GenericFlow.this);
    }

    @Override
    public void onPinReceived(String pin) {
        //new VerifyPin("", GenericFlow.this);
    }

    @Override
    public void onPinValid() {
        // do something
        //new UserDetailsSharePreferences(GenericFlow.this).setPhoneNumber(phoneEditText.getText().toString());
    }

    @Override
    public void onInvalidPin() {
        // do something
    }

    @Override
    public void onPinVerificationFailed() {
        new ShowMessage(GenericFlow.this, "launch from here", 1);
    }
}
