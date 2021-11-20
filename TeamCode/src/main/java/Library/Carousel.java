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

    public void spin() {
<<<<<<< HEAD
        spinner.setPower(-0.4);
=======
        spinner.setPower(-0.5);
>>>>>>> 96a9dc367a246527877252203782113971eba51b
    }

    public void stop() {
        spinner.setPower(0);
    }
}
