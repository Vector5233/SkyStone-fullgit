package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="CollectorTeleOp", group="TeamCode")

public class CollectorTeleOp extends OpMode {
    DcMotor rollerRight, rollerLeft;
    Servo hookX, hookY;

    final double rollerPower = 0.7;

    final double hookXinit = 0;
    final double hookYinit = 0.3;

    final double hookXready = 0.5;
    final double hookYready = 0.9;

    double hookXrun = 0;
    double hookYrun = 0;

    public void init() {
        rollerRight = hardwareMap.dcMotor.get("rollerRight");
        rollerLeft = hardwareMap.dcMotor.get("rollerLeft");

        hookX = hardwareMap.servo.get("hookRightX");
        hookY = hardwareMap.servo.get("hookRightY");

        rollerRight.setDirection(DcMotor.Direction.FORWARD);
        rollerLeft.setDirection(DcMotor.Direction.FORWARD);

        rollerRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rollerLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        hookX.setPosition(hookXinit);
        hookY.setPosition(hookYinit);
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
            hookX.setPosition(hookXready);
            hookY.setPosition(hookYready);
        } else if(gamepad2.y == true){
            hookX.setPosition(hookXinit);
            hookY.setPosition(hookYinit);
        }
    }
}