package org.workcraft.dom.visual;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.workcraft.dom.Node;
import org.workcraft.dom.math.MathNode;
import org.workcraft.dom.visual.connections.VisualConnectionProperties;
import org.workcraft.observation.TransformEvent;
import org.workcraft.observation.TransformObserver;

public class VisualComponentGroupTests {
    class MockTransformObservingNode implements Node, TransformObserver {
        Node parent = null;

        public boolean notified = false;

        @Override
        public Collection<Node> getChildren() {
            return Collections.emptyList();
        }

        @Override
        public Node getParent() {
            return parent;
        }

        @Override
        public void setParent(Node parent) {
            this.parent = parent;
        }

        @Override
        public void notify(TransformEvent e) {
            notified = true;
        }

        @Override
        public void subscribe(TransformDispatcher dispatcher) {
            dispatcher.subscribe(this, parent);
        }
    }

    @Test
    public void testHitComponent() {
        VisualGroup root = createGroup(null);
        VisualGroup node1 = createGroup(root);
        node1.setX(5);

        SquareNode sq1 = new SquareNode(root, new Rectangle2D.Double(1, 1, 1, 1));
        SquareNode sq2 = new SquareNode(node1, new Rectangle2D.Double(2, 2, 1, 1));
        root.add(sq1);
        node1.add(sq2);

        Assert.assertEquals(sq1, HitMan.hitDeepestNodeOfType(new Point2D.Double(1.5, 1.5), root, VisualComponent.class));
        Assert.assertEquals(sq2, HitMan.hitDeepestNodeOfType(new Point2D.Double(7.5, 2.5), root, VisualComponent.class));
        Assert.assertEquals(null, HitMan.hitDeepestNodeOfType(new Point2D.Double(2.5, 2.5), root, VisualComponent.class));
    }

    public void testHitConnection() {
        VisualGroup root = createGroup(null);
        VisualGroup group = createGroup(root);
        group.setX(5);

        SquareNode sqr1 = new SquareNode(root, new Rectangle2D.Double(1, 1, 1, 1));
        SquareNode sqr2 = new SquareNode(root, new Rectangle2D.Double(3, 3, 1, 1));
        root.add(sqr1);
        root.add(sqr2);
        VisualConnectionProperties connectionR = Tools.createConnection(sqr1, sqr2, root);

        SquareNode sqg1 = new SquareNode(group, new Rectangle2D.Double(1, 1, 1, 1));
        SquareNode sqg2 = new SquareNode(group, new Rectangle2D.Double(3, 3, 1, 1));
        group.add(sqg1);
        group.add(sqg2);
        Tools.createConnection(sqg1, sqg2, group);

        Assert.assertEquals(connectionR, HitMan.hitTestFirst(new Point2D.Double(2.5, 1.5), root));
        Assert.assertEquals(group, HitMan.hitTestFirst(new Point2D.Double(7.5, 1.5), root));
    }

