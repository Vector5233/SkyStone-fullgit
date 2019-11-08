package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="SkyStoneTeleOp", group="TeamCode")

public class SkyStoneTeleOp extends OpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft, rightLift, leftLift;
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation;
    CRServo deliveryExtender;

    final double rollerPower = 0.7;

    final double hookHrzInit = 0;
    final double hookVrtInit = 0.3;

    final double hookHrzReady = 0.5;
    final double hookVrtReady = 0.9;

    //test required
    final int maxLift = 700;
    final int minLift = 0;

    double hookHrzRun = 0;
    double hookVrtRun = 0;

    public void init() {
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        rollerRight = hardwareMap.dcMotor.get("rollerRight");
        rollerLeft = hardwareMap.dcMotor.get("rollerLeft");

        hookHrz = hardwareMap.servo.get("hookRightX");
        hookVrt = hardwareMap.servo.get("hookRightY");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void loop() {
        setDriveMotors();
        setRollerMotors();
        setHookMotors();
        setLiftMotors();
        setGrabberMotors();
    }

    private void setDriveMotors() {
        frontRight.setPower(gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
        frontLeft.setPower(- gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
        backRight.setPower(- gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
        backLeft.setPower(gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
    }
    
    private void setRollerMotors(){
        if(gamepad2.right_bumper == true){
            rollerRight.setPower(rollerPower);
            rollerLeft.setPower(-rollerPower);
        } else if(gamepad2.left_bumper == true){
            rollerRight.setPower(-rollerPower);
            rollerLeft.setPower(rollerPower);
        } else{
            rollerRight.setPower(0);
            rollerLeft.setPower(0);
        }
    }
    
    private void setHookMotors(){
        if(gamepad2.x == true){
            hookHrz.setPosition(hookHrzReady);
            hookVrt.setPosition(hookVrtReady);
        } else if(gamepad2.y == true){
            hookHrz.setPosition(hookHrzInit);
            hookVrt.setPosition(hookVrtInit);
        }
    }

    private void setLiftMotors()  {
        // TODO consider carefully what actions could harm the lift and how to avoid doing those things
        //if(minLift <= leftLift.getCurrentPosition() &&leftLift.getCurrentPosition() <= maxLift) {
        rightLift.setPower(gamepad2.left_stick_y / 4);
        leftLift.setPower(gamepad2.left_stick_y / 4);
        // }

    }

    private void setGrabberMotors() {
        boolean ifUnpressedRT = true;
        boolean ifUnpressedLT = true;

        deliveryExtender.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_bumper == true) {
            deliveryGrabber.setPosition(0);
        }

        if (gamepad2.left_bumper == true) {
            deliveryGrabber.setPosition(0.5);
        }

        if  (gamepad2.right_trigger >= 0.5 && ifUnpressedRT == true) {
            if(deliveryRotation.getPosition() < 0.1) {
                deliveryRotation.setPosition(0.5);
                ifUnpressedRT = false;
            }
            else if(0.4 < deliveryRotation.getPosition() && deliveryRotation.getPosition() < 0.6) {
                deliveryRotation.setPosition(1);
                ifUnpressedRT = false;
            }
        }
        else
            ifUnpressedRT = true;

        if (gamepad2.left_trigger >= 0.5 && ifUnpressedLT == true) {
            if(0.9 < deliveryRotation.getPosition()) {
                deliveryRotation.setPosition(0.5);
                ifUnpressedLT = false;
            }
            else if(0.4 < deliveryRotation.getPosition() && deliveryRotation.getPosition() < 0.6) {
                deliveryRotation.setPosition(0);
                ifUnpressedLT = false;
            }
        }
        else{
            ifUnpressedLT = true;
        }
    }
}