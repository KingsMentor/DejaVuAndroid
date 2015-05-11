package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.PinRequest;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.VerifyPin;
import dejavu.appzonegroup.com.dejavuandroid.ShellFramework.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class PinVerificationJsonReader {


    public PinVerificationJsonReader(String result, VerifyPin.pinVerificationListener pinVerificationListener) {
        try {
            JSONArray pinRequestJsonString = new JSONArray(result);
            int serverResponseCode = pinRequestJsonString.getJSONObject(0).getInt("response");
            switch (serverResponseCode) {
                case ServerResponseCodes.SUCCESS:
                    pinVerificationListener.onPinValid();
                    break;
                case ServerResponseCodes.DENY_REQUEST:
                    pinVerificationListener.onInvalidPin();
                    break;
                default:
                    pinVerificationListener.onPinVerificationFailed();
            }
        } catch (Exception e) {
            pinVerificationListener.onPinVerificationFailed();
        }
    }
}
