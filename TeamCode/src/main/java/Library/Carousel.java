package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Carousel {

    private LinearOpMode opMode;
    private DcMotor spinner;
    private Drivetrain drivetrain;


    public Carousel(LinearOpMode opMode) throws InterruptedException {
        this.opMode = opMode;
        spinner = this.opMode.hardwareMap.dcMotor.get("carousel");
        drivetrain = new Drivetrain(opMode);

    }

    public void spin(double power) {
        spinner.setPower(power);
        drivetrain.startMotors(.05,.05);
    }

    public void stop() {
        spinner.setPower(0);
    }
}
