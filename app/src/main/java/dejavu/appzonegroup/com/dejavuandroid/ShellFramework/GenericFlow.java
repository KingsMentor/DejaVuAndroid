package dejavu.appzonegroup.com.dejavuandroid.ShellFramework;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PinRequest;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.VerifyPin;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.BroadcastReceiver.PinReceiver;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.UserPhoneDetails.UserDetailsFromPhone;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

public class GenericFlow extends ActionBarActivity implements PinRequest.onPinRequest, PinReceiver.onPinReceivedListener, VerifyPin.pinVerificationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_flow);
        ((EditText) findViewById(R.id.phone_number_edit_view)).setText(new UserDetailsFromPhone(this).getPhoneNumber());
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
        //do something 
    }

    @Override
    public void onPinRequestDenied() {
        // do something
    }

    @Override
    public void onRequestFailed() {
        // do something
    }

    @Override
    public void onPinReceived(String pin) {
        new VerifyPin(pin, GenericFlow.this);
    }

    @Override
    public void onPinValid() {
        // do something
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
