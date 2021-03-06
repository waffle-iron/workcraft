package org.workcraft.plugins.mpsat.commands;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.workcraft.Framework;
import org.workcraft.gui.MainWindow;
import org.workcraft.gui.graph.commands.AbstractVerificationCommand;
import org.workcraft.interop.Importer;
import org.workcraft.plugins.mpsat.MpsatChainResultHandler;
import org.workcraft.plugins.mpsat.tasks.MpsatConformationTask;
import org.workcraft.plugins.petri.Transition;
import org.workcraft.plugins.stg.Stg;
import org.workcraft.plugins.stg.StgModel;
import org.workcraft.plugins.stg.StgUtils;
import org.workcraft.plugins.stg.interop.DotGImporter;
import org.workcraft.workspace.WorkspaceEntry;
import org.workcraft.workspace.WorkspaceUtils;

public class MpsatConformationVerificationCommand extends AbstractVerificationCommand {

    private static final String TITLE = "Conformation check";

    @Override
    public String getDisplayName() {
        return "   Conformation (without dummies) [MPSat]...";
    }

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, StgModel.class);
    }

    @Override
    public Position getPosition() {
        return Position.BOTTOM;
    }

    @Override
    public final void run(WorkspaceEntry we) {
        final Framework framework = Framework.getInstance();
        final MainWindow mainWindow = framework.getMainWindow();
        Stg stg = WorkspaceUtils.getAs(we, Stg.class);
        // Check for limitations:
        if (stg.getPlaces().isEmpty()) {
            // - The set of device STG place names is non-empty (this limitation can be easily removed).
            JOptionPane.showMessageDialog(mainWindow, "Error: The STG must have places.",
                    TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (hasDisconnectedTransitions(stg)) {
            // - Each transition in the device STG must have some arcs, i.e. its preset or postset is non-empty.
            JOptionPane.showMessageDialog(mainWindow, "Error: The STG must have no disconnected transitions.",
                    TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!stg.getDummyTransitions().isEmpty()) {
            // - The device STG must have no dummies.
            JOptionPane.showMessageDialog(mainWindow, "Error: The STG must have no dummies.",
                    TITLE, JOptionPane.ERROR_MESSAGE);
            return;
        }

        Importer[] importers = {new DotGImporter()};
        JFileChooser fc = mainWindow.createOpenDialog("Open environment file", false, true, importers);
        if (fc.showDialog(null, "Open") == JFileChooser.APPROVE_OPTION) {
            File envFile = fc.getSelectedFile();
            if (mainWindow.checkFileMessageDialog(envFile, null)) {
                Stg envStg = StgUtils.loadStg(envFile);
                if (envStg == null) {
                    JOptionPane.showMessageDialog(mainWindow,
                            "Error: Cannot read an STG model from the file:\n" + envFile.getAbsolutePath() + "\n\n"
                            + "Conformation cannot be checked without environment STG.\n",
                            TITLE, JOptionPane.ERROR_MESSAGE);
                } else {
                    final MpsatConformationTask mpsatTask = new MpsatConformationTask(we, envFile);
                    String description = "MPSat tool chain";
                    String title = we.getTitle();
                    if (!title.isEmpty()) {
                        description += "(" + title + ")";
                    }
                    MpsatChainResultHandler monitor = new MpsatChainResultHandler(mpsatTask);
                    framework.getTaskManager().queue(mpsatTask, description, monitor);
                }
            }
        }
    }

    private boolean hasDisconnectedTransitions(Stg stg) {
        for (Transition t: stg.getTransitions()) {
            if (stg.getPreset(t).isEmpty() && stg.getPostset(t).isEmpty()) {
                return true;
            }
        }
        return false;
    }

}
