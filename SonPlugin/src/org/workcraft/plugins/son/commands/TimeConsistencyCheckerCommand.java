package org.workcraft.plugins.son.commands;

import org.workcraft.Framework;
import org.workcraft.gui.MainWindow;
import org.workcraft.gui.graph.commands.Command;
import org.workcraft.plugins.son.BlockConnector;
import org.workcraft.plugins.son.OutputRedirect;
import org.workcraft.plugins.son.SON;
import org.workcraft.plugins.son.VisualSON;
import org.workcraft.plugins.son.gui.TimeConsistencyDialog;
import org.workcraft.plugins.son.tasks.TimeConsistencyTask;
import org.workcraft.util.GUI;
import org.workcraft.workspace.WorkspaceEntry;
import org.workcraft.workspace.WorkspaceUtils;

public class TimeConsistencyCheckerCommand implements Command {

    @Override
    public boolean isApplicableTo(WorkspaceEntry we) {
        return WorkspaceUtils.isApplicable(we, SON.class);
    }

    @Override
    public String getSection() {
        return "Time analysis";
    }

    @Override
    public String getDisplayName() {
        return "Check for consistency...";
    }

    @Override
    public void run(WorkspaceEntry we) {
        final Framework framework = Framework.getInstance();
        final MainWindow mainWindow = framework.getMainWindow();
        final VisualSON visualNet = WorkspaceUtils.getAs(we, VisualSON.class);
        final SON net = WorkspaceUtils.getAs(we, SON.class);

        BlockConnector.blockBoundingConnector(visualNet);
        net.refreshAllColor();
        TimeConsistencyDialog dialog = new TimeConsistencyDialog(mainWindow, we);
        GUI.centerToParent(dialog, mainWindow);
        dialog.setVisible(true);

        if (dialog.getRun() == 1) {
            OutputRedirect.redirect(30, 48, "Consistency Verification Result");
            TimeConsistencyTask timeTask = new TimeConsistencyTask(we, dialog.getTimeConsistencySettings());
            framework.getTaskManager().queue(timeTask, "Verification");
        }

        if (dialog.getTabIndex() != 1) {
            net.refreshAllColor();
        }

        BlockConnector.blockInternalConnector(visualNet);
    }

}
