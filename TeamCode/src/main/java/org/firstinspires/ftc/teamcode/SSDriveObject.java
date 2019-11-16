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
   *setRollerMoter power

 */

public class SSDriveObject extends Object{
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation ;
    CRServo deliveryExtender;
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft;
    LinearOpMode opmode;
     ModernRoboticsI2cGyro gyro;

    final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    final double ROBOT_RADIUS = 9.8;
    final double TOLERANCE = 2;  // in degrees
    final double MAXSPEED = 0.65;
    private ElapsedTime strafeTimeout;
    private ElapsedTime driveTimeout;
    private ElapsedTime turnTimeout;
    private ElapsedTime rollerTimeout;

    //final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    //final double ROBOT_RADIUS = 9.87;
    //final double TOLERANCE = ??;
    //final double ROOT2 = 1.414;
    //final int CAMERA_MIDPOINT = ??;
    //final int SAMPLING_FORWARD = ?;


    //int distance = 0;
    //double convertion = 0;


    public SSDriveObject(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR, Servo HHRZ, Servo HVRT, Servo DG, Servo DR, CRServo DE, DcMotor RR, DcMotor RL, LinearOpMode parent){
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
        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);

        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()) ;

        stopDriving();
    }

    public void driveDistance(double power, double distance, int time) {
        int DRIVE_TIMEOUT = time;
        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(ticks);
        backRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(ticks);

        driveTimeout = new ElapsedTime();
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);

        while ((frontRight.isBusy() || frontLeft.isBusy()) && opmode.opModeIsActive()){
            if (driveTimeout.milliseconds() > DRIVE_TIMEOUT)
                break;
        }

        telemetryDcMotor();

        stopDriving();
    }

    public void strafeDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if (power > MAXSPEED) {
            power = MAXSEED;
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

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

        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if power > MAXSPEED {
            power = MAXSPEED
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(ticks);
        frontRight.setTargetPosition(-ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

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
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        double target;

        opmode.telemetry.addData("Gyro", gyro.getIntegratedZValue());
        opmode.telemetry.update();
        target = gyro.getIntegratedZValue() + degrees;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

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
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH);

        /*if (power > MAXSPEED) {
            power = MAXSPEED;
        }*/

        double target;
        opmode.telemetry.update();
        target = gyro.getIntegratedZValue() + degrees;

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        setModeAll(DcMotor.RunMode.RUN_TO_POSITION);

        frontLeft.setTargetPosition(-ticks);
        frontRight.setTargetPosition(ticks);
        backLeft.setTargetPosition(-ticks);
        backRight.setTargetPosition(ticks);

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

}
