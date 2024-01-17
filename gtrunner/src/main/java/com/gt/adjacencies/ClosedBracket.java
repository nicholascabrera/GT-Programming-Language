package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class ClosedBracket {
    private NodeVector vector;

    public ClosedBracket(int order, String reference){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.E, order, reference);
    }

    public NodeVector getVector() {
        return vector;
    }
}