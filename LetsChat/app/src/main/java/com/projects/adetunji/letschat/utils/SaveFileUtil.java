package com.projects.adetunji.letschat.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by adetunji on 31/01/2018.
 */

public class SaveFileUtil extends AppCompatActivity{

    /*Save file on internal storage*/

    private Context mFileContext;

    private File mSaveFile;

    public SaveFileUtil(Context mFileContext){
        this.mFileContext = mFileContext;
    }

    public void saveTextToFile(String filename, String text){

        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /*This method extract the filename from url and create file with it*/
    public File createTempCachedFile(String url){

        File mCachedFile = new File(mFileContext.getPackageName());

        try{
            String filename = Uri.parse(url).getLastPathSegment();
            mCachedFile = File.createTempFile(filename, null, mFileContext.getCacheDir());
        } catch(Exception e){

        }
        return mCachedFile;
    }


    /*Check if external storage is available for read and write*/
    public boolean isExternalStorageReadWriteable(){

        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    /*Get directory for user's public picture directory*/
    public File getAlbumStorageDirectory(String pictureName){

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), pictureName);

        if(!file.mkdirs()){
            //Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    /*Create a directory for individual photo album*/
    public File getAlbumStorageDirectory(Context mContext, String pictureName ){
        File file = new File(mContext.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), pictureName);

        if(!file.mkdirs()){
           // Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }



    /*Delete a file*/
    public void DeleteFile(File mFileName){
        mFileName.delete();
    }

    /*Delete a file from external storage*/
    public void DeleteFile(Context mContext, String mFileName){
        mContext.deleteFile(mFileName);
    }


    public static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);

        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for(int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
}
