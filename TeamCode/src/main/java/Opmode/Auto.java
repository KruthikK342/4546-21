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
        if (barcode == 1) {
            
        } else if (barcode == 2) {

        } else {

        }
        drivetrain.moveForward(3);
        carousel.spin();
        // collect duck?
        intake.collect();
        // drivetrain.turnPD();
        drivetrain.moveForward(3);
        // do stuff at shipping hub
        intake.out();
        //drivetrain.turnPD();
        drivetrain.moveForward(3);
        intake.collect();
        intake.out();
        drivetrain.stopMotors();
    }
}