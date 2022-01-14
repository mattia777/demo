package org.example;


import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import org.json.JSONObject;

@DataType()
public class Admin extends User {

    
    @Property()
    private String adminid;

    public Admin(){
    }

    public String getAdminId(){
        return adminid;
    }

    public void setAdminId(String value) {
        this.adminid = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static Admin fromJSONString(String json) {
        String adminid = new JSONObject(json).getString("adminid");

        Admin asset = new Admin();
        asset.setAdminId(adminid);
        return asset;
    }
}
