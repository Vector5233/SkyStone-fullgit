package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="LiftTeleOp", group="TeamCode")

public class LiftTeleOp extends OpMode {   // maybe extend TeleOp?  what are the differences?
    DcMotor rightLift, leftLift;
    //Servo grabberServo, rotationServo;
    //CRServo deliveryServo;

    //final int maxLift = 700;
    //final int minLift = 0;

    public void init() {
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap. dcMotor.get ("leftLift");

        //grabberServo = hardwareMap.servo.get ("grabberServo");
        //deliveryServo = hardwareMap.crservo.get ("deliveryServo");
        //rotationServo = hardwareMap.servo.get ("rotationServo");

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);

        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void loop(){
        setLiftMotors();
        //setGrabberMoters();
    }

    private void setLiftMotors()  {
        // TODO consider carefully what actions could harm the lift and how to avoid doing those things
        //if(minLift <= leftLift.getCurrentPosition() &&leftLift.getCurrentPosition() <= maxLift) {
            rightLift.setPower(gamepad2.left_stick_y / 4);
            leftLift.setPower(gamepad2.left_stick_y / 4);
        // }

    }

    /*private void setGrabberMoters() {
        // TODO all delivery components should be named deliveryXXX NOT grabberXXX

        boolean ifUnpressedRT = true;
        boolean ifUnpressedLT = true;

        deliveryServo.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_bumper == true) {
            grabberServo.setPosition(0);
        }

        if (gamepad2.left_bumper == true) {
            grabberServo.setPosition(0.5);  //TODO fix this value
        }

        if  (gamepad2.right_trigger >= 0.5 && ifUnpressedRT == true) {
            if(rotationServo.getPosition() < 0.1) {
                rotationServo.setPosition(0.5);
                ifUnpressedRT = false;
            }
            else if(0.4 < rotationServo.getPosition() && rotationServo.getPosition() < 0.6) {
                rotationServo.setPosition(1);
                ifUnpressedRT = false;
            }
        }
        else
            ifUnpressedRT = true;

        if (gamepad2.left_trigger >= 0.5 && ifUnpressedLT == true) {
            if(0.9 < rotationServo.getPosition()) {
                rotationServo.setPosition(0.5);
                ifUnpressedLT = false;
            }
            else if(0.4 < rotationServo.getPosition() && rotationServo.getPosition() < 0.6) {
                rotationServo.setPosition(0);
                ifUnpressedLT = false;
            }
        }
        else{
            ifUnpressedLT = true;
        }
    }*/

}
