

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
        outake = hardwareMap.get(DcMotor.class, "Outake");
        spin = hardwareMap.get(DcMotor.class, "carousel");
        arm = hardwareMap.get(DcMotor.class, "arm");
        wrist = hardwareMap.get(Servo.class, "wrist");
        sort = hardwareMap.get(Servo.class, "sort");
        fL.setDirection(DcMotor.Direction.REVERSE);
        bL.setDirection(DcMotor.Direction.REVERSE);
        fR.setDirection(DcMotor.Direction.FORWARD);
        bR.setDirection(DcMotor.Direction.FORWARD);

        // Waits for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // starting program
        while (opModeIsActive()) {

            // Driving [arcade mode]
            double leftPower;
            double rightPower;


            if (Math.abs(gamepad1.left_stick_y) > .05 || Math.abs(gamepad1.left_stick_x) > .05 || Math.abs(gamepad1.right_stick_x) > .05) {
                driveTrainPower(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x * .78);
            } else {
                driveTrainPower(0, 0, 0);
            }

            //Driver 1
            if(gamepad1.right_trigger > .5) intake.setPower(-1); // Intake out
            else if(gamepad1.left_trigger > .5) intake.setPower(1); //Intake in
            else intake.setPower(0);


            if(gamepad1.left_bumper) outake.setPower(.7); //Outake
            else outake.setPower(0);

            if (gamepad1.x) sort.setPosition(.5); //sorting one direction
            if (gamepad1.y) sort.setPosition(0); //sorting other direction


            if (gamepad1.right_bumper) spin.setPower(.2); // Carousel
            else spin.setPower(0);

            if (gamepad1.right_trigger > .5) arm.setPower(.2); // Pulley up
            else arm.setPower(0); // Pulley stop

            if (gamepad1.y) wrist.setPosition(0); // Wrist out
            if (gamepad1.x) wrist.setPosition(1); // Wrist in

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}