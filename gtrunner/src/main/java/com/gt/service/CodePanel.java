package com.gt.service;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashSet;

import javax.swing.JPanel;

import com.gt.adjacencies.AdjacencyList;
import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;

public class CodePanel extends JPanel{
    private AdjacencyList adjacencyList;

    /* Point Constants */
    private Point pointA;   //Blood
    private Point pointB;   //Slashing
    private Point pointC;   //Bludgeoning
    private Point pointD;   //Piercing
    private Point pointE;   //Fire
    private Point pointF;   //Ice
    private Point pointG;   //Acid
    private Point pointH;   //Poison
    private Point pointI;   //Lightning
    private Point pointJ;   //Thunder
    private Point pointK;   //Radiant
    private Point pointL;   //Necrotic
    private Point pointM;   //Force
    private Point pointN;   //Psychic

    private int frame_height;
    private int frame_width;

    final private int CIRCLE_WIDTH_HEIGHT = 5;
    final private int ARROW_LENGTH = 6;

    public CodePanel(AdjacencyList adjacencyList, int frame_width, int frame_height){
        this.adjacencyList = adjacencyList;
        this.frame_height = frame_height;
        this.frame_width = frame_width;
        
        int link_length = Math.min(frame_width/6, frame_height/6);
        int equator = frame_height/2 - 25;

        this.pointA = new Point(link_length, equator);
        this.pointB = new Point(pointA.x+link_length/2, pointA.y-link_length);
        this.pointC = new Point(pointB.x+link_length/2, pointB.y-link_length/2);
        this.pointD = new Point(pointC.x+link_length, pointC.y-link_length/2);
        this.pointE = new Point(pointD.x+link_length, pointD.y);
        this.pointF = new Point(pointE.x+link_length, pointE.y+link_length/2);
        this.pointG = new Point(pointF.x+link_length/2, pointF.y+link_length/2);
        this.pointH = new Point(pointG.x+link_length/2, pointG.y+link_length);
        this.pointI = new Point(pointH.x-link_length/2, pointH.y+link_length);
        this.pointJ = new Point(pointI.x-link_length/2, pointI.y+link_length/2);
        this.pointK = new Point(pointJ.x-link_length, pointJ.y+link_length/2);
        this.pointL = new Point(pointK.x-link_length, pointK.y);
        this.pointM = new Point(pointL.x-link_length, pointL.y-link_length/2);
        this.pointN = new Point(pointM.x-link_length/2, pointM.y-link_length/2);

        repaint();
    }

    public void paintComponent(Graphics g){
        /* Turn on antialiasing to get nicer circles. */
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //draw the connected planar graph
        drawAdjacencyGraph(g);
    }

    private void drawPoint(Graphics g, Point point, String node){
        g.setColor(Color.BLACK);
        g.fillOval(point.x, point.y, CIRCLE_WIDTH_HEIGHT, CIRCLE_WIDTH_HEIGHT);
        int equator = frame_height/2 - 25;

        if(point.y == equator){
            if(point.x < this.frame_width/2){
                g.drawString(node, point.x-10, point.y+5);
            } else {
                g.drawString(node, point.x+10, point.y+5);
            }
        } else if (point.y < equator){
            g.drawString(node, point.x, point.y - 5);
        } else {
            g.drawString(node, point.x, point.y + 15);
        }
        
    }

    private void drawLink(Graphics g, Point startPoint, Point endPoint){
        g.setColor(Color.BLUE);
        Point startCenter = getCenter(startPoint);
        Point endCenter = getCenter(endPoint);
        drawArrow(g, startCenter.x, startCenter.y, endCenter.x, endCenter.y, ARROW_LENGTH, 13); //head angle is the angle that the arrow forms
    }

