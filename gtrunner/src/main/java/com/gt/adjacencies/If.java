package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class If {
    private NodeVector vector;

    public If(int order){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.K, order);
    }

    public NodeVector getVector() {
        return vector;
    }
}
