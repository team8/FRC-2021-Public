import static com.palyrobotics.frc2020.config.constants.ShooterConstants.kTimeToShootPerBallSeconds;
import static org.junit.jupiter.api.Assertions.*;

import com.palyrobotics.frc2020.robot.Commands;
import com.palyrobotics.frc2020.robot.RobotState;
import com.palyrobotics.frc2020.subsystems.Shooter;
import com.palyrobotics.frc2020.util.Util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ShooterTests {

	Shooter shooter = Shooter.getInstance();
	Commands commands = new Commands();
	RobotState state = new RobotState();

	@Test
	@Tag ("slow")
	public void testRumble() throws InterruptedException {

		// First tests are making sure the rumble is off when it needs to be
		assertFalse(shooter.getRumbleOutput());
		shooter.update(commands, state);
		assertFalse(shooter.getRumbleOutput());

		commands.setShooterIdleState();
		shooter.update(commands, state);
		assertFalse(shooter.getRumbleOutput());

		commands.setShooterCustomState(10, Shooter.HoodState.LOW);
		shooter.update(commands, state);
		assertFalse(shooter.getRumbleOutput());

		state.shooterFlywheelVelocity = 2;
		shooter.update(commands, state);
		assertFalse(shooter.getRumbleOutput());

		// Second set of tests are making sure that the rumble turns on when it needs to turn off
		state.shooterFlywheelVelocity = 10;
		state.shooterBlockingSolenoidState = false;
		state.shooterHoodSolenoidState = false;
		shooter.update(commands, state);
		assertTrue(shooter.getRumbleOutput());

		Thread.sleep((long) (Util.clamp(kTimeToShootPerBallSeconds - 0.1 * 1000, 0, Double.MAX_VALUE)));
		shooter.update(commands, state);
		assertTrue(shooter.getRumbleOutput());

		Thread.sleep(200);
		shooter.update(commands, state);
		assertFalse(shooter.getRumbleOutput());
	}

	@Test
	public void testHoodState() {

	}
}
