/*
 * DICOM is a healthcare standard responsible for governing nearly all aspects of medical imaging such 
 * as image transmission, image interpretation, print management, procedure management and off-line 
 * storage, and is used in nearly all healthcare-related imaging “modalities” such as magnetic resonance, 
 * nuclear medicine, computed tomography and ultrasound. Nearly all clinical imaging workflows around 
 * the world are based on the DICOM standard. 
 
FileMetaInformationGroupLength = 230
FileMetaInformationVersion = <bin: 0x0001>
MediaStorageSOPClassUID = 1.2.840.10008.5.1.4.1.1.4
MediaStorageSOPInstanceUID = 1.2.826.0.1.3680043.8.1055.1.20111103111148288.81713267.86905863
TransferSyntaxUID = 1.2.840.10008.1.2.4.70
ImplementationClassUID = 1.2.826.0.1.3680043.8.1055.1
ImplementationVersionName = dicomlibrary-100
SourceApplicationEntityTitle = DICOMLIBRARY
SpecificCharacterSet = ISO_IR 100
ImageType = ORIGINAL\PRIMARY\OTHER
InstanceCreationDate = 20070101
InstanceCreationTime = 120000.000000
SOPClassUID = 1.2.840.10008.5.1.4.1.1.4
SOPInstanceUID = 1.2.826.0.1.3680043.8.1055.1.20111103111148288.81713267.86905863
StudyDate = 20070101
SeriesDate = 20070101
AcquisitionDate = 20070101
ContentDate = 20070101
AcquisitionDateTime = 20070101120000
StudyTime = 120000.000000
SeriesTime = 120000.000000
AcquisitionTime = 120000.000000
ContentTime = 120000.000000
Modality = MR
StudyDescription = Knee (R)
SeriesDescription = AX.  FSE PD 
ReferencedImageSequence = <sequence of 2 items>
ReferencedSOPClassUID = 1.2.840.10008.5.1.4.1.1.4
ReferencedSOPInstanceUID = 1.2.840.113619.2.176.2025.1499492.7040.1171286241.719,  #1
ReferencedSOPClassUID = 1.2.840.10008.5.1.4.1.1.4
ReferencedSOPInstanceUID = 1.2.840.113619.2.176.2025.1499492.7040.1171286241.726
DerivationDescription = Lossless JPEG compression, selection value 1, point transform 0, compression ratio 2.1453 [Lossless JPEG compression, selection value 1, point transform 0, compression ratio 2.1453] 
DerivationCodeSequence = <sequence of 1 item>
CodeValue = 121327
CodingSchemeDesignator = DCM 
CodeMeaning = Full fidelity image, uncompressed or lossless compressed
PatientName = Anonymized
PatientID = 0 
PatientAge = 000Y
PatientWeight = 0
ScanningSequence = SE
SequenceVariant = SK\OSP
ScanOptions = SAT_GEMS\NPW\TRF_GEMS\FILTERED_GEMS\FS
String MRAcquisitionType = 2D
AngioFlag = N
SliceThickness = 4
RepetitionTime = 2800
EchoTime = 27.524
InversionTime = 0
NumberOfAverages = 1
ImagingFrequency = 63.860145
ImagedNucleus = 1H
EchoNumbers = 1
MagneticFieldStrength = 1.5
SpacingBetweenSlices = 4.5
EchoTrainLength = 10
PercentSampling = 100
PercentPhaseFieldOfView = 100
PixelBandwidth = 162.773
ProtocolName = 324-58-2995/6 
HeartRate = 474
CardiacNumberOfImages = 0
TriggerWindow = 0
ReconstructionDiameter = 170
ReceiveCoilName = HD TRknee PA
AcquisitionMatrix = 0\384\224\0
InPlanePhaseEncodingDirection = ROW
FlipAngle = 90
VariableFlipAngleFlag = N
SAR = 0.0533
String PatientPosition = FFS
StudyInstanceUID = 1.2.826.0.1.3680043.8.1055.1.20111103111148288.98361414.79379639
SeriesInstanceUID = 1.2.826.0.1.3680043.8.1055.1.20111103111148288.94019146.71622702
SeriesNumber = 5
AcquisitionNumber = 1
InstanceNumber = 1
ImagePositionPatient = -149.033\-118.499\-61.0464
ImageOrientationPatient = 0.999841\0.000366209\0.0178227\-0.000427244\0.999995\0.00326545
FrameOfReferenceUID = 1.2.840.113619.2.176.2025.1499492.7391.1171285944.389
ImagesInAcquisition = 24
SliceLocation = -59.25741196
SamplesPerPixel = 1
PhotometricInterpretation = MONOCHROME2
Rows = 512
Columns = 512
PixelSpacing = 0.332\0.332
BitsAllocated = 16
BitsStored = 16
HighBit = 15
PixelRepresentation = 1
SmallestImagePixelValue = 0
LargestImagePixelValue = 5145
PixelPaddingValue = 0
WindowCenter = 2572
WindowWidth = 5145
PixelData = <binary data of length: 244412>

import java.time.LocalDateTime; // Import the LocalDateTime class
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
public class Main {
  public static void main(String[] args) {
    LocalDateTime myDateObj = LocalDateTime.now();
    System.out.println("Before formatting: " + myDateObj);
    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    String formattedDate = myDateObj.format(myFormatObj);
    System.out.println("After formatting: " + formattedDate);
  }
}

 * */

