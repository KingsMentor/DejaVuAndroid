package dejavu.appzonegroup.com.dejavuandroid.DataSynchronization;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dejavu.appzonegroup.com.dejavuandroid.DataBases.ClientFlows;
import dejavu.appzonegroup.com.dejavuandroid.DataBases.Entity;
import dejavu.appzonegroup.com.dejavuandroid.DataBases.Function;
import dejavu.appzonegroup.com.dejavuandroid.DataBases.FunctionCategory;


/**
 * Created by emacodos on 2/26/2015.
 */

/**
 * @author Onyejekwe E. C emacodos
 *
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 */
public class FlowSyncService extends IntentService {

    private static final String TAG = FlowSyncService.class.getSimpleName();

    // Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SYNC = "com.zoneapp.action.SYNC";
    public static final String ACTION_CLOUD_MESSAGE = "com.zoneapp.action.CLOUD.MESSAGE";
    public static final String ACTION_DOWNLOAD_FUNCTION = "com.zoneapp.action.FUNCTION";
    public static final String ACTION_DOWNLOAD_FUNCTION_CATEGORY = "com.zoneapp.action.CATEGORY";
    public static final String ACTION_DOWNLOAD_FLOW = "com.zoneapp.action.FLOW";
    public static final String ACTION_DOWNLOAD_FLOW_ENTITY = "com.zoneapp.action.ENTITY";
    public static final String ACTION_INTERNET = "com.zoneapp.action.INTERNET";

    /**
     * Starts this service to perform action Download with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see android.app.IntentService
     */

    private static final String PARAM_ID = "com.zoneapp.param.PARAM_ID";
    private static final String PARAM_DATA = "com.zoneapp.param.PARAM_DATA";
    public static final String PARAM_MESSAGE = "com.zoneapp.param.MESSAGE";

    public static final String URL_FUNCTION_CATEGORY = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFunctionCategory/GetAll/";
    public static final String URL_FUNCTION = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFunction/GetAll/";
    public static final String URL_FLOWS = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFlow/GetAll/";
    public static final String URL_FUNCTION_CATEGORY_ID = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFunctionCategory/Get/";
    public static final String URL_FUNCTION_ID = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFunction/Get/";
    public static final String URL_ENTITY = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFlow/GetFlowEntities/";
    public static final String URL_FLOWS_ID = "http://165.233.246.31:11984/ZoneFlowsApi/api/ClientFlow/Get/";
    public static final String URL_UPLOAD = "http://165.233.246.31:11984/ZoneServiceApi/api/entitydataservice/performupload";

    private static final int ENTITY_PER_BATCH = 2;

    private Object mFlowGroup = new Object();

    // Customize helper method

