package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/* TODO
    * strafeDistance(power, distance, time);
    * strafeDistance(power, distance);
    * turnDegree (power, degrees, time);
 */
public class SSDriveObject extends Object{
    Servo hookRightX, hookRightY, hookLeftX, hookLeftY;
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft;
    LinearOpMode opmode;

    final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    final double ROBOT_RADIUS = 9.8;
    final double TOLERANCE = 2;  // in degrees
    final double MAXSPEED = 0.65;
    private ElapsedTime strafeTimeout;
    private ElapsedTime driveTimeout;
    private ElapsedTime turnTimeout;

    //final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    //final double ROBOT_RADIUS = 9.87;
    //final double TOLERANCE = ??;
    //final double ROOT2 = 1.414;
    //final int CAMERA_MIDPOINT = ??;
    //final int SAMPLING_FORWARD = ?;


    //int distance = 0;
    //double convertion = 0;


    public SSDriveObject(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR, Servo HRX, Servo HRY, Servo HLX, Servo HLY, DcMotor RR, DcMotor RL, LinearOpMode parent){
        frontLeft = FL;
        frontRight = FR;
        backLeft = BL;
        backRight = BR;
        hookRightX = HRX;
        hookRightY = HRY;
        hookLeftX = HLX;
        hookLeftY = HLY;
        rollerRight = RR;
        rollerLeft = RL;
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


    public void driveDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if (power > 0.65) {
            power = 0.65;
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

        //opmode.telemetry.addData();

        stopDriving();
    }
    public void driveDistance(double power, double distance, int time) {

        int DRIVE_TIMEOUT = time;

        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if (power > 0.65) {
            power = 0.65;
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

        stopDriving();


    }

    public void strafeDistance (double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH);

        /*if power > MAXSPEED {
            power = MAXSPEED
        }*/

        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);






    }

}
