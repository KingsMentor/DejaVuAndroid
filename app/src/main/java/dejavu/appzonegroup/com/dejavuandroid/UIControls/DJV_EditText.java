package dejavu.appzonegroup.com.dejavuandroid.UIControls;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.jar.Attributes;

import dejavu.appzonegroup.com.dejavuandroid.Map.AttributeDefiner;
import dejavu.appzonegroup.com.dejavuandroid.Models.UI_Model;

/**
 * Created by CrowdStar on 3/6/2015.
 */
public class DJV_EditText extends EditText {


    static private ArrayList<UI_Model> ui_models;


    public void setViewAttribute(String attributeDict) {
        ui_models = new AttributeDefiner().AttributeReader(attributeDict);
    }


    public void setDefaultAttribute() {
        setId(getCustomViewAttribute().get(0).getId());
        setTextColor(Color.BLACK);
    }

    public ArrayList<UI_Model> getCustomViewAttribute() {
        return ui_models;
    }


    public DJV_EditText(Context context) {
        super(context);
        ui_models = new ArrayList<>();
    }

    public DJV_EditText(Context context, String controlAttributes) {
        super(context);
        setViewAttribute(controlAttributes);
        setDefaultAttribute();
    }

}
