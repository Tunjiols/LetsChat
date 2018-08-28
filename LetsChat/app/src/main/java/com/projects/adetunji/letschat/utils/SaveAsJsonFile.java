package com.projects.adetunji.letschat.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import com.projects.adetunji.letschat.models.Chat;
import com.projects.adetunji.letschat.models.UserData;
import com.projects.adetunji.letschat.models.UserEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

/**
 * Created by adetunji on 27/02/2018.
 */

public class SaveAsJsonFile {
    private static String TAG = "SaveAsJsonFile";
    private static Context mContext;

    public SaveAsJsonFile(Context mContext) {
        this.mContext = mContext;
    }

    /**-------------------------------------------------------------------------------------
     *#1	JSON file for saving userEntity.java data
     * saves the LetsChat users in a list
     */
    // json related keys
    private static final String JSON_UID 			= "Uid";
    private static final String JSON_FULLNAME 		= "String_Fullname";
    private static final String JSON_USERNAME		= "Username";
    private static final String JSON_EMAIL			= "Email";
    private static final String JSON_PHONE			= "Phone";
    private static final String JSON_BIRTHDAY		= "Birthday";
    private static final String JSON_HOBBY			= "Hobby";
    private static final String JSON_RELATIONSHIP	= "Relationship";
    private static final String JSON_CURRENT_CITY	= "Current_city";
    private static final String JSON_STATUS			= "Status";
    private static final String JSON_LASTSEENDATE	= "LastSeenDate";

    private JSONObject convertToJSON(UserEntity user) throws JSONException{

        JSONObject jsonObj = new JSONObject();

        jsonObj.put(JSON_UID, user.mgetuId());
        jsonObj.put(JSON_FULLNAME, user.mgetFullname());
        jsonObj.put(JSON_USERNAME, user.mgetUsername());
        jsonObj.put(JSON_EMAIL, user.Email);
        jsonObj.put(JSON_PHONE, user.mgetPhone());
        jsonObj.put(JSON_BIRTHDAY, user.mgetBirthday());
        jsonObj.put(JSON_HOBBY, user.mgetHobby());
        jsonObj.put(JSON_RELATIONSHIP, user.mgetRelationship());
        jsonObj.put(JSON_CURRENT_CITY, user.mgetCurrent_city());
        jsonObj.put(JSON_STATUS, user.mgetStatus());
        jsonObj.put(JSON_LASTSEENDATE, user.mgetLastSeenDate());

        return jsonObj;
    }

    private UserEntity getJSONObj(JSONObject jsonObj) throws JSONException {
        UserEntity user = new UserEntity();

        user.Uid		    = jsonObj.getString(JSON_UID);
        if (jsonObj.has(JSON_FULLNAME)) {
            user.Fullname = jsonObj.getString(JSON_FULLNAME);
        }else user.Fullname = "";
        if (jsonObj.has(JSON_USERNAME)) {
            user.Username = jsonObj.getString(JSON_USERNAME);
        }else  user.Username = "";
        user.Email			= jsonObj.getString(JSON_EMAIL);
        if (jsonObj.has(JSON_PHONE)) {
            user.Phone = jsonObj.getString(JSON_PHONE);
        }else user.Phone = "";
        if (jsonObj.has(JSON_BIRTHDAY)) {
            user.Birthday = jsonObj.getString(JSON_BIRTHDAY);
        }else user.Birthday = "";
        if (jsonObj.has(JSON_HOBBY)) {
            user.Hobby = jsonObj.getString(JSON_HOBBY);
        }else user.Hobby = "";
        if (jsonObj.has(JSON_RELATIONSHIP)) {
            user.Relationship = jsonObj.getString(JSON_RELATIONSHIP);
        }else user.Relationship = "";
        if (jsonObj.has(JSON_CURRENT_CITY)) {
            user.Current_city = jsonObj.getString(JSON_CURRENT_CITY);
        }else user.Current_city = "";
        if (jsonObj.has(JSON_STATUS)) {
            user.status = jsonObj.getString(JSON_STATUS);
        }else user.status = "Available";
        if (jsonObj.has(JSON_LASTSEENDATE)) {
            user.lastSeenDate = jsonObj.getLong(JSON_LASTSEENDATE);
        }else user.lastSeenDate = 0;

        return user;
    }

