package frc.robot;

import static edu.wpi.first.units.Units.Inches;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.studica.frc.AHRS.NavXComType;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.util.Color;
import frc.lib.util.FieldConstants;

/**
 * Constants file.
 */
public final class Constants {
    /**
     * Stick Deadband
     */
    public static final double STICK_DEADBAND = 0.1;

    /**
     * Driver ID
     */
    public static final int DRIVER_ID = 0;

    /**
     * Operator ID
     */
    public static final int OPERATOR_ID = 1;

    /**
     * How far in the future we should "lead" the aiming of the shooter for shooting while moving.
     */
    public static final double LEAD_GAIN = 0.3;


    /**
     * MoveToPos constants.
     */
    public static class SwerveTransformPID {
        public static final double PID_XKP = 3.5;
        public static final double PID_XKI = 0.0;
        public static final double PID_XKD = 0.0;
        public static final double PID_YKP = 3.5;
        public static final double PID_YKI = 0.0;
        public static final double PID_YKD = 0.0;
        public static final double PID_TKP = 3.0;
        public static final double PID_TKI = 0.0;
        public static final double PID_TKD = 0.0;

        public static final double MAX_ANGULAR_VELOCITY = 9.0;
        public static final double MAX_ANGULAR_ACCELERATION = 9 * 5;
        public static final double STD_DEV_MOD = 2.0;
    }

    /**
     * Motor CAN id's.
     */
    public static final class Motors {
        /**
         * Shooter Id's
         */
        public static final class Shooter {
            public static final int SHOOTER_TOP_ID = 13;
            public static final int SHOOTER_BOTTOM_ID = 15;
        }

        /**
         * Class for elevator and wrist motor constants
         */
        public static final class ElevatorWrist {
            public static final int ELEVATOR_RIGHT_NEO_ID = 57;
            public static final int ELEVATOR_LEFT_NEO_ID = 12;
            public static final int WRIST_NEO_ID = 52;
        }

        /**
         * Intake and indexer motor constants
         */
        public static final class Intake {
            public static final int INTAKE_MOTOR_ID_LEFT = 14;
            public static final int INTAKE_MOTOR_ID_RIGHT = 61;
            public static final int INDEXER_MOTOR_ID = 32;
        }

        /**
         * Climber motor constants
         */
        public static final class Climber {
            public static final int LEFT_MOTOR_ID = 60;
            public static final int RIGHT_MOTOR_ID = 48;
        }
    }

    /**
     * Camera offset constants.
     */
    public static class CameraConstants {

        public static double XY_STD_DEV_COEFF = 0.005;
        public static double THETA_STD_DEV_COEFF = 0.01;

        /**
         * Constants for Front Left Camera
         */
        public static class FrontLeftFacingCamera {
            public static final double ROLL = 0.0;
            public static final double PITCH = Math.toRadians(0);
            public static final double YAW = Math.toRadians(12.0);
            public static final Transform3d KCAMERA_TO_ROBOT = new Transform3d(
                new Translation3d(Units.inchesToMeters(17), Units.inchesToMeters(4.0),
                    Units.inchesToMeters(15)),
                new Rotation3d(ROLL, PITCH, YAW));

            public static final String CAMERA_NAME = "front-left";
            public static final String CAMERA_IP = "10.55.72.12";
            public static final double LARGEST_DISTANCE = 0.1;
        }

        /**
         * Constants for Front Right Camera
         */
        public static class FrontRightFacingCamera {
            public static final double ROLL = Math.toRadians(180);
            public static final double PITCH = Math.toRadians(0);
            public static final double YAW = Math.toRadians(-6);
            public static final Transform3d KCAMERA_TO_ROBOT = new Transform3d(
                new Translation3d(Units.inchesToMeters(17), Units.inchesToMeters(-5),
                    Units.inchesToMeters(15)),
                new Rotation3d(ROLL, PITCH, YAW));

