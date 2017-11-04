package com.example.lattoo.pushphoto;

import android.net.Uri;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kashyap on 31/10/17.
 */
@IgnoreExtraProperties
public class SampleImageInfoUpload {

    public String ProductType;
    public String ProductVariety;
    public String ImageName;
    public String ImageDownloadUrl;

    public SampleImageInfoUpload() {
        //Default constructor required for calls to DataSnapshot.getValue(this.class)
    }

    public SampleImageInfoUpload(String productType, String productVariety, String imageName, String imageDownloadUrl) {
        this.ProductType = productType;
        this.ProductVariety = productVariety;
        this.ImageName = imageName;
        this.ImageDownloadUrl = imageDownloadUrl;
    }

}
