package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.DuckBarcodeBitmap;

@Autonomous(name="AutoBlueNear", group="4546")
public class AutoBlueNear extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;

    public void carousel() {
        drivetrain.moveInches(7, 0.5);
        sleep(500);
        drivetrain.turnPI(98, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(50, 0.4);
        sleep(500);
        carousel.spin();
        sleep(3000);
        carousel.stop();
    }

    public void park() {
        drivetrain.moveInches(5, -0.5);
        sleep(500);
        drivetrain.turnPI(-190, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(250, .8);
        sleep(500);

    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        carousel = new Carousel(this);
        waitForStart();
        carousel();
        park();

    }
}