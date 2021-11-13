package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.Outtake;
import Library.DuckBarcodeBitmap;

@Autonomous(name="AutoRedNear", group="4546")
public class AutoRedNear extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;

    public void carousel() {
        drivetrain.moveInches(8, 0.5);
        sleep(500);
        drivetrain.turnPI(-98, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(60, 0.4);
        sleep(500);
        carousel.spin();
        sleep(4000);
        carousel.stop();
    }


    public void park() {
        highGoal();
        drivetrain.moveInches(3, 0.5);
        sleep(500);
        /*CHECK ANGLE!!!*/ drivetrain.turnPI(10, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(250, -.8);
        sleep(500);
    }

    public void highGoal() {
        drivetrain.moveInches(20, -.5);
        sleep(500);
        outake.highGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        carousel = new Carousel(this);
        outake = new Outtake(this);
        waitForStart();
        carousel();
        park();


    }
}