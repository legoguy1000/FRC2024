package frc.robot.subsystems.swerve;

import java.util.Optional;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import org.photonvision.EstimatedRobotPose;
import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.swerve.SwerveModule;
import frc.robot.Constants;

/**
 * Swerve Subsystem
 */
public class Swerve extends SubsystemBase {
    public SwerveDrivePoseEstimator swerveOdometry;
    public SwerveModule[] swerveMods;
    private final Field2d field = new Field2d();
    private double fieldOffset;
    private SwerveInputsAutoLogged inputs = new SwerveInputsAutoLogged();
    private SwerveIO swerveIO;
    private boolean hasInitialized = false;
    private boolean latencyGood = false;

    // private GenericEntry aprilTagTarget =
    // RobotContainer.autoTab.add("Currently Seeing At Least One April Tag", false)
    // .withWidget(BuiltInWidgets.kBooleanBox)
    // .withProperties(Map.of("Color when true", "green", "Color when false", "red"))
    // .withPosition(8, 4).withSize(2, 2).getEntry();

    /**
     * Swerve Subsystem
     */
    public Swerve(SwerveIO swerveIO) {
        this.swerveIO = swerveIO;
        swerveIO.updateInputs(inputs, swerveOdometry.getEstimatedPosition());
        fieldOffset = getGyroYaw().getDegrees();
        swerveMods = new SwerveModule[] {
            swerveIO.createSwerveModule(0, Constants.Swerve.Mod0.DRIVE_MOTOR_ID,
                Constants.Swerve.Mod0.ANGLE_MOTOR_ID, Constants.Swerve.Mod0.CAN_CODER_ID,
                Constants.Swerve.Mod0.ANGLE_OFFSET),
            swerveIO.createSwerveModule(1, Constants.Swerve.Mod1.DRIVE_MOTOR_ID,
                Constants.Swerve.Mod1.ANGLE_MOTOR_ID, Constants.Swerve.Mod1.CAN_CODER_ID,
                Constants.Swerve.Mod1.ANGLE_OFFSET),
            swerveIO.createSwerveModule(2, Constants.Swerve.Mod2.DRIVE_MOTOR_ID,
                Constants.Swerve.Mod2.ANGLE_MOTOR_ID, Constants.Swerve.Mod2.CAN_CODER_ID,
                Constants.Swerve.Mod2.ANGLE_OFFSET),
            swerveIO.createSwerveModule(3, Constants.Swerve.Mod3.DRIVE_MOTOR_ID,
                Constants.Swerve.Mod3.ANGLE_MOTOR_ID, Constants.Swerve.Mod3.CAN_CODER_ID,
                Constants.Swerve.Mod3.ANGLE_OFFSET)};

        swerveOdometry = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics,
            getGyroYaw(), getModulePositions(), new Pose2d());

        AutoBuilder.configureHolonomic(this::getPose, this::resetOdometry, this::getChassisSpeeds,
            this::setModuleStates, Constants.Swerve.pathFollowerConfig, () -> shouldFlipPath(),
            this);
        SmartDashboard.putBoolean("Currently Seeing At Least One April Tag", false);


