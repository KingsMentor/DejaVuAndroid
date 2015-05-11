package dejavu.appzonegroup.com.dejavuandroid.Map;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import dejavu.appzonegroup.com.dejavuandroid.Models.UI_Model;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Spec;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

/**
 * Created by CrowdStar on 3/16/2015.
 */
public class AttributeDefiner {


    UI_Model ui_model;

    public UI_Model AttributeReader(UI_Spec attribute, JSONObject data, String dataKey) {
        ui_model = new UI_Model();
        try {
            String value = data.getString(dataKey);
            ui_model.setValue(value);
            ui_model.setValueKey(dataKey);
            ui_model.setSpec(attribute);
        } catch (Exception e) {
            ui_model.setValue("");
            ui_model.setValueKey(dataKey);
            ui_model.setSpec(attribute);
        }

        return ui_model;
    }
}
