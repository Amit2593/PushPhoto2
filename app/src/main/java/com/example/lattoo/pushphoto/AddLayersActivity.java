package com.example.lattoo.pushphoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class AddLayersActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE_LAYER1 = 1;
    private static final int CAMERA_REQUEST_CODE_LAYER2 = 2;
    private static final int CAMERA_REQUEST_CODE_LAYER3 = 3;
    private static final int CAMERA_REQUEST_CODE_LAYER4 = 4;
    private static final int CAMERA_REQUEST_CODE_LAYER5 = 5;
    private static final int CAMERA_REQUEST_CODE_SAMPLE_LAYER1 = 6;
    private static final int CAMERA_REQUEST_CODE_SAMPLE_LAYER2 = 7;
    private static final int CAMERA_REQUEST_CODE_SAMPLE_LAYER3 = 8;
    private static final int CAMERA_REQUEST_CODE_SAMPLE_LAYER4 = 9;
    private static final int CAMERA_REQUEST_CODE_SAMPLE_LAYER5 = 10;
    private String LotType = "";
    private String BoxName = "";
    private String PurchaseDate = "";
    private String Layer_ID ="Layer_ID";
    TextView BoxNameInput;
    TextView PurchaseDateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layers);
        Intent callingIntent = getIntent();
        LotType = callingIntent.getStringExtra("LotType");
        BoxName = callingIntent.getStringExtra("BoxName");
        PurchaseDate = callingIntent.getStringExtra("PurchaseDate");
        FirebaseApp.initializeApp(this);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        BoxNameInput = (TextView) findViewById(R.id.BoxName);
        PurchaseDateInput = (TextView) findViewById(R.id.PurchaseDate);
        BoxNameInput.setText("Box Name: " + BoxName);
        PurchaseDateInput.setText("Purchased: " + PurchaseDate);
    }

    public void uploadlayer1photo (View view) {
        Intent intent = new Intent(AddLayersActivity.this, AddLayerPhotosActivity.class);
        intent.putExtra(Layer_ID, "Layer1");
        intent.putExtra("LotType", LotType);
        intent.putExtra("BoxName", BoxName);
        intent.putExtra("PurchaseDate", PurchaseDate);
        startActivityForResult(intent, CAMERA_REQUEST_CODE_LAYER1);
    }

    public void uploadlayer2photo (View view) {
        Intent intent = new Intent(AddLayersActivity.this, AddLayerPhotosActivity.class);
        intent.putExtra(Layer_ID, "Layer2");
        intent.putExtra("LotType", LotType);
        intent.putExtra("BoxName", BoxName);
        intent.putExtra("PurchaseDate", PurchaseDate);
        startActivityForResult(intent, CAMERA_REQUEST_CODE_LAYER2);
    }

    public void uploadlayer3photo (View view) {
        Intent intent = new Intent(AddLayersActivity.this, AddLayerPhotosActivity.class);
        intent.putExtra(Layer_ID, "Layer3");
        intent.putExtra("LotType", LotType);
        intent.putExtra("BoxName", BoxName);
        intent.putExtra("PurchaseDate", PurchaseDate);
        startActivityForResult(intent, CAMERA_REQUEST_CODE_LAYER3);
    }

    public void uploadlayer4photo (View view) {
        Intent intent = new Intent(AddLayersActivity.this, AddLayerPhotosActivity.class);
        intent.putExtra(Layer_ID, "Layer4");
        intent.putExtra("LotType", LotType);
        intent.putExtra("BoxName", BoxName);
        intent.putExtra("PurchaseDate", PurchaseDate);
        startActivityForResult(intent, CAMERA_REQUEST_CODE_LAYER4);
    }

    public void uploadlayer5photo (View view) {
        Intent intent = new Intent(AddLayersActivity.this, AddLayerPhotosActivity.class);
        intent.putExtra(Layer_ID, "Layer5");
        intent.putExtra("LotType", LotType);
        intent.putExtra("BoxName", BoxName);
        intent.putExtra("PurchaseDate", PurchaseDate);
        startActivityForResult(intent, CAMERA_REQUEST_CODE_LAYER5);
    }

    public void uploadlayer1samples (View view) {
        switch(LotType) {
            case "Fuji":
                Intent intent_fuji = new Intent(AddLayersActivity.this, fujiFeatureSelectionLotSampleActivity.class);
                intent_fuji.putExtra(Layer_ID, "Layer1");
                intent_fuji.putExtra("LotType", LotType);
                intent_fuji.putExtra("BoxName", BoxName);
                intent_fuji.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_fuji, CAMERA_REQUEST_CODE_SAMPLE_LAYER1);
                break;
            case "Gala":
                Intent intent_gala = new Intent(AddLayersActivity.this, galaFeatureSelectionLotSampleActivity.class);
                intent_gala.putExtra(Layer_ID, "Layer1");
                intent_gala.putExtra("LotType", LotType);
                intent_gala.putExtra("BoxName", BoxName);
                intent_gala.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_gala, CAMERA_REQUEST_CODE_SAMPLE_LAYER1);
                break;
            case "RedDelicious":
                Intent intent_reddelicious = new Intent(AddLayersActivity.this, reddeliciousFeatureSelectionLotSampleActivity.class);
                intent_reddelicious.putExtra(Layer_ID, "Layer1");
                intent_reddelicious.putExtra("LotType", LotType);
                intent_reddelicious.putExtra("BoxName", BoxName);
                intent_reddelicious.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_reddelicious, CAMERA_REQUEST_CODE_SAMPLE_LAYER1);
        }

    }

    public void uploadlayer2samples (View view) {
        switch(LotType) {
            case "Fuji":
                Intent intent_fuji = new Intent(AddLayersActivity.this, fujiFeatureSelectionLotSampleActivity.class);
                intent_fuji.putExtra(Layer_ID, "Layer2");
                intent_fuji.putExtra("LotType", LotType);
                intent_fuji.putExtra("BoxName", BoxName);
                intent_fuji.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_fuji, CAMERA_REQUEST_CODE_SAMPLE_LAYER2);
                break;
            case "Gala":
                Intent intent_gala = new Intent(AddLayersActivity.this, galaFeatureSelectionLotSampleActivity.class);
                intent_gala.putExtra(Layer_ID, "Layer2");
                intent_gala.putExtra("LotType", LotType);
                intent_gala.putExtra("BoxName", BoxName);
                intent_gala.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_gala, CAMERA_REQUEST_CODE_SAMPLE_LAYER2);
                break;
            case "RedDelicious":
                Intent intent_reddelicious = new Intent(AddLayersActivity.this, reddeliciousFeatureSelectionLotSampleActivity.class);
                intent_reddelicious.putExtra(Layer_ID, "Layer2");
                intent_reddelicious.putExtra("LotType", LotType);
                intent_reddelicious.putExtra("BoxName", BoxName);
                intent_reddelicious.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_reddelicious, CAMERA_REQUEST_CODE_SAMPLE_LAYER2);
        }

    }

    public void uploadlayer3samples (View view) {
        switch(LotType) {
            case "Fuji":
                Intent intent_fuji = new Intent(AddLayersActivity.this, fujiFeatureSelectionLotSampleActivity.class);
                intent_fuji.putExtra(Layer_ID, "Layer3");
                intent_fuji.putExtra("LotType", LotType);
                intent_fuji.putExtra("BoxName", BoxName);
                intent_fuji.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_fuji, CAMERA_REQUEST_CODE_SAMPLE_LAYER3);
                break;
            case "Gala":
                Intent intent_gala = new Intent(AddLayersActivity.this, galaFeatureSelectionLotSampleActivity.class);
                intent_gala.putExtra(Layer_ID, "Layer3");
                intent_gala.putExtra("LotType", LotType);
                intent_gala.putExtra("BoxName", BoxName);
                intent_gala.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_gala, CAMERA_REQUEST_CODE_SAMPLE_LAYER3);
                break;
            case "RedDelicious":
                Intent intent_reddelicious = new Intent(AddLayersActivity.this, reddeliciousFeatureSelectionLotSampleActivity.class);
                intent_reddelicious.putExtra(Layer_ID, "Layer3");
                intent_reddelicious.putExtra("LotType", LotType);
                intent_reddelicious.putExtra("BoxName", BoxName);
                intent_reddelicious.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_reddelicious, CAMERA_REQUEST_CODE_SAMPLE_LAYER3);
        }

    }

    public void uploadlayer4samples (View view) {
        switch(LotType) {
            case "Fuji":
                Intent intent_fuji = new Intent(AddLayersActivity.this, fujiFeatureSelectionLotSampleActivity.class);
                intent_fuji.putExtra(Layer_ID, "Layer4");
                intent_fuji.putExtra("LotType", LotType);
                intent_fuji.putExtra("BoxName", BoxName);
                intent_fuji.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_fuji, CAMERA_REQUEST_CODE_SAMPLE_LAYER4);
                break;
            case "Gala":
                Intent intent_gala = new Intent(AddLayersActivity.this, galaFeatureSelectionLotSampleActivity.class);
                intent_gala.putExtra(Layer_ID, "Layer4");
                intent_gala.putExtra("LotType", LotType);
                intent_gala.putExtra("BoxName", BoxName);
                intent_gala.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_gala, CAMERA_REQUEST_CODE_SAMPLE_LAYER4);
                break;
            case "RedDelicious":
                Intent intent_reddelicious = new Intent(AddLayersActivity.this, reddeliciousFeatureSelectionLotSampleActivity.class);
                intent_reddelicious.putExtra(Layer_ID, "Layer4");
                intent_reddelicious.putExtra("LotType", LotType);
                intent_reddelicious.putExtra("BoxName", BoxName);
                intent_reddelicious.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_reddelicious, CAMERA_REQUEST_CODE_SAMPLE_LAYER4);
        }

    }

    public void uploadlayer5samples (View view) {
        switch(LotType) {
            case "Fuji":
                Intent intent_fuji = new Intent(AddLayersActivity.this, fujiFeatureSelectionLotSampleActivity.class);
                intent_fuji.putExtra(Layer_ID, "Layer5");
                intent_fuji.putExtra("LotType", LotType);
                intent_fuji.putExtra("BoxName", BoxName);
                intent_fuji.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_fuji, CAMERA_REQUEST_CODE_SAMPLE_LAYER5);
                break;
            case "Gala":
                Intent intent_gala = new Intent(AddLayersActivity.this, galaFeatureSelectionLotSampleActivity.class);
                intent_gala.putExtra(Layer_ID, "Layer5");
                intent_gala.putExtra("LotType", LotType);
                intent_gala.putExtra("BoxName", BoxName);
                intent_gala.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_gala, CAMERA_REQUEST_CODE_SAMPLE_LAYER5);
                break;
            case "RedDelicious":
                Intent intent_reddelicious = new Intent(AddLayersActivity.this, reddeliciousFeatureSelectionLotSampleActivity.class);
                intent_reddelicious.putExtra(Layer_ID, "Layer5");
                intent_reddelicious.putExtra("LotType", LotType);
                intent_reddelicious.putExtra("BoxName", BoxName);
                intent_reddelicious.putExtra("PurchaseDate", PurchaseDate);
                startActivityForResult(intent_reddelicious, CAMERA_REQUEST_CODE_SAMPLE_LAYER5);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE_LAYER1:
                    Toast.makeText(AddLayersActivity.this, "Layer1 Photo Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_LAYER2:
                    Toast.makeText(AddLayersActivity.this, "Layer2 Photo Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_LAYER3:
                    Toast.makeText(AddLayersActivity.this, "Layer3 Photo Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_LAYER4:
                    Toast.makeText(AddLayersActivity.this, "Layer4 Photo Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_LAYER5:
                    Toast.makeText(AddLayersActivity.this, "Layer5 Photo Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_SAMPLE_LAYER1:
                    Toast.makeText(AddLayersActivity.this, "Layer1 Sample Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_SAMPLE_LAYER2:
                    Toast.makeText(AddLayersActivity.this, "Layer2 Sample Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_SAMPLE_LAYER3:
                    Toast.makeText(AddLayersActivity.this, "Layer3 Sample Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_SAMPLE_LAYER4:
                    Toast.makeText(AddLayersActivity.this, "Layer4 Sample Added....", Toast.LENGTH_SHORT).show();
                    break;
                case CAMERA_REQUEST_CODE_SAMPLE_LAYER5:
                    Toast.makeText(AddLayersActivity.this, "Layer5 Sample Added....", Toast.LENGTH_SHORT).show();
                    break;

            }
        }

    }

}


