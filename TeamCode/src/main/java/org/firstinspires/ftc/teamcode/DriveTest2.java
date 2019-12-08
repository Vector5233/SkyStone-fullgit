package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

@Autonomous(name="DriveTestAutoOp2", group="TeamCode")

public class DriveTest2 extends LinearOpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rightRoller, leftRoller;
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation, camera, leftFoundation, blockSweeper;
    CRServo deliveryExtender;
    LinearOpMode opmode;
    ModernRoboticsI2cGyro gyro;
    SSDriveObject drive;

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

        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

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

        drive = new SSDriveObject(frontLeft, frontRight, backLeft, backRight, hookHrz, hookVrt, deliveryGrabber, leftFoundation, deliveryExtender, rightRoller, leftRoller, camera, leftFoundation, blockSweeper, gyro, this);
    }

    public void runOpMode() {
        initialize();
        waitForStart();

        drive.setHookVrt(0.9);
        drive.setHookHrz(1);
        sleep(200);
        drive.setHookVrt(0.4);
        sleep(250);
        drive.strafeDistance(1, 5);
        sleep(200);
        drive.setHookHrz(0);
        drive.setRollerMoters(true, 1, 1000);
        //how to check if the block is collected or not (next round)
        drive.setBlockSweeper(true);
        sleep(750);
        drive.setBlockSweeper(false);





        /*
        drive.driveDistance(1, 10, 5000);
        sleep(3000);
        drive.driveDistance(0.5, 10, 5000);
        sleep(50 );
        telemetry.addLine("done");
         */
    }
}