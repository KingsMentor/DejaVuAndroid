package dejavu.appzonegroup.com.dejavuandroid.JSONReader;

import org.json.JSONArray;

import dejavu.appzonegroup.com.dejavuandroid.ServerRequest.ConfigurationRequest;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class JSonConfigurationReader {


    public JSonConfigurationReader(String result, ConfigurationRequest.onConfigurationRequest mConfigurationRequest) {
        try {
            JSONArray configurationJsonArray = new JSONArray(result);
            int flow = configurationJsonArray.getJSONObject(0).getInt("flow");
            boolean debit = configurationJsonArray.getJSONObject(0).getBoolean("debit");
            boolean password = configurationJsonArray.getJSONObject(0).getBoolean("password");
            boolean hardToken = configurationJsonArray.getJSONObject(0).getBoolean("hardtoken");
            boolean softToken = configurationJsonArray.getJSONObject(0).getBoolean("softtoken");
            mConfigurationRequest.onConfigurationRequestSuccessful(flow, debit, password, hardToken, softToken);
        } catch (Exception e) {
            mConfigurationRequest.onConfigurationRequestFailed();
        }
    }
}
