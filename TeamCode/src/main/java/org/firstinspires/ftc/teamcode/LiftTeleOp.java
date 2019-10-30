package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="LiftTeleOp", group="TeamCode")

public class LiftTeleOp extends OpMode {
    DcMotor rightLift, leftLift;
    Servo grabberServo, deliveryServo, rotationServo;

    public void init() {
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap. dcMotor.get ("leftLift");

        grabberServo = hardwareMap.servo.get ("grabberServo");
        deliveryServo = hardwareMap.servo.get ("deliveryServo");
        rotationServo = hardwareMap.servo.get ("rotationServo");

        rightLift.setDirection(DcMotor.Direction.FORWARD);
        leftLift.setDirection(DcMotorSimple.Direction.REVERSE);

        rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


    }

    public void loop() {

    }
}