package com.gt.service;

import javax.swing.JFrame;
import javax.swing.UIManager;

import java.awt.Toolkit;

import com.gt.adjacencies.AdjacencyList;

public class GTCodeGraph extends JFrame{
    private AdjacencyList adjacencyList;
    final private int frame_width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3;
    final private int frame_height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()/2;
    final private int x_location = 15;
    final private int y_location = 15;

    public GTCodeGraph(AdjacencyList adjacencyList){
        this.adjacencyList = adjacencyList;
        
        try { 
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("GT Code Graph");
        setBounds(x_location, y_location, frame_width, frame_height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
    }

    public void graph() throws Exception{
        if(this.adjacencyList != null){
            CodePanel codePanel = new CodePanel(this.adjacencyList, frame_height, frame_height);
            add(codePanel);
        } else {
            throw new Exception("Adjacency List not initialized!");
        }
    }

    public AdjacencyList getAdjacencyList() {
        return adjacencyList;
    }

    public void setAdjacencyList(AdjacencyList adjacencyList) {
        this.adjacencyList = adjacencyList;
    }
}
