package dejavu.appzonegroup.com.dejavuandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import dejavu.appzonegroup.com.dejavuandroid.Activities.CategoryFragment;
import dejavu.appzonegroup.com.dejavuandroid.Activities.MainActivity;
import dejavu.appzonegroup.com.dejavuandroid.Interfaces.GoogleCloudMessagingListener;
import dejavu.appzonegroup.com.dejavuandroid.Interfaces.TokenAuthenticationListener;
import dejavu.appzonegroup.com.dejavuandroid.PushNotification.PushRegistration;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.SoftTokenAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.SharePreferences.UserDetailsSharePreferences;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

/**
 * Created by CrowdStar on 2/19/2015.
 */
public class SoftToken extends Fragment implements TokenAuthenticationListener, GoogleCloudMessagingListener {

    EditText tokenField;
    private ProgressFragment progressFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_token_reg, container, false);
        tokenField = (EditText) rootView.findViewById(R.id.token_field);
        progressFragment = new ProgressFragment();
        rootView.findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tokenField.getText().toString().toString().isEmpty()) {
                    new ShowMessage(getActivity(),"Token can not be empty",Toast.LENGTH_LONG);
                } else {
                    progressFragment.show(getFragmentManager(), "");
                    new SoftTokenAuthentication(getActivity(), SoftToken.this, tokenField.getText().toString());
                }
            }
        });
        return rootView;
    }


    @Override
    public void onAuth() {
        //do something on success;
    }

    @Override
    public void onFailedAuth() {
        //do something on failed


    }

    @Override
    public void onFailedRequest() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onRegistered() {
        new UserDetailsSharePreferences(getActivity()).setFullyAuth(true);
        startActivity(new Intent(getActivity(), CategoryFragment.class));
        getActivity().finish();
    }

    @Override
    public void onRegistrationFailed() {
        new ShowMessage(getActivity(), "Could not receive neccesary info fully. What should happen ?", Toast.LENGTH_LONG);
    }
}
