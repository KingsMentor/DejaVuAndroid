package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.JSONReader;

import org.json.JSONArray;
import org.json.JSONException;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.DebitCardAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class DebitCardResponseJsonReader {

    public DebitCardResponseJsonReader(String result, DebitCardAuthentication.CardAuthenticationListener cardAuthenticationListener) {

        try {
            JSONArray cardResponseJsonArray = new JSONArray(result);
            int serverResponseCode = cardResponseJsonArray.getJSONObject(0).getInt("key");
            switch (serverResponseCode) {
                case ServerResponseCodes.SUCCESS:
                    cardAuthenticationListener.onCardAuthenticated();
                    break;
                case ServerResponseCodes.DENY_REQUEST:
                    cardAuthenticationListener.onInvalidCardDetails();
                    break;
                default:
                    cardAuthenticationListener.onRequestFailed();
            }
        } catch (Exception e) {
            cardAuthenticationListener.onRequestFailed();
        }
    }


}
