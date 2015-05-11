package dejavu.appzonegroup.com.dejavuandroid.UIControls;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.appzone.zone.orchestra.engine.datatypes.Spec;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.jar.Attributes;

import dejavu.appzonegroup.com.dejavuandroid.Map.AttributeDefiner;
import dejavu.appzonegroup.com.dejavuandroid.Models.UI_Model;
import dejavu.appzonegroup.com.dejavuandroid.ObjectParceable.UI_Object;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Spec;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.ToastMessageHandler.ShowMessage;

/**
 * Created by CrowdStar on 3/6/2015.
 */
public class DJV_TextField extends EditText {


    private UI_Model ui_models;
    private UI_Object ui_object;


    public void setUi_object(UI_Object ui_object) {
        this.ui_object = ui_object;
    }

    public UI_Object getUi_object() {
        return ui_object;
    }

    public final void setViewAttribute(UI_Object ui_object) {
        ui_models = new AttributeDefiner().AttributeReader(ui_object.getUi_spec(), ui_object.getStepData(), ui_object.getName());
        setUi_object(ui_object);
    }


    public final void setDefaultAttribute() {
        setText(getCustomViewAttribute().getValue());
        setSingleLine();

    }

    public final UI_Model getCustomViewAttribute() {
        return ui_models;
    }


    public DJV_TextField(Context context, AttributeSet attrs, int defStyleAttr, UI_Object ui_object) {
        super(context, attrs, defStyleAttr);
        setViewAttribute(ui_object);
        setDefaultAttribute();
    }

}
