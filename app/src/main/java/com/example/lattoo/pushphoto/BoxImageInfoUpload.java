package com.example.lattoo.pushphoto;

import android.net.Uri;
import com.google.firebase.database.IgnoreExtraProperties;


/**
 * Created by Kashyap on 31/10/17.
 */
@IgnoreExtraProperties
public class BoxImageInfoUpload {

    public String ProductType;
    public String ProductVariety;
    public String ProductBrand;
    public String BrandPurchaseDate;
    public String ImageName;
    public String ImageDownloadUrl;

    public BoxImageInfoUpload() {
        //Default constructor required for calls to DataSnapshot.getValue(this.class)
    }

    public BoxImageInfoUpload(String productType, String productVariety, String productBrand, String brandPurchaseDate, String imageName, String imageDownloadUrl) {
        ProductType = productType;
        ProductVariety = productVariety;
        ProductBrand = productBrand;
        BrandPurchaseDate = brandPurchaseDate;
        ImageName = imageName;
        ImageDownloadUrl = imageDownloadUrl;
    }

}
