package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.DebitCardAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.HardTokenAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class HardTokenResponseJsonReader {

    public HardTokenResponseJsonReader(String result, HardTokenAuthentication.HardAuthenticationListener listener) {

        try {
            JSONArray cardResponseJsonArray = new JSONArray(result);
            int serverResponseCode = cardResponseJsonArray.getJSONObject(0).getInt("key");
            switch (serverResponseCode) {
                case ServerResponseCodes.SUCCESS:
                    listener.onAuth();
                    break;
                case ServerResponseCodes.DENY_REQUEST:
                    listener.onFailedAuth();
                    break;
                default:
                    listener.onFailedRequest();
            }
        } catch (Exception e) {
            listener.onFailedRequest();
        }
    }


}
