package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;


public class SSDriveObject extends Object{
    Servo hookRightX, hookRightY, hookLeftX, hookLeftY;
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft;

    final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    final double ROBOT_RADIUS = 10;
    final double TOLERANCE = 2;
    private ElapsedTime strafeTimeout;
    private ElapsedTime driveTimeout;
    private ElapsedTime diagonalTimeout;
    private ElapsedTime turnTimeout;
    private ElapsedTime cmTimeout;
    private ElapsedTime lmTimeout;

    //final double TICKS_PER_INCH = 1120.0 / (4 * 3.14159265358979323846264);
    //final double ROBOT_RADIUS = 9.87;
    //final double TOLERANCE = ??;
    //final double ROOT2 = 1.414;
    //final int CAMERA_MIDPOINT = 400;


    //int distance = 0;
    //double convertion = 0;




    public SSDriveObject(){
        /*frontLeft = FL;
        frontRight = FR;
        backLeft = BL;
        backRight = BR;
        hookRightX = HRX
        hookRightY = HRY
        hookLeftX = HLX
        hookLeftY = HLY
        rollerRight = RR
        rollerLeft = RL
        */





    }
}
