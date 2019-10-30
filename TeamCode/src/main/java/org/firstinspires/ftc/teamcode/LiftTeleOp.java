package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="LiftTeleOp", group="TeamCode")

public class LiftTeleOp extends OpMode {
    DcMotor rightLift, leftLift;
    Servo grabberServo, grabberExtender, grabberRotation;

    public void init() {
        rightLift = hardwareMap.dcMotor.get("rightLift");
        leftLift = hardwareMap. dcMotor.get ("leftLift");

        grabberServo = hardwareMap.servo.get ("grabberServo");
        grabberRotation = hardwareMap.servo.get ("grabberRotation");
        grabberExtender = hardwareMap.servo.get ("grabberExtender");


    }

    public void loop() {

    }
}