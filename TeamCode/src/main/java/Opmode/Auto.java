package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import Library.Drivetrain;
import Library.Intake;
import Library.Carousel;

@Autonomous(name="Auto", group="4546")
public class Auto extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private Carousel carousel;
    private Intake intake;

    /* This is a sample auto template. We will work on this more
        once the robot is built since we don't know how much/how far
        to move/turn */
    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
       // drivetrain.turnPD();
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