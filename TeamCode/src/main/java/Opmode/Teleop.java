

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
    private Servo shippingArm = null;
    private Servo hook = null;






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
        intake  = hardwareMap.get(DcMotor.class, "intake");
        outake = hardwareMap.get(DcMotor.class, "outake");
        spin = hardwareMap.get(DcMotor.class, "carousel");
        arm = hardwareMap.get(DcMotor.class, "arm");
        wrist = hardwareMap.get(Servo.class, "wrist");
        sort = hardwareMap.get(Servo.class, "sort");
        shippingArm = hardwareMap.get(Servo.class, "shippingArm");
        hook = hardwareMap.get(Servo.class, "hook");
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Variables to store positions
        double sPos = .05;
        double hPos = .05;
        String liftState = "down";
        double wristDeposit = .6;
        double wristRest = .15;
        double wristTilt = .3;
        double wristGround = .8;
        double liftMax = 1250;
        double liftMin = 0;

        // Waits for the game to start (driver presses PLAY)

        waitForStart();
        runtime.reset();

        // starting program
        double power = 1.0;
        shippingArm.setPosition(.05);
        hook.setPosition(.05);

        while (opModeIsActive()) {
            //Driver 1
            /*
            Driver 1 controls the drivetrain, carousel, intake, sorter, and the base level
            outtake. This ensures the maximum efficiency and speed possible as less coordination is
            needed between drivers for when it matters such as competing for tipping the shared
            shipping hub. Additionally, driver 1 should be able to spin the carousel wheel in either
            direction regardless of team color.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Single button Macro
             */

            // Driving [arcade mode]
            if (Math.abs(gamepad1.left_stick_y) > .05 || Math.abs(gamepad1.left_stick_x) > .05 || Math.abs(gamepad1.right_stick_x) > .05) {
                driveTrainPower(-power*gamepad1.left_stick_y, -power*gamepad1.left_stick_x, -power*gamepad1.right_stick_x * .78);
            } else {
                driveTrainPower(0, 0, 0);
            }

            if (gamepad1.dpad_down) // Half-power mode
                power = 0.5;
            else if (gamepad1.dpad_up) // Full-power mode
                power = 1.0;


            // If the right trigger is pressed down past a threshhold, the intake will run at full
            // power. If left trigger, will run the opposite direction to clear the robot.
            // If neither condition is met, automatically set power to 0 and rest.
            if (gamepad1.right_trigger > .5) intake.setPower(-1); // Intake in
            else if (gamepad1.left_trigger > .5) intake.setPower(.5); // Intake out
            else intake.setPower(0);

            // Outake
            if (gamepad1.a) outake.setPower(.7); // Outake forward
            else if (gamepad1.b) outake.setPower(-.7); //Outake reverse
            else outake.setPower(0);


            // If the left bumper is pressed down past a threshhold, the carousel will run at half
            // power. If right bumper, will run the opposite direction to spin the carousel.
            // If neither condition is met, automatically set power to 0 and rest.
            if(gamepad1.left_bumper) {
                spin.setPower(.45);
                fL.setPower(.05);
                fR.setPower(.05);
                bL.setPower(.05);
                bR.setPower(.05);
            }
            else if (gamepad1.right_bumper) {
                spin.setPower(-.45);
                fL.setPower(.05);
                fR.setPower(.05);
                bL.setPower(.05);
                bR.setPower(.05);
            }
            else spin.setPower(0);

            // Either adjust the servo to sort into the box or into the base outtake
            if (gamepad1.x) sort.setPosition(.45); // Sorting into box
            if (gamepad1.y) sort.setPosition(.7); // Sorting into base outtake


            //Driver 2
            /*
            Driver 2 currently controls the arm and wrist outtake as these mechanisms should be
            operated while moving in order to reduce cycle times as much as possible. Although few
            in terms of controls, Driver 2 is still vital in watching and commanding other robots
            as well as keeping an eye out as Driver 1 will likely be focused on the main overall
            game plan while still outtaking.
//////////////////////////////////////////////////////////////////////////////////////////////////
            POSSIBLE FUTURE CHANGES:
            Test and set TRUE position for servo wrist. Possibly servo programmer.
             */

            // If the right trigger of Driver 2 is pressed past a threshold, the arm will extend out,
            // if the left trigger is pressed, the arm will retract back down. Otherwise rest.
            // Will likely combine with wrist movement and into different levels.

            // Lift Macro
            switch(liftState) {
                case("raise"): {
                    wrist.setPosition(wristTilt); // Tilt box back
                    arm.setPower(1); // Lift arm, increase encoder
                    if (arm.getCurrentPosition() > liftMax) { // Reaches top
                        arm.setPower(0); // Stop lift
                    }
                    break;
                }
                case("deposit"): {
                    wrist.setPosition(wristDeposit); // Deposit, Box points straight down
                    if (runtime.time() > 1) { // Allows a second to allow freight to deposit
                        wrist.setPosition(wristRest); // Tilts box back to resting position
                        liftState = "retract"; // Change case
                    }
                    break;
                }

                case("retract"): {
                    arm.setPower(-.7); // Retract lift
                    if (arm.getCurrentPosition() < liftMin) { // Checks if lift at bottom
                        arm.setPower(0); // Stops lift
                        liftState = "down"; // Change case back to resting
                    }
                    break;
                }

                default: { // Should never be reached as liftState will never be NULL
                    liftState = "down";
                }
            }

            if (gamepad2.right_trigger > 0.5 && arm.getCurrentPosition() > 20) arm.setPower(-.7);
            else if (gamepad2.left_trigger > 0.5 && arm.getCurrentPosition() < 1350) arm.setPower(1);
            else if (liftState == "down") arm.setPower(0);

            if (gamepad2.dpad_up) liftState = "raise"; // Begin lift sequence
            if (gamepad2.dpad_right) {
                runtime.reset(); // Reset time, used for depositing
                liftState = "deposit"; // Deposit the freight and reset box
            }
            if (gamepad2.dpad_down) liftState = "down"; // Part 2, deposit and retract

            if (gamepad2.a) wrist.setPosition(wristRest); // Wrist Rest
            if (gamepad2.b) wrist.setPosition(wristDeposit); // Wrist Deposit
            if (gamepad2.y) wrist.setPosition(wristTilt); // Wrist Tilt
            if (gamepad2.x) wrist.setPosition(wristGround); // Box points straight down
            // Currently rotates the box either upright, tilt back slightly, or deposit element


            // Shipping arm rotates
            // CURRENTLY ONLY FOR TESTING, NOT READY FOR GAME USE
        //    if (gamepad2.dpad_down) shippingArm.setPosition(.8);
          //  else if (gamepad2.dpad_up) shippingArm.setPosition(.6);

            // Hook rotates
            // Left most position hooks
            // CURRENTLY ONLY FOR TESTING, NOT READY FOR GAME USE
            if (gamepad2.dpad_left) {
                hPos += .05;
                hook.setPosition(hPos);
            }
            else if (gamepad2.dpad_right) {
                hPos -= .05;
                hook.setPosition(hPos);
            }


            // Show the arm encoder position and wheel power.
            telemetry.addData("Pulley Encoder", arm.getCurrentPosition());
            telemetry.addData("fR:", fR.getPower());
            telemetry.addData("fL:", fL.getPower());
            telemetry.addData("bR:", bR.getPower());
            telemetry.addData("bL:", bL.getPower());
            /*telemetry.addData("sArm pos:", shippingArm.getPosition());
            telemetry.addData("hArm pos:", hook.getPosition());
            telemetry.addData("sPos:", sPos);
            telemetry.addData("hPos:", hPos);*/
            telemetry.addData("State: ", liftState);
            telemetry.update();
        }
    }
}