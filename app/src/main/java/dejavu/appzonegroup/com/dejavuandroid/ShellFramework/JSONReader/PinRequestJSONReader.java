package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PinRequest;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class PinRequestJSONReader {


    public PinRequestJSONReader(String result, PinRequest.onPinRequest pinRequest) {
        try {
            JSONArray pinRequestJsonString = new JSONArray(result);
            int serverResponseCode = pinRequestJsonString.getJSONObject(0).getInt("response");
            switch (serverResponseCode) {
                case ServerResponseCodes.SUCCESS:
                    pinRequest.onPinRequested();
                    break;
                case ServerResponseCodes.DENY_REQUEST:
                    pinRequest.onPinRequestDenied();
                    break;
                default:
                    pinRequest.onRequestFailed();
            }
        } catch (Exception e) {
            pinRequest.onRequestFailed();
        }
    }


}
