package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.JSONReader;

import org.json.JSONArray;
import org.json.JSONException;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PasswordPinAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class PasswordPinJsonReader {

    public PasswordPinJsonReader(String result, PasswordPinAuthentication.AuthenticationListener authenticationListener) {
        try {
            JSONArray passwordPinJsonArray = new JSONArray(result);
            int serverCodeResult = passwordPinJsonArray.getJSONObject(0).getInt("key");
            switch (serverCodeResult) {
                case ServerResponseCodes.SUCCESS:
                    authenticationListener.onAuth();
                    break;
                default:
                    authenticationListener.onAuthRejected();
                    break;
            }
        } catch (Exception e) {
            authenticationListener.onAuthRejected();
        }

    }
}
