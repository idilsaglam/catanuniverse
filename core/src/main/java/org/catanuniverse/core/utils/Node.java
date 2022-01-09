/*
	22015094 - Idil Saglam*/
package org.catanuniverse.core.utils;

import java.awt.geom.Point2D;

public class Node {
    private Node parent;
    private Node child1, child2;
    private Point2D content;

    public Node(Point2D content) {
        this.child1 = null;
        this.child2 = null;
        this.parent = null;
        this.content = content;
    }

    /**
     * Check whether the given point exists in the current graph or not
     *
     * @param p The point to look for
     * @return True if the given point exists in the current graph or not
     */
    public boolean contains(Point2D p) {
        return this.get(p) != null;
    }

    /**
     * Get the sub node containing the given point
     *
     * @param p The point that we are looking for
     * @return The Node which contains the given point
     */
    public Node get(Point2D p) {
        if (this.content.equals(p)) {
            return this;
        }
        Node result = null;
        if (this.child1 != null) {
            result = this.child1.get(p);
        }
        if (result != null) return result;
        if (this.child2 != null) {
            result = this.child2.get(p);
        }
        return result;
    }

    /**
     * Calculates the distance between current node and the given node
     *
     * @param n The node to calculate the distance with
     * @return The distance between the current node and the given node
     */
    public double distance(Node n) {
        return this.content.distance(n.content);
    }

    /**
     * Calculates the distance between current node and the given point
     *
     * @param p The point to calculate the distance with
     * @return The distance between the current node and the givne point
     */
    public double distance(Point2D p) {
        return this.content.distance(p);
    }

    /**
     * Calculate the length of the current graph
     *
     * @return The length of the current graph
     */
    public int length() {
        int result = 0;
        if (this.child1 != null) {
            result = Math.max(result, this.child1.length());
        }
        if (this.child2 != null) {
            result = Math.max(result, this.child2.length());
        }
        return result;
    }

    /**
     * Adds the given point to the current graph. All nodes should have the same distance between
     * them
     *
     * @param p The point to add
     * @param threshold The maximum distance to accept
     * @return True if the add operation completed correctly, false if not
     */
    public boolean add(Point2D p, double threshold) {
        if (this.distance(p) <= threshold) {
            // The point can be added
            if (this.child1 == null) {
                this.child1 = new Node(p);
                return true;
            }
            if (this.child2 == null) {
                this.child2 = new Node(p);
                return true;
            }
            if (this.parent == null) {
                this.parent = new Node(p);
                return true;
            }
            return false;
        }
        // The point to add is too far
        boolean result = false;
        if (this.child1 != null) {
            result = child1.add(p, threshold);
        }
        if (result) return true;
        if (this.child2 != null) {
            result = child2.add(p, threshold);
        }
        return result;
    }
}
