package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
// note: for imu we are orthogonal 8, logo forward and usb right

// this program holds the functions for the robot to use during auton such as "drive forward' or "turn"
public class robotDrive {

    public DcMotor leftDrive;
    public DcMotor rightDrive;
    public DcMotor intake;

    // motor variables
    // encoder resolution
    double ticks = 537.7;
    // measurements in inches
    double wheelDiam = 3.75;
    // inverse gear ratio of sprockets
    double reduction = 5/7;
    double counts_per_inch = (ticks*reduction)/(wheelDiam*Math.PI);
    // Constructor
    public robotDrive(DcMotor left, DcMotor right, DcMotor intk){
        // have the motors that the program is using be the motors for these functions
        leftDrive = left;
        rightDrive = right;
        intake = intk;

        // configure encoders on motors
        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // set motor's stopping to break
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // configure drive motors' directions
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
    }

    private void stopDrive(){
        leftDrive.setPower(0);
        rightDrive.setPower(0);
    }

    public void drive(String dir, double power, int duration) throws InterruptedException {

        if(dir == "reverse"){
            power = -power;
        }

        leftDrive.setPower(power);
        rightDrive.setPower(power);

        Thread.sleep(duration);
        stopDrive();
    }

    public void turn(String dir, double power, int duration) throws InterruptedException {
        if(dir == "left"){
            leftDrive.setPower(-power);
            rightDrive.setPower(power);
        }else{
            leftDrive.setPower(power);
            rightDrive.setPower(-power);
        }

        Thread.sleep(duration);
        stopDrive();
    }

    public void spinIntake(String dir, double power, int duration) throws InterruptedException{
        if(dir == "reverse") {
            intake.setPower(power);
        }

        Thread.sleep(duration);
        intake.setPower(0);
    }

}
