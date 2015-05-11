package dejavu.appzonegroup.com.dejavuandroid.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.AuthenticationListener;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PasswordPinAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class PasswordPinJsonReader {

    public PasswordPinJsonReader(String result, AuthenticationListener authenticationListener) {
        try {
            JSONArray passwordPinJsonArray = new JSONArray(result);
            int serverCodeResult = passwordPinJsonArray.getJSONObject(0).getInt("key");
            switch (serverCodeResult) {
                case ServerResponseCodes.SUCCESS:
                    authenticationListener.onAuth();
                    break;
                default:
                    authenticationListener.onAuthRejected();
            }
        } catch (Exception e) {
            authenticationListener.onAuthRejected();
        }

    }
}
