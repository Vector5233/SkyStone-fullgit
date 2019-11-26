package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/* TODO
   To prevent robot from lumping at the endpoint
   1) make function that makes the robot smoothly accelerating stars and stops
   2) lowering the power
   3) Having a function that calculates the ticks for a given distance

 */

public class SSDriveObject extends Object{
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation, camera, leftFoundation, rightFoundation, blockSweeper ;
    CRServo deliveryExtender;
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft;
    LinearOpMode opmode;
     ModernRoboticsI2cGyro gyro;

    final double TICKS_PER_INCH_STRAIGHT = (383.6*2) / (4 * 3.14159265358979323846264);
    final double TICKS_PER_INCH_STRAFE = ((383.6*2) / (4 * 3.14159265358979323846264))*1.15;
    final double ROBOT_RADIUS = 9.8;
    final double TOLERANCE = 2;  // in degrees
    final double MAXSPEED = 0.65;
    private ElapsedTime strafeTimeout;
    private ElapsedTime driveTimeout;
    private ElapsedTime turnTimeout;
    private ElapsedTime rollerTimeout;
    private ElapsedTime hookHrzTimeout;
    private ElapsedTime hookVrtTimeout;
    private ElapsedTime DG_Timeout;
    private ElapsedTime DE_Timeout;

    //final double TOLERANCE = ??;
    //final double ROOT2 = 1.414;
    //final int CAMERA_MIDPOINT = ??;
    //final int SAMPLING_FORWARD = ?;


    //int distance = 0;
    //double convertion = 0;


    public SSDriveObject(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR, Servo HHRZ, Servo HVRT, Servo DG, Servo DR, CRServo DE, DcMotor RR, DcMotor RL, Servo RF, Servo LF, Servo BS, LinearOpMode parent){
        frontLeft = FL;
        frontRight = FR;
        backLeft = BL;
        backRight = BR;
        //check the teleop
        hookHrz = HHRZ;
        hookVrt = HVRT;
        deliveryGrabber = DG;
        deliveryRotation = DR;
        deliveryExtender = DE;
        rollerRight = RR;
        rollerLeft = RL;
        leftFoundation = LF;
        rightFoundation = RF;
        blockSweeper = BS;
        //add delivery servos
        opmode = parent;
    }


    public void setModeAll(DcMotor.RunMode mode) {
        frontLeft.setMode(mode);
        frontRight.setMode(mode);
        backLeft.setMode(mode);
        backRight.setMode(mode);
    }

    public void stopDriving() {
        frontLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backLeft.setPower(0.0);
        backRight.setPower(0.0);
    }

    public void telemetryDcMotor(){
        opmode.telemetry.addData("FR", frontRight.getPower());
        opmode.telemetry.addData("FB", frontLeft.getPower());
        opmode.telemetry.addData("BR", backRight.getPower());
        opmode.telemetry.addData("BL", backLeft.getPower());
        opmode.telemetry.update();
    }