            public static final String CAMERA_NAME = "front-right";
            public static final String CAMERA_IP = "10.55.72.10";
            public static final double LARGEST_DISTANCE = 0.1;
        }

        /**
         * Constants for Back Left Camera
         */
        public static class BackLeftFacingCamera {
            public static final double ROLL = 0.0;
            public static final double PITCH = Math.toRadians(0);
            public static final double YAW = Math.toRadians(184);
            public static final Transform3d KCAMERA_TO_ROBOT = new Transform3d(
                new Translation3d(Units.inchesToMeters(-13.0), Units.inchesToMeters(14),
                    Units.inchesToMeters(0)),
                new Rotation3d(ROLL, PITCH, YAW));

            public static final String CAMERA_NAME = "back-left";
            public static final String CAMERA_IP = "10.55.72.13";
            public static final double LARGEST_DISTANCE = 0.1;
        }

        // /**
        // * Constants for Back Right Camera
        // */
        // public static class BackRightFacingCamera {
        // public static final double ROLL = 0.0;
        // public static final double PITCH = Math.toRadians(0);
        // public static final double YAW = Math.toRadians(180);
        // public static final Transform3d KCAMERA_TO_ROBOT =
        // new Transform3d(new Translation3d(Units.inchesToMeters(12.831),
        // Units.inchesToMeters(-8.56), Units.inchesToMeters(17.85)),
        // new Rotation3d(ROLL, PITCH, YAW)).inverse();

        // public static final String CAMERA_NAME = "back-right";
        // // public static final String CAMERA_IP = "10.55.72.10";
        // public static final double LARGEST_DISTANCE = 0.1;
        // }

    }

    /**
     * Swerve Constants
     */
    public static final class Swerve {
        public static final double AUTO_ROTATION_KP = 5.0;
        public static final double AUTO_ROTATION_KI = 0.0;
        public static final double AUTO_ROTATION_KD = 0.0;

        public static final NavXComType navXID = NavXComType.kMXP_SPI;
        public static final boolean invertGyro = true;
        public static final boolean isFieldRelative = true;
        public static final boolean isOpenLoop = false;

        /* Drivetrain Constants */
        public static final double trackWidth = Units.inchesToMeters(23.75);
        public static final double wheelBase = Units.inchesToMeters(17.75);
        public static final Distance wheelDiameter = Inches.of(3.8);
        public static final Distance wheelCircumference = wheelDiameter.times(Math.PI);
        public static final Translation2d MOD0_MODOFFSET =
            new Translation2d(wheelBase / 2.0, trackWidth / 2.0);

        /*
         * Swerve Kinematics No need to ever change this unless you are not doing a traditional
         * rectangular/square 4 module swerve
         */
        public static final SwerveDriveKinematics swerveKinematics =
            new SwerveDriveKinematics(new Translation2d(wheelBase / 2.0, trackWidth / 2.0),
                new Translation2d(wheelBase / 2.0, -trackWidth / 2.0),
                new Translation2d(-wheelBase / 2.0, trackWidth / 2.0),
                new Translation2d(-wheelBase / 2.0, -trackWidth / 2.0));

        /* Module Gear Ratios */
        public static final double driveGearRatio = (8.14 / 1.0); // MK4i L1
        public static final double angleGearRatio = ((150.0 / 7.0) / 1.0); // (150 / 7) : 1

        /* Motor Inverts */
        public static final InvertedValue angleMotorInvert = InvertedValue.Clockwise_Positive;
        public static final InvertedValue driveMotorInvert =
            InvertedValue.CounterClockwise_Positive;

        /* Angle Encoder Invert */
        public static final SensorDirectionValue cancoderInvert =
            SensorDirectionValue.CounterClockwise_Positive;

        /* Swerve Current Limiting */
        public static final int angleCurrentLimit = 25;
        public static final int angleCurrentThreshold = 40;
        public static final double angleCurrentThresholdTime = 0.1;
        public static final boolean angleEnableCurrentLimit = true;

