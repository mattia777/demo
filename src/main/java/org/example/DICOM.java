/*
 * DICOM is a healthcare standard responsible for governing nearly all aspects of medical imaging such 
 * as image transmission, image interpretation, print management, procedure management and off-line 
 * storage, and is used in nearly all healthcare-related imaging “modalities” such as magnetic resonance, 
 * nuclear medicine, computed tomography and ultrasound. Nearly all clinical imaging workflows around 
 * the world are based on the DICOM standard. 
 * */

package org.example;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class DICOM{
    
    @Property()
    private String Filename; // [1x89 char]
    @Property()
    private String FileDate; // '18-Dec-2000 11:06:43'
    @Property()
    private String FileSize; // 525436
    @Property()
    private String Format; // 'DICOM'
    @Property()
    private String FormatVersion; // 3
    @Property()
    private String Width; //512
    @Property()
    private String Height; //512
    @Property()
    private String BitDepth; //16
    @Property()
    private String ColorType; // 'grayscale'

    public DICOM(){
    }

    public void setFilename(String value) {
        this.Filename = value;
    }
    public void setFileDate(String value) {
        this.FileDate = value;
    }
    public void setFileSize(String value) {
        this.FileSize = value;
    }
    public void setFormat(String value) {
        this.Format = value;
    }
    public void setFormatVersion(String value) {
        this.FormatVersion = value;
    }
    public void setWidth(String value) {
        this.Width = value;
    }
    public void setHeight(String value) {
        this.Height = value;
    }
    public void setBitDepth(String value) {
        this.BitDepth = value;
    }
    public void setColorType(String value) {
        this.ColorType = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static DICOM fromJSONString(String json) {
        String Filename = new JSONObject(json).getString("Filename");
        String FileDate = new JSONObject(json).getString("FileDate");
        String FileSize = new JSONObject(json).getString("FileSize");
        String Format = new JSONObject(json).getString("Format");
        String FormatVersion = new JSONObject(json).getString("FormatVersion");
        String Width = new JSONObject(json).getString("Width");
        String Height = new JSONObject(json).getString("Height");
        String BitDepth = new JSONObject(json).getString("BitDepth");
        String ColorType = new JSONObject(json).getString("ColorType");

        DICOM asset = new DICOM();
        asset.setFilename(Filename);
        asset.setFileDate(FileDate);
        asset.setFileSize(FileSize);
        asset.setFormat(Format);
        asset.setFormatVersion(FormatVersion);
        asset.setWidth(Width);
        asset.setHeight(Height);
        asset.setBitDepth(BitDepth);
        asset.setColorType(ColorType);

        return asset;
    }
    
}