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
    public void createPatient(Context ctx, String patientId, String name, String surname, String sex, int age, String hospital) { //<<<<

        PatientEvent event = new PatientEvent();
        event.put("created@", ctx.getStub().getTxTimestamp().toString()); //<<<<
        event.put("Patient_Id", patientId);
        event.put("Patient_name", name);
        event.put("Patient_surname", surname);
        event.put("Patient_sex", sex);
        event.put("Patient_age", age);
        event.put("Hospital", hospital);
        event.put("ID_Transaction", ctx.getStub().getTxId());
        event.put("CalledFnc", ctx.getStub().getFunction());

        boolean exists = patientExists(ctx,patientId);
        if (exists) {
            ctx.getStub().setEvent("Patient "+patientId+" NOT created", event.toJSONString().getBytes(UTF_8));
            throw new RuntimeException("The "+patientId+" already exists");
        }
        Patient asset = new Patient();
        asset.setName(name); // <<<
        asset.setSurname(surname); // <<<
        asset.setSex(sex); // <<<
        asset.setAge(age); // <<<
        asset.setH(hospital); // <<<
        ctx.getStub().putState(patientId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("Patient "+patientId+" created", event.toJSONString().getBytes(UTF_8)); //<<<<
    }

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
    public void updatePatient(Context ctx, String patientId, String name, String surname, String sex, int age, String hospital) {

        PatientEvent event = new PatientEvent();
        event.put("updated@", ctx.getStub().getTxTimestamp().toString()); //<<<<

        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        Patient asset = new Patient();
        asset.setName(name); // <<<
        asset.setSurname(surname); // <<<
        asset.setSex(sex); // <<<
        asset.setAge(age); // <<<
        asset.setH(hospital); // <<<
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








    /** DOCTOR CONTRACT*/
    
    private class DoctorEvent extends JSONObject {
        public DoctorEvent(){
            this.put("eventSource", "DoctorManagerContract");
        }
        public String toJSONString(){
            return this.toString();
        }
    }

    @Transaction() 
    public boolean doctorExists(Context ctx, String doctorId) {
        byte[] buffer = ctx.getStub().getState(doctorId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createDoctor(Context ctx, String doctorId, String name, String surname, String hospital) { //<<<<

        DoctorEvent event = new DoctorEvent();
        event.put("created@", ctx.getStub().getTxTimestamp().toString()); //<<<<
        event.put("Dr_Id", doctorId);
        event.put("Dr_name", name);
        event.put("Dr_surname", surname);
        event.put("Hospital", hospital);
        event.put("ID_Transaction", ctx.getStub().getTxId());
        event.put("CalledFnc", ctx.getStub().getFunction());

        boolean exists = doctorExists(ctx,doctorId);
        if (exists) {
            ctx.getStub().setEvent("Doctor "+doctorId+" NOT created", event.toJSONString().getBytes(UTF_8));
            throw new RuntimeException("The "+doctorId+" already exists");
        }
        Doctor asset = new Doctor();
        asset.setName(name); // <<<
        asset.setSurname(surname); // <<<
        asset.setH(hospital); // <<<
        ctx.getStub().putState(doctorId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("Doctor "+doctorId+" created", event.toJSONString().getBytes(UTF_8)); //<<<<
    }

    /*@Transaction()
    public Doctor readDoctor(Context ctx, String doctorId) {

        DoctorEvent event = new DoctorEvent();
        event.put("read@", ctx.getStub().getTxTimestamp().toString()); //<<<<
        event.put("Id", doctorId); //<<<<
        event.put("hash", ctx.getStub().hashCode()); //<<<<

        boolean exists = doctorExists(ctx, doctorId);
        if (!exists) {
            throw new RuntimeException("The asset "+doctorId+" does not exist");
        }
        
        Doctor newAsset = Doctor.fromJSONString(new String(ctx.getStub().getState(doctorId),UTF_8));
        return newAsset;
    }*/

    @Transaction()
    public void updateDoctor(Context ctx, String doctorId, String name, String surname, String hospital) {

        DoctorEvent event = new DoctorEvent();
        event.put("updated@", ctx.getStub().getTxTimestamp().toString()); //<<<<

        boolean exists = doctorExists(ctx, doctorId);
        if (!exists) {
            throw new RuntimeException("The asset "+doctorId+" does not exist");
        }
        Doctor asset = new Doctor();
        asset.setName(name); // <<<
        asset.setSurname(surname); // <<<
        asset.setH(hospital); // <<<
        ctx.getStub().putState(doctorId, asset.toJSONString().getBytes(UTF_8));
        }

    /*@Transaction()
    public String getuser(Context ctx){
        String usertype = ctx.getClientIdentity().getAttributeValue("usertype");
        return String;
    }*/

}

