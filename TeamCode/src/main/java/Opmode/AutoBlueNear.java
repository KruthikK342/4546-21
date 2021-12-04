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

@Autonomous(name="AutoBlueNear", group="4546")
public class AutoBlueNear extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;

    public void carousel() {
        drivetrain.moveInches(5, 0.5); // Move forward to turn
        sleep(500);
        drivetrain.turnPI(90, 0.25, 0.1, 3000); // Turn right 90
        sleep(500);
        drivetrain.moveInches(8, 0.5); // Move forward
        sleep(500);
        drivetrain.turnPI(145, .25, .15, 3000); //Turn towards carousel
        sleep(500);
        drivetrain.moveInches(11, .5); // Approach Carousel
        sleep(500);
        carousel.spin(.3); //Spin Carousel
        sleep(3000);
        carousel.stop();

    }

    public void park() {
        drivetrain.moveInches(5, 0.5);
        sleep(500);
        drivetrain.turnPI(90, 0.25, 0.1, 3000);
        sleep(500);
        drivetrain.moveInches(30, 0.5);
        sleep(500);
        drivetrain.turnPI(0, .25, .1, 3000);
        sleep(500);
        drivetrain.moveInches(10, .5);
        /*
        highGoal();
        drivetrain.moveInches(10, 0.5);
        sleep(500);
        drivetrain.turnPI(-10, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(150, -.8);
        sleep(500);
        */
    }

    public void highGoal() {
        drivetrain.moveInches(11,-.5); // Back up from Carousel;
        sleep(500);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(500);
        drivetrain.moveInches(20, -.5);
        sleep(500);
        drivetrain.turnPI(145, .25, .1, 3000);
        sleep(500);
        drivetrain.moveInches(5,-.5);
        sleep(500);
        outake.highGoal();
    }

    public void midGoal() {
        drivetrain.moveInches(11,-.5); // Back up from Carousel;
        sleep(500);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(500);
        drivetrain.moveInches(20, -.5);
        sleep(500);
        drivetrain.turnPI(155, .25, .1, 3000);
        sleep(500);
        drivetrain.moveInches(4,-.5);
        sleep(500);
        outake.midGoal();
    }

    public void lowGoal() {
        drivetrain.moveInches(11,-.5); // Back up from Carousel;
        sleep(500);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(500);
        drivetrain.moveInches(20, -.5);
        sleep(500);
        drivetrain.turnPI(145, .25, .1, 3000);
        sleep(500);
        drivetrain.moveInches(3,-.5);
        sleep(500);
        outake.lowGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        carousel = new Carousel(this);
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

        if (barcode == 3) {
            carousel();
            highGoal();
            park();
        } else if (barcode == 2) {
            carousel();
            midGoal();
            park();
        } else {
            carousel();
            highGoal();
            park();
        }


    }
}