        public static final int driveCurrentLimit = 35;
        public static final int driveCurrentThreshold = 60;
        public static final double driveCurrentThresholdTime = 0.1;
        public static final boolean driveEnableCurrentLimit = true;

        /*
         * These values are used by the drive falcon to ramp in open loop and closed loop driving.
         * We found a small open loop ramp (0.25) helps with wear, tipping, etc
         */
        public static final double openLoopRamp = 0.25;
        public static final double closedLoopRamp = 0.0;

        /* Angle Motor PID Values */
        public static final double angleKP = 100.0;
        public static final double angleKI = 0.0;
        public static final double angleKD = 0.0;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.12;
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0;
        public static final double driveKF = 0.0;

        /* Drive Motor Characterization Values From SYSID */
        public static final double driveKS = 0.32;
        public static final double driveKV = 1.51;
        public static final double driveKA = 0.27;

        /* Swerve Profiling Values */
        /** Meters per Second */
        public static final double maxSpeed = 3.0;
        public static final double AUTO_MAX_SPEED = 3.0;
        /** Radians per Second */
        public static final double maxAngularVelocity = 4.0;

        /* Neutral Modes */
        public static final NeutralModeValue angleNeutralMode = NeutralModeValue.Coast;
        public static final NeutralModeValue driveNeutralMode = NeutralModeValue.Brake;

        /* Module Specific Constants */

        /**
         * Front Left Module - Module 0
         */
        public static final class Mod0 {
            public static final int driveMotorID = 6;
            public static final int angleMotorID = 51;
            public static final int canCoderID = 4;
            // public static final Rotation2d angleOffset = Rotation2d.fromDegrees(183.955078125);
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(-0.496826);

        }

        /**
         * Front Right Module - Module 1
         */
        public static final class Mod1 {
            public static final int driveMotorID = 2;
            public static final int angleMotorID = 40;
            public static final int canCoderID = 2;
            // public static final Rotation2d angleOffset = Rotation2d.fromDegrees(325.01953125);
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.405518 + 0.5);

        }

