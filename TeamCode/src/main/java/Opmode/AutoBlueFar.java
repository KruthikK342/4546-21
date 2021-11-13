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
import Library.Outtake;

@Autonomous(name="AutoBlueFar", group="4546")
public class AutoBlueFar extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outtake;

    public void highGoal() {
        drivetrain.moveInches(20, 0.5);
        drivetrain.turnPI(-45, 0.25, 0.25, 2000);
        drivetrain.moveInches(20, 0.5);
        outtake.highGoalServo();
        drivetrain.moveInches(10, -0.5);
        drivetrain.turnPI(-45, 0.25, 0.25, 2000);
        drivetrain.moveInches(80, 1);
    }

    public void park() {
        drivetrain.moveInches(7, 0.5);
        sleep(1000);
        drivetrain.turnPI(-87.5, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(180, 1.0);
        sleep(500);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outtake = new Outtake(this);
        waitForStart();
        park();

    }
}