    public void driveDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()) {
            opmode.telemetry.addData("frontRight: ", frontRight.getCurrentPosition());
            opmode.telemetry.addData("frontLeft: ", frontLeft.getCurrentPosition());
            opmode.telemetry.addData("backRight: ", backRight.getCurrentPosition());
            opmode.telemetry.addData("backLeft: ", backLeft.getCurrentPosition());
            opmode.telemetry.update();
        };

        stopDriving();
    }

    public void driveDistance(double power, double distance, int time) {
        driveTimeout = new ElapsedTime();
        int DRIVE_TIMEOUT = time;
        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()){
            if (driveTimeout.milliseconds() > DRIVE_TIMEOUT)
                break;
        }

        //telemetryDcMotor();

        stopDriving();
    }

    public void strafeDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH_STRAFE);

        /*if (power > MAXSPEED) {
            power = MAXSEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()) {
        }

        stopDriving();
    }

    public void strafeDistance (double power, double distance, int time) {
        strafeTimeout = new ElapsedTime();
        int STRAFE_TIMEOUT = time;

        int ticks = (int) (distance * TICKS_PER_INCH_STRAFE);

        /*if power > MAXSPEED {
            power = MAXSPEED
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()) {
            if (strafeTimeout.milliseconds() > STRAFE_TIMEOUT)
                break;
        }

        stopDriving();
    }

    public void turnDegree(double power, double degrees) {
        // distance in inches
        //conjecture instead of moving 12", wheels will go 12"*cos(45)= 8.5"
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH_STRAIGHT);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        double target;

        opmode.telemetry.addData("Gyro", gyro.getIntegratedZValue());
        opmode.telemetry.update();
        target = gyro.getIntegratedZValue() + degrees;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()) ;

        telemetryDcMotor();

        stopDriving();

        opmode.telemetry.addData("Gyro end of turn", gyro.getIntegratedZValue());
        opmode.telemetry.update();
    }

    public void turnDegree(double power, double degrees, int time) {
        turnTimeout = new ElapsedTime();
        final int TURN_TIMEOUT = time;

        // distance in inches
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH_STRAIGHT);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        double target;
        opmode.telemetry.update();
        target = gyro.getIntegratedZValue() + degrees;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > TURN_TIMEOUT)
                break;
        }

        telemetryDcMotor();

        stopDriving();
        opmode.telemetry.addData("Gyro end of turn", gyro.getIntegratedZValue());
        opmode.telemetry.update();
    }

    public void turnCorrect (double target) {
        /* corrects absolute heading to be target in degrees counterclockwise from initial calibration.
         * Caller must know what final heading should be!
         */
        double g;

        g = gyro.getIntegratedZValue();
        opmode.telemetry.addData("Gyro start correct", g);
        opmode.telemetry.update();

        if (g > target + TOLERANCE){
            turnDegree(0.7, g - target);
        } else if (g < target - TOLERANCE){
            turnDegree(0.7, target - g);
        }
        else{

        }
        opmode.telemetry.addData("Gyro end correct", gyro.getIntegratedZValue());
        opmode.telemetry.update();
    }

    public void setRollerMoters (boolean direction, double power, int time) {
        //direction true = forward
        //direction false = backward
        rollerTimeout = new ElapsedTime();
        final int ROLLER_TIMEOUT = time;

        rollerLeft.setPower(power);
        rollerRight.setPower(power);

        if(direction){
            rollerLeft.setDirection(DcMotorSimple.Direction.FORWARD);
            rollerRight.setDirection(DcMotorSimple.Direction.REVERSE);
        } else if(!direction){
            rollerLeft.setDirection(DcMotorSimple.Direction.REVERSE);
            rollerRight.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        while ((rollerRight.isBusy() || rollerLeft.isBusy()) && opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > ROLLER_TIMEOUT)
                break;
        }
    }
    public void setHookHrz (double position, int time) {
        hookHrzTimeout = new ElapsedTime();
        final int HOOKHRZ_TIMEOUT = time;

        hookHrz.setPosition(position);

        while (opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > HOOKHRZ_TIMEOUT) {
                break;
            }
        }
    }


    public void setHookVrt (double position, int time) {
        hookVrtTimeout = new ElapsedTime();
        final int HOOKVRT_TIMEOUT = time;

        hookVrt.setPosition(position);

        while (opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > HOOKVRT_TIMEOUT){
                break;
            }
        }
    }

    public void setCameraServo (double position) {
        camera.setPosition(position);
    }

    /*public void setFoundationRight (boolean launch) {
        //launch true = grabber down
        //launch false = grabber up
        if (launch) {
            rightFoundation.setPosition(0);
        }
        else if (!launch) {
            rightFoundation.setPosition(0.7);
        }
    }*/

    public void setFoundationLeft (boolean launch) {
        //launch true = grabber down
        //launch false = grabber up
        if (launch) {
            leftFoundation.setPosition(0);
            //rightFoundation.setPosition(0);
        }
        else if (!launch) {
            leftFoundation.setPosition(0.7);
            //rightFoundation.setPosition(0.7);
        }
    }

    public void setBlockSweeper (boolean kick) {
        //kick true = blockSweeper up
        //kick false = blockSweeper down

        if (kick) {
            blockSweeper.setPosition(0.95);

        } else if (!kick) {
            blockSweeper.setPosition(0.25);

        }
    }

    public void setDeliveryGrabber (double position, int time) {
        DG_Timeout = new ElapsedTime();
        final int DG_TIMEOUT = time;

        deliveryGrabber.setPosition(position);

        while (opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > DG_TIMEOUT) {
                break;
            }
        }
    }

    public void setDeliveryRotation (boolean rotate) {
        //rotate true = rotate out
        //rotate false = rotate in
        if (rotate) {
            leftFoundation.setPosition(0);
        }
        else if (!rotate) {
            leftFoundation.setPosition(0.7);
        }
    }

    public void setDeliveryExtender (double power, int time) {
        DE_Timeout = new ElapsedTime();
        final int DE_TIMEOUT = time;

        deliveryExtender.setPower(power);

        while (opmode.opModeIsActive()){
            if (turnTimeout.milliseconds() > DE_TIMEOUT) {
                break;
            }
        }
    }
}
