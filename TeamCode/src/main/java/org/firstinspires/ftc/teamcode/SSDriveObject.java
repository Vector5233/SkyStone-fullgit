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



    public SSDriveObject(DcMotor FL, DcMotor FR, DcMotor BL, DcMotor BR, Servo HHRZ, Servo HVRT, Servo DG, Servo DR, CRServo DE, DcMotor RR, DcMotor RL, Servo RF, Servo LF, Servo BS, ModernRoboticsI2cGyro G, LinearOpMode parent){
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
        gyro = G;
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
        opmode.telemetry.addData("FR", frontRight.getCurrentPosition());
        opmode.telemetry.addData("FB", frontLeft.getCurrentPosition());
        opmode.telemetry.addData("BR", backRight.getCurrentPosition());
        opmode.telemetry.addData("BL", backLeft.getCurrentPosition());
        opmode.telemetry.update();
    }

    /*public void driveDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);

        if (power > MAXSPEED) {
            power = MAXSPEED;
        }

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
    } */

    public void setPowerAll(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backRight.setPower(power);
        backLeft.setPower(power);
    }

    public void driveDistance(double powerLimit, double distance) {
        final double PERCENT = .1;
        double powerMin = 0.22;
        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);


        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_USING_ENCODER);

        if (ticks > 0) {
            while((frontLeft.getCurrentPosition() <= ticks) && opmode.opModeIsActive()) {
                if (frontLeft.getCurrentPosition() < PERCENT * ticks) {
                    setPowerAll(Math.max((1 / PERCENT) * powerLimit * frontLeft.getCurrentPosition() / ticks, powerMin));
                    opmode.telemetry.addLine("accelerating");
                    telemetryDcMotor();
                } else if (frontLeft.getCurrentPosition() < (1 - PERCENT) * ticks) {
                    setPowerAll(powerLimit);
                    opmode.telemetry.addLine("cruising");
                    telemetryDcMotor();
                } else {
                    setPowerAll(Math.max(-(1 / PERCENT) * powerLimit * (frontLeft.getCurrentPosition() - ticks) / ticks, powerMin));
                    opmode.telemetry.addLine("decelerating");
                    telemetryDcMotor();
                }
            }
        } else if (ticks < 0) {
            powerLimit = -powerLimit;
            powerMin = -powerMin;
            while((frontLeft.getCurrentPosition() >= ticks) && opmode.opModeIsActive()) {
                if (frontLeft.getCurrentPosition() > PERCENT * ticks) {
                    setPowerAll(Math.min((1 / PERCENT) * powerLimit * frontLeft.getCurrentPosition() / ticks, powerMin));
                    opmode.telemetry.addLine("accelerating");
                    telemetryDcMotor();
                } else if (frontLeft.getCurrentPosition() > (1 - PERCENT) * ticks) {
                    setPowerAll(powerLimit);
                    opmode.telemetry.addLine("cruising");
                    telemetryDcMotor();
                } else {
                    setPowerAll(Math.min(-(1 / PERCENT) * powerLimit * (frontLeft.getCurrentPosition() - ticks) / ticks, powerMin));
                    opmode.telemetry.addLine("decelerating");
                    telemetryDcMotor();
                }
            }
        }


        stopDriving();

    }

    public void setStrafePowerAll(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public void strafeDistance(double powerLimit, double distance) {
        final double PERCENT = .25;
        final double STRAFECORRECTION = 30/37;
        double powerMin = 0.22;
        int ticks = (int) (distance * TICKS_PER_INCH_STRAFE * STRAFECORRECTION);


        setModeAll(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModeAll(DcMotor.RunMode.RUN_USING_ENCODER);

        if (ticks > 0) {
            while((frontLeft.getCurrentPosition() <= ticks) && opmode.opModeIsActive()) {
                if (frontLeft.getCurrentPosition() < PERCENT * ticks) {
                    setStrafePowerAll(Math.max((1 / PERCENT) * powerLimit * frontLeft.getCurrentPosition() / ticks, powerMin));
                    opmode.telemetry.addLine("accelerating");
                    telemetryDcMotor();
                } else if (frontLeft.getCurrentPosition() < (1 - PERCENT) * ticks) {
                    setStrafePowerAll(powerLimit);
                    opmode.telemetry.addLine("cruising");
                    telemetryDcMotor();
                } else {
                    setStrafePowerAll(Math.max(-(1 / PERCENT) * powerLimit * (frontLeft.getCurrentPosition() - ticks) / ticks, powerMin));
                    opmode.telemetry.addLine("decelerating");
                    telemetryDcMotor();
                }
            }
        } else if (ticks < 0) {
            powerLimit = -powerLimit;
            powerMin = -powerMin;
            while((frontLeft.getCurrentPosition() >= ticks) && opmode.opModeIsActive()) {
                if (frontLeft.getCurrentPosition() > PERCENT * ticks) {
                    setStrafePowerAll(Math.min((1 / PERCENT) * powerLimit * frontLeft.getCurrentPosition() / ticks, powerMin));
                    opmode.telemetry.addLine("accelerating");
                    telemetryDcMotor();
                } else if (frontLeft.getCurrentPosition() > (1 - PERCENT) * ticks) {
                    setStrafePowerAll(powerLimit);
                    opmode.telemetry.addLine("cruising");
                    telemetryDcMotor();
                } else {
                    setStrafePowerAll(Math.min(-(1 / PERCENT) * powerLimit * (frontLeft.getCurrentPosition() - ticks) / ticks, powerMin));
                    opmode.telemetry.addLine("decelerating");
                    telemetryDcMotor();
                }
            }
        }


        stopDriving();

    }

    /*public void driveDistance(double powerLimit, double distance) {

        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);

        final double POWERMIN = 0.22;
        final int NUMBEROFINTERVALS = 20;
        final double ACCELDISTANCEPERCENT = .1;

        final double POWERINCREMENT = (powerLimit - POWERMIN) / NUMBEROFINTERVALS;
        final double ACCELINCREMENT =  ticks / NUMBEROFINTERVALS;

        double accelIndex = ACCELINCREMENT;
        double power = POWERMIN;

        //this loop accelerates the robot up to powerLimit given over a chosen proportion of the distance given
        while (accelIndex < ACCELDISTANCEPERCENT * ticks){
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            if (frontLeft.getCurrentPosition() >= accelIndex * ACCELDISTANCEPERCENT * ticks) {
                power += POWERINCREMENT;
                accelIndex += ACCELINCREMENT;

            }
            telemetryDcMotor();

        }

        while (frontLeft.getCurrentPosition() < ticks - ticks * ACCELDISTANCEPERCENT) {
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);
            telemetryDcMotor();
        }

        accelIndex = (ticks - ticks * ACCELDISTANCEPERCENT) + ACCELINCREMENT;

        while ((accelIndex < ticks)){
            frontLeft.setPower(power);
            frontRight.setPower(power);
            backLeft.setPower(power);
            backRight.setPower(power);

            if (frontLeft.getCurrentPosition() >= accelIndex * ticks) {
                power -= POWERINCREMENT;
                accelIndex -= ACCELINCREMENT;
            }
            telemetryDcMotor();
        }
        stopDriving();
    }
*/


    public void driveDistance(double power, double distance, int time) {
        driveTimeout = new ElapsedTime();
        int DRIVE_TIMEOUT = time;
        int ticks = (int) (distance * TICKS_PER_INCH_STRAIGHT);

        if (power > MAXSPEED) {
            power = MAXSPEED;
        }

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

    /*public void strafeDistance(double power, double distance) {
        int ticks = (int) (distance * TICKS_PER_INCH_STRAFE);

        if (power > MAXSPEED) {
            power = MAXSEED;
        }

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
    }*/

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

    public void setTurnPowerAll(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);
    }

    public boolean outsideOfRange(double targetDegrees, double initialDegrees) {
        final double ERRORTOLERANCE = 10;
        if (gyro.getIntegratedZValue() > (Math.max(targetDegrees, initialDegrees) + ERRORTOLERANCE)) {
            return true;
        } else if (gyro.getIntegratedZValue() < (Math.min(targetDegrees, initialDegrees) - ERRORTOLERANCE)) {
            return true;
        } else {
            return false;
        }
    }

    public void turnToDegree(double powerLimit, double targetDegrees) {
        final double FINALTOLERANCE = 2;
        //this reads the initial theta value for the gyro to be used later
        final double GYROINITIAL = gyro.getIntegratedZValue();
        double powerMin = 0.22;
        double deltaTheta = targetDegrees - gyro.getIntegratedZValue();
        //note that the gyro.getIntegratedZValue() mention here is continuously read and is NOT the GYROINITIAL value (except initially)
        while ((Math.abs(deltaTheta) > FINALTOLERANCE)  && opmode.opModeIsActive()) {
            opmode.sleep(500);
            double gyroValue = gyro.getIntegratedZValue();
            if (!outsideOfRange(targetDegrees, GYROINITIAL)) {
                if (deltaTheta >= 90) {
                    setTurnPowerAll(powerLimit);
                    opmode.telemetry.addData("left turn, max power", gyroValue);
                } else if ((deltaTheta < 90) && (deltaTheta > 0)) {
                    setTurnPowerAll(((1 - powerMin) / 90) * deltaTheta + powerMin);
                    opmode.telemetry.addData("left turn, low power range", gyroValue);
                } else if ((deltaTheta > -90) && (deltaTheta < 0)) {
                    setTurnPowerAll(((1 - powerMin) / 90) * deltaTheta - powerMin);
                    opmode.telemetry.addData("right turn, low power range", gyroValue);
                } else {
                    setTurnPowerAll(-powerLimit);
                    opmode.telemetry.addData("right turn, max power", gyroValue);
                }
            }
            deltaTheta = targetDegrees - gyro.getIntegratedZValue();
            opmode.telemetry.update();
        }

        /*if (Math.abs(targetDegrees - gyro.getIntegratedZValue()) > TOLERANCE) {
            if (gyro.getIntegratedZValue() < targetDegrees) {
                frontLeft.setPower(-power);
                frontRight.setPower(power);
                backLeft.setPower(-power);
                backRight.setPower(power);
            } else if (gyro.getIntegratedZValue() > targetDegrees) {
                frontLeft.setPower(power);
                frontRight.setPower(-power);
                backLeft.setPower(power);
                backRight.setPower(-power);
            }
        }*/
    }


    /*public void turnDegree(double power, double degrees) {
        // distance in inches
        //conjecture instead of moving 12", wheels will go 12"*cos(45)= 8.5"
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH_STRAIGHT);

        if (power > MAXSPEED) {
            power = MAXSPEED;
        }

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

        while ((frontRight.isBusy() || backLeft.isBusy()) && opmode.opModeIsActive()) {
            opmode.telemetry.addData("frontLeft, current", frontLeft.getCurrentPosition());
            opmode.telemetry.addData("backLeft, current", backLeft.getCurrentPosition());
            opmode.telemetry.addData("frontRight, current", frontRight.getCurrentPosition());
            opmode.telemetry.addData("backRight, current", backRight.getCurrentPosition());
            opmode.telemetry.addData("frontLeft, target", frontLeft.getTargetPosition());
            opmode.telemetry.addData("backLeft, target", backLeft.getTargetPosition());
            opmode.telemetry.addData("frontRight, target", frontRight.getTargetPosition());
            opmode.telemetry.addData("backRight, target", backRight.getTargetPosition());
            opmode.telemetry.update();
        }

        //telemetryDcMotor();

        stopDriving();
        opmode.telemetry.addData("frontLeft", frontLeft.getPower());
        opmode.telemetry.addData("backLeft", backLeft.getPower());
        opmode.telemetry.addData("frontRight", frontRight.getPower());
        opmode.telemetry.addData("backRight", backRight.getPower());

        opmode.telemetry.addData("Gyro end of turn", gyro.getIntegratedZValue());
        opmode.telemetry.update();
    }*/

    /*public void turnDegree(double power, double degrees, int time) {
        turnTimeout = new ElapsedTime();
        final int TURN_TIMEOUT = time;

        // distance in inches
        int ticks = (int) ((2 * 3.14159 / 360) * degrees * ROBOT_RADIUS * TICKS_PER_INCH_STRAIGHT);

        if (power > MAXSPEED) {
            power = MAXSPEED;
        }

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
    }*/

    /*public void turnCorrect (double target) {
         corrects absolute heading to be target in degrees counterclockwise from initial calibration.
         * Caller must know what final heading should be!

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
    }*/

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
