package dejavu.appzonegroup.com.dejavuandroid.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.CardAuthenticationListener;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.DebitCardAuthentication;
import dejavu.appzonegroup.com.dejavuandroid.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class DebitCardResponseJsonReader {

    public DebitCardResponseJsonReader(String result, CardAuthenticationListener cardAuthenticationListener) {

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
