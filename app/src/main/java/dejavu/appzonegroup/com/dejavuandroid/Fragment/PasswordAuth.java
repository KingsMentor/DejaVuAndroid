package dejavu.appzonegroup.com.dejavuandroid.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.AuthenticationListener;
import dejavu.appzonegroup.com.dejavuandroid.Models.PasswordPinModel;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PasswordPinAuthentication;

/**
 * Created by CrowdStar on 2/19/2015.
 */
public class PasswordAuth extends Fragment implements AuthenticationListener {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_password_pin_auth, container, false);
        rootView.findViewById(R.id.verify_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PasswordPinAuthentication(getActivity(), PasswordAuth.this, buildPasswordPinModel("password", 1234));
            }
        });
        return rootView;
    }

    @Override
    public void onAuth() {
        //use successfully login
    }

    @Override
    public void onAuthRejected() {
        //login request from user was denied
    }

    private ArrayList<PasswordPinModel> buildPasswordPinModel(String password, int pin) {
        ArrayList<PasswordPinModel> passwordPinModels = new ArrayList<>();
        PasswordPinModel model = new PasswordPinModel();
        model.setPassword(password);
        model.setPin(pin);
        passwordPinModels.add(model);
        return passwordPinModels;
    }
}
