package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTestTeleOp", group="TeamCode")

public class ServoTestTeleOp extends OpMode {
    Servo hookHrz, hookVrt, foundationLGrabber, foundationRGrabber;

    final double hookHrzInit = 0;
    final double hookVrtInit = 0.3;

    final double hookHrzReady = 0.5;
    final double hookVrtReady = 0.9;

    //test required
    final int maxLift = 700;
    final int minLift = 0;

    double hookHrzPos = 0;
    double hookVrtPos = 0;

    boolean if_pressedGp1A = false;
    boolean if_pressedGp1X = false;
    boolean if_pressedGp1B = false;

    double hooHrzVal = 0;

    public void init() {
        hookHrz = hardwareMap.servo.get("hookHrz");
        hookVrt = hardwareMap.servo.get("hookVrt");
        hookHrz.setPosition(0);
        hookVrt.setPosition(1.0);
        // initialize for the servo test
    }

    public void loop() {
        operateHookHrz();
        operateHookVrt();
        moveFoundationGrabberPrac();
        telemetry.addData("foundationLGrabber", foundationLGrabber.getPosition());
        telemetry.addData("foundationRGrabber", foundationRGrabber.getPosition());
        telemetry.addData("hookHrz", hookHrz.getPosition());
        telemetry.addData("hookVrt", hookVrt.getPosition());
    }


    public void operateHookHrz() {
        if (!if_pressedGp1A && gamepad1.a) {
            if (hookHrz.getPosition() >= 0.7 && hookHrz.getPosition() <= 1) {
                //hookHrzPos -= 0.09;
                hookHrz.setPosition(0);
                if_pressedGp1A = true;
            } else if (hookHrz.getPosition() >= 0 && hookHrz.getPosition() < 0.3) {
                //hookHrzPos += 0.09;
                hookHrz.setPosition(0.85);
                if_pressedGp1A = true;
            }
            //hookHrz.setPosition(hookHrzPos);
        } else {
            if (!gamepad1.a) {
                if_pressedGp1A = false;
            }
        }


    }

    public void operateHookVrt() {
        if (!if_pressedGp1X) {
            if (gamepad1.x && (hookVrt.getPosition() >= 0.7 && hookVrt.getPosition() <= 1)) {
                hookVrt.setPosition(0);
                if_pressedGp1X = true;
            } else if (gamepad1.x && (hookVrt.getPosition() >= 0 && hookVrt.getPosition() <= 0.3)) {
                hookVrt.setPosition(1);
                if_pressedGp1X = true;
            }

            //hookHrz.setPosition(hookVrtPos);
        } else {
            if (!gamepad1.x) {
                if_pressedGp1X = false;
            }
        }
    }

    public void moveFoundationGrabber() {
        if(!if_pressedGp1B) {
            if (gamepad1.b && (foundationLGrabber.getPosition() >= 0.35 && foundationLGrabber.getPosition() <= 0.55)) {
                foundationLGrabber.setPosition(0);
                foundationRGrabber.setPosition(0);
            }
            else if (gamepad1.b && (foundationLGrabber.getPosition() >= 0 && foundationLGrabber.getPosition() <= 0.25)) {
                foundationLGrabber.setPosition(0.5);
                foundationRGrabber.setPosition(0.5);
            }
        }

        else {
            if (!gamepad1.b) {
                if_pressedGp1B = false;
            }
        }


    }

    public void moveFoundationGrabberPrac() {
        if (gamepad1.b && foundationLGrabber.getPosition() <= 0.55) {
            foundationLGrabber.setPosition(0);
            foundationRGrabber.setPosition(0);
        }
        else if (gamepad1.b && foundationLGrabber.getPosition() <= 0.25) {
            foundationLGrabber.setPosition(0.7);
            foundationRGrabber.setPosition(0.7);
        }
    }
}

