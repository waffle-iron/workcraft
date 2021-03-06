package org.workcraft.gui.graph.tools;

import org.workcraft.gui.events.GraphEditorMouseEvent;

public interface GraphEditorMouseListener {
    void mouseMoved(GraphEditorMouseEvent e);
    void mouseClicked(GraphEditorMouseEvent e);
    void mouseEntered(GraphEditorMouseEvent e);
    void mouseExited(GraphEditorMouseEvent e);
    void mousePressed(GraphEditorMouseEvent e);
    void mouseReleased(GraphEditorMouseEvent e);

    void startDrag(GraphEditorMouseEvent e);
    void finishDrag(GraphEditorMouseEvent e);
    boolean isDragging();
}
