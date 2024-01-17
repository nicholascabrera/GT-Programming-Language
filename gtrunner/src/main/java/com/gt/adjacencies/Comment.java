package com.gt.adjacencies;

import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class Comment {
    private NodeVector vector;

    public Comment(int order, String message){
        this.vector = new NodeVector(NodeAttribute.N, NodeAttribute.N, order, message);
    }

    public NodeVector getVector() {
        return vector;
    }
}
