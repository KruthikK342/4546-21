package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

import Library.Carousel;
import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
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

        drivetrain.moveInches(3, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(90, 0.8, 0, 3000); // Turn right 90
        sleep(300);
        drivetrain.moveInches(40, 0.5); // Move forward
        sleep(450);
        drivetrain.turnPD(155, .8, 0, 3000);
        sleep(300);
        drivetrain.moveInches(23, .5);
        sleep(200);
        //start carousel spin

        carousel.spin(.5);
        sleep(3000);
        carousel.stop();

        //intake code

        intake.collect();
        sleep(300);
        drivetrain.moveInches(3,.5);
        sleep(400);
        drivetrain.turnPI(190, 1, .1, 3000);
        sleep(250);
        drivetrain.moveInches(3,.5);
        sleep(300);
        //drivetrain.turnPI(180, 1, .1, 3000);
        //sleep(250);

        intake.stop();

        //intake stopped


    }
    public void duckPickup() {
        //preset code
        //collect duck
        drivetrain.moveInches(-2.5, 0.5);
        sleep(500);
        drivetrain.turnPI(130, .35, .3, 3000);
        sleep(500);
        drivetrain.moveInches(2.5, 0.4);
        sleep(500);
        //intake.collect();
        sleep(500);
        //set robot in proper pos to do shipping hub
        drivetrain.turnPI(-90, 0.3, 0.1, 3000); // Turn left 90
        sleep(500);
        drivetrain.moveInches(3, 0.5);
        sleep(500);
        drivetrain.turnPI(-45, .3, .3, 3000);
        sleep(500);
        drivetrain.moveInches(3, 0.5);
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

    public void duckHighGoal() {
        sleep(500);
        drivetrain.turnPD(90, .8, .1, 3000);
        sleep(250);
        drivetrain.moveInches(28, -.5);
        sleep(500);
        outake.highGoal();
    }

    public void reliablePark() {
        drivetrain.moveInches(6.5, -.4);
    }


    public void highGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 3000);
        sleep(550);
        drivetrain.moveInches(10,-.5);
        sleep(600);
        outake.highGoal();
    }

    public void midGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 3000);
        sleep(550);
        drivetrain.moveInches(5,-.5);
        sleep(700);
        outake.highGoal();
    }

    public void lowGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(20, -.5);
        sleep(400);
        drivetrain.turnPI(160,.4, .1, 3000);
        sleep(550);
        drivetrain.moveInches(1,-.5);
        sleep(700);
        outake.lowGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        intake = new Intake(this);
        carousel = new Carousel(this);
        DcMotor fL  = hardwareMap.get(DcMotor.class, "fL");
        DcMotor bL  = hardwareMap.get(DcMotor.class, "bL");
        DcMotor fR  = hardwareMap.get(DcMotor.class, "fR");
        DcMotor bR  = hardwareMap.get(DcMotor.class, "bR");
        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        highGoal();
        carousel();
        reliablePark();
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
            lowGoal();
            park();
        }
    }
}