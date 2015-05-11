package dejavu.appzonegroup.com.dejavuandroid.DataSynchronization;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by emacodos on 4/28/2015.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public static boolean active = false;
    public static Intent mIntent;
    public static String mId;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Bundle extra = intent.getExtras();
        String action = intent.getAction();
        switch (action) {
            case FlowSyncService.ACTION_SYNC:
                active = true;
                mIntent = intent;
                break;
            case FlowSyncService.ACTION_CLOUD_MESSAGE:
                active = true;
                mIntent = intent;
                mId = intent.getStringExtra(FlowSyncService.PARAM_MESSAGE);
                break;
        }
        if (active) {
            if (checkInternet(context)) {
                final String intentAction = mIntent.getAction();
                switch (intentAction) {
                    case FlowSyncService.ACTION_SYNC:
                        FlowSyncService.startActionSync(context);
                        break;
                    case FlowSyncService.ACTION_CLOUD_MESSAGE:
                        FlowSyncService.startActionCloudMessage(context, mId);
                        break;
                }
                active = false;
            }
        }
    }


    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        return serviceManager.isNetworkAvailable();
    }

}
