package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Branch {
    private NodeVector vector;

    public Branch(String label, int order){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.B, order, label);
    }

    public NodeVector getVector() {
        return vector;
    }
}
