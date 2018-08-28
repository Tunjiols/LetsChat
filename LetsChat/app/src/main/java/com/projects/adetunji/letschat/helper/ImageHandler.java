package com.projects.adetunji.letschat.helper;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.projects.adetunji.letschat.R;

/**
 * Created by adetunji on 31/01/2018.
 */

public class ImageHandler extends AppCompatActivity{

    private static final int PICK_Gallery_IMAGE 	= 1;
    private static final int PICK_Camera_IMAGE 		= 2;
    private static final int DELETE_IMAGE			= 3;
    private static final int REQUEST_READ_PERMISSION = 120;

    private ProgressDialog mDialog;
    private Uri mImageUri;
    private Bitmap mBitmap;
    private Context mContext;


    public void onOptionsItemSelected(String item, Context context, View view) {

        this.mContext = context;

        switch (item) {
            case "camera":

                //define the file-name to save photo taken by Camera activity
                String fileName = "new-photo-name.jpg";

                //create parameters for Intent with filename
                ContentValues mValues = new ContentValues();
                mValues.put(MediaStore.Images.Media.TITLE, fileName);
                mValues.put(MediaStore.Images.Media.DESCRIPTION,"Image capture by camera");

                //mImageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
                mImageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mValues);

                //create new Intent
                Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                mIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

                startActivityForResult(mIntent, PICK_Camera_IMAGE);
                break;

            case "gallery":

                try {

                    final Intent mIntent_2 = new Intent(Intent.ACTION_GET_CONTENT);
                    mIntent_2.setType("image/*");
                    startActivityForResult(mIntent_2, PICK_Gallery_IMAGE);
                    /*
                    Intent mIntent_2 = new Intent();
                    mIntent_2.setType("image/*");
                    mIntent_2.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(mIntent_2, "Select Picture"), PICK_Gallery_IMAGE);
                    */
                } catch (Exception e) {

                  //  Helper.displayToastMessage(getParent(), e.getMessage());
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
                        //mSnackBar_2.dismiss();
                    }
                });
        //mSnackBar.setColor(Color.Red_300);
        mSnackBar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri selectedImageUri = null;

        String filePath = null;

        switch (requestCode) {
            case PICK_Gallery_IMAGE:

                if (resultCode == Activity.RESULT_OK) {
                    selectedImageUri = data.getData();

                    if (ActivityCompat.checkSelfPermission(getParent(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions( getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_READ_PERMISSION);
                        return;
                    }
                }
                break;

            case PICK_Camera_IMAGE:

                if (resultCode == RESULT_OK) {

                    //use imageUri here to access the image

                    selectedImageUri = mImageUri;
                    if (ActivityCompat.checkSelfPermission(getParent(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions( getParent(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_READ_PERMISSION);
                        return;
                    }

                } else if (resultCode == RESULT_CANCELED) {

                    Helper.displayToastMessage(getParent(), "Picture was not taken");

                } else {

                    Helper.displayToastMessage(getParent(), "Picture was not taken");
                }


                break;
        }

        if(selectedImageUri != null){
            try {
                // OI FILE Manager
                String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                String selectedImagePath = getPath(selectedImageUri);

                if (selectedImagePath != null) {

                    filePath = selectedImagePath;

                } else if (filemanagerstring != null) {

                    filePath = filemanagerstring;

                } else {

                    Toast.makeText(getParent(), "Unknown path", Toast.LENGTH_LONG).show();
                    Log.e("Bitmap", "Unknown path");
                }

                if (filePath != null) {

                    decodeFile(filePath);

                } else {
                    mBitmap = null;
                }
            } catch (Exception e) {

                Toast.makeText(getParent(), "Internal error", Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Helper.displayToastMessage(getParent(), "Permission needed!");
                //Toast.makeText(getActivity(), "Permission needed!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = managedQuery(uri, projection, null, null, null);

        if (cursor != null) {
            // HERE YOU WILL GET A NULL POINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options mBitmapFac = new BitmapFactory.Options();
        mBitmapFac.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, mBitmapFac);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = mBitmapFac.outWidth, height_tmp = mBitmapFac.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options mBitmapFac_2 = new BitmapFactory.Options();
        mBitmapFac_2.inSampleSize = scale;
        mBitmap = BitmapFactory.decodeFile(filePath, mBitmapFac_2);

    }

    public void setImgView(ImageView mImgView){

        mImgView.setImageBitmap(mBitmap);

    }


    //Bitmap image is good for an icon
	/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			mImageView.setImageBitmap(imageBitmap);
		}
	}
	*/

    //<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
}
