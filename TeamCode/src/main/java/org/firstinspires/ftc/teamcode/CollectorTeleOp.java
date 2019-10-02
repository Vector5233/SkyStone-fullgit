package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="CollectorTeleOp", group="TeamCode")

public class CollectorTeleOp extends OpMode {
    DcMotor rollerRight, rollerLeft;
    Servo hookRightX, hookRightY, hookLeftX, hookLeftY;

    final double rollerPower = 0.7;

    final double hookXinit = 0;
    final double hookYinit = 0.3;

    final double hookXready = 0.5;
    final double hookYready = 0.9;

    final double hookXrun = 0.7;
    final double hookYrun = 1;

    public void init() {
        rollerRight = hardwareMap.dcMotor.get("rollerRight");
        rollerLeft = hardwareMap.dcMotor.get("rollerLeft");

        hookRightX = hardwareMap.servo.get("hookRightX");
        hookRightY = hardwareMap.servo.get("hookRightY");
        hookLeftX = hardwareMap.servo.get("hookLeftX");
        hookLeftY = hardwareMap.servo.get("hookLeftY");

        rollerRight.setDirection(DcMotor.Direction.FORWARD);
        rollerLeft.setDirection(DcMotor.Direction.FORWARD);

        rollerRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rollerLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hookLeftX.setPosition(hookXinit);
        hookLeftY.setPosition(hookYinit);
    }

    public void loop() {
        setRollerMotors();
        setHookMotors();
    }

    private void setRollerMotors(){
        if(gamepad2.right_bumper == true){
            rollerRight.setPower(rollerPower);
            rollerLeft.setPower(-rollerPower);
        } else if(gamepad2.left_bumper == true){
            rollerRight.setPower(-rollerPower);
            rollerLeft.setPower(rollerPower);
        } else{
            rollerRight.setPower(0);
            rollerLeft.setPower(0);
        }
    }
    private void setHookMotors(){
        if(gamepad2.x == true){
            hookLeftX.setPosition(hookXready);
            hookLeftY.setPosition(hookYready);
        } else if(gamepad2.y == true){
            hookLeftX.setPosition(hookXinit);
            hookLeftY.setPosition(hookYinit);
        }
        if(gamepad2.a == true){
            hookLeftX.setPosition(hookXrun);
            hookLeftY.setPosition(hookYrun);
        } else if(gamepad2.b == true){
            hookLeftX.setPosition(hookXready);
            hookLeftY.setPosition(hookYready);
        }
    }
}