        /**
         * Back Left Module - Module 2
         */
        public static final class Mod2 {
            public static final int driveMotorID = 3;
            public static final int angleMotorID = 9;
            public static final int canCoderID = 1;
            // public static final Rotation2d angleOffset = Rotation2d.fromDegrees(124.62890625);
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.348145);

        }

        /**
         * Back Right Module - Module 3
         */
        public static final class Mod3 {
            public static final int driveMotorID = 10;
            public static final int angleMotorID = 8;
            public static final int canCoderID = 10;
            // public static final Rotation2d angleOffset = Rotation2d.fromDegrees(295.400390625);
            public static final Rotation2d angleOffset = Rotation2d.fromRotations(0.317627 + 0.5);
        }


        // public static final HolonomicPathFollowerConfig pathFollowerConfig =
        // new HolonomicPathFollowerConfig(new PIDConstants(5.0, 0, 0),
        // new PIDConstants(AUTO_ROTATION_KP, AUTO_ROTATION_KI, AUTO_ROTATION_KD),
        // // Drive base radius (distance from center to furthest module)
        // AUTO_MAX_SPEED, MOD0_MODOFFSET.getNorm(), new ReplanningConfig());
    }

    /**
     * Climber constants
     */
    public static final class ClimberConstants {
        public static final double CLIMBER_KP = 0.1;
        public static final double CLIMBER_KI = 0.1;
        public static final double CLIMBER_KD = 0.1;
        public static final double CLIMBER_MAX_VELOCITY = 0;
        public static final double CLIMBER_MAX_ACCELERATION = 0;
        public static final double CLIMBER_KS = 0.1;
        public static final double CLIMBER_KG = 0.1;
        public static final double CLIMBER_KV = 0.1;
        public static final double CLIMBER_POWER = 0.8;

        public static final double CLIMBING_DISTANCE = Units.inchesToMeters(15);
        public static final double MAX_CLIMBING_DISTANCE = Units.inchesToMeters(21);

        // 2pi * radius
        public static final double LINEAR_DISTANCE = Units.inchesToMeters(2 * Math.PI * 1);
    }

    /**
     * Auto constants
     */
    public static final class AutoConstants {
        public static final double kMaxSpeedMetersPerSecond = 3;
        public static final double kMaxAccelerationMetersPerSecondSquared = 3;
        public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
        public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

        public static final double kPXController = 1;
        public static final double kPYController = 1;
        public static final double kPThetaController = 1;

        /* Constraint for the motion profilied robot angle controller */
        public static final TrapezoidProfile.Constraints kThetaControllerConstraints =
            new TrapezoidProfile.Constraints(kMaxAngularSpeedRadiansPerSecond,
                kMaxAngularSpeedRadiansPerSecondSquared);
    }

    /**
     * Class for elevator and wrist constants
     */
    public static final class ElevatorWristConstants {

        public static final Rotation2d WRIST_REF_1_ANGLE_MEASURED = Rotation2d.fromRotations(0.454);
        public static final Rotation2d WRIST_REF_2_ANGLE_MEASURED = Rotation2d.fromRotations(0.585);
        public static final Rotation2d WRIST_REF_1_ANGLE_ACTUAL = Rotation2d.fromDegrees(0.0);
        public static final Rotation2d WRIST_REF_2_ANGLE_ACTUAL = Rotation2d.fromDegrees(45.0);

        public static final double WRIST_M;
        public static final double WRIST_B;

        static {
            WRIST_M =
                (WRIST_REF_2_ANGLE_ACTUAL.getRotations() - WRIST_REF_1_ANGLE_ACTUAL.getRotations())
                    / (WRIST_REF_2_ANGLE_MEASURED.getRotations()
                        - WRIST_REF_1_ANGLE_MEASURED.getRotations());
            // meas_1 * m + b = act_1
            // b = act_1 - meas_1 * m
            WRIST_B = WRIST_REF_1_ANGLE_ACTUAL.getRotations()
                - WRIST_REF_1_ANGLE_MEASURED.getRotations() * WRIST_M;
        }

        public static final Rotation2d ELEVATOR_REF_1_ANGLE_MEASURED =
            Rotation2d.fromRotations(0.0);
        public static final Rotation2d ELEVATOR_REF_2_ANGLE_MEASURED =
            Rotation2d.fromRotations(-10030.96);
        public static final double ELEVATOR_REF_1_HEIGHT = 24.0;
        public static final double ELEVATOR_REF_2_HEIGHT = 44.75;

        public static final double ELEVATOR_M;
        public static final double ELEVATOR_B;

        static {
            ELEVATOR_M = (ELEVATOR_REF_2_HEIGHT - ELEVATOR_REF_1_HEIGHT)
                / (ELEVATOR_REF_2_ANGLE_MEASURED.getRotations()
                    - ELEVATOR_REF_1_ANGLE_MEASURED.getRotations());
            // meas_1 * m + b = act_1
            // b = act_1 - meas_1 * m
            ELEVATOR_B =
                ELEVATOR_REF_1_HEIGHT - ELEVATOR_REF_1_ANGLE_MEASURED.getRotations() * ELEVATOR_M;
        }

        /**
         * PID constants
         */
        public static final class PID {

            public static final double ELEVATOR_KP = 10.0;
            public static final double ELEVATOR_KI = 0;
            public static final double ELEVATOR_KD = 0;
            public static final double ELEVATOR_MAX_VELOCITY = 320;
            public static final double ELEVATOR_MAX_ACCELERATION = 200;
            public static final double ELEVATOR_KS = 0;
            public static final double ELEVATOR_KG = 0;
            public static final double ELEVATOR_KV = 0;

            public static final double WRIST_KP = 110;
            public static final double WRIST_AMP_KP = 150;
            public static final double WRIST_LARGE_KP = 60;
            public static final double WRIST_KI = 0.1;
            public static final double WRIST_AMP_KI = 0;
            public static final double WRIST_KD = 0.15;
            public static final double WRIST_AMP_KD = 0;
            public static final double WRIST_MAX_VELOCITY = 0.000001;
            public static final double WRIST_MAX_ACCELERATION = 0.0000000001;
            public static final double WRIST_KS = 0;
            public static final double WRIST_KG = 0;
            public static final double WRIST_KV = 0;
            public static final double WRIST_LOWPASS = 1;
        }

        /**
         * Set points constants for elevator and wrist
         */
        public static final class SetPoints {

            public static final double HOME_HEIGHT = 24;
            public static final Rotation2d INTAKE_ANGLE = Rotation2d.fromDegrees(39);
            public static final Rotation2d HOME_ANGLE = Rotation2d.fromDegrees(5);
            // public static final double AMP_HEIGHT = Units.inchesToMeters(34);
            public static final double AMP_HEIGHT = 43.5;
            public static final Rotation2d AMP_ANGLE = Rotation2d.fromDegrees(-29.95);
            // public static final double TRAP_HEIGHT = Units.inchesToMeters(40);
            public static final double TRAP_HEIGHT = 44;
            public static final Rotation2d TRAP_ANGLE = Rotation2d.fromDegrees(-10);
            // public static final double MAX_EXTENSION = Units.inchesToMeters(48);
            public static final double MAX_EXTENSION = 43.5;
            public static final double CLIMBING_HEIGHT = 40.0;
            public static final Rotation2d CLIMBING_ANGLE = Rotation2d.fromDegrees(-40);
            public static final Rotation2d MIN_ANGLE = Rotation2d.fromDegrees(-44);
            public static final Rotation2d MAX_ANGLE = Rotation2d.fromDegrees(50);

            public static final double LINEAR_DISTANCE = Units.inchesToMeters(2 * Math.PI * 659);

            public static final Rotation2d PODIUM_ANGLE = Rotation2d.fromDegrees(33.0);

        }


    }
    /**
     * Pneumatics CAN id constants.
     */

    public static final class Pneumatics {
    }



    /**
     * Constants of Shooters
     */
    public static final class ShooterConstants {
        public static final double KP = 0.0014;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KS = 0;
        public static final double TOP_KV = 6.18e-4;
        public static final double BOTTOM_KV = 6.18e-4;
        public static final double GEAR_RATIO = 3; // gear ratio
        public static final double HEIGHT_FROM_LOWEST_POS = Units.inchesToMeters(32.0);
        public static final double HEIGHT_FROM_SPEAKER =
            FieldConstants.centerSpeaker - HEIGHT_FROM_LOWEST_POS;
        public static final double SHOOT_SPEAKER_RPM = 11250.0;
    }

    /**
     * Constants for intake
     */
    public static final class IntakeConstants {
        public static final double INTAKE_MOTOR_FORWARD = 0;
        public static final double INTAKE_MOTOR_BACKWARD = -0;
        public static final double INTAKE_MOTOR_STOP = 0;
        public static final double INDEX_MOTOR_FORWARD = .2;
        public static final double INDEX_MOTOR_BACKWARD = -0;
        public static final double INDEX_MOTOR_STOP = 0;
        public static final boolean INTAKE_MOTOR_INVERTED = true;

        public static final int INDEXER_BEAM_BRAKE_DIO_PORT = 8;
        public static final int INTAKE_BEAM_BRAKE_DIO_PORT = 6;
    }

    /**
     * LED constants.
     */
    public static final class LEDConstants {
        public static final int PWM_PORT = 9;
        public static final int LED_COUNT = 60;
        public static final Color INTAKE_COLOR = Color.kGreen;
        public static final Color INDEXER_COLOR = Color.kPurple;
        public static final Color ALERT_COLOR = Color.kWhite;
    }

}
