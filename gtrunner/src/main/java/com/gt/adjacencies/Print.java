package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Print {
    private NodeVector nodeVector;

    public Print(String weight, int order){
        this.nodeVector = new NodeVector(NodeAttribute.D, NodeAttribute.G, order, weight);
    }

    public NodeVector getVector() {
        return nodeVector;
    }
}
