# FRC 2021

Team 8's 2021 FRC code for [Nari](http://palyrobotics.com/robots/). Our code is written in Java with our path visualiser written in python.

## Robot

### Subsystems

![Robot](https://i.imgur.com/m5wDzObh.jpg)

* [Drivetrain](src/main/java/com/palyrobotics/frc2020/subsystems/Drive.java)

    ![Drivetrain]()

    Our drivetrain uses (6 motors?). IDK HOW THIS STUFF WORKS

* [Shooter](src/main/java/com/palyrobotics/frc2020/subsystems/Shooter.java)

    ![Shooter]()

    Our shooter subsystem uses IDK HOW THIS STUFF WORKS

* [Intake](src/main/java/com/palyrobotics/frc2020/subsystems/Intake.java)

    ![Intake]()

    Our intake subsystem uses IDK HOW THIS STUFF WORKS

* [Indexer](src/main/java/com/palyrobotics/frc2020/subsystems/Indexer.java)

    ![Indexer]()

    Our indexer subsystem uses IDK HOW THIS STUFF WORKS

* [Climber](src/main/java/com/palyrobotics/frc2020/subsystems/Climber.java)

    ![Climber]()

    Our climber subsystem uses IDK HOW THIS STUFF WORKS

* [Spinner](src/main/java/com/palyrobotics/frc2020/subsystems/Spinner.java)

    ![Spinner]()

    Our spinner subsystem uses IDK HOW THIS STUFF WORKS

* [Lighting](src/main/java/com/palyrobotics/frc2020/subsystems/Lighting.java)

    ![Lighting]()

    In order to communicate with the drivers and give valuable information such as if our robot is ready to shoot,
    we use LEDs.


## Setup Instructions

### General
1. Clone this repo with ``git clone https://github.com/team8/FRC-2021-Private.git``
2. ``./gradlew build`` - builds the code
3. ``./gradlew simulateJava`` - simulates the robot code locally
4. ``./gradlew deploy`` - deploys the code on to the robot
5. Have fun!

### IDE
We recommend using IntelliJ, however, Visual Studio Code and Eclipse both work.

## Code

### Highlights
* Path following with RAMSETE controller.
    
    We use RAMSETE in order to generate paths for our autos. We found this to be better than the Adaptive Pure
    Pursuit that we used before.

* Vision for target detection
    
    We use Limelight in order to find the distance to the target and use custom OpenCV code in order to find the 
    locations of balls.
  
* Auto grapher
    
    Graphs each auto we code in order to be able to develop auto's at home during the COVID-19 pandemic.
    This sped up out auto development during lockdown and allowed multiple people to test auto's at the same time.

### Packages
* [com.palyrobotics.frc2020](src/main/java/com/palyrobotics/frc2020)

    Contains all the robot code.
  
* [com.palyrobotics.frc2020.robot](src/main/java/com/palyrobotics/frc2020/robot)
    
    Contains all the central classes and functions used in the robot. We [RobotState](src/main/java/com/palyrobotics/frc2020/robot/RobotState.java) 
  to keep data on the state of the robot (velocity, vision targets, spinner hood state), and we use
  [Commands](src/main/java/com/palyrobotics/frc2020/robot/Commands.java) in order to state we want to be done
  (set the shooter hood to medium, go faster, ect.). The classes 
  [HardwareAdapter](src/main/java/com/palyrobotics/frc2020/robot/HardwareAdapter.java), 
  [HardwareReader](src/main/java/com/palyrobotics/frc2020/robot/HardwareReader.java), and
  [HardwareWriter](src/main/java/com/palyrobotics/frc2020/robot/HardwareWriter.java)
  all deal with interfacing with the  actual hardware of our robot.
  
* [com.palyrobotics.frc2020.subsystems](src/main/java/com/palyrobotics/frc2020/subsystems)
  
    Contains all the subsystems. Each subsystem takes an instance of [RobotState](src/main/java/com/palyrobotics/frc2020/robot/RobotState.java)
  and [Commands](src/main/java/com/palyrobotics/frc2020/robot/Commands.java) in order to take what is wanted
  and turn it into something that [HardwareWriter](src/main/java/com/palyrobotics/frc2020/robot/HardwareWriter.java) can use.
  
* [com.palyrobotics.frc2020.behavior](src/main/java/com/palyrobotics/frc2020/behavior)
  
    Handles all the [Routines](src/main/java/com/palyrobotics/frc2020/behavior/RoutineBase.java).
  A Routine is a class that run's for a set period of time and updates commands in order to do something.
  Examples include shooting one ball, shooting 5 balls, driving along a path, ect.

* [com.palyrobotics.frc2020.auto](src/main/java/com/palyrobotics/frc2020/auto)
  
    Handles all the auto's. Each auto is a list of Routines.

* [com.palyrobotics.frc2020.util](src/main/java/com/palyrobotics/frc2020/util)
  
    Contains a lot of utility classes and functions. Also contains things that don't belong anywhere else.

* [com.palyrobotics.frc2020.config](src/main/java/com/palyrobotics/frc2020/config)
  
    Contains both constants and configs that can be reloaded without recompiling.

* [auto_simulator](auto_simulator)
  
    Python code to simulate the auto's. Also contains the csv files for each auto. TODO: EXPLAIN HOW THIS WORKS

### Naming Conventions

* k**** (i.e. ``kTimeToShootPerBallSeconds``): final constants
* m**** (i.e. ``mOutputs``): private variables

## Licence

Our code is released under the DO WE NEED A LICENSE? A copy of this license is included in the ________ file.