package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Intake {

    private LinearOpMode opMode;
    private DcMotor intake;
    private Drivetrain drivetrain;

    public Intake(LinearOpMode opMode) {
        this.opMode = opMode;
        intake = this.opMode.hardwareMap.dcMotor.get("intake");
    }

    public void collect() {

        intake.setPower(-1);

    }
    public void stop() {

        intake.setPower(0);
    }

    public void out() {
        intake.setPower(-1);
    }
}
