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

import java.util.LinkedList;

@Contract(name = "PatientContract",info = @Info(title = "Patient contract", description = "My Smart Contract", version = "0.0.1", license = @License(name = "Apache-2.0", url = ""), contact =  @Contact(email = "scardamaglia.mattia@gmail.com", name = "cartella2", url = "http://cartella2.me")))

@Default
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

    @Transaction()
    public boolean patientExists(Context ctx, String patientId) {
        byte[] buffer = ctx.getStub().getState(patientId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createPatient(Context ctx, String patientId, String name, String surname, String gender, String age) { 

        PatientEvent event = new PatientEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype"); //coppia chiave valore
        if(!usertypevalue.equals("doctor")) {  // CHECK
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges"); // EFFECT
        }
        event.put("created@", ctx.getStub().getTxTimestamp().toString()); // INTERACTIONS
        event.put("Patient_Id", patientId);
        event.put("Patient_name", name);
        event.put("Patient_surname", surname);
        event.put("Patient_gender", gender);
        event.put("Patient_age", age);
        event.put("ID_Transaction", ctx.getStub().getTxId());
        event.put("CalledFnc", ctx.getStub().getFunction());

        boolean exists = patientExists(ctx, patientId);
        if (exists) {
            ctx.getStub().setEvent("Patient "+patientId+" NOT created", event.toJSONString().getBytes(UTF_8));
            throw new RuntimeException("The "+patientId+" already exists");
        }
        Patient asset = new Patient();
        asset.setName(name);
        asset.setSurname(surname);
        asset.setGender(gender);
        asset.setAge(age);
        ctx.getStub().putState(patientId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("Patient created", event.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public Patient readPatient(Context ctx, String patientId) {

        PatientEvent event = new PatientEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor") || !usertypevalue.equals("patient")) { // to fix : patients can ONLY read his info, right now all patients can read everyones info
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("read@", ctx.getStub().getTxTimestamp().toString());
        event.put("Id", patientId);
        event.put("hash", ctx.getStub().hashCode());
        ctx.getStub().setEvent("Reading info of "+patientId, event.toJSONString().getBytes(UTF_8));

        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        Patient newAsset = Patient.fromJSONString(new String(ctx.getStub().getState(patientId),UTF_8)); 
        return newAsset;
    }

    @Transaction()
    public Patient updatePatient(Context ctx, String patientId, String name, String surname, String gender, String age) {

        PatientEvent event = new PatientEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("updated@", ctx.getStub().getTxTimestamp().toString());

        boolean exists = patientExists(ctx, patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        Patient newAsset = Patient.fromJSONString(new String(ctx.getStub().getState(patientId),UTF_8));
        return newAsset;
        }

    @Transaction()
    public void deletePatient(Context ctx, String patientId){
        
        boolean exists = patientExists(ctx,patientId);
        if (!exists) {
            throw new RuntimeException("The asset "+patientId+" does not exist");
        }
        ctx.getStub().delState(patientId);
    }

    /** < DOCTOR CONTRACT > */
    
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
    public void createDoctor(Context ctx, String doctorId, String name, String surname, String hospital) {
        
        DoctorEvent event = new DoctorEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("admin")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("created@", ctx.getStub().getTxTimestamp().toString());
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
        asset.setName(name);
        asset.setSurname(surname);
        asset.setHospital(hospital);
        ctx.getStub().putState(doctorId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("Doctor "+doctorId+" created", event.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public Doctor readDoctor(Context ctx, String doctorId) {

        DoctorEvent event = new DoctorEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("read@", ctx.getStub().getTxTimestamp().toString());
        event.put("Id", doctorId);
        event.put("hash", ctx.getStub().hashCode());
        ctx.getStub().setEvent("Reading info of "+doctorId, event.toJSONString().getBytes(UTF_8));

        boolean exists = doctorExists(ctx, doctorId);
        if (!exists) {
            throw new RuntimeException("The asset "+doctorId+" does not exist");
        }
        
        Doctor newAsset = Doctor.fromJSONString(new String(ctx.getStub().getState(doctorId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public Doctor updateDoctor(Context ctx, String doctorId, String name, String surname, String hospital) {

        DoctorEvent event = new DoctorEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("admin")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("updated@", ctx.getStub().getTxTimestamp().toString());

        boolean exists = doctorExists(ctx, doctorId);
        if (!exists) {
            throw new RuntimeException("The asset "+doctorId+" does not exist");
        }
        Doctor newAsset = Doctor.fromJSONString(new String(ctx.getStub().getState(doctorId),UTF_8));
        return newAsset;
        }

    /** < DICOM CONTRACT > */
    
    private class DICOMEvent extends JSONObject {
        
        public DICOMEvent(){
            this.put("eventSource", "DICOMManagerContract");
        }
        public String toJSONString(){
            return this.toString();
        }
    }

    @Transaction() 
    public boolean dicomExists(Context ctx, String dicomId) {
        byte[] buffer = ctx.getStub().getState(dicomId);
        return (buffer != null && buffer.length > 0);
    }

    @Transaction()
    public void createDICOM(Context ctx, String dicomId, String Filename, String FileDateTime, String PatientID, String PatientName, String PatientAge, String PatientGender, String PatientWeight, String HeartRate, String Modality, String StudyDescription, String AnatomyPlane, String ExtraNotes, String HospitalUID) {

        DICOMEvent event = new DICOMEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("created@", ctx.getStub().getTxTimestamp().toString());
        event.put("ID_Transaction", ctx.getStub().getTxId());
        event.put("CalledFnc", ctx.getStub().getFunction());
        
        LinkedList<String> DICOMList = new LinkedList<String>(); // to fix, it doesnt save the dicomlist, FIX ALL LINKEDLISTS in the contract
        DICOMList.add("dicomId");
        for (String element: DICOMList)
            System.out.println(element);
        event.put(PatientID+" NEW DICOM LIST :", DICOMList);

        boolean exists = dicomExists(ctx, dicomId);
        if (exists) {
            ctx.getStub().setEvent("DICOM "+dicomId+" NOT created", event.toJSONString().getBytes(UTF_8));
            throw new RuntimeException("The "+dicomId+" already exists");
        }
        DICOM asset = new DICOM();
        asset.setFilename(Filename);
        asset.setFileDateTime(FileDateTime);
        asset.setPatientID(PatientID);
        asset.setPatientName(PatientName);
        asset.setPatientAge(PatientAge);
        asset.setPatientGender(PatientGender); 
        asset.setPatientWeight(PatientWeight);
        asset.setHeartRate(HeartRate);
        asset.setModality(Modality);
        asset.setStudyDescription(StudyDescription);
        asset.setAnatomyPlane(AnatomyPlane);
        asset.setExtraNotes(ExtraNotes);
        asset.setHospitalUID(HospitalUID);
        ctx.getStub().putState(dicomId, asset.toJSONString().getBytes(UTF_8));
        ctx.getStub().setEvent("DICOM "+dicomId+" created", event.toJSONString().getBytes(UTF_8));
    }

    @Transaction()
    public DICOM readDICOM(Context ctx, String dicomId, String PatientID) {

        DICOMEvent event = new DICOMEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor") || !usertypevalue.equals("patient")) { // to fix : patients can ONLY read his DICOMs, right now all patients can read everyones DICOMs
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("read@", ctx.getStub().getTxTimestamp().toString());
        event.put("DICOMId", dicomId);
        event.put("PatientId", PatientID);
        event.put("hash", ctx.getStub().hashCode());

        LinkedList<String> DICOMList = new LinkedList<String>();
        for (String element: DICOMList)
            System.out.println(element);
        event.put(PatientID+" DICOM LIST :", DICOMList);

        boolean exists = dicomExists(ctx, dicomId);
        if (!exists) {
            throw new RuntimeException("The asset "+dicomId+" does not exist");
        }
        
        DICOM newAsset = DICOM.fromJSONString(new String(ctx.getStub().getState(dicomId),UTF_8));
        return newAsset;
    }

    @Transaction()
    public DICOM updateDICOM(Context ctx, String dicomId, String Filename, String FileDateTime, String PatientID, String PatientName, String PatientAge, String PatientGender, String PatientWeight, String HeartRate, String Modality, String StudyDescripion, String AnatomyPlane, String ExtraNotes, String HospitalUID) {

        DICOMEvent event = new DICOMEvent();
        String usertypevalue = ctx.getClientIdentity().getAttributeValue("usertype");
        if(!usertypevalue.equals("doctor")) {
            event.put("@time", ctx.getStub().getTxTimestamp().toString());
            throw new RuntimeException("The usertype "+usertypevalue+" does not have the privileges");
        }
        event.put("updated@", ctx.getStub().getTxTimestamp().toString());

        LinkedList<String> DICOMList = new LinkedList<String>();
        DICOMList.addLast("dicomId");
        for (String element: DICOMList)
            System.out.println(element);
        event.put(PatientID+" UPDATED DICOM LIST :", DICOMList);

        boolean exists = dicomExists(ctx, dicomId);
        if (!exists) {
            throw new RuntimeException("The asset "+dicomId+" does not exist");
        }
        DICOM newAsset = DICOM.fromJSONString(new String(ctx.getStub().getState(dicomId),UTF_8));
        return newAsset;
        }  

}
