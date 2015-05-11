package dejavu.appzonegroup.com.dejavuandroid.ShellFramework.SharePreferences;

import android.content.Context;

/**
 * Created by CrowdStar on 2/12/2015.
 */
public class UserDetailsSharePreferences {

    private Context mContext;

    public UserDetailsSharePreferences(Context context) {
        mContext = context;
    }

    public void setPhoneNumber(String number) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".phoneNumber", number).clear();
    }

    public String getUserPhoneNumber() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".phoneNumber", "");
    }

    public void setLastName(String lastName) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".lastName", lastName).clear();

    }

    public String getLastName() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".lastName", "");
    }

    public void setOtherName(String otherName) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".otherName", otherName).clear();

    }

    public String getOtherName() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".otherName", "");
    }

    public void setGenderValue(int genderValue) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putInt(getClass().getSimpleName() + ".genderValue", genderValue).clear();

    }

    public int getGenderValue() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getInt(getClass().getSimpleName() + ".genderValue", -1);
    }


    public void setDate(long dateTime) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putLong(getClass().getSimpleName() + ".date", dateTime).clear();

    }

    public long getDate() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getLong(getClass().getSimpleName() + ".date", -1l);
    }


    public void setEmail(String email) {
        mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).edit().putString(getClass().getSimpleName() + ".email", email).clear();

    }

    public String getEmail() {
        return mContext.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE).getString(getClass().getSimpleName() + ".email", "");
    }


}
