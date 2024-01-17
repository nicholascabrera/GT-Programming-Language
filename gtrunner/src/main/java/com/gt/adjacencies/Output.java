package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Output {
    private NodeVector vector;

    public Output(NodeAttribute attribute, int order){
        setVector(attribute, order);
    }

    public Output(NodeAttribute attribute, int order, String weight){
        setVector(attribute, order, weight);
    }

    private void setVector(NodeAttribute attribute, int order, String weight) {
        this.vector = new NodeVector(NodeAttribute.I, attribute, order, weight);
    }

    public void setVector(NodeAttribute attribute, int order) {
        this.vector = new NodeVector(NodeAttribute.I, attribute, order);
    }

    public NodeVector getVector() {
        return this.vector;
    }
}
