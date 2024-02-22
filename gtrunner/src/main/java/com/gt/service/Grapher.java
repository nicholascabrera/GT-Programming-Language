package com.gt.service;

import javax.swing.SwingWorker;

import com.gt.adjacencies.AdjacencyList;

public class Grapher extends SwingWorker<Void, Void>{
    private AdjacencyList adjacencyList;
    private GTCodeGraph gtCodeGraph;

    public Grapher(AdjacencyList adjacencyList){
        this.adjacencyList = adjacencyList;
    }

    @Override
    protected Void doInBackground() throws Exception {
        this.gtCodeGraph = new GTCodeGraph(adjacencyList);
        this.gtCodeGraph.graph();
        return null;
    }

    @Override
    public void done(){
        this.gtCodeGraph.setVisible(true);
    }
}
