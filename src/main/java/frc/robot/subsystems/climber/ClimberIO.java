package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

/**
 * Subsystem for climber IO
 */
public interface ClimberIO {

    /**
     * Auto logs designated climber inputs
     */
    @AutoLog
    public static class ClimberInputs {
        public double climberLeftMotorVoltage;
        public double climberLeftMotorAmp;
        public double climberRightMotorVoltage;
        public double climberRightMotorAmp;
        public double leftMotorEncoderValue;
        public double rightMotorEncoderValue;

    }

    public default void updateInputs(ClimberInputs inputs) {}

    public default void setClimberVoltage(double volts) {}
}
