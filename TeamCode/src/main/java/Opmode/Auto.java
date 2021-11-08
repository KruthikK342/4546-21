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

@Autonomous(name="Auto", group="4546")
public class Auto extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;

    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        int barcode = vision.getBarcode();
        telemetry.addLine("Barcode: " + barcode);
        telemetry.update();
        sleep(2000);
        drivetrain.turnPD(90, 0.5, 0.5, 2000);
        sleep(1000);
        drivetrain.stopMotors();
        // drivetrain.moveForward(3);
        /*
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