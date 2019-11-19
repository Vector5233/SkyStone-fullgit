package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="SkyStoneTeleOp", group="TeamCode")

public class SkyStoneTeleOp extends OpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rightRoller, leftRoller, rightLift, leftLift;
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation, leftFoundation, blockSweeper; //, rightFoundation;
    CRServo deliveryExtender;

    final double rollerPower = 1.0;

    final double hookHrzInit = 0;
    final double hookVrtInit = 0.3;

    final double hookHrzReady = 0.5;
    final double hookVrtReady = 0.9;

    //test required
    final int maxLift = 700;
    final int minLift = 0;

    double grabberVal = 0;

    boolean ifUnpressedRT = true;
    boolean ifUnpressedLT = true;

    boolean if_pressedGp1A = false;
    boolean if_pressedGp1X = false;
    boolean if_pressedGp1Y = false;

    public void init() {
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        rightRoller = hardwareMap.dcMotor.get("rightRoller");
        leftRoller = hardwareMap.dcMotor.get("leftRoller");
        
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap.dcMotor.get("leftLift");

        hookHrz = hardwareMap.servo.get("hookHrz");
        hookVrt = hardwareMap.servo.get("hookVrt");

        deliveryGrabber = hardwareMap.servo.get("deliveryGrabber");

        deliveryRotation = hardwareMap.servo.get("deliveryRotation");

        leftFoundation = hardwareMap.servo.get("leftFoundation");

        blockSweeper = hardwareMap.servo.get("blockSweeper");

        deliveryExtender = hardwareMap.crservo.get("deliveryExtender");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        rightRoller.setDirection(DcMotor.Direction.REVERSE);
        leftRoller.setDirection(DcMotor.Direction.FORWARD);

        rightLift.setDirection(DcMotor.Direction.REVERSE);
        leftLift.setDirection(DcMotor.Direction.REVERSE);

        deliveryExtender.setDirection(CRServo.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        blockSweeper.setPosition(0.95);


    }

    public void loop() {
        setDriveMotors();
        setRollerMotors();
        setLiftMotors();
        setDeliveryServos();
        setFoundationGrabber();
        setHookServos();
        setBlockSweeper();
    }

    private void setDriveMotors() {
        frontRight.setPower(- gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
        frontLeft.setPower( gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
        backRight.setPower(gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
        backLeft.setPower(- gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
    }
    
    private void setRollerMotors(){
        if(gamepad1.right_bumper == true){
            rightRoller.setPower(rollerPower);
            leftRoller.setPower(rollerPower);
        } else if(gamepad1.left_bumper == true){
            rightRoller.setPower(-rollerPower);
            leftRoller.setPower(-rollerPower);
        } else{
            rightRoller.setPower(0);
            leftRoller.setPower(0);
        }
    }

    private void setLiftMotors()  {
        // TODO consider carefully what actions could harm the lift and how to avoid doing those things
        //if(minLift <= leftLift.getCurrentPosition() &&leftLift.getCurrentPosition() <= maxLift) {
        if(gamepad2.left_stick_y > 0.5) {
            rightLift.setPower(gamepad2.left_stick_y * 0.1);
            leftLift.setPower(gamepad2.left_stick_y * 0.1);
        }
        else if(gamepad2.left_stick_y < -0.5    ) {
            rightLift.setPower(gamepad2.left_stick_y * 0.8);
            leftLift.setPower(gamepad2.left_stick_y * 0.8);
        }
        else{
            rightLift.setPower(0);
            leftLift.setPower(0);
        }
        // }

    }

    private void setDeliveryServos() {
        deliveryExtender.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_bumper == true) {
            deliveryGrabber.setPosition(0.72  );
        }

        if (gamepad2.left_bumper == true) {
            deliveryGrabber.setPosition(0.65);
        }

        if (gamepad2.right_trigger >= 0.5){
            deliveryRotation.setPosition(1);
        } else if (gamepad2.left_trigger >= 0.5){
            deliveryRotation.setPosition(0);
        }
/*
        if  (gamepad2.right_trigger >= 0.5 && ifUnpressedRT) {
            if(deliveryRotation.getPosition() < 0.1) {
                deliveryRotation.setPosition(0.5);
                telemetry.addData("Grabber", deliveryRotation.getPosition());
                ifUnpressedRT = false;
            }
            else if(0.4 < deliveryRotation.getPosition() && deliveryRotation.getPosition() < 0.6) {
                deliveryRotation.setPosition(1);
                telemetry.addData("Grabber", deliveryRotation.getPosition());
                ifUnpressedRT = false;
            }
        }
        else
            ifUnpressedRT = true;

        if (gamepad2.left_trigger >= 0.5 && ifUnpressedLT) {
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
        }*/
    }

    public void setHookServos() {
        if(!if_pressedGp1A && gamepad1.y) {
            if (hookHrz.getPosition() >= 0.7 && hookHrz.getPosition() <= 1) {
                //hookHrzPos -= 0.09;
                hookHrz.setPosition(0.2);
                if_pressedGp1A = true;
            } else if (hookHrz.getPosition() >= 0 && hookHrz.getPosition() < 0.3) {
                //hookHrzPos += 0.09;
                hookHrz.setPosition(0.6);
                if_pressedGp1A = true;
            }
            //hookHrz.setPosition(hookHrzPos);
        }

        else {
            if(!gamepad1.y) {
                if_pressedGp1A = false;
            }
        }

        if(!if_pressedGp1X) {
            if (gamepad1.x && (hookVrt.getPosition() >= 0.5 && hookVrt.getPosition() <= 1)) {
                hookVrt.setPosition(0.3);
                if_pressedGp1X = true;
            } else if (gamepad1.x && (hookVrt.getPosition() >= 0 && hookVrt.getPosition() <= 0.5)) {
                hookVrt.setPosition(0.8);
                if_pressedGp1X = true;
            }

            //hookHrz.setPosition(hookVrtPos);
        }

        else {
            if(!gamepad1.x) {
                if_pressedGp1X = false;
            }
        }
    }

    public void setFoundationGrabber() {
        if (gamepad1.a) {
            leftFoundation.setPosition(0.05);
            //rightFoundation.setPosition(0.05);
        }
        else if (gamepad1.b) {
            leftFoundation.setPosition(0.6);
            //rightFoundation.setPosition(0.2);

        }
    }
    public void setBlockSweeper() {
        if (gamepad2.x){
            blockSweeper.setPosition(0.95);
        }
        else if (gamepad2.y){
            blockSweeper.setPosition(0.25);
        }
    }
}
