package org.example;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

@DataType()
public class Doctor extends User {
    
    @Property()
    private String hospital;

    public Doctor(){
    }

    public void setHospital(String value) {
        this.hospital = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static Doctor fromJSONString(String json) {
        String hospital = new JSONObject(json).getString("hospital");

        Doctor asset = new Doctor();
        asset.setHospital(hospital);
        return asset;
    }
}