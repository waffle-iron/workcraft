package org.workcraft.plugins.stg.concepts;

import java.io.File;
import java.util.ArrayList;

import org.workcraft.gui.DesktopApi;
import org.workcraft.plugins.shared.tasks.ExternalProcessResult;
import org.workcraft.plugins.shared.tasks.ExternalProcessTask;
import org.workcraft.plugins.stg.StgSettings;
import org.workcraft.tasks.ProgressMonitor;
import org.workcraft.tasks.Result;
import org.workcraft.tasks.SubtaskMonitor;
import org.workcraft.tasks.Task;
import org.workcraft.tasks.Result.Outcome;

public class ConceptsTask implements Task<ExternalProcessResult> {

    private final File inputFile;
    private Object[] includeList;

    public ConceptsTask(File inputFile) {
        this.inputFile = inputFile;
    }

    public ConceptsTask(File inputFile, Object[] includeList) {
        this.inputFile = inputFile;
        this.includeList = includeList;
    }

    @Override
    public Result<? extends ExternalProcessResult> run(ProgressMonitor<? super ExternalProcessResult> monitor) {
        try {
            ArrayList<String> command = new ArrayList<>();

            command.add("stack");
            command.add("runghc");
            String translateLocation = DesktopApi.getOs().isWindows() ? "translate\\Main.hs" : "translate/Main.hs";
            //TODO: Make concepts folder location a setting
            command.add(StgSettings.getConceptsFolderLocation() + translateLocation);
            String stackYamlCommand = "--stack-yaml=" + StgSettings.getConceptsFolderLocation() + "stack.yaml";
            command.add(stackYamlCommand);
            command.add("--");
            command.add(inputFile.getAbsolutePath());

            for (Object i : includeList) {
                command.add("-i");
                command.add(i.toString());
            }

            ExternalProcessTask task = new ExternalProcessTask(command, new File("."));
            SubtaskMonitor<Object> mon = new SubtaskMonitor<>(monitor);
            Result<? extends ExternalProcessResult> result = task.run(mon);
            if (result.getOutcome() != Outcome.FINISHED) {
                return result;
            }
            ExternalProcessResult retVal = result.getReturnValue();
            ExternalProcessResult finalResult = new ExternalProcessResult(retVal.getReturnCode(), retVal.getOutput(),
                    retVal.getErrors(), null);

            if (retVal.getReturnCode() == 0) {
                return Result.finished(finalResult);
            } else {
                return Result.failed(finalResult);
            }
        } catch (NullPointerException e) {
            // Open window dialog was cancelled, do nothing
        }
        return null;
    }

}
