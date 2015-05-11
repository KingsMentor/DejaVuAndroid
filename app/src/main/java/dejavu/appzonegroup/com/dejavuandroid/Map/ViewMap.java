package dejavu.appzonegroup.com.dejavuandroid.Map;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.HashMap;

import dejavu.appzonegroup.com.dejavuandroid.Interfaces.FileChooserListener;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_Button;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_DatePicker;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_EditText;
import dejavu.appzonegroup.com.dejavuandroid.UIControls.DJV_FileChooser;

/**
 * Created by CrowdStar on 3/11/2015.
 */
public class ViewMap {

    static HashMap hashMap = new HashMap();

    static public HashMap getViewMap(Context context, String attributeSerializer) {
        hashMap.put(UI_Type.DJV_SingleLineField, new DJV_EditText(context, attributeSerializer));
        hashMap.put(UI_Type.DJV_Button, new DJV_Button(context, attributeSerializer));
        return hashMap;
    }

    static public HashMap getViewMap(Context context, String attributeSerializer, FragmentManager fragmentManager) {
        hashMap.put(UI_Type.DJV_DateField, new DJV_DatePicker(context, attributeSerializer, fragmentManager));
        return hashMap;
    }

    static public HashMap getViewMap(Context context, String attributeSerializer, FileChooserListener mListener) {
        hashMap.put(UI_Type.DJV_FileUpload, new DJV_FileChooser(context, attributeSerializer, mListener));
        return hashMap;
    }
}