    public static void startActionSync(Context context) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_SYNC);
        context.startService(intent);
    }

    public static void startActionInternet(Context context) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_INTERNET);
        context.startService(intent);
    }

    public static void startActionCloudMessage(Context context, String data) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_CLOUD_MESSAGE);
        intent.putExtra(PARAM_DATA, data);
        context.startService(intent);
    }

    public static void startActionDownloadFunctions(Context context) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_DOWNLOAD_FUNCTION);
        context.startService(intent);
    }

    public static void startActionDownloadFunctionCategories(Context context) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_DOWNLOAD_FUNCTION_CATEGORY);
        context.startService(intent);
    }

    public static void startActionDownloadFlows(Context context, String id) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_DOWNLOAD_FLOW);
        intent.putExtra(PARAM_ID, id);
        context.startService(intent);
    }

    public static void startActionDownloadEntities(Context context, String id) {
        Intent intent = new Intent(context, FlowSyncService.class);
        intent.setAction(ACTION_DOWNLOAD_FLOW_ENTITY);
        intent.putExtra(PARAM_ID, id);
        context.startService(intent);
    }

    public FlowSyncService() {
        super("FlowSyncService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is starting",
                Toast.LENGTH_LONG).show();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_SYNC:
                    handleActionSync();
                    break;
                case ACTION_INTERNET:
                    handleActionInternet();
                    break;
                case ACTION_CLOUD_MESSAGE:
                    final String data = intent.getStringExtra(PARAM_DATA);
                    handleActionCloudMessage(data);
                    break;
                case ACTION_DOWNLOAD_FUNCTION_CATEGORY:
                    handleActionDownloadFunctionCategory();
                    break;
                case ACTION_DOWNLOAD_FUNCTION:
                    handleActionDownloadFunction();
                    break;
                case ACTION_DOWNLOAD_FLOW:
                    final String functionId = intent.getStringExtra(PARAM_ID);
                    handleActionDownloadFlows(functionId);
                    break;
                case ACTION_DOWNLOAD_FLOW_ENTITY:
                    final String flowId = intent.getStringExtra(PARAM_ID);
                    handleActionDownloadEntity(flowId);
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service is done updating",
                Toast.LENGTH_LONG).show();
    }

    /**
     * Handle action Sync: Synchronization of Flows and Entity
     */
    private void handleActionSync() {
        String c = downloadFunctionCategories(ACTION_SYNC, URL_FUNCTION_CATEGORY, null);
        String f = downloadFunction(ACTION_SYNC, URL_FUNCTION, null);
        if (f != null && c != null){
            saveCategory(c);
            saveFunction(f);
            sendMessage(ACTION_DOWNLOAD_FUNCTION_CATEGORY, 1);
            ArrayList<Function> fs = Function.getAllFunctions(this);
            for (int i=0; i<fs.size(); i++) {
                Function function = fs.get(i);
                String cf = downloadFlows(ACTION_SYNC, function.getFlowGuid(), null);
                String en = downloadEntity(ACTION_SYNC, function.getFlowGuid(), null);
                if (cf != null && en != null){
                    saveClientFlows(cf);
                    saveFlowEntity(en);
                    sendMessage(ACTION_DOWNLOAD_FLOW, function.getFlowGuid());
                }
                else {
                    sendMessage(ACTION_DOWNLOAD_FLOW, null);
                }
            }
            ZoneDataUtils.startAlarmService(this, MyScheduleReceiver.REPEAT_TIME);
        }
        else {
            sendMessage(ACTION_DOWNLOAD_FUNCTION_CATEGORY, -1);
        }

    }

    /**
     * Handle action Sync: Synchronization of Flows and Entity
     */
    private void handleActionInternet() {
        doDownload();
        doUpload();
    }

    /**
     * Handle action new: sync for the first time
     */
    private void handleActionDownloadFunctionCategory() {
        //Download Flows from Server
        String c = downloadFunctionCategories(ACTION_DOWNLOAD_FUNCTION_CATEGORY, URL_FUNCTION_CATEGORY, null);
        if (c != null) {
            saveCategory(c);
            sendMessage(ACTION_DOWNLOAD_FUNCTION_CATEGORY, 1);
        }
        else {
            sendMessage(ACTION_DOWNLOAD_FUNCTION_CATEGORY, -1);
        }
    }

    /**
     * Handle action new: sync for the first time
     */
    private void handleActionDownloadFunction() {
        //Download Flows from Server
        String f = downloadFunction(ACTION_DOWNLOAD_FUNCTION, URL_FUNCTION, null);
        if (f != null) {
            saveFunction(f);
            sendMessage(ACTION_DOWNLOAD_FUNCTION, 1);
        }
        else {
            sendMessage(ACTION_DOWNLOAD_FUNCTION, -1);
        }
    }

    /**
     * Handle action new: sync for the first time
     */
    private void handleActionDownloadFlows(String id) {
        //Download Flows from Server
        String cf = downloadFlows(ACTION_DOWNLOAD_FLOW, id, null);
        if (id != null) {
            saveClientFlows(cf);
            sendMessage(ACTION_DOWNLOAD_FLOW, id);
        }
        else {
            sendMessage(ACTION_DOWNLOAD_FLOW, null);
        }
    }

    /**
     * Handle action new: sync for the first time
     */
    private void handleActionDownloadEntity(String id) {
        //Download Flows from Server
        String en = downloadEntity(ACTION_DOWNLOAD_FLOW_ENTITY, id, null);
        if (id != null && en != null) {
            sendMessage(ACTION_DOWNLOAD_FLOW_ENTITY, id);
        }
        else {
            sendMessage(ACTION_DOWNLOAD_FLOW_ENTITY, null);
        }
    }

    /**
     * Handle action cloud messages for update
     * {
     *     "push":{
     *       "ObjectType":"value",
     *       "Id":"value"
     *       "OperationType":"value"
     *       "Name":"value"
     *      }
     * }
     */
    private void handleActionCloudMessage(String data) {
        //handle what happens when message is gotten from the server
        if (data != null) {
            String type, id, operation;
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONObject object = jsonData.getJSONObject("push");
                type = object.getString("ObjectType");
                id = object.getString("Id");
                operation = object.getString("OperationType");
                String entityName = object.optString("Name");
                switch (type) {
                    case "ClientFunction":
                        if (operation != null && operation.equalsIgnoreCase("delete")) {
                            Function function = Function.getFunctionById(this, Integer.parseInt(id));
                            function.delete(this);
                        } else {
                            downloadFunction(ACTION_CLOUD_MESSAGE, URL_FUNCTION_ID + id, data);
                        }
                        break;

                    case "ClientFunctionCategory":
                        if (operation != null && operation.equalsIgnoreCase("delete")) {
                            FunctionCategory category = FunctionCategory.getFunctionCategoryById(this, Integer.parseInt(id));
                            category.delete(this);
                        } else {
                            downloadFunctionCategories(ACTION_CLOUD_MESSAGE, URL_FUNCTION_CATEGORY_ID + id, data);
                        }
                        break;

                    case "ClientFlow":
                        if (operation != null && operation.equalsIgnoreCase("delete")) {
                            ClientFlows flows = ClientFlows.getFlowById(this, id);
                            flows.delete(this);
                        } else {
                            String flows = downloadFlows(ACTION_CLOUD_MESSAGE, id, data);
                            saveClientFlows(flows);
                        }
                        break;

                    case "Entity":
                        if (operation != null && operation.equalsIgnoreCase("delete")) {
                            ArrayList<Entity> entities = Entity.getAllEntityByName(this, entityName);
                            for (int i = 0; i < entities.size(); i++) {
                                entities.get(i).delete(this);
                            }
                        } else {
                            JSONObject instruction = new JSONObject(), entityData = new JSONObject();
                            try {
                                instruction.put(LocalEntityService.NAME_TYPE, LocalEntityService.TYPE_SERVER);
                                instruction.put(LocalEntityService.NAME_OPERATION, LocalEntityService.OPERATION_RETRIEVE);
                                instruction.put(LocalEntityService.NAME_ENTITY, entityName);

                                entityData.put("Id", id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (isNetworkAvailable()) {
                                JsonObject dataObj = new JsonParser().parse(entityData.toString()).getAsJsonObject();
                                JsonObject insObj = new JsonParser().parse(instruction.toString()).getAsJsonObject();

                                JsonObject json = new JsonObject();
                                json.add(LocalEntityService.PARAM_INSTRUCTION, insObj);
                                json.add(LocalEntityService.PARAM_DATA, dataObj);

                                String mOutput = null;
                                try {
                                    mOutput = Ion.with(this)
                                            .load("POST", LocalEntityService.URL_ENTITY)
                                            .setJsonObjectBody(json)
                                            .asString()
                                            .get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (mOutput != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(mOutput);
                                        String eventName = jsonObject.optString(LocalEntityService.NAME_EVENT_NAME);
                                        JSONArray eventData = jsonObject.getJSONArray(LocalEntityService.NAME_EVENT_DATA);
                                        if (eventName.equalsIgnoreCase(LocalEntityService.VALUE_RETRIEVED)) {
                                            for (int i = 0; i < eventData.length(); i++) {
                                                JSONObject entityJson = eventData.getJSONObject(i);
                                                int entityId = entityJson.optInt(Entity.COLUMN_ID);
                                                Entity table = Entity.getEntityById(this, entityName, entityId);
                                                if (table == null) {
                                                    Entity entity = new Entity();
                                                    entity.setSync(true);
                                                    entity.setEntityId(entityId);
                                                    entity.setValue(entityJson.toString());
                                                    entity.setEntityName(entityName);
                                                    entity.save(this);
                                                } else {
                                                    table.setSync(true);
                                                    table.setEntityId(entityId);
                                                    table.setValue(entityJson.toString());
                                                    table.setEntityName(entityName);
                                                    table.save(this);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    private void deleteFlows(Function function) {
//        //Delete the Links associated with the function
//        List<Link> links = function.getLinks(this).all().toList();
//        for (int j=0; j<links.size(); j++){
//            Link l = links.get(j);
//            l.delete(this);
//        }
//        // Delete the ClientFlows associated with the function
//        List<ClientFlows> clientFlows = function.getClientFlows(this).all().toList();
//        for(int j=0; j<clientFlows.size(); j++){
//            ClientFlows c = clientFlows.get(j);
//            c.delete(this);
//        }
//        // Delete the EntityFlows associated with the function
//        List<EntityFlows> entityFlows = function.getEntityFlows(this).all().toList();
//        for(int j=0; j<entityFlows.size(); j++){
//            EntityFlows c = entityFlows.get(j);
//            c.delete(this);
//        }
//        //error message sent to the caller
//    }
//
//    private ArrayList<Function> getUpdatedList(List<Function> serverList, List<Function> localList) {
//        ArrayList<Function> updatedList = new ArrayList<Function>();
//        for (int i=0; i<localList.size(); i++){
//            for (int j=0; j<serverList.size(); j++) {
//                if (localList.get(i).getID().equalsIgnoreCase(serverList.get(j).getID())) {
//                    if(localList.get(i).getVersionNumber() < serverList.get(j).getVersionNumber()){
//                        updatedList.add(serverList.get(j));
//                    }
//                    break;
//                }
//            }
//        }
//        return updatedList;
//    }
//
//    private ArrayList<Function> getUpdateList(List<Function> serverList, List<Function> localList) {
//        ArrayList<Function> updateList = new ArrayList<Function>();
//        for (int i=0; i<localList.size(); i++){
//            for (int j=0; j<serverList.size(); j++) {
//                if (localList.get(i).getID().equalsIgnoreCase(serverList.get(j).getID())) {
//                    if(localList.get(i).getVersionNumber() < serverList.get(j).getVersionNumber()){
//                        updateList.add(localList.get(i));
//                    }
//                    break;
//                }
//            }
//        }
//        return updateList;
//    }
//
//    private ArrayList<Function> getNewList(ArrayList<Function> serverList, ArrayList<Function> localList) {
//        ArrayList<Function> newList = new ArrayList<Function>();
//        for (int i=0; i<serverList.size(); i++){
//            if(localList.size() > 0) {
//                for (int j = 0; j < localList.size(); j++) {
//                    if (serverList.get(i).getID().equalsIgnoreCase(localList.get(j).getID())) {
//                        break;
//                    }
//                    newList.add(serverList.get(i));
//                }
//            }
//            else {
//                newList = serverList;
//            }
//        }
//        return newList;
//    }

    private ArrayList<Integer> getDeleteList(ArrayList<Integer> serverList, ArrayList<Integer> localList) {
        ArrayList<Integer> deleteList = new ArrayList<>();
        for (int i=0; i<localList.size(); i++){
            for (int j=0; j<serverList.size(); j++) {
                if (localList.get(i).equals(serverList.get(j))) {
                    break;
                }
                else {
                    deleteList.add(localList.get(i));
                }
            }
        }
        return deleteList;
    }

    /*
    * download all function categories based on the user capability
    */
    private String downloadFunctionCategories(String action, String url, String data) {
        String mOutput = null;
        RetryManager retryManager = new RetryManager();
        if (isNetworkAvailable()) {
            while (retryManager.shouldRetry()) {
                try {
                    mOutput = Ion.with(this)
                            .load("GET", url)
                            .group(mFlowGroup)
                            .asString()
                            .get();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG + "/url/download/category", e.getMessage());
                    if (isNetworkAvailable()) {
                        try {
                            retryManager.errorOccured();
                        } catch (Exception e1) {
                            Log.e(TAG + "/url/download/category/retry", e1.getMessage());
                            return null;
                        }
                    }
                    else {
                        sendInternetBroadcast(action, data);
                        stopSelf();
                        break;
                    }
                }
            }
            return mOutput;
        }
        else {
            sendInternetBroadcast(action, data);
            stopSelf();
        }
        return null;
    }

    private boolean saveCategory(String mOutput) {
        if (mOutput != null) {
            //Successful functions categories downloaded
            JSONArray jsonArray;
            try {
                JSONObject categories = new JSONObject(mOutput);
                jsonArray = categories.getJSONArray("functionCategories");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG + "/json/download_category", e.getMessage());
                return false;
            }

            for (int i=0; i<jsonArray.length(); i++){
                int id = 0;
                JSONObject jsonObject = null;
                try{
                    jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getInt(FunctionCategory.COLUMN_ID);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                String versionNumber = jsonObject != null ? jsonObject.optString(
                        FunctionCategory.COLUMN_VERSION_NUMBER) : null;
                String name = jsonObject != null ? jsonObject.optString(
                        FunctionCategory.COLUMN_NAME) : null;
                int parentId = jsonObject != null ? jsonObject.optInt(
                        FunctionCategory.COLUMN_PARENT_CATEGORY_ID) : 0;

                FunctionCategory category = FunctionCategory.getFunctionCategoryById(this, id);

                if (category == null){
                    FunctionCategory functionCategory = new FunctionCategory();
                    functionCategory.setName(name);
                    try {
                        functionCategory.setValue(jsonArray.get(i).toString());
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    functionCategory.setVersionNumber(versionNumber);
                    functionCategory.setParentCategoryID(parentId);
                    functionCategory.save(this, id);
                }
                else {
                    category.setName(name);
                    try {
                        category.setValue(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    category.setVersionNumber(versionNumber);
                    category.setParentCategoryID(parentId);
                    category.save(this);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /*
    * download all function based on the user capability
    */
    private String downloadFunction(String action, String url, String data) {
        String output = null;
        RetryManager retryManager = new RetryManager();
        if (isNetworkAvailable()) {
            while (retryManager.shouldRetry()) {
                try {
                    output = Ion.with(this)
                            .load("GET", url)
                            .group(mFlowGroup)
                            .asString()
                            .get();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG+"/url/download/function", e.getMessage());
                    if (isNetworkAvailable()) {
                        try {
                            retryManager.errorOccured();
                        } catch (Exception e1) {
                            Log.e(TAG + "/url/download/function/retry", e1.getMessage());
                            return null;
                        }
                    }
                    else {
                        sendInternetBroadcast(action, data);
                        stopSelf();
                        break;
                    }
                }
            }
            return output;
        }
        else {
            sendInternetBroadcast(action, data);
            stopSelf();
        }
        return null;
    }

    private boolean saveFunction(String output) {
        if (output != null) {
            //Successful functions categories downloaded
            JSONArray jsonArray;
            try {
                JSONObject functions = new JSONObject(output);
                jsonArray = functions.getJSONArray("functions");
            }
            catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject;
                int id;
                try {
                    jsonObject = jsonArray.getJSONObject(i);
                    id = jsonObject.getInt(Function.COLUMN_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG + "/json/function/id", e.getMessage());
                    return false;
                }
                String versionNumber = jsonObject.optString(Function.COLUMN_VERSION_NUMBER);
                String name = jsonObject.optString(Function.COLUMN_NAME);
                String description = jsonObject.optString(Function.COLUMN_DESCRIPTION);
                String flowGuid = jsonObject.optString(Function.COLUMN_FLOWGUID);
                int categoryId = jsonObject.optInt(Function.COLUMN_CATEGORY_ID);

                Function function = Function.getFunctionById(this, id);
                if (function == null){
                    Function newFunction = new Function();
                    try {
                        newFunction.setValue(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    newFunction.setVersionNumber(versionNumber);
                    newFunction.setName(name);
                    newFunction.setDescription(description);
                    newFunction.setFlowGuid(flowGuid);
                    newFunction.setCategoryID(categoryId);
                    newFunction.save(this, id);
                }
                else {
                    try {
                        function.setValue(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    function.setVersionNumber(versionNumber);
                    function.setName(name);
                    function.setDescription(description);
                    function.setFlowGuid(flowGuid);
                    function.setCategoryID(categoryId);
                    function.save(this);
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /*
    * download flows based on functions
    */
    private String downloadFlows(String action, String guid, String data) {
        String flowsContent = null;
        RetryManager retryManager = new RetryManager();
        if (isNetworkAvailable()) {
            while (retryManager.shouldRetry()) {
                try {
                    flowsContent = Ion.with(this)
                            .load("GET", URL_FLOWS_ID + guid)
                            .group(mFlowGroup)
                            .asString()
                            .get();
                    break;
                } catch (Exception e) {
                    //Interrupted Download
                    Log.e(TAG+"/url/download/flows", e.getMessage());
                    if (isNetworkAvailable()) {
                        try {
                            retryManager.errorOccured();
                        } catch (Exception e1) {
                            Log.e(TAG + "/url/download/flows/retry", e1.getMessage());
                            return null;
                        }
                    }
                    else {
                        sendInternetBroadcast(action, data);
                        stopSelf();
                        break;
                    }
                }
            }
            return flowsContent;
        }
        else {
            sendInternetBroadcast(action, data);
            stopSelf();
        }
        return null;
    }

    private boolean saveClientFlows(String flowsContent) {
        if (flowsContent != null) {
            try {
                JSONObject object = new JSONObject(flowsContent);
                String flowId = object.getString(ClientFlows.COLUMN_ID);

                ClientFlows flows = ClientFlows.getFlowById(this, flowId);
                if (flows == null) {
                    ClientFlows clientFlows = new ClientFlows();
                    clientFlows.setFlows(flowsContent);
                    clientFlows.setFlowsID(flowId);
                    clientFlows.save(this);
                } else {
                    flows.setFlows(flowsContent);
                    flows.setFlowsID(flowId);
                    flows.save(this);
                }
                return true;
            }
            catch (Exception e){
                return false;
            }
        }
        else {
            return false;
        }
    }

    /*
    * download entities based on flows
    */
    private String downloadEntity(String action, String guid, String data) {
        String output = null;
        RetryManager retryManager = new RetryManager();
        if (isNetworkAvailable()) {
            while (retryManager.shouldRetry()) {
                try {
                    output = Ion.with(this)
                            .load("GET", URL_ENTITY+guid)
                            .setStringBody(guid)
                            .group(mFlowGroup)
                            .asString()
                            .get();
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG+"/url/download/flowEntity", e.getMessage());
                    if (isNetworkAvailable()) {
                        try {
                            retryManager.errorOccured();
                        } catch (Exception e1) {
                            Log.e(TAG + "/url/download/flowEntity/retry", e1.getMessage());
                            return null;
                        }
                    }
                    else {
                        sendInternetBroadcast(action, data);
                        stopSelf();
                        break;
                    }
                }
            }
            return output;
        }
        else {
            sendInternetBroadcast(action, data);
            stopSelf();
        }
        return null;
    }

    private boolean saveFlowEntity(String entityOutput) {
        if (entityOutput != null) {
            try {
                JSONObject outputJson = new JSONObject(entityOutput);
                JSONArray jsonArray = outputJson.getJSONArray("entities");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String entityName = object.getString("EntityType");
                    JSONObject entityJson = object.getJSONObject("Entity");
                    int entityID = entityJson.optInt(Entity.COLUMN_ID);

                    Entity entity = Entity.getEntityById(this, entityName, entityID);
                    if (entity == null) {
                        Entity newEntity = new Entity();
                        newEntity.setEntityId(entityID);
                        newEntity.setEntityName(entityName);
                        newEntity.setSync(true);
                        newEntity.setValue(entityJson.toString());
                        newEntity.save(this);
                    } else {
                        entity.setSync(true);
                        entity.setValue(entityJson.toString());
                        entity.save(this);
                    }
                }
                return true;
            }
            catch (Exception e){
                return false;
            }
        }
        else {
            return false;
        }
    }

    /*
    * Perform Upload functions
    */
    private void doDownload(){

    }

    /*
    * Perform Upload functions
    */
    private void doUpload(){
        ArrayList<Entity> allEntities = Entity.getUnSyncedEntity(this);

        if (allEntities.size() > 0) {
            //Create batches of entity to send to the server
            ArrayList<ArrayList<Entity>> splitted = new ArrayList<>();
            int arrayLength = allEntities.size();
            ArrayList<Entity> aBatch = new ArrayList<>(ENTITY_PER_BATCH);
            for (int i = 0; i < arrayLength; i++) {
                int arrayIndex = i % ENTITY_PER_BATCH;
                if (arrayIndex == 0) {
                    if (i != 0) {
                        splitted.add(aBatch);
                    }
                    aBatch = new ArrayList<>(ENTITY_PER_BATCH);
                }
                aBatch.add(arrayIndex, allEntities.get(i));
            }
            splitted.add(aBatch);

            for (int j = 0; j < splitted.size(); j++) {
                ArrayList<Entity> batchEntity = splitted.get(j);
                if (batchEntity != null) {
                    JSONArray batch = new JSONArray();
                    for (int i = 0; i < batchEntity.size(); i++) {
                        String input = batchEntity.get(i).getValue();
                        String entityName = batchEntity.get(i).getEntityName();
                        JSONObject object = new JSONObject();
                        try {
                            JSONObject data = new JSONObject(input);
                            object.put(LocalEntityService.NAME_ENTITY, entityName);
                            object.put(LocalEntityService.PARAM_DATA, data);
                            batch.put(object);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, input);
                        }
                    }
                    uploadEntities(batchEntity, batch);
                }
            }
        }
    }

    /*
    * Upload Entities to the server
    */
    private void uploadEntities(ArrayList<Entity> entities, JSONArray batch) {
        JsonArray jsonArray = new JsonParser().parse(batch.toString()).getAsJsonArray();
        String result;
        try {
            result = Ion.with(this)
                    .load("POST", URL_UPLOAD)
                    .setJsonArrayBody(jsonArray)
                    .group(LocalEntityService.mEntityGroup)
                    .asString()
                    .get();
            JSONObject output = new JSONObject(result);
            if (output.getString(LocalEntityService
                    .NAME_EVENT_NAME).equalsIgnoreCase(LocalEntityService.VALUE_UPLOADED)){
                for (int i=0; i<entities.size(); i++) {
                    Entity entity = entities.get(i);
                    entity.setSync(true);
                    entity.save(FlowSyncService.this);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG+"/UploadEntities", e.getMessage());
        }
    }

    /*
    * Check for network connection availability
    */
    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /*
    * sends broadcast
    */
    private void sendMessage(String action, int id) {
        Intent intent = new Intent(action);
        // add data
        intent.putExtra(PARAM_MESSAGE, id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*
    * sends broadcast
    */
    private void sendMessage(String action, String id) {
        Intent intent = new Intent(action);
        // add data
        intent.putExtra(PARAM_MESSAGE, id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*
    * sends broadcast for Internet listening
    */
    private void sendInternetBroadcast(String action, String id) {
        Intent intent = new Intent(this, NetworkChangeReceiver.class);
        intent.setAction(action);
        intent.putExtra(PARAM_MESSAGE, id);
        sendBroadcast(intent);
    }
}
