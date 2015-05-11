package dejavu.appzonegroup.com.dejavuandroid.UIControls;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.appzone.zone.orchestra.engine.datatypes.Spec;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dejavu.appzonegroup.com.dejavuandroid.DataBases.Entity;
import dejavu.appzonegroup.com.dejavuandroid.Map.AttributeDefiner;
import dejavu.appzonegroup.com.dejavuandroid.Models.UI_Model;
import dejavu.appzonegroup.com.dejavuandroid.ObjectParceable.UI_Object;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Entity;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Spec;

/**
 * Created by CrowdStar on 3/9/2015.
 */
public class DJV_DropDown extends Spinner {
    private UI_Model ui_models;
    private String[] items;
    private ArrayAdapter itemAdapter;
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
        if (getUi_object().getUi_spec().getParameterMode().equalsIgnoreCase("entity")) {
            getUi_object().setParameterMode(true);
            String entityString = getUi_object().getUi_singleField().getEntitySource();

            UI_Entity entity = new UI_Entity(entityString);
            ArrayList<Entity> entityArrayList = Entity.getAllEntityByName(getUi_object().getContext(), entity.getName());
            getUi_object().setEntityObject(entityArrayList);
            String keep = "";
            for (int entityIndex = 0; entityIndex < entityArrayList.size(); entityIndex++) {
                try {
                    JSONObject jsonObject = new JSONObject(entityArrayList.get(entityIndex).getValue());
                    keep = jsonObject.optString("::DisplayName::") + ",";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (!keep.trim().isEmpty())
                items = keep.substring(0, keep.length() - 1).split(",");
            else
                items = new String[0];
        } else {
            items = getUi_object().getUi_singleField().getSourceContent().split(",");
        }
    }

    public final UI_Model getCustomViewAttribute() {
        return ui_models;
    }


    public DJV_DropDown(Context context) {
        super(context);
    }

    public DJV_DropDown(Context context, UI_Object ui_object) {
        super(context);
        setViewAttribute(ui_object);
        setDefaultAttribute();
        itemAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, items) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                //we know that simple_spinner_item has android.R.id.text1 TextView:

        /* if(isDroidX) {*/
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);//choose your color :)
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                //we know that simple_spinner_item has android.R.id.text1 TextView:

        /* if(isDroidX) {*/
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);//choose your color :)
                return view;
            }
        };


        setAdapter(itemAdapter);
    }
}