    /* Stolen code */
    public void drawArrow(Graphics g, int x0, int y0, int x1, int y1, int headLength, int headAngle) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = { x1 + (int) (headLength * Math.cos(angle + offs)), x1, x1 + (int) (headLength * Math.cos(angle - offs)) };
        int[] ys = { y1 + (int) (headLength * Math.sin(angle + offs)), y1, y1 + (int) (headLength * Math.sin(angle - offs)) };
        g.drawLine(x0, y0, x1, y1);
        g.drawPolyline(xs, ys, 3);
    }

    private Point getCenter(Point point){
        return new Point(point.x+CIRCLE_WIDTH_HEIGHT/2, point.y+CIRCLE_WIDTH_HEIGHT/2);
    }
 
    @SuppressWarnings("unused")
    private void drawAllPoints(Graphics g){
        //draw all 14 verticies
        drawPoint(g, pointA, "A");
        drawPoint(g, pointB, "B");
        drawPoint(g, pointC, "C");
        drawPoint(g, pointD, "D");
        drawPoint(g, pointE, "E");
        drawPoint(g, pointF, "F");
        drawPoint(g, pointG, "G");
        drawPoint(g, pointH, "H");
        drawPoint(g, pointI, "I");
        drawPoint(g, pointJ, "J");
        drawPoint(g, pointK, "K");
        drawPoint(g, pointL, "L");
        drawPoint(g, pointM, "M");
        drawPoint(g, pointN, "N");
    }

    @SuppressWarnings("unused")
    private void drawConnectedPlanarGraph(Graphics g){
        g.setColor(Color.BLACK);

        //draw all 14 verticies and link them into a complete planar graph
        drawLink(g, pointA, pointB);    //draw the links first! this way, the letters aren't getting blocked.
        drawLink(g, pointB, pointC);
        drawLink(g, pointB, pointD);
        drawLink(g, pointC, pointE);
        drawLink(g, pointD, pointE);
        drawLink(g, pointA, pointF);
        drawLink(g, pointD, pointF);
        drawLink(g, pointA, pointG);
        drawLink(g, pointC, pointG);
        drawLink(g, pointE, pointH);
        drawLink(g, pointH, pointI);
        drawLink(g, pointH, pointJ);
        drawLink(g, pointI, pointK);
        drawLink(g, pointJ, pointK);
        drawLink(g, pointJ, pointL);
        drawLink(g, pointF, pointL);
        drawLink(g, pointI, pointM);
        drawLink(g, pointG, pointM);
        drawLink(g, pointK, pointN);
        drawLink(g, pointL, pointN);
        drawLink(g, pointM, pointN);

        drawPoint(g, pointA, "A");
        drawPoint(g, pointB, "B");
        drawPoint(g, pointC, "C");
        drawPoint(g, pointD, "D");
        drawPoint(g, pointE, "E");
        drawPoint(g, pointF, "F");
        drawPoint(g, pointG, "G");
        drawPoint(g, pointH, "H");
        drawPoint(g, pointI, "I");
        drawPoint(g, pointJ, "J");
        drawPoint(g, pointK, "K");
        drawPoint(g, pointL, "L");
        drawPoint(g, pointM, "M");
        drawPoint(g, pointN, "N");
    }

    public Point getNodePoint(NodeAttribute attribute) {
        if(attribute != null) {
            switch (attribute){
                case A:
                    return this.pointA;
                case B:
                    return this.pointB;
                case C:
                    return this.pointC;
                case D:
                    return this.pointD;
                case E:
                    return this.pointE;
                case F:
                    return this.pointF;
                case G:
                    return this.pointG;
                case H:
                    return this.pointH;
                case I:
                    return this.pointI;
                case J:
                    return this.pointJ;
                case K:
                    return this.pointK;
                case L:
                    return this.pointL;
                case M:
                    return this.pointM;
                case N:
                    return this.pointN;
                default:
                    // do nothing for now, but later i will need to add new nodes and link them up.
                    return new Point(0,0);
            }
        }

        return new Point(0,0);
    }

    public void drawAdjacencyGraph(Graphics g){
        HashSet<NodeAttribute> nodesToDraw = new HashSet<>();

        for (NodeVector nodeVector : this.adjacencyList) {
            Point startPoint = getNodePoint(nodeVector.getStartNode());
            Point endPoint = getNodePoint(nodeVector.getEndNode());
            if(startPoint != null && endPoint != null && endPoint.x != 0 && endPoint.y != 0){
                drawLink(g, startPoint, endPoint);
                nodesToDraw.add(nodeVector.getStartNode());
                nodesToDraw.add(nodeVector.getEndNode());
            }
        }

        for(NodeAttribute nodeAttribute : nodesToDraw){
            if(nodeAttribute != null){
                drawPoint(g, getNodePoint(nodeAttribute), nodeAttribute.toString());
            }
        }
    }    
}