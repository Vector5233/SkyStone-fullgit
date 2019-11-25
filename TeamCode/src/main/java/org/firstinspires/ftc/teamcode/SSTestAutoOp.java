package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="SSTestAutoOp", group="TeamCode")

public class SSTestAutoOp extends LinearOpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rightRoller, leftRoller;
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation, camera, leftFoundation, blockSweeper;
    CRServo deliveryExtender;
    LinearOpMode opmode;
    ModernRoboticsI2cGyro gyro;
    SSDriveObject drive;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    final double WEBCAM_TO_BLOCKS = 9.5;

    final double CENTER_PIXELS = 0;
    final double BLOCK_LENGTH = 8;
    final double ARM_TO_WEBCAM = 9;

    double inchPerPixel = 0;

    double LS_leftPixel = 0;
    double LS_rightPixel = 0;

    double RS_leftPixel = 0;
    double RS_rightPixel = 0;

    double SS_leftPixel = 0;
    double SS_rightPixel = 0;

    double displacement = 0;

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public void initialize(){
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        rightRoller = hardwareMap.dcMotor.get("rightRoller");
        leftRoller = hardwareMap.dcMotor.get("leftRoller");

        hookHrz = hardwareMap.servo.get("hookHrz");
        hookVrt = hardwareMap.servo.get("hookVrt");

        deliveryGrabber = hardwareMap.servo.get("deliveryGrabber");
        deliveryRotation = hardwareMap.servo.get("deliveryRotation");

        leftFoundation = hardwareMap.servo.get("leftFoundation");

        deliveryExtender = hardwareMap.crservo.get("deliveryExtender");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        //change frontLeft into reverse
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        rightRoller.setDirection(DcMotor.Direction.REVERSE);
        leftRoller.setDirection(DcMotor.Direction.FORWARD);

        deliveryExtender.setDirection(CRServo.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        drive = new SSDriveObject(frontLeft, frontRight, backLeft, backRight, hookHrz, hookVrt, deliveryGrabber, leftFoundation, deliveryExtender, rightRoller, leftRoller, camera, leftFoundation, blockSweeper, this);

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = "AbLVQDn/////AAABma+zV9cCqU+7pLBUXgQ3J6II1u1B8Vg4mrnGfVawPjc1l7C6GWoddOaL6Wqj5kXPBVUh3U3WND38234Tm0h3+LKmmTzzaVPRwOk3J+zBwKlOvv93+u7chctULk8ZYEyf0NuuEfsGwpgJx7xL9hIFBoaB2G1SpbJIt+n94wz6EvfRYSusBEiST/lUqgDISIlaeOLPWEipHh46axomcrGVRRl09pg6pCt2h7rU6us+guN5nKhupTXvM+BTUYW3kCO9YsUjz16jLr7GyFh8wVQbRS3dikSX7kzVsdkLjZnJdyinYaB5oDXfmmXtaC6ZXeD6vKs62vpaydAq9VGAlCtnSyq2J4NLI+LOIOvdtsCwarfS";
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        initTfod();
    }
    public void runOpMode(){
        initialize();
        waitForStart();

        if (tfod != null) {
            tfod.activate();
        }

        //drive.strafeDistance(-1, 24.5, 1000);
        detectStones();
        getDisplacement();
        //collect
        //drive till fnd
        //find navi img
        //cw 90
        //deliver
        //ccw 90
        //drive back til block
        //collect
        //drive till fnd
        //cw 90
        //deliver
        //strafe till line
        //park
        //init for teleop
    }

    public void initTfod(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.6;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    public void detectStones(){
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                if (tfod != null) {
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());
                        int i = 0;
                        int stoneCount = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            if(recognition.getLabel().equals("Skystone")){
                                SS_leftPixel = recognition.getLeft();
                                SS_rightPixel = recognition.getRight();
                            } else if(recognition.getLabel().equals("Stone")){
                                stoneCount ++;
                                telemetry.addData("stone count", stoneCount);

                                if(i == 0){
                                    RS_rightPixel = recognition.getRight();
                                    RS_leftPixel = recognition.getLeft();
                                } else if(i == 1){
                                    LS_rightPixel = recognition.getRight();
                                    LS_leftPixel = recognition.getLeft();
                                }

                                if(stoneCount == 2){
                                    createVirtualStone();
                                }
                            }
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData("  left", "%.03f", recognition.getLeft());
                            telemetry.addData("  right", "%.03f", recognition.getRight());
                        }
                    }
                }

                telemetry.update();
            }
        }

        if (tfod != null) {
            tfod.shutdown();
        }
    }

    public void createVirtualStone(){
        double RS_size = RS_rightPixel - RS_leftPixel;
        double LS_size = LS_rightPixel - LS_leftPixel;

        SS_leftPixel = Math.max(RS_rightPixel, LS_rightPixel);
        SS_rightPixel = SS_leftPixel + (RS_size + LS_size)/2;

        telemetry.addData("  Virtual left", "%.03f", SS_leftPixel);
        telemetry.addData("  Virtual right", "%.03f", SS_rightPixel);
        telemetry.update();
    }

    public void getDisplacement(){
        inchPerPixel = BLOCK_LENGTH/(SS_rightPixel - SS_leftPixel);
        telemetry.addData("  inch / pixel", "%.03f", inchPerPixel);
        displacement = ((5/8)*(SS_rightPixel - SS_leftPixel) + SS_leftPixel - CENTER_PIXELS)*inchPerPixel - ARM_TO_WEBCAM;
        telemetry.addData("  displacement", "%.03f", displacement);
        telemetry.update();
    }
}
