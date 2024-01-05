package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Drive")

public class Drive extends OpMode {
    // declare motors, ticks (encoder resolution), and newTarget for motor's future target pos
    DcMotor leftDrive;
    DcMotor rightDrive;
    DcMotor intake;
    DcMotor arm;
    Servo launcher;
    CRServo linear;
    boolean runningEncoder = false;
    boolean started = false;
    private ElapsedTime runTime = new ElapsedTime();

    @Override
    public void init() {
        // map motors to their names in the driver hub
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        arm = hardwareMap.get(DcMotor.class, "arm");
        launcher = hardwareMap.get(Servo.class, "launcher");
        linear = hardwareMap.get(CRServo.class, "linear");

        // setup motors to use encoders and set their stopping to brake
        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        launcher.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void loop() {
        // reset run time since it starts when you hit init
        if(!started){
            runTime.reset();
            started = true;
        }

        // y is the controller's left stick y value on the y axis, and x is the value on the x axis
        double y = gamepad1.left_stick_y;
        double x = gamepad1.right_stick_x/1.5;
        double leftPower = (-y-x)/1.1;
        double rightPower = (y-x)/1.1;

        telemetry.clear();
        telemetry.addData("Left Power", leftPower);
        telemetry.addData("Right Power", rightPower);

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);

        // when the controller has the right trigger held down, intake goes forward for however much
        // it is being held down, while when the controller has the left trigger held down, intake
        // goes backwards
        double intakePower = -gamepad1.right_trigger + (gamepad1.left_trigger/3);
        intake.setPower(intakePower);

        // arm angle variable accounts for gear ratio (20:100, 1:5)
        double armAngle = arm.getCurrentPosition()/5.0;

        telemetry.addData("Servo Position:", launcher.getPosition());

        if(!runningEncoder) {
            // manual option for the arm
            if (gamepad1.right_bumper) {
                // forward when right bumper pressed
                arm.setPower(0.5);
            } else if (gamepad1.left_bumper) {
                // backwards when left bumper pressed
                arm.setPower(-0.5);
            } else {
                // else set to 0
                arm.setPower(0);
            }
        }

        if(gamepad1.x){
            runningEncoder = true;

            int newTarget = -420;
            arm.setTargetPosition(newTarget*5);
            arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            if(armAngle >= 0){
                arm.setPower(-1);
            }else {
                arm.setPower(1);
            }

            while(arm.isBusy()){
                telemetry.addData("Running to", newTarget);
            }

            arm.setPower(0);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            runningEncoder = false;

        }

        if(gamepad1.y){
            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if(gamepad1.b) {
            launcher.setPosition(1.0);
        }

        if(gamepad1.dpad_down){
            launcher.setPosition(0.0);
        }

        if(gamepad1.dpad_right){
            linear.setPower(1);
        }

        if(gamepad1.dpad_left){
            linear.setPower(0);
        }

        telemetry.addData("Intake Power", intakePower);
        telemetry.addData("Arm power", arm.getPowerFloat());
        telemetry.addData("Arm Encoder Angle", armAngle);
    }

}
