/*
 * SPDX-License-Identifier: Apache-2.0
 */
package org.example;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.json.JSONObject;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import static java.nio.charset.StandardCharsets.UTF_8;

@Contract(name = "PatientContract",
    info = @Info(title = "Patient contract", description = "My Smart Contract", version = "0.0.1",
    license = @License(name = "Apache-2.0", url = ""), contact =  @Contact(email = "scardamaglia.mattia@gmail.com", name = "cartella2", url = "http://cartella2.me")))

@Default //The @Default() annotation indicates that this contract class is the default contract class. Being able to mark a contract class as the default contract class is useful in some smart contracts which have multiple contract classes.
public class PatientContract implements ContractInterface {

    private class PatientEvent extends JSONObject {
        public PatientEvent(){
            this.put("eventSource", "PatientManagerContract");
        }
        public String toJSONString(){
            return this.toString();
        }
    }

    public  PatientContract() {

    }

    @Transaction() //The Java annotation @Transaction is used to mark this method as a transaction definition; 
    public boolean patientExists(Context ctx, String patientId) {
        byte[] buffer = ctx.getStub().getState(patientId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createPatient(Context ctx, String patientId, String patientname, String value) { //<<<<

        PatientEvent event = new PatientEvent();
        event.put("created@", ctx.getStub().getTxTimestamp().toString()); //<<<<
        event.put("Patient_Id", patientId);
        event.put("Patient_name", patientname);
        event.put("Value", value);
        event.put("ID_Transaction", ctx.getStub().getTxId());
        event.put("CalledFnc", ctx.getStub().getFunction());

        boolean exists = patientExists(ctx,patientId);
        if (exists) {
            ctx.getStub().setEvent("Patient "+patientId+" NOT created", event.toJSONString().getBytes(UTF_8));
            throw new RuntimeException("The "+patientId+" already exists");
        }
        Patient asset = new Patient();
        asset.setValue(value);
        ctx.getStub().putState(patientId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("Patient "+patientId+" created", event.toJSONString().getBytes(UTF_8)); //<<<<
    }

    /*

    @Transaction() // <<<<
    public boolean ehrExists(Context ctx, String ehrId) {
        byte[] buffer = ctx.getStub().getState(ehrId);
        return (buffer != null && buffer.length > 0);
    }
    
    @Transaction() // <<<<
    public void createEHR(Context ctx, String ehrId){
        boolean ehrexists = ehrExists(ctx, ehrId);
        if(ehrexists){
            throw new RuntimeException("The Electronic Health Record " + ehrId + " already exists");
        }
        EHR asset = new EHR(); 
    }
    */

    @Transaction()
    public Patient readPatient(Context ctx, String patientId) {

        PatientEvent event = new PatientEvent();
        event.put("read@", ctx.getStub().getTxTimestamp().toString()); //<<<<
        event.put("Id", patientId); //<<<<
        event.put("hash", ctx.getStub().hashCode()); //<<<<

        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }

        Patient newAsset = Patient.fromJSONString(new String(ctx.getStub().getState(patientId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public void updatePatient(Context ctx, String patientId, String newValue) {

        PatientEvent event = new PatientEvent();
        event.put("updated@", ctx.getStub().getTxTimestamp().toString()); //<<<<

        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        Patient asset = new Patient();
        asset.setValue(newValue);
        ctx.getStub().putState(patientId, asset.toJSONString().getBytes(UTF_8));
        }

    
    @Transaction()
    public void deletePatient(Context ctx, String patientId) {
        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        ctx.getStub().delState(patientId);
    }
    

}

/* 
sc > ...> create new project >
sc > ... > package open project >
sc > ... > fabric env. > my channel > +deploy sc >


cercare file connection profile file json
cercare wallet utente (sempre un file con vhiave privata utente)
studiare protocollo dicom 
migliorare sc solo per i medici 
paziente può leggere dati propri
se un utente è dottore può leggere i dati dei pazienti,
vedere l'utente chiamante con ctx.getStub e verificare se un utente è paziente o dottore

 */
