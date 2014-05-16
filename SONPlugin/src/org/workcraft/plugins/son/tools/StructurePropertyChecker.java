package org.workcraft.plugins.son.tools;

import org.workcraft.Framework;
import org.workcraft.Tool;
import org.workcraft.plugins.son.OutputRedirect;
import org.workcraft.plugins.son.SON;
import org.workcraft.plugins.son.SONModel;
import org.workcraft.plugins.son.VisualSON;
import org.workcraft.plugins.son.gui.StructureVerifyDialog;
import org.workcraft.plugins.son.verify.SONStructureTask;
import org.workcraft.util.GUI;
import org.workcraft.util.WorkspaceUtils;
import org.workcraft.workspace.WorkspaceEntry;

public class StructurePropertyChecker implements Tool {

	private final Framework framework;

	public StructurePropertyChecker(Framework framework){

		this.framework = framework;

	}

	public boolean isApplicableTo(WorkspaceEntry we) {
		return WorkspaceUtils.canHas(we, SON.class);

	}

	public String getSection(){
		return "Verification";
	}

	public String getDisplayName(){
		return "Check structure property";
	}

	public void run(WorkspaceEntry we){

		SONModel net=(SONModel)we.getModelEntry().getMathModel();
		VisualSON vnet = (VisualSON)we.getModelEntry().getVisualModel();

		StructureVerifyDialog dialog = new StructureVerifyDialog(framework.getMainWindow(), net);
		GUI.centerToParent(dialog, framework.getMainWindow());
		dialog.setVisible(true);

		if (dialog.getRun() == 1){
			OutputRedirect.Redirect();
			vnet.connectToBlocks();
			//framework.getTaskManager().queue(new SONStructureTask(dialog.getSetting(), net, vnet), "Verification");
			framework.getTaskManager().execute(new SONStructureTask(dialog.getSetting(), net, vnet), "Verification");
			vnet.connectToBlocksInside();
		}
	}
}
