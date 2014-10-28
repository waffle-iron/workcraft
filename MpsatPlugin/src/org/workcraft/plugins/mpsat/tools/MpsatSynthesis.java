package org.workcraft.plugins.mpsat.tools;

import org.workcraft.Framework;
import org.workcraft.Tool;
import org.workcraft.plugins.mpsat.MpsatChainResultHandler;
import org.workcraft.plugins.mpsat.MpsatMode;
import org.workcraft.plugins.mpsat.MpsatSettings;
import org.workcraft.plugins.mpsat.MpsatSettings.SolutionMode;
import org.workcraft.plugins.mpsat.tasks.MpsatChainTask;
import org.workcraft.plugins.stg.STGModel;
import org.workcraft.util.WorkspaceUtils;
import org.workcraft.workspace.WorkspaceEntry;

abstract public class MpsatSynthesis implements Tool {

	private final Framework framework;

	public MpsatSynthesis(Framework framework) {
		this.framework = framework;
	}

	@Override
	public boolean isApplicableTo(WorkspaceEntry we) {
		return WorkspaceUtils.canHas(we, STGModel.class);
	}

	@Override
	public String getSection() {
		return "Synthesis";
	}

	@Override
	public void run(WorkspaceEntry we) {
		final MpsatSettings settings = new MpsatSettings("synthesis", getSynthesisMode(), 0, SolutionMode.FIRST, 1, null);
		final MpsatChainTask task = new MpsatChainTask(we, settings, framework);
		framework.getTaskManager().queue(task, "Logic synthesis with MPSat", new MpsatChainResultHandler(task));
	}

	abstract public MpsatMode getSynthesisMode();

}
