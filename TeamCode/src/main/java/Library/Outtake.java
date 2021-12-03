package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {
    LinearOpMode opMode;
    private DcMotor outake;
    private DcMotor pulley;
    private Servo wrist;

    public Outtake(LinearOpMode opMode) {
        this.opMode = opMode;
        outake = this.opMode.hardwareMap.dcMotor.get("outake");
        pulley = this.opMode.hardwareMap.dcMotor.get("arm");
        wrist = this.opMode.hardwareMap.get(Servo.class, "wrist");
        pulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulley.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pulley.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void highGoal() {
        wrist.setPosition(.1);
        while (pulley.getCurrentPosition() < 2600) {

            pulley.setPower(.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.45);
        opMode.sleep(1000);
        wrist.setPosition(.3);
        opMode.sleep(200);
        wrist.setPosition(.45);
        opMode.sleep(1000);
        wrist.setPosition(0.1);

        opMode.sleep(400);

        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();
        }
        pulley.setPower(0);


    }

    public void midGoal() {


        wrist.setPosition(.1);
        while (pulley.getCurrentPosition() < 2200) {

            pulley.setPower(.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.45);
        opMode.sleep(200);
        wrist.setPosition(.3);
        opMode.sleep(200);
        wrist.setPosition(.45);
        opMode.sleep(1000);
        wrist.setPosition(0.1);

        opMode.sleep(400);

        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();
        }
        pulley.setPower(0);


        /*
        while (pulley.getCurrentPosition() < 500) {
            pulley.setPower(.7);
        }
        wrist.setPosition(1);
        opMode.sleep(1000);
        wrist.setPosition(0);
        opMode.sleep(1000);
        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.7);
        }


         */
    }

    public void lowGoal() {
        wrist.setPosition(.1);
        while (pulley.getCurrentPosition() < 1650) {

            pulley.setPower(.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.45);
        opMode.sleep(200);
        wrist.setPosition(.3);
        opMode.sleep(200);
        wrist.setPosition(.45);
        opMode.sleep(1000);
        wrist.setPosition(0.1);

        opMode.sleep(400);

        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();
        }
        pulley.setPower(0);

    }

}