    private void saveJSONfile(List<UserEntity> userData, String uid)throws IOException, JSONException{

        String mFilename = uid+"UsersEntityData.json";

        // Make an array in JSON format
        JSONArray jArray = new JSONArray();

        // And load it
        for (UserEntity user : userData)
            jArray.put(convertToJSON(user));

        // Now write it to the private disk space of our app
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private List<UserEntity> loadJSONfile(String uid) throws IOException, JSONException{

        List<UserEntity> mfriendList = new ArrayList<>();
        String mFilename = uid+"UsersEntityData.json";

        BufferedReader reader = null;
        try {
            InputStream inputStr 	= mContext.openFileInput(mFilename);
            reader 					= new BufferedReader(new InputStreamReader(inputStr));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                mfriendList.add(getJSONObj(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "SaveAsJsonFile #1 userEntity.java — Reading JSON file " + e);
        } finally {// This will always run
            if (reader != null)
                reader.close();
        }
        return mfriendList;
    }



    public void saveUsersEntity(List<UserEntity> userEntity, String uid){
        try{
            saveJSONfile(userEntity, uid);
        }catch(Exception e){
            Log.e("Error Savin FriendsList","", e);
        }
    }

    public  List<UserEntity> loadUsersEntity(String uid){

        List<UserEntity> userEntityArray;

        try {
            userEntityArray = loadJSONfile(uid);
        } catch (Exception e) {
            userEntityArray = new ArrayList<UserEntity>();
            Log.e("Error load Friendslist:", "", e);
        }
        return userEntityArray;
    }





    /**----------------------------------------------------------------------------------------------------
     *#2	JSON file for saving userData.java data
     * saves the selected friend
     */

    // json related keys
    private static final String KEY_TO = "to";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_DESC = "description";
    private static final String KEY_IMG_PATH = "imagePath";
    private static final String KEY_IMG_RES = "imageResource";

    private JSONObject convertImageToJSONObj(String email, String desc, ArrayList<String> imgPath, ArrayList<Resources> imgView) {

        //JSONObject jsonObjectBody = new JSONObject();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put(KEY_DESC, desc);
            jsonObj.put(KEY_EMAIL, email);
            jsonObj.put(KEY_IMG_PATH, imgPath);
            jsonObj.put(KEY_IMG_RES, imgView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj;
    }

    private static JSONObject convertTextToJSONObj(UserData dataToSave)throws JSONException {

        JSONObject jsonObj = new JSONObject();

        jsonObj.put(KEY_USERNAME, dataToSave.getUsername());
        jsonObj.put(KEY_EMAIL, dataToSave.getEmail());

        return jsonObj;
    }

    private static UserData getTextJSONObj(JSONObject jsonObj) throws JSONException {
        UserData data = null;

        data.Email		= jsonObj.getString(JSON_EMAIL);
        if (jsonObj.has(JSON_USERNAME)) {
            data.Username	= jsonObj.getString(JSON_USERNAME);
        }else data.Username = "";

        return data;
    }

    public void saveObjectToFile(UserData userdata, String filename) throws IOException, JSONException{
        //String path = Environment.getExternalStorageDirectory() + File.separator + "/LetsChat/App_cache" + File.separator;

       // List<UserData> data = null;
       // data.add(userdata);
        String path = filename + ".json";
		/*
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File datafile = new File(path);

        if (!datafile.createNewFile()) {
            datafile.delete();
            datafile.createNewFile();
        }
		*/

        JSONArray jArray = new JSONArray();

        // Load into Array
       // for (UserData udata : data)
            jArray.put(convertTextToJSONObj(userdata));

        // Now write it to the private disk space of our app
        Writer writer = null;

        try {
            OutputStream out = mContext.openFileOutput(path, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(datafile));

        //objectOutputStream.writeObject(object);
        //objectOutputStream.close();

        //return path;
    }

    public List<UserData> objectFromFile(String path) throws IOException, JSONException, ClassNotFoundException {

        List<UserData> data = new ArrayList<>();

        BufferedReader reader = null;
        try {
            InputStream inputStr 	= mContext.openFileInput(path);
            reader 					= new BufferedReader(new InputStreamReader(inputStr));
            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for (int i = 0; i < jArray.length(); i++) {
                data.add(getTextJSONObj(jArray.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Log.e(TAG, "SaveAsJsonFile #2 userData.java — Reading JSON file " + e);
        } finally {// This will always run
            if (reader != null)
                reader.close();
        }
        //return data;
        //Object object = null;
        // File data = new File(path);

        //  if (data.exists()) {
        //      ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
        //      object = objectInputStream.readObject();
        //     objectInputStream.close();
        // }
         return data;
    }






    /**----------------------------------------------------------------------------------------
     *#3	JSON file for saving Chat.java data
     * saves the friend list
     */

    private static final String KEY_DATA = "data";
    private static final String KEY_IN = "chat_rooms";

    private static final String KEY_SENDER = "sender";
    private static final String KEY_RECEIVER = "receiver";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_SENDER_UID = "senderUid";
    private static final String KEY_RECEIVER_UID = "receiverUid";
    private static final String KEY_ISREAD = "isRead";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_LIKE_STATUS = "LIKE_STATUS";

    private static JSONObject convertChatToJSONObj(Chat dataToSave)throws JSONException {

        JSONObject jsonObjectBodyDateAsKey = new JSONObject();
        JSONObject jsonObjectData = new JSONObject();

        jsonObjectData.put(KEY_SENDER, dataToSave.sender);
        jsonObjectData.put(KEY_RECEIVER, dataToSave.receiver);
        jsonObjectData.put(KEY_MESSAGE, dataToSave.message);
        jsonObjectData.put(KEY_SENDER_UID, dataToSave.senderUid);
        jsonObjectData.put(KEY_RECEIVER_UID, dataToSave.receiverUid);
        jsonObjectData.put(KEY_ISREAD, dataToSave.isRead);
        jsonObjectData.put(KEY_TIMESTAMP, dataToSave.timestamp);
        jsonObjectData.put(KEY_LIKE_STATUS, dataToSave.LIKE_STATUS);

        jsonObjectBodyDateAsKey.put(String.valueOf(dataToSave.timestamp), jsonObjectData);

        return jsonObjectData;
    }


    private static Chat getChatJSONObj(JSONObject jsonObjectOuterBodyWithKey, String senderUId, String receiUId) throws JSONException {
        JSONObject jsonObjectBody = new JSONObject();
        JSONArray jsonObjectInsideArrayWithNoKey = new JSONArray();
        Chat data = null;

        final String room_type_1 = senderUId + "_" + receiUId;
        final String room_type_2 = receiUId + "_" + senderUId;


        if (jsonObjectOuterBodyWithKey.has(room_type_1)) {

            //jsonObjectInsideArrayWithNoKey.put(jsonObjectOuterBodyWithKey);

            for (int i = 0; i < jsonObjectOuterBodyWithKey.length(); i++) {
				/*
				for (var key in jsonObjectInsideArrayWithNoKey.getJSONObject(i) {
					var value = data[key];
					alert(key + ", " + value);
				}

				Set keys = jsonObjectInsideArrayWithNoKey.get(i).keySet();
				Iterator itr = keys.iterator();
				while(itr.hasNext()) {
					String key = (String)a.next();
					// loop to get the dynamic key
					String value = (String)jsonObjectInsideArrayWithNoKey.get(key);
				}
				*/
                data.sender = jsonObjectBody.getString(KEY_SENDER);
                data.receiver = jsonObjectBody.getString(KEY_RECEIVER);
                data.message = jsonObjectBody.getString(KEY_MESSAGE);
                data.senderUid = jsonObjectBody.getString(KEY_SENDER_UID);
                data.receiverUid = jsonObjectBody.getString(KEY_RECEIVER_UID);
                data.timestamp = jsonObjectBody.getLong(KEY_TIMESTAMP);
                data.isRead = jsonObjectBody.has(KEY_ISREAD) && jsonObjectBody.getBoolean(KEY_ISREAD);

                if (jsonObjectBody.has(KEY_LIKE_STATUS)) {
                    data.LIKE_STATUS = jsonObjectBody.getInt(KEY_LIKE_STATUS);
                }
            }
        } else if (jsonObjectOuterBodyWithKey.has(room_type_2)) {

            //jsonObjectInsideArrayWithNoKey.getJSONObject(room_type_2);

            for (int i = 0; i < jsonObjectOuterBodyWithKey.length(); i++) {
                data.sender = jsonObjectBody.getString(KEY_SENDER);
                data.receiver = jsonObjectBody.getString(KEY_RECEIVER);
                data.message = jsonObjectBody.getString(KEY_MESSAGE);
                data.senderUid = jsonObjectBody.getString(KEY_SENDER_UID);
                data.receiverUid = jsonObjectBody.getString(KEY_RECEIVER_UID);
                data.timestamp = jsonObjectBody.getLong(KEY_TIMESTAMP);
                data.isRead = jsonObjectBody.has(KEY_ISREAD) && jsonObjectBody.getBoolean(KEY_ISREAD);

                if (jsonObjectBody.has(KEY_LIKE_STATUS)) {
                    data.LIKE_STATUS = jsonObjectBody.getInt(KEY_LIKE_STATUS);
                }
            }
        }
        return data;
    }

    public static void saveChatJSONArrayToFile(List<Chat> data, String filename) throws IOException, JSONException{
        //String path = Environment.getExternalStorageDirectory() + File.separator + "/LetsChat/App_cache" + File.separator;
        String path = filename + ".json";

        JSONArray jsonObjectInsideArrayWithNoKey = new JSONArray();
        JSONObject jsonObjectOuterBodyWithKey = new JSONObject();
        //JSONArray jArray = new JSONArray();

        // Make the last array in JSON format
        JSONArray jArray = new JSONArray();


        // Load into Array
        for (Chat cdata : data){

            //Put the JSON object inside JSON Array
            jsonObjectInsideArrayWithNoKey.put(convertChatToJSONObj(cdata));

            final String room_type_1 = cdata.senderUid + "_" + cdata.receiverUid;
            final String room_type_2 = cdata.receiverUid + "_" + cdata.senderUid;

            if (jsonObjectOuterBodyWithKey.has(room_type_1)) {
                jsonObjectOuterBodyWithKey.put(room_type_1, jsonObjectInsideArrayWithNoKey);
                Log.e(TAG, "saved message into Chats JSON file: " + room_type_1 + " room_type_1 exists");
            } else if (jsonObjectOuterBodyWithKey.has(room_type_2)) {
                jsonObjectOuterBodyWithKey.put(room_type_2, jsonObjectInsideArrayWithNoKey);
                Log.e(TAG, "saved message into Chats JSON file: " + room_type_2 + " room_type_2 exists");
            } else {
                jsonObjectOuterBodyWithKey.put(room_type_1, jsonObjectInsideArrayWithNoKey);
                Log.e(TAG, "saved message into Chats JSON file: " + room_type_1 + " room_type_1 created");
            }

            // And load it
            jArray.put(jsonObjectOuterBodyWithKey);
        }

        // Now write it to the private disk space of the app
        Writer writer = null;

        try {
            OutputStream out = mContext.openFileOutput(path, mContext.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    public static Chat getChatJSONArrayFromFile(String filename, String senderUId, String receiUId) throws IOException, JSONException, ClassNotFoundException {

        String path = filename + ".json";

        Chat data = null;
        JSONObject jsonObjectData = null;
        JSONObject jsonObjectBody;
        BufferedReader reader = null;

        try {
            InputStream inputStr 	= mContext.openFileInput(path);
            reader 					= new BufferedReader(new InputStreamReader(inputStr));

            StringBuilder jsonString = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            //List<Chat> dataInner = null;

            for (int i = 0; i < jArray.length(); i++) {

                data = getChatJSONObj(jArray.getJSONObject(i), senderUId, receiUId);
                //data = dataInner(i);
            }

            //data.add(getChatJSONObj(jsonObjectBody));

        } catch (FileNotFoundException e) {
            Log.e(TAG, "SaveAsJsonFile #3 Chat.java — Reading JSON file " + e);
        } finally {
            // This will always run
            if (reader != null)
                reader.close();
        }

        //JSONObject jsonObject = new JSONObject(JSON);
        //
        //Object level = getSth.get("2");
        /*


        //
        // File data = new File(path);

        //  if (data.exists()) {
        //      ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(data));
        //      object = objectInputStream.readObject();
        //     objectInputStream.close();
        // }
        //return object;
        */
        return data;
    }

    /**----------------------------------------------------------------------------------------
     *#4	JSON file for saving  data
     * saves the friend list
     */

    public JSONObject convertSingleDataToJSONObj(String KEY, String value)throws JSONException {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put(KEY, value);

        return jsonObj;
    }

    private void saveSingleJSONfile(List<String> data, String filename)throws IOException, JSONException{

        String mFilename = filename+".json";

        // Make an array in JSON format
        JSONArray jArray = new JSONArray();

        // And load it
        for (String user : data)
            //jArray.put(convertToJSON(user));

            // Now write it to the private disk space of our app
            //Writer writer = null;
            try {
                OutputStream out = this.mContext.openFileOutput(mFilename, this.mContext.MODE_PRIVATE);
                //writer = new OutputStreamWriter(out);
                // writer.write(jArray.toString());
            } finally {
                // if (writer != null) {
                //     writer.close();
                // }
            }
    }

    private String read(Context context, String fileName) {
        try {
            FileInputStream jSonFile = context.openFileInput(fileName);
            InputStreamReader inputStreamR = new InputStreamReader(jSonFile);
            BufferedReader bufferedReader = new BufferedReader(inputStreamR);
            StringBuilder stringBuild = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuild.append(line);
            }
            return stringBuild.toString();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
    }




    private boolean create(Context context, String fileName, String jsonString){
        String FILENAME = "storage.json";
        try {
            FileOutputStream openJsonFile = this.mContext.openFileOutput(fileName,Context.MODE_PRIVATE);
            if (jsonString != null) {
                openJsonFile.write(jsonString.getBytes());
            }
            openJsonFile.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent(Context context, String fileName) {
        String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public String getDataJSON(Context context){
        boolean isFilePresent = isFilePresent(context, "storage.json");
        String jsonString = null;
        if(isFilePresent) {
            jsonString = read(context, "storage.json");
            //do the json parsing here and do the rest of functionality of app
        } else {
            boolean isFileCreated = create(context, "storage.json", "{}");
            if(isFileCreated) {
                //pr oceed with storing the first todo  or show ui
            } else {
                //show error or try again.
            }
        }
        return jsonString;
    }

}


/*

@Override
protected void onPause(){
super.onPause();
mNoteAdapter.saveNotes();
}


Object intervention = json.get("intervention");
if (intervention instanceof JSONArray) {
    // It's an array
    interventionJsonArray = (JSONArray)intervention;
}
else if (intervention instanceof JSONObject) {
    // It's an object
    interventionObject = (JSONObject)intervention;
}
else {
    // It's something else, like a string or number
}
*/
