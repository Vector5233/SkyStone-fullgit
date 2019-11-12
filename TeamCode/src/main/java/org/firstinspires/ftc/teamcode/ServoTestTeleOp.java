package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ServoTestTeleOp", group="TeamCode")

public class ServoTestTeleOp extends OpMode {
        Servo hookHrz, hookVrt;

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
            hookHrz = hardwareMap.servo.get("hookHrz");
            hookVrt = hardwareMap.servo.get("hookVrt");
            hookHrz.setPosition(0.5);
            hookVrt.setPosition(0.5);
            // initialize for the servo test
        }

        public void loop () {
            moveHookHrz();
            moveHookVrt();
            telemetry.addData("hookHrz", hookHrz.getPosition());
            telemetry.addData("hookVrt", hookVrt.getPosition());
        }


        public void moveHookHrz () {
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

    public void moveHookVrt () {
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

