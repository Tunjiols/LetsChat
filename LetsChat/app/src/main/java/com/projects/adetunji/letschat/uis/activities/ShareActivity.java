package com.projects.adetunji.letschat.uis.activities;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by adetunji on 24/02/2018.
 */

public class ShareActivity extends AppCompatActivity{
    /*
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, download this app!");
    startActivity(shareIntent);


    Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setText(shareText)
            .getIntent();
if (shareIntent.resolveActivity(getPackageManager()) != null) {
        startActivity(shareIntent);
    }


    File imageFile = ...;
    Uri uriToImage = FileProvider.getUriForFile(
            context, FILES_AUTHORITY, imageFile);
    Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
            .setStream(uriToImage)
            .getIntent();
// Provide read access
shareIntent.setData(uriToImage);
shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


    //Receiving file
    Uri uri = ShareCompat.IntentReader.from(activity).getStream();
    Bitmap bitmap = null;
try {
        // Works with content://, file://, or android.resource:// URIs
        InputStream inputStream =
                getContentResolver().openInputStream(uri);
        bitmap = BitmapFactory.decodeStream(inputStream);
    } catch (FileNotFoundException e) {
        // Inform the user that things have gone horribly wrong
    }*/
}
