package frc.robot.subsystems.swerve.drive;

import org.ironmaple.simulation.drivesims.GyroSimulation;
import edu.wpi.first.units.Units;

/** Gyro implementation for Maple-Sim */
public class SwerveSim implements SwerveIO {

    private final GyroSimulation simulation;

    /** Create new sim drivetrain */
    public SwerveSim(GyroSimulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void updateInputs(SwerveInputs inputs) {
        inputs.connected = true;
        inputs.yawPosition = simulation.getGyroReading();
        inputs.yawVelocityRadPerSec =
            simulation.getMeasuredAngularVelocity().in(Units.RadiansPerSecond);
    }

}
