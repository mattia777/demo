package org.example;
//import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
//import java.util.HashSet;
import java.util.Set;
//import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hyperledger.fabric.gateway.Contract;
//import org.hyperledger.fabric.gateway.ContractEvent;
//import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
//import org.hyperledger.fabric.gateway.Wallet.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
//import org.hyperledger.fabric.sdk.User;
//import org.hyperledger.fabric.sdk.identity.X509Identity;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.apache.log4j.Logger;

public class testUserCreator {
        static String caUrl = "http://localhost:17110";
        private static final Logger log = Logger.getLogger(testUserCreator.class);
        static Wallet fabricWallet;
        Gateway gateway1, gateway2, gateway3;
        static Gateway.Builder builder1;
        Gateway.Builder builder2;
        Gateway.Builder builder3;
        Network network1, network2, network3;
        Contract PatientManagerContract;
        String homedir = System.getProperty("C:\\Users\\scard");
        static Path walletPath1 = Paths.get("C:\\Users\\scard\\fabric-vscode\\v2\\environments\\1 Org Local Fabric\\wallets\\Org1"); // Load an existing wallet holding identities used to access the network.
        static Path connectionProfilePath = Paths.get("C:\\Users\\scard\\.fabric-vscode\\v2\\environments\\1 Org Local Fabric\\gateways\\Org1 Gateway.json"); // Path to a common connection profile describing the network.        
        String patient = "patient";
        String doctor = "doctor";
        String DICOM = "DICOM";
        static boolean isLocalhostURL = JavaSmartContractUtil.hasLocalhostURLs(connectionProfilePath);
        static String[][] userIDs = {{"patient01", "doctor01", "DICOM01", "patient02", "doctor02", "DICOM02"},{"patient", "doctor", "DICOM"}};

        public static void main(String... args) throws Exception {
                JavaSmartContractUtil.setDiscoverAsLocalHost(isLocalhostURL);
                fabricWallet = Wallet.createFileSystemWallet(walletPath1);
                builder1 = Gateway.createBuilder();
                Set<String> s = fabricWallet.getAllLabels();
                System.out.println(s);
                CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
                HFCAClient caClient = HFCAClient.createNewInstance(caUrl, null);
                caClient.setCryptoSuite(cryptoSuite);
                Enrollment adminEnrollment = caClient.enroll("admin", "adminpw");
                log.info(adminEnrollment);
                AppUser admin = new AppUser("admin", "org1", "Org1MSP", adminEnrollment);
                log.info(admin);
                for (int i =0; i <userIDs[0].length; i++) {
                        RegistrationRequest rr = new RegistrationRequest(userIDs[0][i], "org1");
                        log.info(rr);
                        Attribute attr = new Attribute("usertype", userIDs[1][i], true);
                        rr.addAttribute(attr);
                        String userSecret = caClient.register(rr, admin);
                        log.info(userSecret);
                        Enrollment userEnrollment = caClient.enroll(userIDs[0][i], userSecret);
                        log.info(userEnrollment);
                        AppUser appUser = new AppUser(userIDs[0][i], "org1", "Org1MSP", userEnrollment);
                        log.info(appUser);
                        Wallet.Identity wi = Wallet.Identity.createIdentity("Org1MSP", userEnrollment.getCert(), userEnrollment.getKey());
                        fabricWallet.put(userIDs[0][i], wi);
                }

        }
}
