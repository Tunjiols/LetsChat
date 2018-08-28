package com.projects.adetunji.letschat.uis.fragments;

import android.app.Dialog;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;

import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.adetunji.letschat.R;
import com.projects.adetunji.letschat.models.StringModel;
import com.projects.adetunji.letschat.uis.activities.UserListingActivity;
import com.projects.adetunji.letschat.uis.adapters.StringModelViewAdapter;
import com.projects.adetunji.letschat.utils.CameraPreview;
import com.projects.adetunji.letschat.utils.MediaSelection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Displays a {@link CameraPreview} of the first {@link Camera}.
 * An error message is displayed if the Camera is not available.
 * <p>
 * This Fragment is only used to illustrate that access to the Camera API has been granted (or
 * denied) as part of the runtime permissions model. It is not relevant for the use of the
 * permissions API.
 * <p>
 * Implementation is based directly on the documentation at
 * http://developer.android.com/guide/topics/media/camera.html
 */
/**
 * Created by adetunji on 14/03/2018.
 */

public class CameraFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "CameraFragment";

    /**
     * Id of the camera to access. 0 is the first camera.
     */
    private static final int CAMERA_ID = 0;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final int PICK_Gallery_IMAGE = 3;

    private boolean isRecording = false;

    private MediaRecorder mMediaRecorder;
    private CameraPreview mPreview;
    private Camera mCamera;
    private ImageView shooter_btn;

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Hide the action bar
        UserListingActivity userListingActivity = new UserListingActivity();
       // userListingActivity.getSupportActionBar().hide();

        // Open an instance of the first camera and retrieve its info.
        mCamera = getCameraInstance(CAMERA_ID);
        Camera.CameraInfo cameraInfo = null;

        if (mCamera != null) {
            // Get camera info only if the camera is available
            cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(CAMERA_ID, cameraInfo);
        }

        if (mCamera == null || cameraInfo == null) {
            // Camera is not available, display error message
            Toast.makeText(getContext(), "Camera is not available.", Toast.LENGTH_SHORT).show();
            return inflater.inflate(R.layout.fragment_camera_unavailable, null);
        }

        View root = inflater.inflate(R.layout.fragment_camera, null);

        // Get the rotation of the screen to adjust the preview image accordingly.
        final int displayRotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

        // Create the Preview view and set it as the content of this Fragment.
        mPreview = new CameraPreview(getContext(), mCamera, cameraInfo, displayRotation);
        FrameLayout preview     = (FrameLayout) root.findViewById(R.id.camera_preview);
        shooter_btn             = (ImageView) root.findViewById(R.id.shooter_btn);
        TextView mTimer         = (TextView) root.findViewById(R.id.timer);
        ImageView gallery_btn   = (ImageView) root.findViewById(R.id.gallery_letschat_btn);
        ImageView text_btn      = (ImageView) root.findViewById(R.id.Text_btn);

        preview.addView(mPreview);

        // Add a listener to the Capture button
        shooter_btn.setOnClickListener(this);
        gallery_btn.setOnClickListener(this);
        text_btn.setOnClickListener(this);

        // Add a Long listener to the shooter_btn Capture button
        shooter_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mediaRecording();
                return false;
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera access
        releaseMediaRecorder();       // release MediaRecorder first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance(int cameraId) {
        Camera c = null;
        if (UserListingActivity.getCameraPermission()) {
            try {
                c = Camera.open(cameraId); // attempt to get a Camera instance
            } catch (Exception e) {
                // Camera is not available (in use or does not exist)
                Log.d(TAG, "Camera " + cameraId + " is not available: " + e.getMessage());
            }
        }
        return c; // returns null if camera is unavailable
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.shooter_btn:
                // get an image from the camera
                mCamera.takePicture(null, null, mPicture);
                break;
            case R.id.gallery_letschat_btn:
                //open gallery
                MediaSelection mediaSelection = new MediaSelection();
                mediaSelection.gallery(getContext());
                break;
            case R.id.Text_btn:
                //Load Fragment for text editing
                break;
        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            DialogShowUp(data);
            File pictureFile = null;
            try {
                pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(pictureFile);
                fileOutputStream.write(data);
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) throws IOException {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) throws IOException {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), TAG);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "creating file directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "LetsChat_IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "LetsChat_VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    //====================================================================================

    /**
     * Prepare the MediaRecorder class for video recording
     */
    private boolean prepareVideoRecorder() throws IOException {

        mCamera = getCameraInstance(CAMERA_ID);
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void mediaRecording() {
        if (isRecording) {
            // stop recording and release camera
            shooter_btn.setBackgroundColor(getContext().getResources().getColor(R.color.white));
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            //DialogShowUp(data);

            // inform the user that recording has stopped
            //setCaptureButtonText("Capture");
            isRecording = false;
        } else {
            // initialize video camera
            try {
                if (prepareVideoRecorder()) {
                    // Camera is available and unlocked, MediaRecorder is prepared,now start recording
                    mMediaRecorder.start();
                    // inform the user that recording has started
                    shooter_btn.setBackgroundColor(getContext().getResources().getColor(R.color.red_300));
                    // setCaptureButtonText("Stop");
                    isRecording = true;
                } else {
                    // prepare didn't work, release the camera
                    releaseMediaRecorder();
                    shooter_btn.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                    // inform user
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }



    //=====================================================

    private void DialogShowUp(byte[] data) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.toast_list_view_list);

        final ListView listView = (ListView) dialog.findViewById(R.id.list);
        dialog.show();

        String[] values = new String[]{"Forward", "Set as profile picture", "Delete"};

        List<StringModel> mStringObjArray = new ArrayList<StringModel>();

        for (int i = 0; i < values.length; i++) {
            StringModel mStrObj = new StringModel();
            mStrObj.text = values[i];
            mStringObjArray.add(mStrObj);
        }

        StringModelViewAdapter mArrayViewAdapter = new StringModelViewAdapter(getContext(), mStringObjArray);

        listView.setAdapter(mArrayViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  itemValue = listView.getItemAtPosition(position).toString();
                switch (itemValue){
                    case "Forward":

                        break;
                    case "Set as profile picture":
                        break;
                    case "Delete":
                        break;
                }

                dialog.cancel();
            }

        });
    }
}
