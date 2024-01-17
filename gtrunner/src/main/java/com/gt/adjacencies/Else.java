package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Else {
    private NodeVector vector;

    public Else(int order){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.L, order);
    }

    public NodeVector getVector() {
        return vector;
    }
}
