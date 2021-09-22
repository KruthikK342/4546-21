package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Teleop", group="4546")
public class Teleop extends OpMode {

    private DcMotor forwardMotor;
    //private DcMotor backwardMotor;

    @Override
    public void init() {
        forwardMotor = hardwareMap.dcMotor.get("testing");
        //backwardMotor = hardwareMap.dcMotor.get("backwardMotor");
        //forwardMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //backwardMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        telemetry.addData("init", "completed");
        telemetry.update();
    }

    @Override
    public void loop() {
        //forwardMotor.setPower(gamepad1.left_stick_y - gamepad1.left_stick_x);
        //backwardMotor.setPower(gamepad1.right_stick_y + gamepad1.right_stick_x);
        forwardMotor.setPower(0.2);
    }
}