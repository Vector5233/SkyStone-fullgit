package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="LiftTeleOp", group="TeamCode")

public class LiftTeleOp extends OpMode {
    DcMotor rightLift, leftLift;
    Servo grabberServo, rotationServo;
    CRServo deliveryServo;

    public void init() {
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap. dcMotor.get ("leftLift");

        grabberServo = hardwareMap.servo.get ("grabberServo");
        deliveryServo = hardwareMap.crservo.get ("deliveryServo");
        rotationServo = hardwareMap.servo.get ("rotationServo");

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);

        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void loop(){
        setLiftMotors();
        setGrabberMoters();
    }

    private void setLiftMotors()  {
        rightLift.setPower(gamepad2.left_stick_y);
        leftLift.setPower(gamepad2.left_stick_y);

    }

    private void setGrabberMoters() {
        deliveryServo.setPower(gamepad2.right_stick_y);

        if (gamepad2.right_bumper == true) {
            grabberServo.setPosition(0);
        }

        if (gamepad2.left_bumper == true) {
            grabberServo.setPosition(0.5);
        }

        if  (gamepad2.right_trigger >= 0.5) {
            rotationServo.setPosition();
        }
    }
}