        // RobotContainer.autoTab.add("Field Pos", field).withWidget(BuiltInWidgets.kField)
        // .withSize(8, 6) // make the widget 2x1
        // .withPosition(0, 0); // place it in the top-left corner
        SmartDashboard.putData("Field Pos", field);
    }

    /**
     * Tele-Op Drive method
     *
     * @param translation The magnitude in XY
     * @param rotation The magnitude in rotation
     * @param fieldRelative Whether or not field relative
     * @param isOpenLoop Whether or not Open or Closed Loop
     */
    public void drive(Translation2d translation, double rotation, boolean fieldRelative,
        boolean isOpenLoop) {
        ChassisSpeeds chassisSpeeds = fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(),
                rotation, getFieldRelativeHeading())
            : new ChassisSpeeds(translation.getX(), translation.getY(), rotation);

        setModuleStates(chassisSpeeds);
    }

    /**
     * Set Swerve Module States
     *
     * @param desiredStates Array of desired states
     */
    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(desiredStates, Constants.Swerve.maxSpeed);

        for (SwerveModule mod : swerveMods) {
            mod.setDesiredState(desiredStates[mod.moduleNumber], false);
        }
    }

    /**
     * Sets swerve module states using Chassis Speeds.
     *
     * @param chassisSpeeds The desired Chassis Speeds
     */
    public void setModuleStates(ChassisSpeeds chassisSpeeds) {
        ChassisSpeeds targetSpeeds = ChassisSpeeds.discretize(chassisSpeeds, 0.02);
        SwerveModuleState[] swerveModuleStates =
            Constants.Swerve.swerveKinematics.toSwerveModuleStates(targetSpeeds);
        setModuleStates(swerveModuleStates);
    }

    /**
     * Get current Chassis Speeds
     *
     * @return The current {@link ChassisSpeeds}
     */
    public ChassisSpeeds getChassisSpeeds() {
        return Constants.Swerve.swerveKinematics.toChassisSpeeds(getModuleStates());
    }

    /**
     * Get Swerve Module States
     *
     * @return Array of Swerve Module States
     */
    public SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (SwerveModule mod : swerveMods) {
            states[mod.moduleNumber] = mod.getState();
        }
        return states;
    }

    /**
     * Get Swerve Module Positions
     *
     * @return Array of Swerve Module Positions
     */
    public SwerveModulePosition[] getModulePositions() {
        SwerveModulePosition[] positions = new SwerveModulePosition[4];
        for (SwerveModule mod : swerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    /**
     * Get Position on field from Odometry
     *
     * @return Pose2d on the field
     */
    @AutoLogOutput(key = "Odometry/Robot")
    public Pose2d getPose() {
        return swerveOdometry.getEstimatedPosition();
    }

    /**
     * Set the position on the field with given Pose2d
     *
     * @param pose Pose2d to set
     */
    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(getGyroYaw(), getModulePositions(), pose);
    }

    /**
     * Get Rotation of robot from odometry
     *
     * @return Heading of robot relative to the field as {@link Rotation2d}
     */
    public Rotation2d getHeading() {
        return getPose().getRotation();
    }

    /**
     * Get Rotation from the gyro
     *
     * @return Current rotation/yaw of gyro as {@link Rotation2d}
     */
    public Rotation2d getGyroYaw() {
        float yaw = inputs.yaw;
        return (Constants.Swerve.invertGyro) ? Rotation2d.fromDegrees(-yaw)
            : Rotation2d.fromDegrees(yaw);
    }

    /**
     * Get Field Relative Heading
     *
     * @return The current field relative heading in {@link Rotation2d}
     */
    public Rotation2d getFieldRelativeHeading() {
        return Rotation2d.fromDegrees(getGyroYaw().getDegrees() - fieldOffset);
    }

    /**
     * Resets the gyro field relative driving offset
     */
    public void resetFieldRelativeOffset() {
        // gyro.zeroYaw();
        fieldOffset = getGyroYaw().getDegrees();
    }

    /**
     * Reset all modules to their front facing position
     */
    public void resetModulesToAbsolute() {
        for (SwerveModule mod : swerveMods) {
            mod.resetToAbsolute();
        }
    }

    @Override
    public void periodic() {
        swerveOdometry.update(getGyroYaw(), getModulePositions());
        swerveIO.updateInputs(inputs, swerveOdometry.getEstimatedPosition());
        Logger.processInputs("Swerve", inputs);
        for (double latency : inputs.latencies) {
            latencyGood = latency < 0.6 ? true : false;
        }
        SmartDashboard.putBoolean("photonGood", latencyGood);
        Rotation2d yaw = Rotation2d.fromDegrees(inputs.yaw);
        swerveOdometry.update(yaw, getSwerveModulePositions());
        if (!hasInitialized /* || DriverStation.isDisabled() */) {
            var robotPose = inputs.positions[0];
            if (robotPose.isPresent()) {
                swerveOdometry.resetPosition(Rotation2d.fromDegrees(inputs.yaw),
                    getSwerveModulePositions(), robotPose.get().toPose2d());
                hasInitialized = true;
            }
        } else {
            for (Optional<EstimatedRobotPose> estimatedPose : inputs.estimatedRobotPose) {
                if (estimatedPose.isPresent()) {
                    var camPose = estimatedPose.get();
                    if (camPose.targetsUsed.get(0).getArea() > 0.7) {
                        swerveOdometry.addVisionMeasurement(camPose.estimatedPose.toPose2d(),
                            camPose.timestampSeconds);
                    }
                    field.getObject("Cam Est Pose").setPose(camPose.estimatedPose.toPose2d());
                } else {
                    field.getObject("Cam Est Pose")
                        .setPose(new Pose2d(-100, -100, new Rotation2d()));
                }
            }
        }

        field.setRobotPose(getPose());

        boolean targetSeen = false;
        for (boolean seen : inputs.seesTarget) {
            if (seen) {
                targetSeen = seen;
                break;
            }
        }
        SmartDashboard.putBoolean("Currently Seeing At Least One April Tag", targetSeen);
        SmartDashboard.putBoolean("Has Initialized", hasInitialized);
        SmartDashboard.putNumber("Robot X", getPose().getX());
        SmartDashboard.putNumber("Robot Y", getPose().getY());
        SmartDashboard.putNumber("Robot Rotation", getPose().getRotation().getDegrees());
        SmartDashboard.putNumber("Gyro Yaw", yaw.getDegrees());
        SmartDashboard.putNumber("Field Offset", fieldOffset);
        SmartDashboard.putNumber("Gyro Yaw - Offset", getFieldRelativeHeading().getDegrees());
        SmartDashboard.putNumber("Gyro roll", inputs.roll);
        for (SwerveModule mod : swerveMods) {
            mod.periodic();
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " CANcoder",
                mod.getCANcoder().getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Angle",
                mod.getPosition().angle.getDegrees());
            SmartDashboard.putNumber("Mod " + mod.moduleNumber + " Velocity",
                mod.getState().speedMetersPerSecond);
        }
    }

    /**
     * Sets motors to 0 or inactive.
     *
     * @param isOpenLoop Open or closed loop system
     * @param fieldRelative Whether the movement is relative to the field or absolute
     */
    public void setMotorsZero(boolean isOpenLoop, boolean fieldRelative) {
        System.out.println("Setting Zero!!!!!!");
        setModuleStates(new ChassisSpeeds(0, 0, 0));
    }

    /**
     * Make an X pattern with the wheels
     */
    public void wheelsIn() {
        swerveMods[0].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(45)), false);
        swerveMods[1].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(135)), false);
        swerveMods[2].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(-45)), false);
        swerveMods[3].setDesiredState(new SwerveModuleState(2, Rotation2d.fromDegrees(-135)),
            false);
        this.setMotorsZero(Constants.Swerve.isOpenLoop, Constants.Swerve.isFieldRelative);
    }

    public SwerveModulePosition[] getSwerveModulePositions() {
        SwerveModulePosition positions[] = new SwerveModulePosition[4];
        for (SwerveModule mod : swerveMods) {
            positions[mod.moduleNumber] = mod.getPosition();
        }
        return positions;
    }

    /**
     * Determine whether or not to flight the auto path
     *
     * @return True if flip path to Red Alliance, False if Blue
     */
    public static boolean shouldFlipPath() {
        Optional<Alliance> ally = DriverStation.getAlliance();
        if (ally.isPresent()) {
            return ally.get() == Alliance.Red;
        }
        return false;
    }

}
