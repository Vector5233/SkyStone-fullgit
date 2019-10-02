package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ChassisTeleOp", group="TeamCode")

public class ChassisTeleOp extends OpMode {
    DcMotor frontRight, frontLeft, backRight, backLeft, rollerRight, rollerLeft;
    Servo hookRightX, hookRightY, hookLeftX, hookLeftY;

    public void init() {
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        rollerRight = hardwareMap.dcMotor.get("rollerRight");
        rollerLeft = hardwareMap.dcMotor.get("rollerLeft");

        hookRightX = hardwareMap.servo.get("hookRightX");
        hookRightY = hardwareMap.servo.get("hookRightY");
        hookLeftX = hardwareMap.servo.get("hookLeftX");
        hookLeftY = hardwareMap.servo.get("hookLeftY");

        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void loop() {
        setDriveMotors();
    }

    private void setDriveMotors() {
        if (gamepad1.right_stick_y - gamepad1.right_stick_x > 1) {
            frontLeft.setPower(1 - gamepad1.left_stick_x / 2);
            backRight.setPower(1 + gamepad1.left_stick_x / 2);
        } else {
            frontLeft.setPower((gamepad1.right_stick_y - gamepad1.right_stick_x) - gamepad1.left_stick_x / 2);
            backRight.setPower((gamepad1.right_stick_y - gamepad1.right_stick_x) + gamepad1.left_stick_x / 2);
        }
        if (gamepad1.right_stick_y + gamepad1.right_stick_x > 1) {
            frontRight.setPower(1 + gamepad1.left_stick_x / 2);
            backLeft.setPower(1 - gamepad1.left_stick_x / 2);
        } else {
            frontRight.setPower((gamepad1.right_stick_y + gamepad1.right_stick_x) + gamepad1.left_stick_x / 2);
            backLeft.setPower((gamepad1.right_stick_y + gamepad1.right_stick_x) - gamepad1.left_stick_x / 2);
        }
    }
}