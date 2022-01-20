/*
 * SPDX-License-Identifier: Apache License 2.0
 */
package org.example;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows; 
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.nio.charset.StandardCharsets;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractEvent;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.fail;

 public final class PatientContractTest {

    Wallet fabricWallet;
    Gateway gateway1, gateway2;
    Gateway.Builder builder1, builder2; // Configure the gateway connection used to access the network.
    Network network1, network2;
    Contract contractProcessManager1, contractDomainExpert1, contractAIExpert1, contractProcessManager2, contractDomainExpert2, contractAIExpert2,contractexpertiseCertificationManager1;
    String homedir = System.getProperty("C:/Users/scard");
    Path walletPath1 = Paths.get(homedir, ".fabric-vscode\\v2\\environments\\1 Org Local Fabric\\wallets\\Org1"); // Load an existing wallet holding identities used to access the network.
    Path connectionProfilePath = Paths.get(homedir, ".fabric-vscode", "environments", "VQA2OrgNet", "gateways", "Org1","Org1.json"); // Path to a common connection profile describing the network.
    String processManager1 = "processManager01";
    String domainExpert1 = "domainExpert01";
    String AIExpert1 = "AIExpert01";
    String processManager2 = "processManager02";
    String domainExpert2 = "domainExpert02";
    String AIExpert2 = "AIExpert02";
    String expertiseCertificationManager1 = "expertiseCertificationManager01";
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

            Set<String> set = fabricWallet.getAllLabels();

            builder1 = Gateway.createBuilder(); //connessione con la blockchain attraverso l'oggetto Gateway, ha bisogno del connection profile e del wallet, ci sarà una connessione diversa per ogni utente, che verrà inizializzata ogni qual volta che si effettua un nuovo test su una funzione
            builder1.identity(fabricWallet, processManager1).networkConfig(connectionProfilePath).discovery(true);
            gateway1 = builder1.connect();
            network1 = gateway1.getNetwork("mychannel");
            contractProcessManager1 = network1.getContract("MlProcess", "MlProcessContract");

            builder2 = Gateway.createBuilder();
            builder2.identity(fabricWallet, domainExpert1).networkConfig(connectionProfilePath).discovery(true);
            gateway2 = builder2.connect();
            network2 = gateway2.getNetwork("mychannel");
            contractDomainExpert1 = network2.getContract("MlProcess", "MlProcessContract");

            contractProcessManager1.addContractListener(eventListener);

        }).doesNotThrowAnyException();
    }

    @AfterEach //va a chiudere le connessioni
    public void after() {
        gateway1.close();
        gateway2.close();
        contractProcessManager1.removeContractListener(eventListener);
    }
    
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

/** PATIENT UNIT TEST */

@Nested
    class PatientCreates { //ogni funzione avrà una classe innessata solo per essa

        @Test
        public void newPatientCreate() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"ThePatient\"}";

            contract.createPatient(ctx, "10001", "Name", "Surname", "Gender", "Age");

            verify(stub).putState("patientId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "patientId", "Name", "Surname", "Gender", "Age"};
            //byte[] response = PatientManagerContract.submitTransaction("createPatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("Patient "+patientId+" created"); // se ritorna true il test sarà positivo
        }

        @Test
        public void PatientalreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("patientId")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {contract.createPatient(ctx, "patientId", "Name", "Surname", "Gender", "Age");
            });

            assertEquals(thrown.getMessage(), "Patient already exists");

            //String[] args = new String[]{ctx, "patientId"};
            //byte[] response = PatientManagerContract.evaluateTransaction("patientExists", args); //se è una transizione di lettura
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("Patient "+patientId+" NOT created"); // se ritorna true il test sarà positivo

        }

    @Test
    public void PatientRead() {
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

        //String[] args = new String[]{ctx, "patientId"};
        //byte[] response = PatientManagerContract.evaluateTransaction("readPatient", args); //se è una transizione di lettura
        //String responseString = new String(response);
        //assertThat(responseString).isEqualTo("Reading info of "+patientId"); // se ritorna true il test sarà positivo
    }

    @Nested
    class PatientUpdates {
        @Test
        public void PatientUpdateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("patientId")).thenReturn(new byte[] { 42 });

            contract.updatePatient(ctx, "patientId", "updates", "NameSurname", "Gender", "Age");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("patientId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "patientId", "updates", "NameSurname", "Gender", "Age"};
            //byte[] response = PatientManagerContract.submitTransaction("updatePatient", args); // se è una transizione di scrittura, viene eseguita su tutti i nodi
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("updated@"); // se ritorna true il test sarà positivo
        }

}

/** DOCTOR UNIT TEST */

@Nested
    class DoctorCreates {

        @Test
        public void newDoctorCreate() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"TheDoctor\"}";

            contract.createDoctor(ctx, "doctorId", "Name", "Surname", "Hospital");

            verify(stub).putState("doctorId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "doctorId", "Name", "Surname", "Hospital"};
            //byte[] response = PatientManagerContract.submitTransaction("createDoctor", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("Doctor "+doctorId+" created");
        }

        @Test
        public void DoctoralreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("doctorId")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.doctorExists(ctx, "doctorId");
            
            });

            assertEquals(thrown.getMessage(), "Doctor already exists");

            //String[] args = new String[]{ctx, "doctorId"};
            //byte[] response = PatientManagerContract.evaluateTransaction("doctorExists", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("The "+doctorId+" already exists");

        }


    @Test
    public void DoctorRead() {
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

        //String[] args = new String[]{ctx, "doctorId"};
        //byte[] response = PatientManagerContract.evaluateTransaction("readDoctor", args);
        //String responseString = new String(response);
        //assertThat(responseString).isEqualTo("Reading info of "+doctorId"");
    }

    @Nested
    class DoctorUpdates {
        @Test
        public void DoctorupdateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("doctorId")).thenReturn(new byte[] { 42 });

            contract.updateDoctor(ctx, "doctorId", "Name", "Surname", "Hospital");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("doctorId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "doctorId", "Name", "Surname", "Hospital};
            //byte[] response = PatientManagerContract.submitTransaction("updateDoctor", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("updated@");
        }

    }

}

/** DICOM UNIT TEST */

@Nested
    class DICOMCreates {

        @Test
        public void newDICOMCreate() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"TheDICOM\"}";

            contract.createDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");

            verify(stub).putState("dicomId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID"};
            //byte[] response = PatientManagerContract.submitTransaction("createDICOM", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("DICOM "+dicomId+" created");
        }

        @Test
        public void DICOMalreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("dicomId")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");
            
            });

            assertEquals(thrown.getMessage(), "DICOM already exists");

            //String[] args = new String[]{ctx, "dicomId"};
            //byte[] response = PatientManagerContract.evaluateTransaction("dicomExists", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("The "+dicomId+" already exists");

        }

    @Test
    public void DICOMRead() {
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

        //String[] args = new String[]{ctx, "dicomId", "PatientID"};
        //byte[] response = PatientManagerContract.evaluateTransaction("readDICOM", args);
        //String responseString = new String(response);
        //assertThat(responseString).isEqualTo("Reading info of "+dicomId"");
    }

    @Nested
    class DICOMUpdates {
        @Test
        public void DICOMupdateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("dicomId")).thenReturn(new byte[] { 42 });

            contract.updateDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("dicomId", json.getBytes(UTF_8));

            //String[] args = new String[]{ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID"};
            //byte[] response = PatientManagerContract.submitTransaction("updateDICOM", args);
            //String responseString = new String(response);
            //assertThat(responseString).isEqualTo("updated@");
        }
    }
}
}
}