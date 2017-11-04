package com.example.lattoo.pushphoto;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Button;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;

public class SampleTypeActivity extends AppCompatActivity {

    private ImageView mImageView;
    private static final String TAG = "AnonymousAuth";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private static final int CAMERA_REQUEST_CODE_FUJI = 1;
    private static final int CAMERA_REQUEST_CODE_REDDELICIOUS = 2;
    private static final int CAMERA_REQUEST_CODE_GALA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_type);
        mAuth = FirebaseAuth.getInstance();
        //FirebaseApp.initializeApp(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
    }

    public void takeFujiSamplePics(View view) {
        Intent featureselectionfuji = new Intent(this, fujiFeatureSelectionActivity.class);
        startActivityForResult(featureselectionfuji, CAMERA_REQUEST_CODE_FUJI);
    }

    public void takeRedDeliciousSamplePics(View view) {
        Intent featureselectionreddelicious = new Intent(this, reddeliciousFeatureSelectionActivity.class);
        startActivityForResult(featureselectionreddelicious, CAMERA_REQUEST_CODE_REDDELICIOUS);
    }

    public void takeGalaSamplePics(View view) {
        Intent featureselectiongala = new Intent(this, galaFeatureSelectionActivity.class);
        startActivityForResult(featureselectiongala, CAMERA_REQUEST_CODE_GALA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE_FUJI && resultCode == RESULT_OK) {
            Toast.makeText(SampleTypeActivity.this, "Fuji Sample Added....", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CAMERA_REQUEST_CODE_GALA && resultCode == RESULT_OK) {
            Toast.makeText(SampleTypeActivity.this, "Gala Sample Added....", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CAMERA_REQUEST_CODE_REDDELICIOUS && resultCode == RESULT_OK) {
            Toast.makeText(SampleTypeActivity.this, "RedDelicious Sample Added....", Toast.LENGTH_SHORT).show();
        }

    }
}
