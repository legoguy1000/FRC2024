package frc.robot.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.lib.util.FieldConstants;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.CommandFactory;
import frc.robot.subsystems.elevator_wrist.ElevatorWrist;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.swerve.Swerve;

/**
 * P321 Auto
 */
public class P321 extends SequentialCommandGroup {

    Swerve swerveDrive;
    ElevatorWrist elevatorWrist;
    Intake intake;
    Shooter shooter;

    /**
     * P321 Auto
     *
     * @param swerveDrive Swerve Drive Subsystem
     * @param elevatorWrist Elevator Wrist Subsystem
     * @param intake Intake Subsystem
     * @param shooter Shooter Subsystem
     */
    public P321(Swerve swerveDrive, ElevatorWrist elevatorWrist, Intake intake, Shooter shooter) {
        this.swerveDrive = swerveDrive;
        this.elevatorWrist = elevatorWrist;
        this.intake = intake;
        this.shooter = shooter;

        PathPlannerPath path1 = PathPlannerPath.fromPathFile("1 - Resnick 2 Shoot Initial Note");
        PathPlannerPath path2 = PathPlannerPath.fromPathFile("2 - Resnick 2 Intake P3");
        PathPlannerPath path3 = PathPlannerPath.fromPathFile("3 - Resnick 2 Intake P2");
        PathPlannerPath path4 = PathPlannerPath.fromPathFile("4 - Resnick 2 Intake P1");
        PathPlannerPath path5 = PathPlannerPath.fromPathFile("5 - Resnick 2 midline");

        Command wait = Commands.waitSeconds(.5);
        Command followPath1 = AutoBuilder.followPath(path1);
        Command followPath2 = AutoBuilder.followPath(path2);
        Command followPath3 = AutoBuilder.followPath(path3);
        Command followPath4 = AutoBuilder.followPath(path4);
        Command followPath5 = AutoBuilder.followPath(path5);

        Command resetPosition = Commands.runOnce(() -> {
            Pose2d initialState =
                FieldConstants.allianceFlip(path1.getPreviewStartingHolonomicPose());
            swerveDrive.resetOdometry(initialState);
        });
        SequentialCommandGroup part1 = followPath1
            .alongWith(
                elevatorWrist.goToPosition(Constants.ElevatorWristConstants.SetPoints.HOME_HEIGHT,
                    Rotation2d.fromDegrees(39.0)).withTimeout(1))
            .andThen(CommandFactory.Auto.runIndexer(intake));

        SequentialCommandGroup part2 = followPath2
            .alongWith(
                elevatorWrist.goToPosition(Constants.ElevatorWristConstants.SetPoints.HOME_HEIGHT,
                    Rotation2d.fromDegrees(37.0)).withTimeout(.5))
            .deadlineWith(CommandFactory.intakeNote(intake))
            .andThen(Commands.either(Commands.none(),
                Commands.sequence(CommandFactory.intakeNote(intake),
                    CommandFactory.Auto.runIndexer(intake)),
                () -> !intake.getintakeBeamBrakeStatus() && !intake.getIndexerBeamBrakeStatus()));

        SequentialCommandGroup part3 = followPath3
            .alongWith(
                elevatorWrist.goToPosition(Constants.ElevatorWristConstants.SetPoints.HOME_HEIGHT,
                    Rotation2d.fromDegrees(37.5)).withTimeout(.5))
            .deadlineWith(CommandFactory.intakeNote(intake))
            .andThen(Commands.either(Commands.none(),
                Commands.sequence(CommandFactory.intakeNote(intake),
                    CommandFactory.Auto.runIndexer(intake)),
                () -> !intake.getintakeBeamBrakeStatus() && !intake.getIndexerBeamBrakeStatus()));

        SequentialCommandGroup part4 = followPath4
            .alongWith(
                elevatorWrist.goToPosition(Constants.ElevatorWristConstants.SetPoints.HOME_HEIGHT,
                    Rotation2d.fromDegrees(37.0)).withTimeout(.5))
            .deadlineWith(CommandFactory.intakeNote(intake))
            .andThen(Commands.either(Commands.none(),
                Commands.sequence(CommandFactory.intakeNote(intake),
                    CommandFactory.Auto.runIndexer(intake)),
                () -> !intake.getintakeBeamBrakeStatus() && !intake.getIndexerBeamBrakeStatus()));

        Command part5 = Commands.either(followPath5.alongWith(CommandFactory.intakeNote(intake)),
            Commands.none(), () -> RobotContainer.goToCenter.getEntry().getBoolean(false));

        SequentialCommandGroup followPaths =
            part1.andThen(part2).andThen(part3).andThen(part4).andThen(part5);

        // Command autoAlignWrist = CommandFactory.autoAngleWristSpeaker(elevatorWrist,
        // swerveDrive);
        Command shootCommand = shooter.shootSpeaker();

        addCommands(resetPosition, wait, followPaths.alongWith(shootCommand));
    }
}
