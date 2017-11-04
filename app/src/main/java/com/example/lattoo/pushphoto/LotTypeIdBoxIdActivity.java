package com.example.lattoo.pushphoto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageMetadata;

public class LotTypeIdBoxIdActivity extends AppCompatActivity {

    private String LotType = "";
    private String BoxName = "";
    private String PurchaseDate = "";
    private String Price = "NA";
    private StorageReference mStorage;
    private static final int CAMERA_REQUEST_CODE_BOX = 1;
    TextView PurchaseDateInput;
    EditText BoxNameInput;
    private ProgressDialog mProgress;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_type_id_box_id);

        PurchaseDateInput = (TextView) findViewById(R.id.purchaseDate);
        BoxNameInput = (EditText) findViewById(R.id.enterBoxName);
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
    }

    public void setDate (View view)  {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        PurchaseDateInput.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void selectLotType(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.lottype_Fuji:
                if (checked)
                    LotType = "Fuji";
                break;
            case R.id.lottype_Gala:
                if (checked)
                    LotType = "Gala";
                break;
            case R.id.lottype_Reddelicious:
                if (checked)
                    LotType = "RedDelicious";
                break;
        }
    }

    public void submitLotTypeIdBoxId (View view) {
        PurchaseDate = PurchaseDateInput.getText().toString();
        BoxName = BoxNameInput.getText().toString();

        if (LotType.length() == 0) {
            Toast.makeText(LotTypeIdBoxIdActivity.this, "Please Select Lot Type....", Toast.LENGTH_SHORT).show();
            return;
        }

        if (BoxName.length() == 0) {
            Toast.makeText(LotTypeIdBoxIdActivity.this, "Please Enter valid Box Name....", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PurchaseDate.length() == 0) {
            Toast.makeText(LotTypeIdBoxIdActivity.this, "Please Enter the Purchase Date....", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgress.setMessage("Looking for Box Photo and Price.....");
        mProgress.show();

        PurchaseDate = PurchaseDateInput.getText().toString();
        BoxName = BoxNameInput.getText().toString();
        BoxName = BoxName.replaceAll("\\s+","").toUpperCase();
        mStorage = FirebaseStorage.getInstance().getReference();



        mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(BoxName + "_" + PurchaseDate + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mStorage.child("Lot Photos").child(LotType).child(BoxName).child(PurchaseDate).child(BoxName + "_" + PurchaseDate + ".jpg").getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        // Metadata now contains the metadata for said file
                        Price = storageMetadata.getCustomMetadata("Price");
                        mProgress.dismiss();
                        if (Price.equals("NA")) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            //Yes button clicked
                                            Intent intent_upload_price = new Intent(LotTypeIdBoxIdActivity.this, AddBoxActivity.class);
                                            intent_upload_price.putExtra("LotType", LotType);
                                            intent_upload_price.putExtra("BoxName", BoxName);
                                            intent_upload_price.putExtra("PurchaseDate", PurchaseDate);
                                            intent_upload_price.putExtra("Upload Price", "YES");
                                            startActivity(intent_upload_price);
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            Intent intent = new Intent(LotTypeIdBoxIdActivity.this, AddLayersActivity.class);
                                            intent.putExtra("LotType", LotType);
                                            intent.putExtra("BoxName", BoxName);
                                            intent.putExtra("PurchaseDate", PurchaseDate);
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(LotTypeIdBoxIdActivity.this);
                            builder.setMessage("Box Photo Available! Want to upload box price?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("Skip", dialogClickListener).show();
                        }
                        else {
                            Toast.makeText(LotTypeIdBoxIdActivity.this, "Box Photo and Price available....", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LotTypeIdBoxIdActivity.this, AddLayersActivity.class);
                            intent.putExtra("LotType", LotType);
                            intent.putExtra("BoxName", BoxName);
                            intent.putExtra("PurchaseDate", PurchaseDate);
                            startActivity(intent);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // File not found
                //POP UP TO SAY - ADD BOX PHOTO
                mProgress.dismiss();
                new AlertDialog.Builder(LotTypeIdBoxIdActivity.this)
                        .setMessage("Please upload box photo!")
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                return;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(LotTypeIdBoxIdActivity.this, AddBoxActivity.class);
                                intent.putExtra("LotType", LotType);
                                intent.putExtra("BoxName", BoxName);
                                intent.putExtra("PurchaseDate", PurchaseDate);
                                intent.putExtra("Upload Price", "NO");
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });

    }

}
