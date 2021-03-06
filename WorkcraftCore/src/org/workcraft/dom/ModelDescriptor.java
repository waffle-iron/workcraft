package org.workcraft.dom;

import org.workcraft.dom.math.MathModel;

public interface ModelDescriptor {
    String getDisplayName();
    MathModel createMathModel();
    VisualModelDescriptor getVisualModelDescriptor();
}
