package org.firstinspires.ftc.teamcode;
import android.app.Notification;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Timer;

@TeleOp(name="SkyStoneTeleOp", group="TeamCode")

public class SkyStoneTeleOp extends OpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rightRoller, leftRoller, rightLift, leftLift;
    Servo hookHrz, hookVrt, deliveryGrabber, deliveryRotation, leftFoundation, blockSweeper, capServo, cameraServo; //, rightFoundation;
    CRServo deliveryExtender;

    final double rollerPower = 1.0;

    //test required
    final int maxLift = 700;
    final int minLift = 0;

    final String BACK = "BACK";
    final String FORWARD = "FORWARD";

    final String rotationNotIn = "RotationNotIn";
    final String rotationIn = "rotationIn";
    final String rotationMovingIn = "rotationMovingIm";
    final String rotationMovingOut = "rotationMovingOut";
    final String extenderIn = "extenderIn";
    final String extenderMovingOut = "extenderMovingOut";
    final String extenderMovingIn = "extenderMovingIn";
    final String extenderOut = "extenderOut";

    final String grabberOpen = "grabberOpen";
    final String grabberClose = "grabberClose";
    final String grabberClosing = "grabberClosing";
    final String grabberOpening = "grabberOpening";

    final String liftUp = "liftUp";
    final String liftDown = "liftDown";


    boolean ifUnpressedRT = true;
    boolean ifUnpressedLT = true;

    boolean if_pressedGp1X = false;
    boolean if_pressedGp1Y = false;
    boolean if_pressedGp2A = false;
    boolean if_pressedGp1B = false;

    boolean if_pressedRB = false;
    boolean if_pressedLB = false;

    String GrabberState = null;
    String LiftGrabberState = null;
    String RotationState = null;
    String ExtenderState = null;

    ElapsedTime grabberTime = new ElapsedTime();
    final int GRABBERTIMEOUT = 400;
    ElapsedTime rotationTime = new ElapsedTime();
    final int ROTATIONTIMEOUT = 500;
    ElapsedTime extenderTime = new ElapsedTime();
    final int EXTENDERIMEOUT = 500;

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

        capServo = hardwareMap.servo.get("capServo");

        cameraServo = hardwareMap.servo.get("cameraServo");

        deliveryExtender = hardwareMap.crservo.get("deliveryExtender");

        frontRight.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
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

        GrabberState = grabberClosing;

        deliveryRotation.setPosition(0);

        blockSweeper.setPosition(1);

        hookVrt.setPosition(0.7);
        hookHrz.setPosition(0);

        leftFoundation.setPosition(0.1);
        // set all states here
    }

    public void loop() {
        setDriveMotors();
        setRollerMotors();
        setLiftMotors();
        setDeliveryMotors();
        setDeliveryGrabber();
        setFoundationGrabber();
        setHook();
        setBlockSweeper();
        setCapServo();
        //setCameraServo();
        telemetry.addData("hookHrz", hookHrz.getPosition());
        telemetry.addData("hookVrt", hookVrt.getPosition());
        telemetry.addData("blockSweeper", blockSweeper.getPosition());
    }

    private void setDriveMotors() {
        frontRight.setPower(-gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
        frontLeft.setPower(gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
        backRight.setPower(gamepad1.right_stick_x - gamepad1.right_stick_y - gamepad1.left_stick_x);
        backLeft.setPower(-gamepad1.right_stick_x - gamepad1.right_stick_y + gamepad1.left_stick_x);
    }

    private void setRollerMotors() {
        if (gamepad1.right_bumper == true) {
            rightRoller.setPower(rollerPower);
            leftRoller.setPower(rollerPower);
        } else if (gamepad1.left_bumper == true) {
            rightRoller.setPower(-rollerPower);
            leftRoller.setPower(-rollerPower);
        } else {
            rightRoller.setPower(0);
            leftRoller.setPower(0);
        }
    }

    private void setLiftMotors() {
        final double UPPOWER = 0.8;
        final double DOWNPOWER = 0.3;
        final double THRESHOLD = 0.5;
        // TODO consider carefully what actions could harm the lift and how to avoid doing those things
        //if(minLift <= leftLift.getCurrentPosition() &&leftLift.getCurrentPosition() <= maxLift) {
        if (gamepad2.left_stick_y > THRESHOLD) {
            if (GrabberState == grabberClose) {
                rightLift.setPower(gamepad2.left_stick_y * DOWNPOWER);
                leftLift.setPower(gamepad2.left_stick_y * DOWNPOWER);
            }
            else {
                GrabberState = grabberClosing;
            }
        } else if (gamepad2.left_stick_y < -THRESHOLD) {
            if (GrabberState == grabberClose) {
                rightLift.setPower(gamepad2.left_stick_y * UPPOWER);
                leftLift.setPower(gamepad2.left_stick_y * UPPOWER);
            }
            else {
                GrabberState = grabberClose;
            }

        } else {
            rightLift.setPower(0);
            leftLift.setPower(0);
        }
        // }

    }

    private void setDeliveryMotors() {
        deliveryExtender.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_bumper == true) {
            deliveryGrabber.setPosition(.35);
        }

        if (gamepad2.left_bumper == true) {
            deliveryGrabber.setPosition(.435);
        }

        if  (gamepad2.right_trigger >= 0.5 && ifUnpressedRT) {
            if(deliveryRotation.getPosition() < 0.25) {
                deliveryRotation.setPosition(0.5);
                telemetry.addData("Grabber", deliveryRotation.getPosition());
                ifUnpressedRT = false;
            } else if (0.25 < deliveryRotation.getPosition() && deliveryRotation.getPosition() < 0.75) {
                deliveryRotation.setPosition(1);
                telemetry.addData("Grabber", deliveryRotation.getPosition());
                ifUnpressedRT = false;
            }
        } else
            ifUnpressedRT = true;

        if (gamepad2.left_trigger >= 0.5 && ifUnpressedLT) {
            if (0.75 < deliveryRotation.getPosition()) {
                deliveryRotation.setPosition(0.5);
                ifUnpressedLT = false;
            } else if (0.25 < deliveryRotation.getPosition() && deliveryRotation.getPosition() < 0.75) {
                deliveryRotation.setPosition(0);
                ifUnpressedLT = false;
            }
        } else {
            ifUnpressedLT = true;
        }
    }

    public void setHook() {
        if (!if_pressedGp1X && gamepad1.x) {
            if (hookHrz.getPosition() >= 0.5 && hookHrz.getPosition() <= 1) {
                hookHrz.setPosition(0);
                if_pressedGp1X = true;
            } else if (hookHrz.getPosition() >= 0 && hookHrz.getPosition() < 0.5) {
                hookHrz.setPosition(1);
                if_pressedGp1X = true;
            }
        } else {
            if (!gamepad1.x) {
                if_pressedGp1X = false;
            }
        }

        if (!if_pressedGp1Y) {
            if (gamepad1.y && (hookVrt.getPosition() >= 0.75 && hookVrt.getPosition() <= 1)) {
                hookVrt.setPosition(0.4);
                if_pressedGp1Y = true;
            } else if (gamepad1.y && (hookVrt.getPosition() >= 0 && hookVrt.getPosition() <= 0.75)) {
                hookVrt.setPosition(0.9);
                if_pressedGp1Y = true;
            }
        } else {
            if (!gamepad1.y) {
                if_pressedGp1Y = false;
            }
        }
    }

    public void setFoundationGrabber() {
        if (gamepad1.b && !if_pressedGp1B) {
            if (leftFoundation.getPosition() >= 0.5) {
                leftFoundation.setPosition(0);
                if_pressedGp1B = true;
            } else if (leftFoundation.getPosition() <= 0.5) {
                leftFoundation.setPosition(0.5);
                if_pressedGp1B = true;
            }
        } else {
            if (!gamepad1.b) {
                if_pressedGp1B = false;
            }
        }
    }

    public void setBlockSweeper() {
        if (gamepad1.a) {
            blockSweeper.setPosition(0.725);
        } else {
            blockSweeper.setPosition(1);
        }
    }

    public void setCapServo() {
        if (gamepad2.a) {
            capServo.setPosition(0);
        } else {
            capServo.setPosition(.8);
        }
    }

    public void setDeliveryGrabber() {
        switch (GrabberState) {
            case grabberOpen:
                //deliveryGrabber.setPosition(0.35);
                grabberTime.reset();
                if (gamepad2.right_bumper) {
                    if (!if_pressedRB) {
                        GrabberState = grabberClosing;
                        if_pressedRB = true;
                    }
                }
                else {
                    if_pressedRB = false;
                }
                break;

            case grabberClose:
                //deliveryGrabber.setPosition(0.435);
                grabberTime.reset();
                if (gamepad2.left_bumper) {
                    if(!if_pressedLB) {
                        GrabberState = grabberOpening;
                        if_pressedLB = true;
                    }

                }
                else {
                    if_pressedLB = false;
                }
                break;

            case grabberOpening:
                deliveryGrabber.setPosition(0.35);
                if (grabberTime.milliseconds() >= GRABBERTIMEOUT) {
                    GrabberState = grabberOpen;
                }
                break;
            case grabberClosing:
            default:
                deliveryGrabber.setPosition(0.435);
                if (grabberTime.milliseconds() >= GRABBERTIMEOUT) {
                    GrabberState = grabberClose;
                }
                break;

        }

    }

    /*public void setCameraServo () {
        if (gamepad2.x){
            cameraServo.setPosition(0);
        } else if (gamepad2.y){
            cameraServo.setPosition(1);
        }
    }

    public void automatingDelivery () {

    }
*/
    public void setLayerLift() {
        // need to measure the height of block and double-check with Jayden
        if (gamepad2.dpad_up) {
            leftLift.setPower(1);
            rightLift.setPower(1);

            if (leftLift.getCurrentPosition() == 1) {
                //max 767.2
                leftLift.setPower(0);
                rightLift.setPower(0);
            }
        } else if (gamepad2.dpad_down) {
            leftLift.setPower(-1);
            rightLift.setPower(-1);
        }
    }

  /*  public void setDeliveryExtender() {
        switch (ExtenderState) {
            case extenderOut:
                // There is no way to code the position of continuous servo...
                extenderTime.reset();
                if (gamepad2.right_stick_y >= 0.5 && EXTENDERIMEOUT >= maxExtenderTime) {
                    if (rotationIn){
                        ExtenderState = extenderMovingIn;
                    }
                    else {
                        rotationState = rotationMovingIn;
                    }

                }

                break;
            case extenderIn:
                extenderTime.reset();
                if (gamepad2.right_stick_y <= -0.5 && EXTENDERIMEOUT >= maxExtenderTime) {
                    if (rotationIn){
                        ExtenderState = extenderMovingOut;
                    }
                    else {
                        rotationState = rotationMovingIn;
                    }

                }

                break;

            case extenderMovingIn:
                deliveryExtender.setPower(1);
                if (extenderTime.milliseconds() >= EXTENDERIMEOUT) {
                    ExtenderState = extenderIn;
                }
                break;
            case extenderMovingOut:
                deliveryExtender.setPower(-1);
                if (extenderTime.milliseconds() >= EXTENDERIMEOUT) {
                    ExtenderState = extenderOut;
                }
                break;

        }
    }

    public void setDeliveryRotation() {
        switch (RotationState) {
            case rotationIn:
                if (ExtenderState == extenderIn) {

            }
        }
    }

   */
}