package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Carousel;
import Library.Outtake;

/* Ports:
0-Intake
1-carousel
2-arm
3-outake
 */

@Autonomous(name="AutoBlueFar", group="4546")
public class AutoBlueFar extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;

    public void midGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);
        // drivetrain.turnPI(-255, 0.05, 0, 2000);
        drivetrain.turnPD(-135, 0.45, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(6.3, -0.45);
        sleep(500);
        outake.midGoal();
        sleep(500);
    }

    public void highGoal() {
        drivetrain.moveInches(4.5, 0.5);
        sleep(800);

        drivetrain.turnPD(-130, 0.45, 0.15, 3000);
        sleep(500);
        drivetrain.moveInches(6.7, -0.45);
        sleep(500);
        outake.highGoal();
        sleep(500);
    }

    public void lowGoal() {
        drivetrain.moveInches(5.5, 0.5);
        sleep(800);

        drivetrain.turnPI(-135, 0.25, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(4, -0.45);
        sleep(500);
        outake.lowGoal();
        sleep(500);
    }

    public void park() {
        drivetrain.moveInches(2.5, 0.5);
        sleep(800);

        drivetrain.turnPI(0, 0.25, 0.1, 3000);
        sleep(500);
        drivetrain.turnPI(80,.25,.1,3000);

        drivetrain.moveInches(7, -.8);
        sleep(1000);
        drivetrain.moveInches(40, -1);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        telemetry.addLine("Robot Initialized");
        telemetry.update();


        while (!isStarted())
        {
            telemetry.addData("xpos: ", vision.getImageWidth());
            telemetry.addData("Team Element Pixel Count: ", vision.getTeamElementPixelCount());
            telemetry.addData("Barcode: ", vision.getBarcode(false));
            telemetry.update();
        }
        waitForStart();

        barcode = vision.getBarcode(false);

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