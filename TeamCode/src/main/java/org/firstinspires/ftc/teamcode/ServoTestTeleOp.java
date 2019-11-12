package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTestTeleOp", group="TeamCode")

public class ServoTestTeleOp extends OpMode {

        DcMotor frontRight, frontLeft, backRight, backLeft, rightRoller, leftRoller, rightLift, leftLift;
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

        double hookHrzPos = 0;
        double hookVrtPos = 0;

        boolean if_pressedLT = false;

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

            deliveryExtender = hardwareMap.crservo.get("deliveryExtender");

            frontRight.setDirection(DcMotor.Direction.FORWARD);
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
            backRight.setDirection(DcMotor.Direction.FORWARD);
            backLeft.setDirection(DcMotor.Direction.REVERSE);

            rightRoller.setDirection(DcMotor.Direction.REVERSE);
            leftRoller.setDirection(DcMotor.Direction.FORWARD);

            rightLift.setDirection(DcMotor.Direction.REVERSE);
            leftLift.setDirection(DcMotor.Direction.REVERSE);

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            rightRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftRoller.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            rightLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            leftLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            hookHrz.setPosition(0.5);
            hookVrt.setPosition(0.5);
        }

        public void loop () {
            movehookHrz();
            movehookVrt();
            telemetry.addData("hookHrz", hookHrz.getPosition());
            telemetry.addData("hookVrt", hookVrt.getPosition());
        }


        public void movehookHrz () {
            if(!if_pressedLT) {
                if (gamepad1.a && hookHrz.getPosition() <= 1) {
                    hookHrzPos += 0.02;
                    if_pressedLT = true;
                } else if (gamepad1.b && hookHrz.getPosition() >= -1) {
                    hookHrzPos -= 0.02;
                    if_pressedLT = true;
                }

                hookHrz.setPosition(hookHrzPos);
            }

            else {
                if(!gamepad1.a && !gamepad1.b) {
                    if_pressedLT = false;
                }
            }


        }

    public void movehookVrt () {
        if(!if_pressedLT) {
            if (gamepad1.x && hookHrz.getPosition() <= 1) {
                hookVrtPos += 0.02;
                if_pressedLT = true;
            } else if (gamepad1.y && hookHrz.getPosition() >= -1) {
                hookVrtPos -= 0.02;
                if_pressedLT = true;
            }

            hookHrz.setPosition(hookVrtPos);
        }

        else {
            if(!gamepad1.x && !gamepad1.y) {
                if_pressedLT = false;
            }
        }


    }



}

