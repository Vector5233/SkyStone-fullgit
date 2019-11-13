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

        boolean if_pressedGp1A = false;
        boolean if_pressedGp1X = false;
        
        double hooHrzVal = 0;

        public void init() {
            hookHrz = hardwareMap.servo.get("hookHrz");
            hookVrt = hardwareMap.servo.get("hookVrt");
            hookHrz.setPosition(0);
            hookVrt.setPosition(1.0);
            // initialize for the servo test
        }

        public void loop () {
            moveHookHrz();
            moveHookVrt();
            telemetry.addData("hookHrz", hookHrz.getPosition());
            telemetry.addData("hookVrt", hookVrt.getPosition());
        }


        public void moveHookHrz () {
            if(!if_pressedGp1A) {
                if (gamepad1.a && (hookHrz.getPosition() >= 0.7 && hookHrz.getPosition() <= 1)) {
                    hookHrzPos += 0.09;
                    if_pressedGp1A = true;
                } else if (gamepad1.b && (hookHrz.getPosition() >= 0 && hookHrz.getPosition() <= 0.2)) {
                    hookHrzPos -= 0.09;
                    if_pressedGp1A = true;
                }
                hookHrz.setPosition(hookHrzPos);
            }

            else {
                if(!gamepad1.a && !gamepad1.b) {
                    if_pressedGp1A = false;
                }
            }


        }

    public void moveHookVrt () {
        if(!if_pressedGp1X) {
            if (gamepad1.x && (hookVrt.getPosition() >= 0.7 && hookVrt.getPosition() <= 1)) {
                hookVrtPos += 0.02;
                if_pressedGp1X = true;
            } else if (gamepad1.y && (hookVrt.getPosition() >= 0 && hookVrt.getPosition() <= 0.3)) {
                hookVrtPos -= 0.02;
                if_pressedGp1X = true;
            }

            hookHrz.setPosition(hookVrtPos);
        }

        else {
            if(!gamepad1.x && !gamepad1.y) {
                if_pressedGp1X = false;
            }
        }


    }



}

