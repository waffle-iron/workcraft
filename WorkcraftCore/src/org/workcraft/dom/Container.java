package org.workcraft.dom;

import java.util.Collection;

public interface Container extends Node {

    void add(Node node);
    void add(Collection<Node> nodes);

    void remove(Node node);
    void remove(Collection<Node> nodes);

    void reparent(Collection<Node> nodes);
    void reparent(Collection<Node> nodes, Container newParent);

}