package org.example;

//import org.apache.commons.collections.set.AbstractSortedSetDecorator;
//import org.bouncycastle.pkcs.PKCS12MacCalculatorBuilderProvider;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;

//import java.time.LocalDateTime; // Import the LocalDateTime class
//import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class

@DataType()
public class DICOM{
    
    @Property()
    private String Filename;
    //@Property()
    //private LocalDateTime FileDateTime = LocalDateTime.now();
    @Property()
    private Integer PatientID;
    @Property()
    private String PatientName;
    @Property()
    private Integer PatientAge;
    @Property()
    private String PatientGender;
    @Property()
    private Float PatientWeight;
    @Property()
    private Integer HeartRate;
    @Property()
    private String Modality;
    @Property()
    private String StudyDescription;
    @Property()
    private Integer ImagesInAcquisition;
    @Property()
    private String AnatomyPlane; // sagittale,coronale,trasverso
    @Property()
    private String ExtraNotes;
    @Property()
    private String HospitalUID;


    public DICOM(){
    }

    public void setFilename(String value) {
        this.Filename = value;
    }
    //public void setFileDateTime(LocalDateTime value) {
        //this.FileDateTime = value;
    //}
    public void setPatientID(Integer value) {
        this.PatientID = value;
    }
    public void setPatientName(String value) {
        this.PatientName = value;
    }
    public void setPatientAge(Integer value) {
        this.PatientAge = value;
    }
    public void setPatientGender(String value){
        this.PatientGender = value;
    }
    public void setPatientWeight(Float value) {
        this.PatientWeight = value;
    }
    public void setHeartRate(Integer value){
        this.HeartRate = value;
    }
    public void setModality(String value) {
        this.Modality = value;
    }
    public void setStudyDescription(String value) {
        this.StudyDescription = value;
    }
    public void setImagesInAcquisition(Integer value) {
        this.ImagesInAcquisition = value;
    }
    public void setAnatomyPlane(String value) {
        this.AnatomyPlane = value;
    }
    public void setExtraNotes(String value) {
        this.ExtraNotes = value;
    }
    public void setHospitalUID(String value) {
        this.HospitalUID = value;
    }

    public String toJSONString() {
        return new JSONObject(this).toString();
    }

    public static DICOM fromJSONString(String json) {
        String Filename = new JSONObject(json).getString("Filename");
        //LocalDateTime FileDateTime = new JSONObject(json).; <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        Integer PatientID = new JSONObject(json).getInt("PatientID");
        String PatientName = new JSONObject(json).getString("PatientName");
        Integer PatientAge = new JSONObject(json).getInt("PatientAge");
        String PatientGender = new JSONObject(json).getString("PatientGender");
        Float PatientWeight = new JSONObject(json).getFloat("PatientWeight");
        Integer HeartRate = new JSONObject(json).getInt("HeartRate");
        String Modality = new JSONObject(json).getString("Modality");
        String StudyDescription = new JSONObject(json).getString("StudyDescription");
        String AnatomyPalne = new JSONObject(json).getString("AnatomyPlane");
        String ExtraNotes = new JSONObject(json).getString("ExtraNotes");
        String HospitalUID = new JSONObject(json).getString("HospitalUID");

        DICOM asset = new DICOM();
        asset.setFilename(Filename);
        //asset.setFileDateTime(FileDateTime);
        asset.setPatientID(PatientID);
        asset.setPatientName(PatientName);
        asset.setPatientAge(PatientAge);
        asset.setPatientGender(PatientGender);
        asset.setPatientWeight(PatientWeight);
        asset.setHeartRate(HeartRate);
        asset.setModality(Modality);
        asset.setStudyDescription(StudyDescription);
        asset.setAnatomyPlane(AnatomyPalne);
        asset.setExtraNotes(ExtraNotes);
        asset.setHospitalUID(HospitalUID);

        return asset;
    }
    
}