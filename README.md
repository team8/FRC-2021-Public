# FRC 2021

Team 8's 2021 FRC code for [Nari](http://palyrobotics.com/robots/). Our code is written in Java with our path visualiser written in python.

## Setup Instructions

### General
1. Clone this repo with ``git clone https://github.com/team8/FRC-2021-Private.git``
2. Run ``./gradlew`` to download gradle and all the needed dependencies
2. Use ``./gradlew tasks`` to see what can be run
4. Have fun!

### IDE
We recommend using IntelliJ, however, Visual Studio Code and Eclipse both work.

### Gradle Commands
* ``./gradlew build`` - builds the code
* ``./gradlew simulateJava`` - simulates the robot code locally
* ``./gradlew deploy`` - deploys the code on to the robot

## Code

### Highlights
* Path following with RAMSETE controller.
    
    TODO: WRITE STUFF ABOUT THIS

* Vision for target detection
    
    We use Limelight in order to find the distance to the target and use custom OpenCV code in order to find the 
    locations of balls.
  
* TODO: MORE BAIT FEATURES

### Packages
* com.palyrobotics.frc2020

    Contains all the robot code.
  
* com.palyrobotics.frc2020.robot
    
    Contains all of the cennteral classes and functions used in the robot. We RobotState to keep data on the state of the robot
  (velocity, vision targets, spinner hood state) and we use Commands in order to state we want to be done (set the shooter hood to medium, go faster, ect.).
  The classes HardwareAdapter, HardwareReader, and HardwareWriter all deal with interfacing with the  actual hardware of our robot.
  
* com.palyrobotics.frc2020.subsystems
  
    Contains all the subsystems. Each subsystem takes an instance of RobotState and Commands in order to take what is wanted
  and turn it into something that HardwareWriter can use.
  
* com.palyrobotics.frc2020.behavior
  
    Handles all the Routines. A Routine is a class that run's for a set period of time and updates commands in order to do something.
  Examples include shooting one ball, shooting 5 balls, driving along a path, ect.

* com.palyrobotics.frc2020.auto
  
    Handles all the auto's. Each auto is a list of Routines.

* com.palyrobotics.frc2020.util
  
    Contains a lot of utility classes and functions. Also contains things that don't belong anywhere else.

* com.palyrobotics.frc2020.config
  
    Contains both constants and configs that can be reloaded without recompiling.
  
### Naming Conventions

* k**** (i.e. ``kTimeToShootPerBallSeconds``): final constants
* m**** (i.e. ``mOutputs``): private variables