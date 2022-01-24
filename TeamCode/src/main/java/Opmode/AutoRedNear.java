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
    private int barcode;

    public void carousel() {

        drivetrain.moveInches(3, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(-106.5, 0.63, .1, 3000);
        sleep(300);

        drivetrain.moveInches(56, 0.45); // Move forward
        sleep(450);




        carousel.spin(-.42);
        sleep(2600);
        sleep(300);
        carousel.spin(.3);
        sleep(800);
        carousel.stop();

    }

    public void intake() {
        //intake code
        intake.collect();
        sleep(100);
        drivetrain.moveInches(3,.5);
        sleep(200);
        drivetrain.turnPI(210, 1, .1, 3000);
        sleep(1500);
        intake.stop();
    }

    /*
    public void park() {
        drivetrain.moveInches(6.2,-.4);
    }
    /
     */

    public void park() {
        drivetrain.moveInches(2.5, 0.5);
        sleep(800);

        drivetrain.turnPD(-3, 0.30, 0.25, 2000);
        sleep(500);
        drivetrain.turnPD(-113,.25,.25,2000);

        drivetrain.moveInches(7, -.8);
        sleep(1000);
        drivetrain.moveInches(60, -1);
    }


    public void midGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(-90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(15, -.5);
        sleep(500);
        drivetrain.turnPI(177,.38, .1, 3000);
        sleep(450);
        drivetrain.moveInches(2, -.4);
        sleep(500);
        outake.midGoal();
    }

    public void highGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(-90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(15, -.5);
        sleep(500);
        drivetrain.turnPI(177,.38,.08, 3000);
        sleep(500);
        drivetrain.moveInches(2.3, -.4);
        sleep(500);
        outake.highGoal();


    }

    public void lowGoal() {
        drivetrain.moveInches(9,.4); // Back up from Carousel;
        sleep(400);
        drivetrain.turnPI(-90, .25, .1, 3000); // Turn towards warehouse
        sleep(400);
        drivetrain.moveInches(15, -.5);
        sleep(500);
        drivetrain.turnPI(177,.38, .1, 3000);
        sleep(450);
        drivetrain.moveInches(2, -.4);
        sleep(500);
        outake.lowGoal();
    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        intake = new Intake(this);
        barcode = vision.getBarcode(true);
        carousel = new Carousel(this);
        /*
        DcMotor fL  = hardwareMap.get(DcMotor.class, "fL");
        DcMotor bL  = hardwareMap.get(DcMotor.class, "bL");
        DcMotor fR  = hardwareMap.get(DcMotor.class, "fR");
        DcMotor bR  = hardwareMap.get(DcMotor.class, "bR");
        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

         */
        while(!isStarted()) {
            telemetry.addData("Barcode: ", barcode);
            telemetry.update();
        }
        waitForStart();

        barcode = vision.getBarcode(true);
        sleep(
                6000);
        if (barcode == 1) {
            lowGoal();
        } else if (barcode == 3) {
            highGoal();
        } else {
            midGoal();
        }

        carousel();
        park();
    }

}