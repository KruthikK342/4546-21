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
        wrist = this.opMode.hardwareMap.servo.get("wrist");
    }

    public void highGoalServo() {
        pulley.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pulley.setTargetPosition(2);
        pulley.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        pulley.setPower(.1);
        while (pulley.isBusy())
            ;
        pulley.setPower(0);
        wrist.setPosition(0);
        wrist.setPosition(.75);
    }
}
