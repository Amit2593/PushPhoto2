package com.example.lattoo.pushphoto;

import android.net.Uri;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Kashyap on 31/10/17.
 */
@IgnoreExtraProperties
public class LayerImageInfoUpload {

    public String ProductType;
    public String ProductVariety;
    public String ProductBrand;
    public String BrandPurchaseDate;
    public String LayerID;
    public String ImageName;
    public String ImageDownloadUrl;

    public LayerImageInfoUpload() {
        //Default constructor required for calls to DataSnapshot.getValue(this.class)
    }

    public LayerImageInfoUpload(String productType, String productVariety, String productBrand, String brandPurchaseDate, String layerID, String imageName, String imageDownloadUrl) {
        this.ProductType = productType;
        this.ProductVariety = productVariety;
        this.ProductBrand = productBrand;
        this.BrandPurchaseDate = brandPurchaseDate;
        this.LayerID = layerID;
        this.ImageName = imageName;
        this.ImageDownloadUrl = imageDownloadUrl;
    }

}
