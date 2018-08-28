package com.projects.adetunji.letschat.uis.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.projects.adetunji.letschat.LetsChatMainApp;
import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.helper.Helper;
import com.projects.adetunji.letschat.helper.HelperFirebaseStorage;
import com.projects.adetunji.letschat.helper.ImageHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by adetunji on 02/02/2018.
 */

public class PhotoOptSelectDialogFragment extends DialogFragment implements AlertDialog.OnClickListener{

    private ImageView mImageView = null;

    private Uri mImageUri;
    private Bitmap mBitmap;
    private Context mContext;
    private static final int PICK_Gallery_IMAGE 	= 1;
    private static final int PICK_Camera_IMAGE 		= 2;
    private static final int REQUEST_READ_PERMISSION = 120;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.photo_selection_option_dialog_fragment, null);

        final ImageView 	mGallery			= (ImageView)dialogView.findViewById(R.id.gallery);
        final ImageView		mCamera   			= (ImageView)dialogView.findViewById(R.id.camera);
        final ImageView 	mDelete				= (ImageView)dialogView.findViewById(R.id.delete);



        builder.setView(dialogView);
        builder.setTitle("");

        mGallery.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                onOptionsItemSelected("gallery", getContext(), NavProfilePictureFragment.getViews());
                dismiss();

            }
        });

        mCamera.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                onOptionsItemSelected("camera", getContext(), NavProfilePictureFragment.getViews());
                dismiss();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                onOptionsItemSelected("delete", getContext(), NavProfilePictureFragment.getViews());
                dismiss();
            }
        });

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
       /* switch (which) {
            case mGallery:
                dismiss();
                break;
            case mCamera:
                dismiss();
                break;
            case mDelete:
                dismiss();
                break;
        }*/
    }


    public void onOptionsItemSelected(String item, Context context, View view) {

        this.mContext = context;

        switch (item) {
            case "camera":

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
				/*
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, PICK_Camera_IMAGE);
                }
				*/
                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        // Error occurred while creating the File
                        Helper.displayToastMessage(getContext(), e.getMessage());
                        Log.e(e.getClass().getName(), e.getMessage(), e);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mImageUri = FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);

                        startActivityForResult(takePictureIntent, PICK_Camera_IMAGE);
                    }
                }
                break;

            case "gallery":

                try {

                    Intent mIntent_2 = new Intent();
                    mIntent_2.setType("image/*");
                    mIntent_2.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(mIntent_2, "Select Picture"), PICK_Gallery_IMAGE);

                } catch (Exception e) {

                    Helper.displayToastMessage(getContext(), e.getMessage());
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
                break;

            case "delete":
                showSnackbar(view);
                break;
        }
    }

    private void showSnackbar(View layoutView)
    {
        Snackbar mSnackBar = Snackbar.make(layoutView, "Picture deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Snackbar mSnackBar_2 = Snackbar.make(view, "Picture restored", Snackbar.LENGTH_SHORT);
                        mSnackBar_2.show();
                    }
                });
        //mSnackBar.setColor(Color.Red_300);
        mSnackBar.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = null;

        switch (requestCode) {
            case PICK_Camera_IMAGE:

                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    mImageView.setImageBitmap(imageBitmap);

                }else if (resultCode == Activity.RESULT_CANCELED) {

                    Helper.displayToastMessage(getContext(), "Picture was not taken");

                }
                break;
            case PICK_Gallery_IMAGE:

                if ( resultCode == Activity.RESULT_OK) {

                    mImageUri = data.getData();
                    try{
                        imageBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mImageUri);

                        mImageView.setImageBitmap(imageBitmap);

                    }catch(IOException e){
                        //Helper.displayToastMessage(getContext(), e);
                    }

                    String imagePath = getPath(mImageUri);

                }else if (resultCode == Activity.RESULT_CANCELED) {

                    Helper.displayToastMessage(getContext(), "Picture was not taken");

                }
                break;
        }

        //save image path to firebase cloud
        String uid = ((LetsChatMainApp)getActivity().getApplication()).getFirebaseUserAuthenticateId();
        HelperFirebaseStorage storageHelper = new HelperFirebaseStorage(getActivity());
        storageHelper.saveProfileImageToCloud(uid, mImageUri, mImageView);

        LetsChatMainApp.mImageUri 		= mImageUri;
        LetsChatMainApp.imageBitmap 	= imageBitmap;

    }

    //get directory path of the photo
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }


    public void setPhotoInto(ImageView imageView){
        this.mImageView = imageView;
    }

    public ImageView getImgView(){
        return mImageView;
    }

    private 	String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
//<uses-feature android:name="android.hardware.camera" android:required="true" />
}


/*<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="my_images" path="Android/data/com.example.package.name/files/Pictures" />
</paths>*/