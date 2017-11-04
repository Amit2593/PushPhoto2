package com.example.lattoo.pushphoto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddBoxActivity extends AppCompatActivity {

    private String LotType = "";
    private String BoxName = "";
    private String PurchaseDate = "";
    private String Price = "NA";
    private String UploadPrice = "";
    private StorageReference mStorage;
    private StorageReference filepath;
    private DatabaseReference mDatabase;
    private String pictureImagePath = "";
    private Uri imageUri;
    private String User;
    private ProgressDialog mProgress;
    private static final int CAMERA_REQUEST_CODE_BOX = 1;
    private static final String DEBUG = "AddBoxPhoto";
    TextView PurchaseDateInput;
    TextView BoxNameInput;
    EditText PriceInput;
    ImageView BoxImage;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_box);

        Intent callingIntent = getIntent();
        LotType = callingIntent.getStringExtra("LotType");
        PurchaseDate = callingIntent.getStringExtra("PurchaseDate");
        BoxName = callingIntent.getStringExtra("BoxName");
        BoxName = BoxName.replaceAll("\\s+","").toUpperCase();
        UploadPrice = callingIntent.getStringExtra("Upload Price");
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        User = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);
        PurchaseDateInput = (TextView) findViewById(R.id.PurchaseDate);
        BoxNameInput = (TextView) findViewById(R.id.BoxName);
        PriceInput = (EditText) findViewById(R.id.enterprice);
        BoxImage = (ImageView) findViewById(R.id.boxImage);
        progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        PurchaseDateInput.setText("Purchase Date: " + PurchaseDate);
        BoxNameInput.setText("Box Name: " + BoxName);

        if (UploadPrice.equals("YES")) {
            filepath = mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(BoxName + "_" + PurchaseDate + ".jpg");
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(AddBoxActivity.this).load(uri.toString()).into(BoxImage);
                    progressBar.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        else {
            openBackCamera();
        }
    }

    private void openBackCamera() {
        String imageFileName = BoxName + "_" + PurchaseDate + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        imageUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE_BOX);
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

    public void skipEnteringPrice (View view) {
        if (UploadPrice.equals("YES")) {
            Intent intent = new Intent(AddBoxActivity.this, AddLayersActivity.class);
            intent.putExtra("LotType", LotType);
            intent.putExtra("BoxName", BoxName);
            intent.putExtra("PurchaseDate", PurchaseDate);
            startActivity(intent);
            finish();
        }
        else {
            filepath = mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(DEBUG, "uploading:success");
                    StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg")
                            .setCustomMetadata("Price", "NA")
                            .build();
                    filepath.updateMetadata(metadata);
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
            Intent intent = new Intent(AddBoxActivity.this, AddLayersActivity.class);
            intent.putExtra("LotType", LotType);
            intent.putExtra("BoxName", BoxName);
            intent.putExtra("PurchaseDate", PurchaseDate);
            startActivity(intent);
            finish();

        }

    }

    public void submitPrice (View view) {
        Price = PriceInput.getText().toString();
        if (UploadPrice.equals("YES")) {
            filepath = mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(BoxName + "_" + PurchaseDate + ".jpg");
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .setCustomMetadata("Price", Price)
                    .build();
            filepath.updateMetadata(metadata);
            Toast.makeText(AddBoxActivity.this, "Price info updated....", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddBoxActivity.this, AddLayersActivity.class);
            intent.putExtra("LotType", LotType);
            intent.putExtra("BoxName", BoxName);
            intent.putExtra("PurchaseDate", PurchaseDate);
            startActivity(intent);
            finish();
        }

        else {
            filepath = mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(imageUri.getLastPathSegment());
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(DEBUG, "uploading:success");
                    StorageMetadata metadata = new StorageMetadata.Builder().setContentType("image/jpeg")
                            .setCustomMetadata("Price", Price).build();
                    filepath.updateMetadata(metadata);
                    Uri imageDownloadUrl = taskSnapshot.getDownloadUrl();
                    writenewchild(User, "Apple", LotType, BoxName, PurchaseDate, imageUri.getLastPathSegment(), imageDownloadUrl.toString());
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
            Toast.makeText(AddBoxActivity.this, "Price info updated....", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddBoxActivity.this, AddLayersActivity.class);
            intent.putExtra("LotType", LotType);
            intent.putExtra("BoxName", BoxName);
            intent.putExtra("PurchaseDate", PurchaseDate);
            startActivity(intent);
            finish();
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE_BOX && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(pictureImagePath);
            BoxImage.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
            //resizeImage(pictureImagePath, 512, 512);

        }

        else {
            Toast.makeText(AddBoxActivity.this, "Box Photo discarded",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void writenewchild(String uid, String productType, String productVariety, String productBrand, String brandPurchaseDate, String imageName, String imageDownloadUrl) {

        BoxImageInfoUpload child = new BoxImageInfoUpload(productType, productVariety, productBrand, brandPurchaseDate, imageName, imageDownloadUrl);

        String key = mDatabase.child(uid).push().getKey();

        mDatabase.child(uid).child(key).setValue(child);
    }


}
