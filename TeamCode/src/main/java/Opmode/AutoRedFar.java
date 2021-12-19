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
    private int barcode;



    public void park() {
        drivetrain.moveInches(2.5, 0.5);
        sleep(800);

        drivetrain.turnPD(-3, 0.30, 0.25, 2000);
        sleep(500);
        drivetrain.turnPD(-113,.25,.25,2000);

        drivetrain.moveInches(7, -.8);
        sleep(1000);
        drivetrain.moveInches(40, -1);
    }

    public void midGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);
        // drivetrain.turnPI(-255, 0.05, 0, 2000);
        drivetrain.turnPD(145, 0.45, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.3, -0.45);
        sleep(500);
        outake.midGoal();
        sleep(500);
    }

    public void highGoal() {
        drivetrain.moveInches(4.5, 0.5);
        sleep(800);

        drivetrain.turnPD(140, 0.45, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.7, -0.45);
        sleep(500);
        outake.highGoal();
        sleep(500);

    }

    public void lowGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);

        drivetrain.turnPD(145, 0.45, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.1, -0.45);
        sleep(500);
        outake.lowGoal();
        sleep(500);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        while(!isStarted()) {
            telemetry.addData("Barcode: ", vision.getBarcode(true));
            telemetry.update();
        }
        waitForStart();
        /*
           Get the barcode for vision so we can determine what level to put the
           freight on. The boolean variable in getBarcode() is used to differentiate
           between the red and blue side. After that, we use the barcode to execute
           the corresponding ___Goal() function and then park.
        */
        barcode = vision.getBarcode(true);
        if (barcode == 1) {
            lowGoal();
        } else if (barcode == 3) {
            highGoal();
        } else {
            midGoal();
        }
        park();
    }
}