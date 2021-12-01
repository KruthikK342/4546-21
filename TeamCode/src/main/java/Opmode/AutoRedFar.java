package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.Outtake;

@Autonomous(name="AutoRedFar", group="4546")
public class AutoRedFar extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;


    public void highGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(500);
        drivetrain.turnPI(120, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.15, -0.5);
        sleep(500);
        //outake.highGoal();
        sleep(500);

    }


    public void park() {
        drivetrain.moveInches(3., 0.5);
        sleep(800);

        drivetrain.turnPI(-45, 0.25, 0.25, 2000);
        sleep(500);

        drivetrain.moveInches(7, -.8);
        sleep(1000);
        drivetrain.moveInches(25, -.9);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        waitForStart();
        highGoal();
        park();

    }
}