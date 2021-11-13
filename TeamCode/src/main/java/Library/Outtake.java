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
        wrist =  this.opMode.hardwareMap.servo.get("wrist");
    }

    public void highGoal () {
        while (pulley.getCurrentPosition() < 2400) {
            pulley.setPower(.4);
        }
        wrist.setPosition(.4);
        opMode.sleep(2000);
        wrist.setPosition(0);
        opMode.sleep(2000);
        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.4);
        }
    }

    public void midGoal () {
        while (pulley.getCurrentPosition() < 500) {
            pulley.setPower(.4);
        }
        wrist.setPosition(1);
        opMode.sleep(1000);
        wrist.setPosition(0);
        opMode.sleep(1000);
        while (pulley.getCurrentPosition() > 100) {
            pulley.setPower(-.4);
        }
    }

    public void lowGoal () {
        outake.setPower(1);
    }

}
