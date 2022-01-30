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


    public void goal() {
        wrist.setPosition(.1);
        while (pulley.getCurrentPosition() < 2500 && opMode.opModeIsActive()) {

            pulley.setPower(.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            opMode.telemetry.update();

        }
        pulley.setPower(0);
        opMode.sleep(500);
        wrist.setPosition(.475);
        opMode.sleep(1500);
        wrist.setPosition(0.1);

        opMode.sleep(400);

        while (pulley.getCurrentPosition() > 100 && opMode.opModeIsActive()) {
            pulley.setPower(-.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();
        }
        pulley.setPower(0);


    }
    public void highGoal() {
        wrist.setPosition(.2);
        while (pulley.getCurrentPosition() < 1300 && opMode.opModeIsActive()) {

            pulley.setPower(0.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.6); //depositing
        opMode.sleep(500);
        wrist.setPosition(.2); //resting position
        opMode.sleep(500);

        while (pulley.getCurrentPosition() > 50 && opMode.opModeIsActive()) {
            pulley.setPower(-.7);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();
        }
        pulley.setPower(0);


    }

    public void midGoal() {


        wrist.setPosition(.3);
        while (pulley.getCurrentPosition() < 1100 && opMode.opModeIsActive()) {

            pulley.setPower(.5);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.6);
        opMode.sleep(500);
        wrist.setPosition(.05);
        opMode.sleep(500);

        while (pulley.getCurrentPosition() > 50 && opMode.opModeIsActive()) {
            pulley.setPower(-.7);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();
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
        wrist.setPosition(.41);
        while (pulley.getCurrentPosition() < 1100 && opMode.opModeIsActive()) {

            pulley.setPower(1);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();

        }
        pulley.setPower(0);
        wrist.setPosition(.8);
        opMode.sleep(500);
        wrist.setPosition(.1);
        opMode.sleep(500);

        while (pulley.getCurrentPosition() > 50 && opMode.opModeIsActive()) {
            pulley.setPower(-.7);
            opMode.telemetry.addData("pulley position", pulley.getCurrentPosition());
            //opMode.telemetry.update();
        }
        pulley.setPower(0);

    }

}
