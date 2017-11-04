package com.example.lattoo.pushphoto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddLayerPhotosActivity extends AppCompatActivity {

    private String LotType = "";
    private String PurchaseDate = "";
    private String BoxName = "";
    private String Layer_ID ="";
    private StorageReference mStorage;
    private StorageReference filepath;
    private DatabaseReference mDatabase;
    private String pictureImagePath = "";
    private Uri imageUri;
    private String User;
    private ProgressDialog mProgress;
    private static final int CAMERA_REQUEST_CODE_LAYER = 1;
    private static final String DEBUG = "AddLayerPhotos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layer_photos);

        Intent callingIntent = getIntent();
        LotType = callingIntent.getStringExtra("LotType");
        BoxName = callingIntent.getStringExtra("BoxName");
        BoxName = BoxName.replaceAll("\\s+","").toUpperCase();
        PurchaseDate = callingIntent.getStringExtra("PurchaseDate");
        Layer_ID = callingIntent.getStringExtra("Layer_ID");
        openBackCamera();
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
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

    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = Layer_ID + "_" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        imageUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_LAYER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE_LAYER && resultCode == RESULT_OK) {
            resizeImage(pictureImagePath, 512, 512);
            //storing the data on cloud
            filepath = mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(Layer_ID).child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(DEBUG, "uploading:success");
                    Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                    writenewchild(User, "Apple", LotType, BoxName, PurchaseDate, Layer_ID, imageUri.getLastPathSegment(), imageDownloadUrl.toString());

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

        else {
            Toast.makeText(AddLayerPhotosActivity.this, LotType + " " + Layer_ID + " photo discarded",
                    Toast.LENGTH_SHORT).show();
            Intent _result = new Intent();
            setResult(RESULT_CANCELED, _result);
            finish();
        }

    }

    private void writenewchild(String uid, String productType, String productVariety, String productBrand, String brandPurchaseDate, String LayerID, String imageName, String imageDownloadUrl) {

        LayerImageInfoUpload child = new LayerImageInfoUpload(productType, productVariety, productBrand, brandPurchaseDate, LayerID, imageName, imageDownloadUrl);

        String key = mDatabase.child(uid).push().getKey();

        mDatabase.child(uid).child(key).setValue(child);
    }
}
