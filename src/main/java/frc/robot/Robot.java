/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

// motor controllers
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.VictorSP;

// pneumatics
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

// driver control
import edu.wpi.first.wpilibj.Joystick;

// camera
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static OI m_oi;

  //motor controlers
  TalonSRX rDrive0 = new TalonSRX(0);
  TalonSRX lDrive0 = new TalonSRX(3);
  TalonSRX Elevator0 = new TalonSRX(8);
  VictorSPX rDrive1 = new VictorSPX(1);
  VictorSPX rDrive2 = new VictorSPX(2);
  VictorSPX lDrive1 = new VictorSPX(4);
  VictorSPX lDrive2 = new VictorSPX(5);
  VictorSPX Elevator1 = new VictorSPX(7);
  public VictorSP rCompliant;
  public VictorSP lCompliant;

  //pneumatics
  DoubleSolenoid solenoid = new DoubleSolenoid(0, 1);
  Compressor airComp = new Compressor(0);

  // driver control
  public Joystick driverController;
  

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_oi = new OI();
    m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
    // chooser.addOption("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);

    // motor controllers
    rDrive0.set(ControlMode.PercentOutput, 0);
    lDrive0.set(ControlMode.PercentOutput, 0);
    Elevator1.set(ControlMode.PercentOutput, 0);
    rDrive1.follow(rDrive0);
    rDrive2.follow(rDrive0);
    lDrive1.follow(lDrive0);
    lDrive2.follow(lDrive0);
   // Elevator1.follow(Elevator0);
    rCompliant = new VictorSP(0);
    lCompliant = new VictorSP(1);

    // pneumatics
    airComp.setClosedLoopControl(false);

    // driver control
    driverController = new Joystick(0);

    // camera
    CameraServer.getInstance().startAutomaticCapture();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   * You can use it to reset any subsystem information you want to clear when
   * the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString code to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional commands to the
   * chooser code above (like the commented example) or additional comparisons
   * to the switch structure below with additional strings & commands.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_chooser.getSelected();

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector",
     * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
     * = new MyAutoCommand(); break; case "Default Auto": default:
     * autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.start();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    System.out.println("Yes, you can run code on Hal!");
    double x = driverController.getRawAxis(0);
    double y = driverController.getRawAxis(1);
    double throttle = (1+(-driverController.getRawAxis(3)))/2;
    boolean btn2 = driverController.getRawButton(2);
    boolean btn3 = driverController.getRawButton(3);
    boolean btn4 = driverController.getRawButton(4);
    boolean btn5 = driverController.getRawButton(5);
    boolean btn6 = driverController.getRawButton(6);


    // Put code here to manage throttle/gearshifter. Temporary code controls motor speed directly with
    // the throttle, and controls the gearshifter manually with buttons 3 and 4.

    if (btn3){
      solenoid.set(DoubleSolenoid.Value.kForward);
    }
    if (btn4){
      solenoid.set(DoubleSolenoid.Value.kReverse);
    }

    double motorThrottle = throttle;

    if (!btn2){
      lDrive0.set(ControlMode.PercentOutput, -(y+x)*motorThrottle);
      rDrive0.set(ControlMode.PercentOutput, (y-x)*motorThrottle);
      Elevator1.set(ControlMode.PercentOutput, 0);
    }  else{
      lDrive0.set(ControlMode.PercentOutput, 0);
      rDrive0.set(ControlMode.PercentOutput, 0);
      Elevator1.set(ControlMode.PercentOutput, y);
    }

    if (btn5){
      airComp.setClosedLoopControl(false);
    }
    if (btn6){
      airComp.setClosedLoopControl(true);
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
