package org.firstinspires.ftc.teamcode;


import androidx.core.view.TintableBackgroundView;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


import org.opencv.core.Mat;


@Autonomous (name = "backstage")
public class near extends LinearOpMode {




    //declare motors
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor intake = null;
    private final ElapsedTime runtime = new ElapsedTime();


    //encoder resolution
    double ticks = 537.7;


    //measurements in inches
    double wheelDiam = 3.75;
    double robotDiam = 16.0;
    double robotCircumference = (robotDiam* Math.PI);


    //inverse gear ratio of sprockets
    double reduction = 0.714285;
    double counts_per_inch = (ticks * reduction)/(wheelDiam * Math.PI);
    double slow = 0.1;




    @Override
    public void runOpMode() {




        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");


        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.FORWARD);


        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry.addData("Starting at",  "%7d :%7d",
                leftDrive.getCurrentPosition(),
                rightDrive.getCurrentPosition());
        telemetry.update();


        waitForStart();


        // drive forward to middle spike mark and drop pixel off
        encoderDrive(slow, 18, 18, 5.0);
        intake.setPower(0.2);
        sleep(500);
        telemetry.addData("Now at", "spike mark");
        telemetry.update();


        // drive back to start
        encoderDrive(slow, -18, -18, 5.0);
        telemetry.addData("Now at", "Starting position");
        telemetry.update();


        // turn right 90 degrees
        encoderDrive(slow, robotCircumference/4, -robotCircumference/4, 7.0);
        telemetry.addData("Turned right", "90 degrees");
        telemetry.update();


        // drive backwards to backstage wall and drop off pixel
        encoderDrive(slow, -57,-57, 13.0);
        intake.setPower(0.2);
        sleep(1000);
        telemetry.addData("Path", "Complete");
        telemetry.update();


        sleep(1000);
    }
    public void encoderDrive(double speed, double leftInches, double rightInches, double timeoutS) {


        int newLeftTarget;
        int newRightTarget;


        if (opModeIsActive()) {


            newLeftTarget = (leftDrive.getCurrentPosition() + (int)(leftInches * counts_per_inch));
            newRightTarget = (rightDrive.getCurrentPosition() + (int)(rightInches * counts_per_inch));
            leftDrive.setTargetPosition(newLeftTarget);
            rightDrive.setTargetPosition(newRightTarget);


            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


            runtime.reset();
            leftDrive.setPower(Math.abs(speed));
            rightDrive.setPower(Math.abs(speed));


            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (leftDrive.isBusy() && rightDrive.isBusy())) {
                telemetry.addData("Running to", " %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Currently at", " at %7d :%7d", leftDrive.getCurrentPosition(), rightDrive.getCurrentPosition());
                telemetry.addData("Motor speed at", speed);
                telemetry.addData("Seconds left:", timeoutS - runtime.seconds());
                telemetry.update();
            }


            leftDrive.setPower(0);
            rightDrive.setPower(0);


            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


            sleep(250);
        }
    }
}
