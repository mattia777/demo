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
/*
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

*/

/*
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;

class Sample {
    public static void main(String[] args) throws IOException {
        // Load an existing wallet holding identities used to access the network.
        Path walletDirectory = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletDirectory);

        // Path to a common connection profile describing the network.
        Path networkConfigFile = Paths.get("connection.json");

        // Configure the gateway connection used to access the network.
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(networkConfigFile);

        // Create a gateway connection
        try (Gateway gateway = builder.connect()) {

            // Obtain a smart contract deployed on the network.
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            // Submit transactions that store state to the ledger.
            byte[] createCarResult = contract.createTransaction("createCar")
                    .submit("CAR10", "VW", "Polo", "Grey", "Mary");
            System.out.println(new String(createCarResult, StandardCharsets.UTF_8));

            // Evaluate transactions that query state from the ledger.
            byte[] queryAllCarsResult = contract.evaluateTransaction("queryAllCars");
            System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));

        } catch (ContractException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
*/


 public final class PatientContractTest {
/*
    Wallet fabricWallet;
    Gateway gateway1, gateway2, gateway3, gateway4, gateway5, gateway6, gateway7;
    Gateway.Builder builder1, builder2, builder3, builder4, builder5, builder6 , builder7; // Configure the gateway connection used to access the network.
    Network network1, network2, network3, network4, network5, network6, network7;
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

    static Consumer<ContractEvent> eventListener = a -> System.out
            .println(a.getName() + new String(a.getPayload().get()));
    
    @BeforeAll
    public static void beforeAll() {
        currTime = Instant.now().toString();
    }

    @BeforeEach
    public void before() throws Exception {
        assertThatCode(() -> {
            JavaSmartContractUtil.setDiscoverAsLocalHost(isLocalhostURL);
            fabricWallet = Wallet.createFileSystemWallet(walletPath1);

            Set<String> set = fabricWallet.getAllLabels();

            builder1 = Gateway.createBuilder();
            builder1.identity(fabricWallet, processManager1).networkConfig(connectionProfilePath).discovery(true);
            gateway1 = builder1.connect();
            network1 = gateway1.getNetwork("mychannel");
            contractProcessManager1 = network1.getContract("MlProcess", "MlProcessContract");

            builder2 = Gateway.createBuilder();
            builder2.identity(fabricWallet, domainExpert1).networkConfig(connectionProfilePath).discovery(true);
            gateway2 = builder2.connect();
            network2 = gateway2.getNetwork("mychannel");
            contractDomainExpert1 = network2.getContract("MlProcess", "MlProcessContract");
           

            builder3 = Gateway.createBuilder();
            builder3.identity(fabricWallet, AIExpert1).networkConfig(connectionProfilePath).discovery(true);
            gateway3 = builder3.connect();
            network3 = gateway3.getNetwork("mychannel");
            contractAIExpert1 = network3.getContract("MlProcess", "MlProcessContract");

            
            builder4 = Gateway.createBuilder();
            builder4.identity(fabricWallet, processManager2).networkConfig(connectionProfilePath).discovery(true);
            gateway4 = builder4.connect();
            network4 = gateway4.getNetwork("mychannel");
            contractProcessManager2 = network4.getContract("MlProcess", "MlProcessContract");
            
            
            builder5 = Gateway.createBuilder();
            builder5.identity(fabricWallet, domainExpert2).networkConfig(connectionProfilePath).discovery(true);
            gateway5 = builder5.connect();
            network5 = gateway5.getNetwork("mychannel");
            contractDomainExpert2 = network5.getContract("MlProcess", "MlProcessContract");
         
            builder6 = Gateway.createBuilder();
            builder6.identity(fabricWallet, AIExpert2).networkConfig(connectionProfilePath).discovery(true);
            gateway6 = builder6.connect();
            network6 = gateway6.getNetwork("mychannel");
            contractAIExpert2 = network6.getContract("MlProcess", "MlProcessContract");
            
            builder7 = Gateway.createBuilder();
            builder7.identity(fabricWallet, expertiseCertificationManager1).networkConfig(connectionProfilePath).discovery(true);
            gateway7 = builder7.connect();
            network7 = gateway7.getNetwork("mychannel");
            contractexpertiseCertificationManager1 = network7.getContract("MlProcess", "MlProcessContract");

            contractProcessManager1.addContractListener(eventListener);

        }).doesNotThrowAnyException();
    }

    @AfterEach
    public void after() {
        gateway1.close();
        gateway2.close();
        gateway3.close();
        gateway4.close();
        gateway5.close();
        gateway6.close();
        contractProcessManager1.removeContractListener(eventListener);
    }
*/

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

/** PATIENT UNIT TEST */

@Nested
    class PatientCreates {

        @Test
        public void newPatientCreate() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"ThePatient\"}";

            contract.createPatient(ctx, "10001", "Name", "Surname", "Gender", "Age");

            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void PatientalreadyExists() {
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
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        Patient returnedAsset = contract.readPatient(ctx, "10001");
        assertEquals(returnedAsset.getName(), asset.getName());
    }

    @Nested
    class PatientUpdates {
        @Test
        public void PatientUpdateExisting() {
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
        public void PatientupdateMissing() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(null);

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updatePatient(ctx, "10001", "ThePatient", "NameSurname", "Gender", "Age");
            });

            assertEquals(thrown.getMessage(), "The Patient does not exist");
        }

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

            contract.createDoctor(ctx, "10001", "Name", "Surname", "Hospital");

            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void DoctoralreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createDoctor(ctx, "10001", "Name", "Surname", "Hospital");
            
            });

            assertEquals(thrown.getMessage(), "Doctor already exists");

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
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        Doctor returnedAsset = contract.readDoctor(ctx, "10001");
        assertEquals(returnedAsset.getName(), asset.getName());
    }

    @Nested
    class DoctorUpdates {
        @Test
        public void DoctorupdateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] { 42 });

            contract.updateDoctor(ctx, "10001", "Name", "Surname", "Hospital");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void DoctorupdateMissing() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(null);

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updateDoctor(ctx, "10001", "Name", "Surname", "Hospital");

            });

            assertEquals(thrown.getMessage(), "The Doctor does not exist");
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

            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void DICOMalreadyExists() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");
            
            });

            assertEquals(thrown.getMessage(), "DICOM already exists");

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
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        DICOM returnedAsset = contract.readDICOM(ctx, "10001", "10002");
        assertEquals(returnedAsset.getFilename(), asset.getFilename());
    }

    @Nested
    class DICOMUpdates {
        @Test
        public void DICOMupdateExisting() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] { 42 });

            contract.updateDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void DICOMupdateMissing() {
            PatientContract contract = new  PatientContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(null);

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updateDICOM(ctx, "dicomId", "Filename", "FileDateTime", "PatientID", "PatientName", "PatientAge", "PatientGender", "PatientWeight", "HeartRate", "Modality", "StudyDescription", "AnatomyPlane", "ExtraNotes", "HospitalUID");

            });

            assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
        }

    }
}
}