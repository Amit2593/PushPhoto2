package com.example.lattoo.pushphoto;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.content.ClipData;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.os.Build;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.squareup.picasso.Picasso;
import com.google.firebase.storage.StorageMetadata;

public class reddeliciousFeatureSelectionActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE_REDDELICIOUS = 1;
    private static final String DEBUG = "reddeliciousFeatSel";
    private StorageReference mStorage;
    private StorageReference filepath;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;
    private ImageView mImageView;
    private Uri imageUri;
    private String User;
    private FirebaseAuth mAuth;
    private String pictureImagePath = "";
    private String ColorValue = "";
    private String PressureValue = "";
    private String TouchesValue = "";
    private String TopDamageValue = "";
    private String BottomTouchesValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddelicious_feature_selection);
        openBackCamera();
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
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
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_REDDELICIOUS);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE_REDDELICIOUS && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
            mImageView.setImageBitmap(bitmap);


        }

        else {
            Toast.makeText(reddeliciousFeatureSelectionActivity.this, "RedDelicious Sample discarded",
                    Toast.LENGTH_SHORT).show();
            Intent _result = new Intent();
            setResult(RESULT_CANCELED, _result);
            finish();
        }

    }

    public void selectColor(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.color_premium:
                if (checked)
                    ColorValue = "premium";
                break;
            case R.id.color_exfancy:
                if (checked)
                    ColorValue = "extra fancy";
                break;
            case R.id.color_fancy:
                if (checked)
                    ColorValue = "fancy";
                break;
            case R.id.color_half:
                if (checked)
                    ColorValue = "half";
                break;
            case R.id.color_brown:
                if (checked)
                    ColorValue = "brown";
                break;

        }
    }

    public void selectPressure(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.pressure_good:
                if (checked)
                    PressureValue = "good";
                break;
            case R.id.pressure_medium:
                if (checked)
                    PressureValue = "medium";
                break;
            case R.id.pressure_average:
                if (checked)
                    PressureValue = "average";
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

    public void selectTopDamage(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.topdamage_high:
                if (checked)
                    TopDamageValue = "high";
                break;
            case R.id.topdamage_medium:
                if (checked)
                    TopDamageValue = "medium";
                break;
            case R.id.topdamage_low:
                if (checked)
                    TopDamageValue = "low";
                break;
            case R.id.topdamage_na:
                if (checked)
                    TopDamageValue = "NA";
                break;
        }
    }

    public void selectBottomTouches(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.bottomtouches_large:
                if (checked)
                    BottomTouchesValue = "large";
                break;
            case R.id.bottomtouches_medium:
                if (checked)
                    BottomTouchesValue = "medium";
                break;
            case R.id.bottomtouches_small:
                if (checked)
                    BottomTouchesValue = "small";
                break;
            case R.id.bottomtouches_na:
                if (checked)
                    BottomTouchesValue = "NA";
                break;
        }
    }
    public void saveFeatures(View view) {
        resizeImage(pictureImagePath, 512, 512);

        //storing the data on cloud
        filepath = mStorage.child("Sample Photos").child("RedDelicious").child(imageUri.getLastPathSegment());
        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg")
                        .setCustomMetadata("Color", ColorValue)
                        .setCustomMetadata("Pressure", PressureValue)
                        .setCustomMetadata("Touches", TouchesValue)
                        .setCustomMetadata("TopDamage", TopDamageValue)
                        .setCustomMetadata("BottomTouches", BottomTouchesValue)
                        .build();
                Log.d(DEBUG, "uploading:success");
                filepath.updateMetadata(metadata);
                Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                writenewchild(User, "Apple", "RedDelicious", imageUri.getLastPathSegment(), imageDownloadUrl.toString());

                File fdelete = new File(pictureImagePath);
                if (fdelete.exists()) {
                    fdelete.delete();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(DEBUG, "uploading:failed");
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
        Toast.makeText(reddeliciousFeatureSelectionActivity.this, "RedDelicious Sample discarded",
                Toast.LENGTH_SHORT).show();
        Intent _result = new Intent();
        setResult(RESULT_CANCELED, _result);
        finish();
    }

}
