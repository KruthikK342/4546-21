package Opmode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import Library.Carousel;
import Library.Drivetrain;
import Library.DuckBarcodeBitmap;
import Library.Intake;
import Library.Outtake;

@Autonomous(name="AutoBlueCarouselLite", group="4546")
public class AutoBlueCarouselLite extends LinearOpMode {
    // front left, front right, back left, back right motors
    private Drivetrain drivetrain;
    private DuckBarcodeBitmap vision;
    private Carousel carousel;
    private Intake intake;
    private Outtake outake;
    private int barcode;


    public void carousel() {
        drivetrain.moveInches(24, 0.5); // Move forward to turn
        sleep(450);
        drivetrain.turnPD(90, 0.5, 0.1, 3000); // Turn right 90
        sleep(300);
        drivetrain.moveInches(29, 0.5); // Move forward
        sleep(450);
        drivetrain.turnPD(180, 0.5, 0.1, 3000);
        sleep(450);
        drivetrain.moveInches(17, 0.5); // Move forward
        sleep(450);
        carousel.spin(.3);
        sleep(4000);
        carousel.stop();
    }


    public void park() {
        drivetrain.turnPD(180, 0.5, 0.05, 3000);
        sleep(450);
        drivetrain.moveInches(24.5, -0.5);
    }



    @Override
    public void runOpMode() throws InterruptedException {
        // Vuforia stuff here
        vision = new DuckBarcodeBitmap(this);
        drivetrain = new Drivetrain(this);
        outake = new Outtake(this);
        intake = new Intake(this);
        carousel = new Carousel(this);
        DcMotor fL  = hardwareMap.get(DcMotor.class, "fL");
        DcMotor bL  = hardwareMap.get(DcMotor.class, "bL");
        DcMotor fR  = hardwareMap.get(DcMotor.class, "fR");
        DcMotor bR  = hardwareMap.get(DcMotor.class, "bR");
        fL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        telemetry.addLine("Robot Initialized");
        telemetry.update();

        while (!isStarted())
        {
            telemetry.addData("xpos: ", vision.getImageWidth());
            telemetry.addData("Team Element Pixel Count: ", vision.getTeamElementPixelCount());
            telemetry.addData("Barcode: ", vision.getBarcode(false));
            telemetry.update();
        }

        waitForStart();
        carousel();
        park();
    }
}