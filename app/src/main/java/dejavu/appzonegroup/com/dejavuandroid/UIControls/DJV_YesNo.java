package dejavu.appzonegroup.com.dejavuandroid.UIControls;

import android.content.Context;
import android.graphics.Color;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.appzone.zone.orchestra.engine.datatypes.Spec;

import org.json.JSONObject;

import java.util.ArrayList;

import dejavu.appzonegroup.com.dejavuandroid.Map.AttributeDefiner;
import dejavu.appzonegroup.com.dejavuandroid.Models.UI_Model;
import dejavu.appzonegroup.com.dejavuandroid.ObjectParceable.UI_Object;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Spec;

/**
 * Created by CrowdStar on 3/20/2015.
 */
public class DJV_YesNo extends RadioGroup {
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

    }


    public final void setDefaultAttribute() {
        setId(Integer.parseInt(getCustomViewAttribute().getSpec().getId()));
    }

    public final UI_Model getCustomViewAttribute() {
        return ui_models;
    }

    public DJV_YesNo(Context context) {
        super(context);
    }

    public DJV_YesNo(Context context,UI_Object ui_object) {
        super(context);
        setViewAttribute(ui_object);
        setDefaultAttribute();
        RadioButton yesRadio = new RadioButton(context);
        RadioButton noRadio = new RadioButton(context);
        noRadio.setId(getId() + 1);
        yesRadio.setId(getId() + 2);
        yesRadio.setText("Yes");
        yesRadio.setTextColor(Color.BLACK);
        noRadio.setText("No");
        noRadio.setTextColor(Color.BLACK);
        setOrientation(HORIZONTAL);
        addView(yesRadio);
        addView(noRadio);
        check(getId() + 1);

    }
}
