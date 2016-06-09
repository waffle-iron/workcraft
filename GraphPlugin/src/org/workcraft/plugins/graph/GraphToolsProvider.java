package org.workcraft.plugins.graph;

import java.util.ArrayList;

import org.workcraft.gui.graph.tools.CommentGeneratorTool;
import org.workcraft.gui.graph.tools.ConnectionTool;
import org.workcraft.gui.graph.tools.CustomToolsProvider;
import org.workcraft.gui.graph.tools.DefaultNodeGenerator;
import org.workcraft.gui.graph.tools.GraphEditorTool;
import org.workcraft.gui.graph.tools.NodeGeneratorTool;
import org.workcraft.gui.graph.tools.SelectionTool;
import org.workcraft.plugins.graph.tools.GraphSimulationTool;

public class GraphToolsProvider implements CustomToolsProvider {

    @Override
    public Iterable<GraphEditorTool> getTools() {
        ArrayList<GraphEditorTool> result = new ArrayList<>();

        result.add(new SelectionTool(true, false, true, true));
        result.add(new CommentGeneratorTool());
        result.add(new ConnectionTool(false, true, true));
        result.add(new NodeGeneratorTool(new DefaultNodeGenerator(Vertex.class)));
        result.add(new GraphSimulationTool());
        return result;
    }

}
