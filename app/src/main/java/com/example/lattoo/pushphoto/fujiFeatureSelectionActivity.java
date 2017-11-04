package com.example.lattoo.pushphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.squareup.picasso.Picasso;
import com.google.firebase.storage.StorageMetadata;

public class fujiFeatureSelectionActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE_FUJI = 1;
    private static final String DEBUG = "fujiFeatSel";
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private StorageReference filepath;
    private ProgressDialog mProgress;
    private ImageView mImageView;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private String User;
    private String pictureImagePath = "";
    private String ColorValue = "";
    private String ShapeValue = "";
    private String TouchesValue = "";
    private String SpotMarksValue = "";
    private String RustingValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuji_feature_selection);
        openBackCamera();
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();
        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        mImageView = (ImageView) findViewById(R.id.image_from_url);
    }


    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        imageUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_FUJI);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE_FUJI && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
            mImageView.setImageBitmap(bitmap);


        }

        else {
            Toast.makeText(fujiFeatureSelectionActivity.this,  "Fuji Sample discarded",
                    Toast.LENGTH_SHORT).show();
            Intent _result = new Intent();
            setResult(RESULT_CANCELED, _result);
            finish();
        }
    }

    public void resizeImage(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        //return BitmapFactory.decodeFile(photoPath, bmOptions);
        File file = new File(pictureImagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            bitmap.recycle();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(file);
    }


    public void selectColor(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.color_full:
                if (checked)
                    ColorValue = "full";
                break;
            case R.id.color_medium:
                if (checked)
                    ColorValue = "medium";
                break;
            case R.id.color_average:
                if (checked)
                    ColorValue = "average";
                break;
        }
    }

    public void selectShape(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.shape_good:
                if (checked)
                    ShapeValue = "good";
                break;
            case R.id.shape_medium:
                if (checked)
                    ShapeValue = "medium";
                break;
            case R.id.shape_average:
                if (checked)
                    ShapeValue = "average";
                break;
        }
    }

    public void selectTouches(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.touches_dent:
                if (checked)
                    TouchesValue = "dent";
                    break;
            case R.id.touches_medium:
                if (checked)
                    TouchesValue = "medium";
                    break;
            case R.id.touches_small:
                if (checked)
                    TouchesValue = "small";
                    break;
            case R.id.touches_na:
                if (checked)
                    TouchesValue = "NA";
                    break;
        }
    }

    public void selectSpotmarks(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.spotmarks_yes:
                if (checked)
                    SpotMarksValue = "yes";
                    break;
            case R.id.spotmarks_no:
                if (checked)
                    SpotMarksValue = "no";
                    break;
            case R.id.spotmarks_na:
                if (checked)
                    SpotMarksValue = "NA";
                    break;
        }
    }

    public void selectRusting(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rusting_high:
                if (checked)
                    RustingValue = "high";
                    break;
            case R.id.rusting_medium:
                if (checked)
                    RustingValue = "medium";
                    break;
            case R.id.rusting_low:
                if (checked)
                    RustingValue = "low";
                    break;
            case R.id.rusting_na:
                if (checked)
                    RustingValue = "NA";
                    break;
        }
    }
    public void saveFeatures(View view) {
        resizeImage(pictureImagePath, 512, 512);
        filepath = mStorage.child("Sample Photos").child("Fuji").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg")
                        .setCustomMetadata("Color", ColorValue)
                        .setCustomMetadata("Shape", ShapeValue)
                        .setCustomMetadata("Touches", TouchesValue)
                        .setCustomMetadata("Spot Marks", SpotMarksValue)
                        .setCustomMetadata("Rusting", RustingValue)
                        .build();
                filepath.updateMetadata(metadata);
                //Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                //writenewchild(User, "Apple", "Fuji", imageUri.getLastPathSegment(), imageDownloadUrl.toString());

                Log.e(DEBUG, "uploading:success; image name: " + imageUri.getLastPathSegment());
                File fdelete = new File(pictureImagePath);
                if (fdelete.exists()) {
                    fdelete.delete();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(DEBUG, "uploading:failed; image name: " + imageUri.getLastPathSegment());
                File fdelete = new File(pictureImagePath);
                if (fdelete.exists()) {
                    fdelete.delete();
                }
            }
        });

        Intent _result = new Intent();
        setResult(RESULT_OK, _result);
        finish();

    }

    private void writenewchild(String uid, String productType, String productVariety, String imageName, String imageDownloadUrl) {

        SampleImageInfoUpload child = new SampleImageInfoUpload(productType, productVariety, imageName, imageDownloadUrl);

        String key = mDatabase.child(uid).push().getKey();

        mDatabase.child(uid).child(key).setValue(child);
    }

    @Override
    public void onBackPressed(){
        File fdelete = new File(pictureImagePath);
        if (fdelete.exists()) {
            fdelete.delete();
        }
        Toast.makeText(fujiFeatureSelectionActivity.this, "Fuji Sample discarded",
                Toast.LENGTH_SHORT).show();
        Intent _result = new Intent();
        setResult(RESULT_CANCELED, _result);
        finish();
    }

}
