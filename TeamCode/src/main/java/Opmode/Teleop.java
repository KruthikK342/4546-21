

package Opmode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="TeleOp", group="4546")
public class Teleop extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor fL, bL, fR, bR; // front left, back left, front right, back right
    private Double leftDrive = null; // left  drive call for other motors
    private Double rightDrive = null; // right drive call for other motors
    private DcMotor intake = null; // intake motor
    private DcMotor outake = null; // outputting unnecessary
    private Servo sort = null; // sorting items collected
    private DcMotor spin = null; // carousel control motor
    private DcMotor arm = null; // pulley motor
    private Servo wrist = null; // wrist like outake

    public double WeightAvg(double x, double y, double z) {
        double speed_D = 0;

        if ((Math.abs(x) + Math.abs(y) + Math.abs(z))  != 0.0) {
            speed_D = ((x * Math.abs(x)) + (y * Math.abs(y)) + (z * Math.abs(z)))
                    / (Math.abs(x) + Math.abs(y) + Math.abs(z));
        }
        return (speed_D);
    }

    public void driveTrainPower(double forward, double strafe, double rotate){
        fL.setPower(WeightAvg(forward,strafe,-rotate));
        fR.setPower(WeightAvg(forward,-strafe,rotate));
        bL.setPower(WeightAvg(forward,-strafe,-rotate));
        bR.setPower(WeightAvg(forward,strafe,rotate));
    }

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        fL  = hardwareMap.get(DcMotor.class, "fL");
        bL  = hardwareMap.get(DcMotor.class, "bL");
        fR  = hardwareMap.get(DcMotor.class, "fR");
        bR  = hardwareMap.get(DcMotor.class, "bR");
        intake  = hardwareMap.get(DcMotor.class, "Intake");
        outake = hardwareMap.get(DcMotor.class, "outake");
        spin = hardwareMap.get(DcMotor.class, "carousel");
        arm = hardwareMap.get(DcMotor.class, "arm");
        wrist = hardwareMap.get(Servo.class, "wrist");
        sort = hardwareMap.get(Servo.class, "sort");
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Waits for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // starting program
        double power = 1.0;
        while (opModeIsActive()) {

            if (gamepad1.dpad_down)
                power = 0.5;
            else if (gamepad1.dpad_up)
                power = 1.0;

            // Driving [arcade mode]

            //Driver 1
            /*
            Driver 1 controls the drivetrain, carousel, intake, sorter, and the base level
            outtake. This ensures the maximum efficiency and speed possible as less coordination is
            needed between drivers for when it matters such as competing for tipping the shared
            shipping hub. Additionally, driver 1 should be able to spin the carousel wheel in either
            direction regardless of team color.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Create different teleOp classes for red vs blue so that only 1 button is needed for
            carousel.
            Create a "half-speed" mode for more precise movements, more specifically for carousel.
            If necessary account for alterations in wheel movements/positioning.
             */
            if (Math.abs(gamepad1.left_stick_y) > .05 || Math.abs(gamepad1.left_stick_x) > .05 || Math.abs(gamepad1.right_stick_x) > .05) {
                driveTrainPower(-power*gamepad1.left_stick_y, -power*gamepad1.left_stick_x, -power*gamepad1.right_stick_x * .78);
            } else {
                driveTrainPower(0, 0, 0);
            }

            // If the right trigger is pressed down past a threshhold, the intake will run at full
            // power. If left trigger, will run the opposite direction to clear the robot.
            // If neither condition is met, automatically set power to 0 and rest.
            if(gamepad1.right_trigger > .5) intake.setPower(-1); // Intake out
            else if (gamepad1.left_trigger > .5) intake.setPower(1);
            else intake.setPower(0);

            if(gamepad1.a) outake.setPower(1); // Outake forward
            else if(gamepad1.b) outake.setPower(-1); //Outake reverse
            else outake.setPower(0);




            // If the left bumper is pressed down past a threshhold, the carousel will run at half
            // power. If right bumper, will run the opposite direction to spin the carousel.
            // If neither condition is met, automatically set power to 0 and rest.
            if(gamepad1.left_bumper) spin.setPower(.7);
            else if (gamepad1.right_bumper) spin.setPower(-.7);
            else spin.setPower(0);

            // Either adjust the servo to sort into the box or into the base outtake
            if (gamepad1.x) sort.setPosition(.5); //Sorting into box
            if (gamepad1.y) sort.setPosition(0); //sorting into base outtake

            //Driver 2
            /*
            Driver 2 currently controls the arm and wrist outtake as these mechanisms should be
            operated while moving in order to reduce cycle times as much as possible. Although few
            in terms of controls, Driver 2 is still vital in watching and commanding other robots
            as well as keeping an eye out as Driver 1 will likely be focused on the main overall
            game plan while still outtaking.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Create macros to rise and automatically turn the box at the mid and high levels. (PID?)
            Create encoder limits to ensure the pulley line does not snap.
            Test and set TRUE position for servo wrist. Possibly servo programmer.
             */

            // If the right trigger of Driver 2 is pressed past a threshold, the arm will extend out,
            // if the left trigger is pressed, the arm will retract back down. Otherwise rest.
            // Will likely combine with wrist movement and into different levels.

            if (gamepad2.right_trigger > 0.5) arm.setPower(-.7);
            else if (gamepad2.left_trigger > 0.5) arm.setPower(.7);
            else arm.setPower(0);

            if  (gamepad2.a) wrist.setPosition(.1); // Wrist Rest
            if (gamepad2.b) wrist.setPosition(.45); // Wrist Deposit
            if (gamepad2.y) wrist.setPosition(.3); // Wrist Flick
            // Currently rotates the box either upright or to be slightly tilted downwards


            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}