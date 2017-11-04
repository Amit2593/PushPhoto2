package com.example.lattoo.pushphoto;

import android.net.Uri;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Kashyap on 30/10/17.
 */

@IgnoreExtraProperties
public class LotSampleImageInfoUpload {

    public String ProductType;
    public String ProductVariety;
    public String ProductBrand;
    public String BrandPurchaseDate;
    public String SampleLayerID;
    public String ImageName;
    public String ImageDownloadUrl;


    public LotSampleImageInfoUpload() {
        //Default constructor required for calls to DataSnapshot.getValue(this.class)
    }

    public LotSampleImageInfoUpload(String productType, String productVariety, String productBrand, String brandPurchaseDate, String sampleLayerID, String imageName, String imageDownloadUrl) {
        this.ProductType = productType;
        this.ProductVariety = productVariety;
        this.ProductBrand = productBrand;
        this.BrandPurchaseDate = brandPurchaseDate;
        this.SampleLayerID = sampleLayerID;
        this.ImageName = imageName;
        this.ImageDownloadUrl = imageDownloadUrl;
    }

}
