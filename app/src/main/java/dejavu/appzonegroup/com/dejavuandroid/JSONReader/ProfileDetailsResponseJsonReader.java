package dejavu.appzonegroup.com.dejavuandroid.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.ProfileDetailsSubmissionListener;
import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.ProfileDetailsSubmission;
import dejavu.appzonegroup.com.dejavuandroid.Constant.ServerResponseCodes;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class ProfileDetailsResponseJsonReader {


    public ProfileDetailsResponseJsonReader(String result, ProfileDetailsSubmissionListener submissionListener) {
        try {
            JSONArray pinRequestJsonString = new JSONArray(result);
            int serverResponseCode = pinRequestJsonString.getJSONObject(0).getInt("response");
            switch (serverResponseCode) {
                case ServerResponseCodes.SUCCESS:
                    submissionListener.onDetailsSubmitted();
                    break;
                case ServerResponseCodes.DENY_REQUEST:
                    submissionListener.onDetailsSubmissionRejected();
                    break;
                default:
                    submissionListener.onSubmissionFailed();
            }
        } catch (Exception e) {
            submissionListener.onSubmissionFailed();
        }
    }
}


