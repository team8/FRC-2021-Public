# FRC 2021

Team 8's 2021 FRC code for [Nari](http://palyrobotics.com/robots/). Our code is written in Java with our path visualiser written in python.

## Robot

### Subsystems

![Robot](https://i.imgur.com/m5wDzObh.jpg)

* [Drivetrain](src/main/java/com/palyrobotics/frc2020/subsystems/Drive.java)

    Our drivetrain uses a 6 wheel west coast drive powered by 4 Falcon 500s. It can reach a top speed of 13.5 ft (ca. 4 m)/sec.

* [Shooter](src/main/java/com/palyrobotics/frc2020/subsystems/Shooter.java)

    Our shooter subsystem uses a pneumatically adjustable hood with 3 states. We also use 2 NEO motors to shoot the balls
    from anywhere between the front of the control panel and the target zone. In order to choose between the hood states
    and the motors' velocity we use a Limelight to get the distance and an interpolating tree map to use that distance
    to find the needed velocities.

* [Intake](src/main/java/com/palyrobotics/frc2020/subsystems/Intake.java)

    Our intake subsystem uses a virtual 4-bar intake and utilizes stationary sprocket and chain system to fold inwards
    when stowed or impacted.

* [Indexer](src/main/java/com/palyrobotics/frc2020/subsystems/Indexer.java)

    Our indexer subsystem uses a 5 ball linear indexer with “V” configured belts for internal ball singulating

* [Climber](src/main/java/com/palyrobotics/frc2020/subsystems/Climber.java)

    Our climber subsystem is a telescoping climber which enables sub 5 sec. climbs when the switch is tipped or stationary

* [Spinner](src/main/java/com/palyrobotics/frc2020/subsystems/Spinner.java)

    Our spinner subsystem uses IDK HOW THIS STUFF WORKS

* [Lighting](src/main/java/com/palyrobotics/frc2020/subsystems/Lighting.java)

    LED animations allow for better driver and operator synchronization used to indicate bot alignment, climber lock,
    power cell pickup, ball launches and more.

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
    
    ![auto simulator example](auto_simulator/resources/BarrelRacingSimulation.gif)
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

## Sponsors

|![](http://palyrobotics.com/assets/img/sponsors/BayerFund.png)|![](http://palyrobotics.com/assets/img/sponsors/KLA.png)|![](http://palyrobotics.com/assets/img/sponsors/TEconnectivity.png)|![](http://palyrobotics.com/assets/img/sponsors/apple.svg)|
| --- | --- | --- | --- |
|![](http://palyrobotics.com/assets/img/sponsors/GHF.jpg)|![](http://palyrobotics.com/assets/img/sponsors/Intuitive_Foundation.png)|![](http://palyrobotics.com/assets/img/sponsors/arm.png)|![](http://palyrobotics.com/assets/img/sponsors/NASA.png)|
|![](http://palyrobotics.com/assets/img/sponsors/d&k.gif)|![](http://palyrobotics.com/assets/img/sponsors/markforged.png)|![](http://palyrobotics.com/assets/img/sponsors/solidworks.png)|![](http://palyrobotics.com/assets/img/sponsors/dropbox.jpg)|
|![](http://palyrobotics.com/assets/img/sponsors/kirks.png)|![](http://palyrobotics.com/assets/img/sponsors/Asiain_Box.jpeg)|![](http://palyrobotics.com/assets/img/sponsors/robby.png)|![](http://palyrobotics.com/assets/img/sponsors/emotiv.png)|

## Licence

Our code is released under the DO WE NEED A LICENSE? A copy of this license is included in the ________ file.
