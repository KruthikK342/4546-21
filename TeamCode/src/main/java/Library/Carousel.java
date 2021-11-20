package Library;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Carousel {

    private LinearOpMode opMode;
    private DcMotor spinner;

    public Carousel(LinearOpMode opMode) {
        this.opMode = opMode;
        spinner = this.opMode.hardwareMap.dcMotor.get("carousel");
    }

    public void spin() { spinner.setPower(-0.4); }

    public void stop() {
        spinner.setPower(0);
    }
}
