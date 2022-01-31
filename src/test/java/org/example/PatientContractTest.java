/*
 * SPDX-License-Identifier: Apache License 2.0
 */
package org.example;
//import static java.nio.charset.StandardCharsets.UTF_8;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows; 
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import java.nio.charset.StandardCharsets;
//import org.hyperledger.fabric.contract.Context;
//import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
//import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
//import java.util.Arrays;
//import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
//import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
//import static org.junit.jupiter.api.Assertions.fail;

 public final class PatientContractTest {

    Wallet fabricWallet;
    Gateway gateway1, gateway2, gateway3;
    Gateway.Builder builder1, builder2, builder3; // Configure the gateway connection used to access the network.
    Network network1, network2, network3;
    Contract PatientManagerContract;
    String homedir = System.getProperty("C:\\Users\\scard");
    Path walletPath1 = Paths.get("C:\\Users\\scard\\fabric-vscode\\v2\\environments\\1 Org Local Fabric\\wallets\\Org1"); // Load an existing wallet holding identities used to access the network.
    Path connectionProfilePath = Paths.get("C:\\Users\\scard\\.fabric-vscode\\v2\\environments\\1 Org Local Fabric\\gateways\\Org1 Gateway.json"); // Path to a common connection profile describing the network.
    String admin = "admin";
    String doctor = "doctor";
    String patient = "patient";
    boolean isLocalhostURL = JavaSmartContractUtil.hasLocalhostURLs(connectionProfilePath);
    static String currTime;
    static Consumer<ContractEvent> eventListener = a -> System.out.println(a.getName() + new String(a.getPayload().get())); // legge gli eventi che sono emessi dalla blockchain e li stampa a schermo
    
    // junit permette di definire attraverso le annotazioni che alcuni metodi sono "speciali"(come BeforeAll e BeforeEach)
    @BeforeAll
    public static void beforeAll() {
        currTime = Instant.now().toString();
    }

    @BeforeEach //viene eseguito prima di ogni singola transazione, instaura una nuova connessione della blockchain exnovo (ogni nuova chiamata ogni nuovo test sia exnovo, così da non avere effetti collaterali con chiamate precedenti)
    public void before() throws Exception {
        assertThatCode(() -> {
            JavaSmartContractUtil.setDiscoverAsLocalHost(isLocalhostURL);
            fabricWallet = Wallet.createFileSystemWallet(walletPath1);

            //Set<String> set = fabricWallet.getAllLabels();

            builder1 = Gateway.createBuilder(); //connessione con la blockchain attraverso l'oggetto Gateway, ha bisogno del connection profile e del wallet, ci sarà una connessione diversa per ogni utente, che verrà inizializzata ogni qual volta che si effettua un nuovo test su una funzione
            builder1.identity(fabricWallet, patient).networkConfig(connectionProfilePath).discovery(true);
            gateway1 = builder1.connect();
            network1 = gateway1.getNetwork("mychannel");
            PatientManagerContract = network1.getContract("patientProcess", "patientContract");
            
            builder2 = Gateway.createBuilder();
            builder2.identity(fabricWallet, doctor).networkConfig(connectionProfilePath).discovery(true);
            gateway2 = builder2.connect();
            network2 = gateway2.getNetwork("mychannel");
            PatientManagerContract = network2.getContract("doctorProcess", "doctorContract");
            
            builder3 = Gateway.createBuilder();
            builder3.identity(fabricWallet, admin).networkConfig(connectionProfilePath).discovery(true);
            gateway3 = builder3.connect();
            network3 = gateway3.getNetwork("mychannel");
            PatientManagerContract = network3.getContract("adminProcess", "adminContract");
            
            
            PatientManagerContract.addContractListener(eventListener);

        }).doesNotThrowAnyException();
    }

    @AfterEach //va a chiudere le connessioni
    public void after() {
        gateway1.close();
        gateway2.close();
        gateway3.close();
        PatientManagerContract.removeContractListener(eventListener);
    }
/** PATIENT UNIT TEST */

@Nested
    class PatientCreates { //ogni funzione avrà una classe innessata solo per essa

        @Test
        public void newPatientCreate() throws ContractException, TimeoutException, InterruptedException {
            String patientId = "patientId" + currTime;
            String name = "name";
            String surname = "surname";
            String gender = "gender";
            String age = "age";
            String[] args = new String[] { patientId, name, surname, gender, age };
            byte[] response = PatientManagerContract.submitTransaction("createPatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("Patient " + patientId + " created"); // se ritorna true il test sarà positivo
        }
           
        @Test
        public void PatientalreadyExists() throws ContractException, TimeoutException, InterruptedException {
            String patientId = "patientId" + currTime;
            String[] args = new String[] { patientId };
            byte[] response = PatientManagerContract.submitTransaction("patientExists", args);
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("The Patient " + patientId + " already exists");
        }

    @Test
    public void PatientRead() throws ContractException, TimeoutException, InterruptedException {
        String patientId = "patientId" + currTime;
        String[] args = new String[] { patientId};
        byte[] response = PatientManagerContract.evaluateTransaction("readPatient", args);
        String responseString = new String(response);
        System.out.println(responseString);
        assertThat(responseString).contains("reading patient" + patientId + "files");
    }

    /*
    @Test
    public void PatientReadFail() throws ContractException, TimeoutException, InterruptedException {
        String patientId = "patientId" + currTime;
        String[] args = new String[] { processID, patientId };
        byte[] response = PatientManagerContract.evaluateTransaction("readPatient", args);
        String responseString = new String(response);
        assertThat(responseString).contains("Invalid user type: patient");
    }
    */

    @Nested
    class PatientUpdates {
        @Test
        public void UpdateExistingPatient() throws ContractException, TimeoutException, InterruptedException {
            String patientId = "patientId" + currTime;
            String name = "name";
            String surname = "surname";
            String gender = "gender";
            String age = "age";
            String[] args = new String[] { patientId, name, surname, gender, age };
            byte[] response = PatientManagerContract.submitTransaction("updatePatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("Patient" + patientId + "updated"); // se ritorna true il test sarà positivo
        }
}

/** DOCTOR UNIT TEST */

@Nested
    class DoctorCreates {

        @Test
        public void newDoctorCreate() throws ContractException, TimeoutException, InterruptedException {
            String doctorId = "doctorId" + currTime;
            String name = "doctorname";
            String surname = "doctorsurnmae";
            String hospital = "hospital";
            String[] args = new String[] { doctorId, name, surname, hospital };
            byte[] response = PatientManagerContract.submitTransaction("createDoctor", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("Doctor" + doctorId + "created"); // se ritorna true il test sarà positivo
        }

        @Test
        public void DoctoralreadyExists() throws ContractException, TimeoutException, InterruptedException {
            String doctorId = "doctorId" + currTime;
            String[] args = new String[] { doctorId };
            byte[] response = PatientManagerContract.submitTransaction("doctorExists", args);
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("The Doctor " + doctorId + " already exists");
        }

    @Test
    public void DoctorRead() throws ContractException, TimeoutException, InterruptedException {
        String doctorId = "doctorId" + currTime;
        String[] args = new String[] { doctorId };
        byte[] response = PatientManagerContract.evaluateTransaction("readDoctor", args);
        String responseString = new String(response);
        System.out.println(responseString);
        assertThat(responseString).contains("reading doctor " + doctorId + " files");
    }
    /*
    @Test
    public void DoctorReadFail() throws ContractException, TimeoutException, InterruptedException {
        String doctorId = "doctorId";
        String[] args = new String[] { processID, doctorId };
        byte[] response = PatientManagerContract.evaluateTransaction("readDoctor", args);
        String responseString = new String(response);
        assertThat(responseString).contains("Invalid user type: doctor");
    }
    */

    @Nested
    class DoctorUpdates {
        @Test
        public void updateExistingDoctor() throws ContractException, TimeoutException, InterruptedException {
            String doctorId = "doctorId" + currTime;
            String name = "doctorname";
            String surname = "doctorsurnmae";
            String hospital = "hospital";
            String[] args = new String[] { doctorId, name, surname, hospital };
            byte[] response = PatientManagerContract.submitTransaction("updateDoctor", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("Doctor " + doctorId + " updated"); // se ritorna true il test sarà positivo
        }
    }
}

/** DICOM UNIT TEST */

@Nested
    class DICOMCreates {

        @Test
        public void newDICOMCreate() throws ContractException, TimeoutException, InterruptedException {
                String dicomId = "dicomId" + currTime;
                String Filename = "Filename";
                String FileDateTime = "FileDateTime";
                String PatientID = "PatientID";
                String PatientName = "PatientName";
                String PatientAge = "PatientAge";
                String PatientGender = "PatientGender";
                String PatientWeight = "PatientWeight";
                String HeartRate = "HeartRate";
                String Modality = "Modality";
                String StudyDescription = "StudyDescription";
                String AnatomyPlane = "AnatomyPlane";
                String ExtraNotes = "ExtraNotes";
                String HospitalUID = "HospitalUID";
                String[] args = new String[] { dicomId, Filename, FileDateTime, PatientID, PatientName, PatientAge, PatientGender, PatientWeight, HeartRate, Modality, StudyDescription, AnatomyPlane, ExtraNotes, HospitalUID };
                byte[] response = PatientManagerContract.submitTransaction("createDICOM", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
                String responseString = new String(response);
                assertThat(responseString).isEqualTo("DICOM created"); // se ritorna true il test sarà positivo
        }

        @Test
        public void DICOMalreadyExists() throws ContractException, TimeoutException, InterruptedException {
            String dicomId = "dicomId" + currTime;
            String[] args = new String[] { dicomId };
            byte[] response = PatientManagerContract.submitTransaction("dicomExists", args);
            String responseString = new String(response);
            assertThat(responseString).isEqualTo("The DICOM " + dicomId + " already exists");
        }

    @Test
    public void DICOMRead() throws ContractException, TimeoutException, InterruptedException {
        String dicomId = "dicomId" + currTime;
        String PatientID = "PatientID";
        String[] args = new String[] { dicomId, PatientID };
        byte[] response = PatientManagerContract.evaluateTransaction("readDICOM", args);
        String responseString = new String(response);
        System.out.println(responseString);
        assertThat(responseString).contains("reading patient's "+ PatientID +" dicom " +dicomId);
    }

    /*
    @Test
    public void DICOMReadFail() throws ContractException, TimeoutException, InterruptedException {
        String dicomId = "dicomId" + currTime;
        String PatientID = "PatientID";
        String[] args = new String[] { dicomId, PatientID };
        byte[] response = PatientManagerContract.evaluateTransaction("readDICOM", args);
        String responseString = new String(response);
        assertThat(responseString).contains("Invalid user type: doctor");
    }
    */

    @Nested
    class DICOMUpdates {
        @Test
        public void DICOMupdateExisting() throws ContractException, TimeoutException, InterruptedException {

                String dicomId = "dicomId" + currTime;
                String Filename = "Filename";
                String FileDateTime = "FileDateTime";
                String PatientID = "PatientID";
                String PatientName = "PatientName";
                String PatientAge = "PatientAge";
                String PatientGender = "PatientGender";
                String PatientWeight = "PatientWeight";
                String HeartRate = "HeartRate";
                String Modality = "Modality";
                String StudyDescription = "StudyDescription";
                String AnatomyPlane = "AnatomyPlane";
                String ExtraNotes = "ExtraNotes";
                String HospitalUID = "HospitalUID";
                String[] args = new String[] { dicomId, Filename, FileDateTime, PatientID, PatientName, PatientAge, PatientGender, PatientWeight, HeartRate, Modality, StudyDescription, AnatomyPlane, ExtraNotes, HospitalUID };
                byte[] response = PatientManagerContract.submitTransaction("updateDICOM", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
                String responseString = new String(response);
                assertThat(responseString).isEqualTo("DICOM updated"); // se ritorna true il test sarà positivo
        }
    
    }
}
}
}

/*
PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
String json = "{\"value\":\"ThePatient\"}";
contract.createPatient(ctx, "10001", "Name", "Surname", "Gender", "Age");
verify(stub).putState("patientId", json.getBytes(UTF_8));
String[] args = new String[]{"patientId", "Name", "Surname", "Gender", "Age"};
byte[] response = PatientManagerContract.submitTransaction("createPatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
String responseString = new String(response);
assertThat(responseString).isEqualTo("Patient created"); // se ritorna true il test sarà positivo

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("patientId")).thenReturn(new byte[] { 42 });
Exception thrown = assertThrows(RuntimeException.class, () -> {contract.createPatient(ctx, "patientId", "Name", "Surname", "Gender", "Age");});
assertEquals(thrown.getMessage(), "Patient already exists");
String[] args = new String[]{"ctx", "patientId"};
byte[] response = PatientManagerContract.evaluateTransaction("patientExists", args); //se è una transizione di lettura
String responseString = new String(response);
assertThat(responseString).isEqualTo("Patient NOT created"); // se ritorna true il test sarà positivo

PatientContract contract = new PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
Patient asset = new  Patient();
asset.setName("name");
asset.setSurname("surname");
asset.setGender("gender");
asset.setAge("age");
String json = asset.toJSONString();
when(stub.getState("patientId")).thenReturn(json.getBytes(StandardCharsets.UTF_8));
Patient returnedAsset = contract.readPatient(ctx, "patientId");
assertEquals(returnedAsset.getName(), asset.getName());
String[] args = new String[]{"ctx", "patientId"};
byte[] response = PatientManagerContract.evaluateTransaction("readPatient", args); //se è una transizione di lettura
String responseString = new String(response);
assertThat(responseString).isEqualTo("Reading info of patient"); // se ritorna true il test sarà positivo

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("patientId")).thenReturn(new byte[] { 42 });
contract.updatePatient(ctx, "patientId", "updates", "NameSurname", "Gender", "Age");
String json = "{\"value\":\"updates\"}";
verify(stub).putState("patientId", json.getBytes(UTF_8));
String[] args = new String[]{"ctx", "patientId", "updates", "NameSurname", "Gender", "Age"};
byte[] response = PatientManagerContract.submitTransaction("updatePatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
String responseString = new String(response);
assertThat(responseString).isEqualTo("updated@"); // se ritorna true il test sarà positivo

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
String json = "{\"value\":\"TheDoctor\"}";
contract.createDoctor(ctx, "doctorId", "Name", "Surname", "Hospital");
verify(stub).putState("doctorId", json.getBytes(UTF_8));
String[] args = new String[]{"ctx", "doctorId", "Name", "Surname", "Hospital"};
byte[] response = PatientManagerContract.submitTransaction("createDoctor", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("Doctor created");

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("doctorId")).thenReturn(new byte[] { 42 });
Exception thrown = assertThrows(RuntimeException.class, () -> {contract.doctorExists(ctx, "doctorId");
});
assertEquals(thrown.getMessage(), "Doctor already exists");
String[] args = new String[]{"ctx", "doctorId"};
byte[] response = PatientManagerContract.evaluateTransaction("doctorExists", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("The Doctor already exists");

PatientContract contract = new PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
Doctor asset = new Doctor();
asset.setName("name");
asset.setSurname("surname");
asset.setHospital("hospital");
String json = asset.toJSONString();
when(stub.getState("doctorId")).thenReturn(json.getBytes(StandardCharsets.UTF_8));
Doctor returnedAsset = contract.readDoctor(ctx, "doctorId");
assertEquals(returnedAsset.getName(), asset.getName());
String[] args = new String[]{"ctx", "doctorId"};
byte[] response = PatientManagerContract.evaluateTransaction("readDoctor", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("Reading info of Doctor");

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("doctorId")).thenReturn(new byte[] { 42 });
contract.updateDoctor(ctx, "doctorId", "Name", "Surname", "Hospital");
String json = "{\"value\":\"updates\"}";
verify(stub).putState("doctorId", json.getBytes(UTF_8));
String[] args = new String[]{"ctx", "doctorId", "Name", "Surname", "Hospital"};
byte[] response = PatientManagerContract.submitTransaction("updateDoctor", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("updated@");

PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
String json = "{\"value\":\"TheDICOM\"}";
contract.createDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");
verify(stub).putState("dicomId", json.getBytes(UTF_8));
String[] args = new String[]{"ctx", "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID"};
byte[] response = PatientManagerContract.submitTransaction("createDICOM", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("DICOM created");
 
PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("dicomId")).thenReturn(new byte[] { 42 });
Exception thrown = assertThrows(RuntimeException.class, () -> {contract.createDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");
});
assertEquals(thrown.getMessage(), "DICOM already exists");
String[] args = new String[]{"ctx", "dicomId"};
byte[] response = PatientManagerContract.evaluateTransaction("dicomExists", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("The DICOM already exists");

PatientContract contract = new PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
DICOM asset = new DICOM();
asset.setFilename("Filename");
asset.setFileDateTime("FileDateTime");
asset.setPatientID("PatientID");
asset.setPatientName("PatientName");
asset.setPatientAge("PatientAge");
asset.setPatientGender("PatientGender"); 
asset.setPatientWeight("PatientWeight");
asset.setHeartRate("HeartRate");
asset.setModality("Modality");
asset.setStudyDescription("StudyDescription");
asset.setAnatomyPlane("AnatomyPlane");
asset.setExtraNotes("ExtraNotes");
asset.setHospitalUID("HospitalUID");
String json = asset.toJSONString();
when(stub.getState("dicomId")).thenReturn(json.getBytes(StandardCharsets.UTF_8));
DICOM returnedAsset = contract.readDICOM(ctx, "dicomId", "PatientID");
assertEquals(returnedAsset.getFilename(), asset.getFilename());
String[] args = new String[]{"ctx", "dicomId", "PatientID"};
byte[] response = PatientManagerContract.evaluateTransaction("readDICOM", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("Reading info of DICOM");
        
PatientContract contract = new  PatientContract();
Context ctx = mock(Context.class);
ChaincodeStub stub = mock(ChaincodeStub.class);
when(ctx.getStub()).thenReturn(stub);
when(stub.getState("dicomId")).thenReturn(new byte[] { 42 });
contract.updateDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");
String json = "{\"value\":\"updates\"}";
verify(stub).putState("dicomId", json.getBytes(UTF_8));
String[] args = new String[]{"ctx", "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID"};
byte[] response = PatientManagerContract.submitTransaction("updateDICOM", args);
String responseString = new String(response);
assertThat(responseString).isEqualTo("updated@");
*/

    /*
    @Nested
    class AssetExists {
        @Test
        public void noProperAsset() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] {});
            boolean result = contract.patientExists(ctx,"10001");
            assertFalse(result);
        }
        @Test
        public void assetExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] {42});
            boolean result = contract.patientExists(ctx,"10001");
            assertTrue(result);
        }
        @Test
        public void noKey() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10002")).thenReturn(null);
            boolean result = contract.patientExists(ctx,"10002");
            assertFalse(result);
        }
    }
    @Nested
    class AssetCreates {
        @Test
        public void newAssetCreate() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            String json = "{\"value\":\"ThePatient\"}";
            contract.createPatient(ctx, "10001", "Name", "Surname", "Gender", "Age");
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }
        @Test
        public void alreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10002")).thenReturn(new byte[] { 42 });
            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createPatient(ctx, "10001", "Name", "Surname", "Gender", "Age");
            });
            assertEquals(thrown.getMessage(), "Patient already exists");
        }
    @Test
    public void assetRead() {
        PatientContract contract = new PatientContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        Patient asset = new  Patient();
        asset.setName("name");
        asset.setSurname("surname");
        asset.setGender("gender");
        asset.setAge("age");
        String json = asset.toJSONString();
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));
        Patient returnedAsset = contract.readPatient(ctx, "10001");
        assertEquals(returnedAsset.getName(), asset.getName());
    }
    @Nested
    class AssetUpdates {
        @Test
        public void updateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] { 42 });
            contract.updatePatient(ctx, "10001", "updates", "NameSurname", "Gender", "Age");
            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }
        @Test
        public void updateMissing() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(null);
            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updatePatient(ctx, "10001", "ThePatient", "NameSurname", "Gender", "Age");
            });
            assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
        }
    }
    @Test
    public void assetDelete() {
        PatientContract contract = new  PatientContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getState("10001")).thenReturn(null);
        Exception thrown = assertThrows(RuntimeException.class, () -> {
            contract.deletePatient(ctx, "10001");
        });
        assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
    }
}
*/