package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class OpenBracket {
    private NodeVector vector;

    public OpenBracket(int order){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.I, order);
    }

    public NodeVector getVector() {
        return vector;
    }
}