    @Test
    public void testHitNode() {
        VisualGroup group = new VisualGroup();

        Rectangle2D.Double r1 = new Rectangle2D.Double();
        Rectangle2D.Double r1b = new Rectangle2D.Double();
        Rectangle2D.Double r2 = new Rectangle2D.Double();
        Rectangle2D.Double r2b = new Rectangle2D.Double();
        Rectangle2D.Double r3 = new Rectangle2D.Double();
        Rectangle2D.Double r3b = new Rectangle2D.Double();

        r1.setRect(0, 0, 2, 2);
        r1b.setRect(0.1, 0.1, 1.8, 1.8);
        r2.setRect(0.5, 0.5, 2, 2);
        r2b.setRect(0.6, 0.6, 1.8, 1.8);
        r3.setRect(1, 1, 2, 2);
        r3b.setRect(1.1, 1.1, 1.8, 1.8);

        VisualNode node1 = new SquareNode(group, r1, r1b);
        VisualNode node2 = new SquareNode(group, r2, r2b);
        VisualNode node3 = new SquareNode(group, r3, r3b);

    //    Assert.assertNull(group.getBoundingBoxInLocalSpace());

        group.add(node1);
        group.add(node2);
        group.add(node3);
        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(-1, -1), group));
        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(10, 10), group));
        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(0.05, 0.05), group));
        Assert.assertEquals(node1, HitMan.hitTestFirst(new Point2D.Double(0.15, 0.5), group));
        Assert.assertEquals(node1, HitMan.hitTestFirst(new Point2D.Double(0.55, 0.55), group));
        Assert.assertEquals(node2, HitMan.hitTestFirst(new Point2D.Double(0.65, 0.65), group));
        Assert.assertEquals(node2, HitMan.hitTestFirst(new Point2D.Double(1.05, 1.05), group));
        Assert.assertEquals(node3, HitMan.hitTestFirst(new Point2D.Double(1.15, 1.15), group));
        Assert.assertEquals(node3, HitMan.hitTestFirst(new Point2D.Double(1.95, 1.95), group));
        Assert.assertEquals(node3, HitMan.hitTestFirst(new Point2D.Double(2.35, 1.35), group));
        Assert.assertEquals(node3, HitMan.hitTestFirst(new Point2D.Double(2.45, 1.45), group));
        Assert.assertEquals(node3, HitMan.hitTestFirst(new Point2D.Double(2.85, 2.85), group));
        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(2.95, 2.95), group));
    }

    private SquareNode getSquareNode(VisualNode parent, double x, double y) {
        return new SquareNode(null, new Rectangle2D.Double(x, y, 1, 1));
    }

    @Test
    public void testHitSubGroup() {
        VisualGroup root = new VisualGroup();

        VisualGroup node1 = new VisualGroup();
        VisualGroup node2 = new VisualGroup();
        root.add(node1);
        root.add((VisualNode) node2);
        node1.add(getSquareNode(node1, 0, 0));
        node2.add(getSquareNode(node2, 1, 1));
        Assert.assertEquals(node2, HitMan.hitTestFirst(new Point2D.Double(1.5, 1.5), root));
        Assert.assertEquals(node1, HitMan.hitTestFirst(new Point2D.Double(0.5, 0.5), root));
    }

    @Test
    public void testUngroup() {
        VisualGroup root = new VisualGroup();

        VisualGroup node1 = new VisualGroup();
        root.add(node1);

        node1.setX(10);
        node1.setY(15);

        VisualGroup node2 = new VisualGroup();
        node1.add(node2);

        SquareNode sq1 = getSquareNode(node1, 0, 0);
        node1.add(sq1);
        SquareNode sq2 = getSquareNode(node1, 1, 1);
        node1.add(sq2);
        SquareNode sq3 = getSquareNode(node1, 2, 2);
        node1.add(sq3);

        Assert.assertEquals(sq1, HitMan.hitTestFirst(new Point2D.Double(10.5, 15.5), node1));
        Assert.assertEquals(sq2, HitMan.hitTestFirst(new Point2D.Double(11.5, 16.5), node1));

        Assert.assertEquals(node1, HitMan.hitTestFirst(new Point2D.Double(10.5, 15.5), root));
        Assert.assertEquals(node1, HitMan.hitTestFirst(new Point2D.Double(11.5, 16.5), root));
        Assert.assertEquals(null, HitMan.hitTestFirst(new Point2D.Double(13.5, 15.5), root));

        Iterable<Node> unGroup = node1.unGroup();
        ArrayList<Node> list = new ArrayList<>();
        for (Node node: unGroup) {
            list.add(node);
        }

        Assert.assertEquals(4, list.size());
        Assert.assertTrue(list.contains(sq1));
        Assert.assertTrue(list.contains(sq2));
        Assert.assertTrue(list.contains(sq3));
        Assert.assertTrue(list.contains(node2));

        Assert.assertTrue(list.indexOf(sq2) > list.indexOf(sq1));
        Assert.assertTrue(list.indexOf(sq3) > list.indexOf(sq2));

        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(0.5, 0.5), node1));
        Assert.assertNull(HitMan.hitTestFirst(new Point2D.Double(1.5, 1.5), node1));

//        Assert.assertEquals(sq1, HitMan.hitTestForSelection(new Point2D.Double(10.5, 15.5), root));
//        Assert.assertEquals(sq2, HitMan.hitTestForSelection(new Point2D.Double(11.5, 16.5), root));
//        Assert.assertEquals(null, HitMan.hitTestForSelection(new Point2D.Double(10.5, 16.5), root));
    }

    private VisualGroup createGroup(VisualGroup parent) {
        return VisualNodeTests.createGroup(parent);
    }

    @Test
    public void testTransformChangeNotification() {
        TransformEventPropagator p = new TransformEventPropagator();

        VisualGroup root = createGroup(null);

        MockTransformObservingNode node1 = new MockTransformObservingNode();
        root.add(node1);

        p.attach(root);

        Assert.assertFalse("already hit o_O", node1.notified);
        root.setX(8);
        Assert.assertTrue("not hit", node1.notified);
    }

    class DummyNode extends VisualComponent {
        DummyNode(VisualGroup parent) {
            super(null);
            parent.add(this);
        }

        @Override
        public Rectangle2D getBoundingBoxInLocalSpace() {
            return new Rectangle2D.Double(0, 0, 1, 1);
        }

        @Override
        public boolean hitTestInLocalSpace(Point2D pointInLocalSpace) {
            return false;
        }

        @Override
        public Collection<MathNode> getMathReferences() {
            return null;
        }

        @Override
        public void draw(DrawRequest r) {
        }
    }

}
