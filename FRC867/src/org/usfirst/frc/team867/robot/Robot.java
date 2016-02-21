
//default imports

package org.usfirst.frc.team867.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


//additional imports

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Compressor;
import com.ni.vision.NIVision;
import com.ni.vision.VisionException;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

import org.usfirst.frc.team867.robot.toggleVal;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String flywheel = "flywheel";
	final String nautilus = "nautilus";
	String firemode;
	SendableChooser chooser;

	//global variables

	//motors - note: make sure CAN devices are numbered properly in the web interface (connect roborio and go to 172.22.11.2). 
	//Start device IDs from 1. Leave 0 free for new devices.
	//code refers to device 1 with an index of 0
	CANTalon[] motorlist; //stores all motors

	//joysticks - note: buttons start at 1
	Joystick joyDrive; //driver joystick
	Joystick joyManip; //manipulator joystick

	//joystick values
	double leftDrive; //stores left joystick y axis value
	double rightDrive; //stores right joystick y axis value


	//compressor
	Compressor comp;

	//valves
	Solenoid flyLift;
	Solenoid flyPush;

	//camera
	int camSes0;
//	int camSes1;
	Image frame;

	//controller axes
	final int leftY = 1;
	final int rightY = 5;

	//controller buttons
	final int yButton = 4;
	final int xButton = 3;
	final int aButton = 1;
	final int bButton = 2;
	
	final int leftButton = 5;
	final int rightButton = 6;
	
	final int leftStickButton = 9;
	final int rightStickButton = 10;
	
	final int backButton = 7;
	final int startButton = 8;

	//cameraServos

	Servo pan;
	Servo tilt;

	double panVal = 70;
	double tiltVal = 70;


	//state variables
	boolean firstRun;
	boolean useCam0;
	boolean toggleCamera; //to check if in the process of toggle (true = can toggle, false = DO NOT TOGGLE YET)
	boolean justSwitched;
	
	boolean nautLimFirst;
	
	toggleVal toggleNaut;
	toggleVal toggleLift;
	
	
	
	//naut Limit
	DigitalInput nautLimit;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser = new SendableChooser();
		chooser.addDefault("flywheel", flywheel);
		chooser.addObject("nautilus", nautilus);
		SmartDashboard.putData("firemode", chooser);



		//motorlist
		motorlist = new CANTalon[6];
		for(int i=0; i<=5; i++)
		{
			motorlist[i] = new CANTalon(i+1);
		}

		//enable brake mode for drive motors
		for(int i=0; i<=3; i++)
		{
			motorlist[i].enableBrakeMode(true);
		}

		//joysticks
		joyDrive = new Joystick(0);
		joyManip = new Joystick(1);


		//compressor
		comp = new Compressor();
		//enable
		comp.start();

		//valves
		
		flyLift = new Solenoid(0);
		flyPush = new Solenoid(1);
		
		//camera

		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		//camSes0
		try 
		{
			camSes0 = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		}
		catch(VisionException e)
		{
			SmartDashboard.putString("Camera error @ camSes0 open", e.toString());
		}

		try 
		{
			NIVision.IMAQdxConfigureGrab(camSes0);
		}
		catch(VisionException e)
		{
			SmartDashboard.putString("Camera error @ camSes0 configGrab", e.toString());
		}

