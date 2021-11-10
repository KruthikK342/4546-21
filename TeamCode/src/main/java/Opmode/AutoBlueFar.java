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

@Autonomous(name="AutoBlueFar", group="4546")
public class AutoBlueFar extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;

    public void park() {
        drivetrain.moveInches(7, 0.5);
        sleep(1000);
        drivetrain.turnPI(-87.5, 0.25, 0.25, 2000);
        sleep(500);
        drivetrain.moveInches(150, 1.0);
        sleep(500);
    }
    /*  public void carousel() {
            drivetrain.moveInches(1, 0.5);
            sleep(1000);
            drivetrain.turnPI(-87.5, 0.25, 0.25, 2000);
            sleep(500);
            drivetrain.moveInches(6, 1.0);
            sleep(500);
            carousel.spin();
        }
    */
    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        waitForStart();
            park();

        /*
        while (opModeIsActive()) {
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
            drivetrain.stopMotors();
        }  */
    }
}