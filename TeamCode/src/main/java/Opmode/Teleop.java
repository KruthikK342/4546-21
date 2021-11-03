

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

            double drive = -gamepad1.left_stick_y; // forward and backwards
            double turn  =  gamepad1.right_stick_x; // left and right
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            fL.setPower(leftPower);
            bL.setPower(leftPower);
            fR.setPower(rightPower);
            bR.setPower(rightPower);

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