/*
		
		//camSes1
		try 
		{
			camSes1 = NIVision.IMAQdxOpenCamera("cam1", NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		}
		catch(VisionException e)
		{
			SmartDashboard.putString("Camera error @ camSes1 open", e.toString());
		}

		try 
		{
			NIVision.IMAQdxConfigureGrab(camSes1);
		}
		catch(VisionException e)
		{
			SmartDashboard.putString("Camera error @ camSes1 configGrab", e.toString());
		}
*/		

		//cameraServos

		pan = new Servo(0);
		tilt = new Servo(1);

		panVal = 70;
		tiltVal = 70;


		//state variables
		firstRun = true;
		useCam0 = true;
		toggleCamera = true;
		justSwitched = true;
		
		nautLimFirst = false; //first time has not occured yet
		
		toggleLift = new toggleVal(false);
		toggleNaut = new toggleVal(false);
		
		//naut Limit
		
		nautLimit = new DigitalInput(9);
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {

		firemode = (String) chooser.getSelected();
		System.out.println("firemode: " + firemode);
		
		//starts up cameras on first teleop cycle
		if(firstRun)
		{

			//to draw a rect, uncomment and modify the line below; also for one more right after image grab ***
			//NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);

		}

		//toggle for cameras

		//uncomment for camera2 toggle
		
		/*
		if(joyDrive.getRawButton(bButton) && toggleCamera)
		{
			toggleCamera = false; //on first press, disable toggle
			useCam0 = !useCam0;	//switch cameras, if button is still held down, it will not toggle anymore, and will not re-enable toggle

			justSwitched = true;
		}
		else if(!joyDrive.getRawButton(bButton)) //when button is finally released, re-enable toggle
		{
			toggleCamera = true;
		}
		*/
		
		//grabs image

/*		if(useCam0 && justSwitched)
		{
			try
			{
				NIVision.IMAQdxStopAcquisition(camSes1);
			}
			catch(VisionException e)
			{
				SmartDashboard.putString("Camera error @ camSes1 stopAcq", e.toString());
			}
*/
			try
			{
				NIVision.IMAQdxStartAcquisition(camSes0);
			}
			catch(VisionException e)
			{
				SmartDashboard.putString("Camera error @ camSes0 startAcq", e.toString());
			}
/*
			justSwitched = false;
		}
		else if(!useCam0 && justSwitched)
		{

			try
			{
				NIVision.IMAQdxStopAcquisition(camSes0);
			}
			catch(VisionException e)
			{
				SmartDashboard.putString("Camera error @ camSes0 stopAcq", e.toString());
			}


			try
			{
				NIVision.IMAQdxStartAcquisition(camSes1);
			}
			catch(VisionException e)
			{
				SmartDashboard.putString("Camera error @ camSes1 startAcq", e.toString());
			}

			justSwitched = false;
		}

*/
		try
		{
			//NIVision.IMAQdxGrab(useCam0 ? camSes0 : camSes1, frame, 1);
			NIVision.IMAQdxGrab(camSes0, frame, 1);
		}
		catch(VisionException e)
		{
			SmartDashboard.putString("Camera error @ grabImage", e.toString());

		}

		//***uncomment to draw predefined rectangle
		// NIVision.imaqDrawShapeOnImage(frame, frame, rect,DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);


		//sends to camera
		CameraServer.getInstance().setImage(frame);

		//joystick code
		leftDrive = joyDrive.getRawAxis(leftY); 
		rightDrive = joyDrive.getRawAxis(rightY);
		

		//motor code

		motorlist[0].set(-1 * leftDrive);
		motorlist[1].set(-1 * leftDrive);
		motorlist[4].set(1 * rightDrive);
		motorlist[5].set(1 * rightDrive);

		//nautilus code		
		if(firemode.equals(nautilus))
		{
			toggleNaut.checkForToggle(joyDrive.getRawButton(startButton));
		
			if(toggleNaut.get()) //if started
			{
				motorlist[2].set(-1);

				if(!nautLimit.get()) //first press of limit (default state of limit is true, so pressed = false)
				{
					if(!nautLimFirst) //if first time, set to true
					{
						nautLimFirst = true;
					}
				}
				else
				{
					if(nautLimFirst) //after first time
					{
						nautLimFirst = false; //resets first press
						toggleNaut.set(false); //no longer sending start signal
					}
				}
				
			}
			else
			{
				motorlist[2].set(0);
			}
			
			//debug
			SmartDashboard.putBoolean("start pressed", joyDrive.getRawButton(startButton) );
			SmartDashboard.putBoolean("toggleNaut", toggleNaut.get() );
			SmartDashboard.putBoolean("nautLimit (false = pressed)", nautLimit.get() );
			SmartDashboard.putBoolean("nautTrigPressed", nautLimFirst);
		}
		
		//servo code

		//default position

		if(joyDrive.getRawButton(backButton)==true)
		{
			panVal=70;
			tiltVal=70;
			pan.setAngle(panVal);
			tilt.setAngle(tiltVal);
		}

		//PAN rotating left and right

		if(joyDrive.getPOV(0)==90 && panVal>=0 && panVal<=180)
		{
			panVal=panVal-5;
			if(panVal<0)
			{
				panVal = 0;
			}
			pan.setAngle(panVal);
		}

		if(joyDrive.getPOV(0)==270 && panVal>=0 && panVal<=180)
		{
			panVal=panVal+5;
			if(panVal>180)
			{
				panVal = 180;
			}
			pan.setAngle(panVal);
		}



		//TILT rotating up and down

		if(joyDrive.getPOV(0)== 0 && tiltVal >=0 && tiltVal <=180)
		{
			tiltVal = tiltVal-2;
			if(tiltVal < 0)
			{
				tiltVal = 0;
			}
			tilt.setAngle(tiltVal);
		}

		if(joyDrive.getPOV(0)== 180 && tiltVal >=0 && tiltVal <=180)
		{
			tiltVal = tiltVal+2;
			if(tiltVal > 180)
			{
				tiltVal = 180;
			}
			tilt.setAngle(tiltVal);
		}
		
		
		
		//Solenoid code
		
		toggleLift.checkForToggle(joyDrive.getRawButton(yButton)); //toggles value
		flyLift.set(toggleLift.get()); //sets based on toggle
		
		flyPush.set(joyDrive.getRawButton(aButton));
		

		//flywheel code
		
		if(joyDrive.getRawButton(leftButton))
		{
			motorlist[2].set(-1);
			motorlist[3].set(1);
		}
		else if(joyDrive.getRawButton(rightButton))
		{
			motorlist[2].set(1);
			motorlist[3].set(-1);
		}
		else
		{
			motorlist[2].set(0);
			motorlist[3].set(0);
		}
		
		//sets to false on first run
		firstRun = false;

	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}   
}

