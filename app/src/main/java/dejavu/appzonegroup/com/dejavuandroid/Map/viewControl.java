package dejavu.appzonegroup.com.dejavuandroid.Map;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.appzone.zone.orchestra.engine.datatypes.Spec;

import org.json.JSONObject;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.FileChooserListener;
import dejavu.appzonegroup.com.dejavuandroid.ObjectParceable.UI_Object;
import dejavu.appzonegroup.com.dejavuandroid.PageRenderer.UI_Spec;
import dejavu.appzonegroup.com.dejavuandroid.R;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_Button;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_CheckBox;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_DatePicker;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_DropDown;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_Label;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_List;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_ReadOnly;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_TextArea;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_TextField;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_FileChooser;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_YesNo;

/**
 * Created by CrowdStar on 3/12/2015.
 */
public class viewControl {

    static public View getViewByType(UI_Object ui_object) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 2, 2, 2);
        switch (ui_object.getType()) {
            case UI_Type.DJV_SingleLineField:
                DJV_TextField djv_textField = new DJV_TextField(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_TextField_Style), null, 0, ui_object);
                djv_textField.setLayoutParams(layoutParams);
                return djv_textField;
            case UI_Type.DJV_SingleSelection:
                return new DJV_DropDown(ui_object.getContext(), ui_object);
            case UI_Type.DJV_MultilineField:
                DJV_TextArea djv_textArea = new DJV_TextArea(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_TextArea_Style), null, 0, ui_object);
                djv_textArea.setLayoutParams(layoutParams);
                return djv_textArea;
            case UI_Type.DJV_ReadOnly:
                DJV_ReadOnly djv_readOnly = new DJV_ReadOnly(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_TextField_Style), null, 0, ui_object);
                djv_readOnly.setLayoutParams(layoutParams);
                return djv_readOnly;
            case UI_Type.DJV_Button:
                DJV_Button djv_button = new DJV_Button(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_Button_Style), null, 0, ui_object);
                djv_button.setLayoutParams(layoutParams);
                return djv_button;
            case UI_Type.DJV_CheckBox:
                DJV_CheckBox djv_checkBox = new DJV_CheckBox(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_CheckBox_Style), null, 0, ui_object);
                djv_checkBox.setLayoutParams(layoutParams);
                return djv_checkBox;
            case UI_Type.DJV_Label:
                return new DJV_Label(ui_object.getContext(), null, 0, ui_object);
            case UI_Type.DJV_YesNO:
                return new DJV_YesNo(ui_object.getContext(), ui_object);
            case UI_Type.DJV_DropDown:
                return new DJV_DropDown(ui_object.getContext(), ui_object);
            case UI_Type.DJV_List:
                return new DJV_List(ui_object.getContext(), ui_object);
            case UI_Type.DJV_DateField:
                return new DJV_DatePicker(ui_object.getContext(), ui_object);
            case UI_Type.DJV_FileUpload:
                layoutParams = new LinearLayout.LayoutParams(
                        ViewParams.width, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(2, 2, 2, 2);
                DJV_FileChooser djv_fileChooser = new DJV_FileChooser(new ContextThemeWrapper(ui_object.getContext(), R.style.DJV_Button_Style), null, 0, ui_object);
                djv_fileChooser.setLayoutParams(layoutParams);
                return djv_fileChooser;
            default:
                return new View(ui_object.getContext());
        }
    }


}
