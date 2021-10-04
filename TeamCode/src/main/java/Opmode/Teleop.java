

package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Teleop", group="4546")
@Disabled
public class Teleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null; // left drive motor
    private DcMotor rightDrive = null; // right drive motor
    private DcMotor intake = null; // intake motor
    private DcMotor outake = null; // outputting unneccesary
    private Servo sort = null; // sorting items collected
    private DcMotor spin = null; // carousel control motor
    private DcMotor arm = null; // pulley motor
    private Servo wrist = null; // wrist like outake

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Waits for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // starting program
        while (opModeIsActive()) {

            // Driving [arcade mode]
            double leftPower;
            double rightPower;

            double drive = -gamepad1.left_stick_y; // forward and backwards
            double turn  =  gamepad1.right_stick_x; // left and right
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            //Driver 1
            if(gamepad1.right_trigger > .5) intake.setPower(-1); // Intake out
            if(gamepad1.left_trigger > .5) intake.setPower(1); //Intake in
            if(gamepad1.left_bumper) outake.setPower(.7); //Outake
            if (gamepad1.x) sort.setPosition(.5); //sorting one direction
            if (gamepad1.y) sort.setPosition(0); //sorting other direction

            // Driver 2
            if (gamepad2.right_bumper) spin.setPower(.2); // Carousel
            if (gamepad2.right_trigger > .5) arm.setPower(.2); // Pulley up
            if (gamepad2.right_trigger < .5) arm.setPower(0); // Pulley stop
            if (gamepad2.y) wrist.setPosition(0); // Wrist out
            if (gamepad2.x) wrist.setPosition(1); // Wrist in

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}