package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Teleop", group="4546")
public class Teleop extends OpMode {

    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;

    @Override
    public void init() {
        leftDrive = hardwareMap.dcMotor.get("testing");
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
        double leftPower;
        double rightPower;

        double drive = -gamepad1.left_stick_y;
        double turn  = -gamepad1.right_stick_x;
        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        //change
        //
        gamepad1.right_trigger
    }
}