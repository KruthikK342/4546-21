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

@Autonomous(name="AutoRedNear", group="4546")
public class AutoRedNear extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;

    public void carousel() {
        drivetrain.moveInches(5, 0.5);
        sleep(500);
        drivetrain.turnPI(-93, 0.25, 0.25, 2000);
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
        drivetrain.turnPI(200, 0.25, 0.25, 2000);
        sleep(500);
        /*
        drivetrain.moveInches(90, .8);
        sleep(500);
        */

    }

    public void highGoal() {
        drivetrain.moveInches(5, -.5);
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


            /*
            park();
            carousel.spin();
            intake.collect();
            drivetrain.moveForward(3);
            if (barcode == 1) {
                intake.out();
            } else if (barcode == 2) {
                intake.out();
            } else {
                intake.out();
            }
            //drivetrain.turnPD();
            drivetrain.moveForward(3);
            intake.collect();
            intake.out();
            drivetrain.stopMotors();*/

    }
}