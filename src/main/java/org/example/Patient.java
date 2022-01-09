/*
 * SPDX-License-Identifier: Apache-2.0
 */

package org.example;

//import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
//import com.google.gson.JsonObject;
//import com.owlike.genson.Genson;
import org.json.JSONObject;

@DataType()
public class Patient extends User {

    //private final static Genson genson = new Genson();
    
    @Property()
    private String studyid;

    public Patient(){
    }

    public String getStudyId(){
        return studyid;
    }

    public void setStudyId(String value) {
        this.studyid = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
        //return genson.serialize(this).toString();
    }

    public static Patient fromJSONString(String json) {
        String studyid = new JSONObject(json).getString("studyid");

        Patient asset = new Patient();
        asset.setName(studyid);
        //Patient asset = genson.deserialize(json, Patient.class);
        return asset;
    }
}
