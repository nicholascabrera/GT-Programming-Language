package com.gt.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import javax.swing.JPanel;

import com.gt.adjacencies.AdjacencyList;
import com.gt.node.NodeAttribute;
import com.gt.node.NodeVector;
import com.gt.service.Compiler;

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
    private HashMap<String, Point> userVariables;   /* the name of the variable, its location */
    private HashMap<String, String> nodeOrders;     /* start->end, its order within the program */

    private int frame_height;
    private int frame_width;

    final private int CIRCLE_WIDTH_HEIGHT = 5;
    final private int ARROW_LENGTH = 6;

    public CodePanel(AdjacencyList adjacencyList, int frame_width, int frame_height){
        this.adjacencyList = adjacencyList;
        this.frame_height = frame_height;
        this.frame_width = frame_width;
        this.userVariables = new HashMap<>();
        this.nodeOrders = new HashMap<>();
        
        /* decide the major measurements based on the frame width and height */
        int link_length = Math.min(frame_width/10, frame_height/10);
        int equator = frame_height/2 - 25;
        int startingX = (frame_width/2)-link_length*2;

        /* Map the points in a circle - see "vertex graph.drawio" for layout */
        this.pointA = new Point(startingX, equator);
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

        /* paint */
        repaint();
    }

    public void paintComponent(Graphics g){
        /* Turn on antialiasing to get nicer circles. */
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        /* draw the adjacency list as a graph. */
        drawAdjacencyList(g);
    }

    /**
     * Draws a point at a given location, and then labels it.
     * @param g
     * @param point
     * @param node
     */
    private void drawPoint(Graphics g, Point point, String node){
        /* points and their labels are black. */
        g.setColor(Color.BLACK);
        /* draw the point. */
        g.fillOval(point.x, point.y, CIRCLE_WIDTH_HEIGHT, CIRCLE_WIDTH_HEIGHT);


        int equator = frame_height/2 - 25;
        /* Logic for label placement */
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

    /**
     * Draws a directed arrow between two points.
     * @param g
     * @param startPoint
     * @param endPoint
     */
    private void drawLink(Graphics g, Point startPoint, Point endPoint){
        /* links between points are blue. */
        g.setColor(Color.BLUE);

        /* The center of a point and its location are different, hence we need the location of the center. */
        Point startCenter = getCenter(startPoint);
        Point endCenter = getCenter(endPoint);

        /* Separate function which draws both a line and a head, so its a directed arrow. */
        drawArrow(g, startCenter.x, startCenter.y, endCenter.x, endCenter.y, ARROW_LENGTH, 13); //head angle is the angle that the arrow forms
    }

    /**
     * Draws a circle starting and ending at the same point.
     * @param g
     * @param point
     */
    private void drawSelfReferentialLink(Graphics g, Point point){
        /* links between points are blue. */
        g.setColor(Color.BLUE);

        /* The center of a point and its location are different, hence we need the location of the center. */
        Point pointCenter = getCenter(point);
        g.drawOval(pointCenter.x, pointCenter.y, 10, 10);
    }

    /* Stolen code - not sure what it does. */
    public void drawArrow(Graphics g, int x0, int y0, int x1, int y1, int headLength, int headAngle) {
        double offs = headAngle * Math.PI / 180.0;
        double angle = Math.atan2(y0 - y1, x0 - x1);
        int[] xs = { x1 + (int) (headLength * Math.cos(angle + offs)), x1, x1 + (int) (headLength * Math.cos(angle - offs)) };
        int[] ys = { y1 + (int) (headLength * Math.sin(angle + offs)), y1, y1 + (int) (headLength * Math.sin(angle - offs)) };
        g.drawLine(x0, y0, x1, y1);
        g.drawPolyline(xs, ys, 3);
    }

    /**
     * Given a point, returns the point at the center of the given point.
     * @param point
     * @return
     */
    private Point getCenter(Point point){
        return new Point(point.x+CIRCLE_WIDTH_HEIGHT/2, point.y+CIRCLE_WIDTH_HEIGHT/2);
    }

    /**
     * Given a string name, adds a new point to the user variables and draws the point.
     * @param name
     */
    private void addPoint(Graphics g, String name){
        /* If the graph does not already have the user variable, we must add it. */
        if(!this.userVariables.containsKey(name)){
            /**
             * the layout of user variables will be a certain distance away from the center of the frame,
             * and separated from each other by a certain angle interval. when the total angle displaced is
             * >= 360, the angle is reset to zero with a small offset and the distance increased by an interval.
             */
            int angleInterval = 20;
            int offsetInterval = 5;
            int initialDistance = (int)(Math.min(frame_width/10, frame_height/10) * 3.5);
            int distanceInterval = 20;

            /* Start at 0 degrees, and increase by 15 degrees with each added variable. */
            int totalAngle = (this.userVariables.size()) * angleInterval;
            /* Number of times we have revolved around the graph. */
            int numRevolutions = totalAngle/360;
            /* Limit the total angle from 0 to 360. */
            totalAngle %= 360;
            /* Offset the total angle by our offset interval, and set it to our final angle. */
            int angle = totalAngle + numRevolutions * offsetInterval;
            /* Find the distance from the center. */
            int distance = initialDistance + distanceInterval * numRevolutions;
            /* Find the point given by our angle and distance. */
            int x = (int)(distance*Math.cos(Math.toRadians(angle-90))+this.frame_width/2);
            int y = (int)(distance*Math.sin(Math.toRadians(angle-90))+this.frame_height/2);
            Point userPoint = new Point(x, y);
            /* Add the point to the user variable hash map. */
            this.userVariables.put(name, userPoint);
        }
        /* If the variable already exists, we skip to here. */
        /* Once the variable has been added or determined to exist, we draw it. */
        drawPoint(g, this.userVariables.get(name), name);
    }

    /**
     * Given a node attribute, returns the corresponding point.
     * @param attribute
     * @return
     */
    private Point getNodePoint(NodeAttribute attribute) {
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
            }
        }

        return new Point(0,0);
    }

    /**
     * Adds the order with which vectors occur within a program to a hash map.
     * @param nodeVector
     */
    public void addOrders(NodeVector nodeVector){
        String start = nodeVector.getStartNode().toString();
        String end = "";

        if(nodeVector.getEndNode() != null){
            end = nodeVector.getEndNode().toString();
        } else {
            /* A ":" is added so that we can tell it is a user variable, rather than an attribute. */
            end = ":" + nodeVector.getNodeName();
        }

        String nodeString = start + "->" + end;
        
        if(this.nodeOrders.containsKey(nodeString)){
            this.nodeOrders.put(nodeString, this.nodeOrders.get(nodeString).concat(", " + nodeVector.getOrder()));
        } else {
            this.nodeOrders.put(nodeString, Integer.toString(nodeVector.getOrder()));
        }
    }

    /**
     * Draws the order of vectors where they need to be.
     * Not used, as it would clutter everything up too much.
     * @param g
     */
    public void drawOrders(Graphics g){
        /* Use a lambda expression to iterate over the hash map. */
        this.nodeOrders.forEach((key, value) -> {
            Point startPoint = new Point();
            Point endPoint = new Point();

            try {
                NodeAttribute startNode = NodeAttribute.getNodeAttribute(Character.toString(key.charAt(0)));
                startPoint = getNodePoint(startNode);
                /* Is this a user variable? */
                if(Compiler.inputValidation(Pattern.compile("[A-Z]->:[A-Za-z0-9]+"), key)){
                    String end = key.substring(4);
                    endPoint = this.userVariables.get(end);
                } else {
                    NodeAttribute endNode = NodeAttribute.getNodeAttribute(Character.toString(key.charAt(3)));
                    endPoint = getNodePoint(endNode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /* applying the midpoint formula to find the center */
            @SuppressWarnings("unused")
            int midX = (startPoint.x + endPoint.x)/2 - value.length();
            @SuppressWarnings("unused")
            int midY = (startPoint.y + endPoint.y)/2;

            /* Draw the orders at the center of the line. */
            // g.drawString(value, midX, midY);     // commented because it clutters everything up
        });
    }
    
    /**
     * Used to see all the points.
     * @param g
     */
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

    /**
     * Used to graph a connected planar graph. Warning - this does not work anymore.
     * @param g
     */
    @SuppressWarnings("unused")
    private void drawConnectedPlanarGraph(Graphics g){
        g.setColor(Color.BLACK);

        //draw all 14 verticies and link them into a complete planar graph
        drawLink(g, pointA, pointB);    //draw the links first! this way, the letters aren't getting blocked.
        drawLink(g, pointB, pointC);    //blocked letters are no longer a problem with the new design, but the point being blocked is.
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

    /**
     * Given an adjacency list, graph it.
     * @param g
     */
    private void drawAdjacencyList(Graphics g){
        HashSet<NodeAttribute> nodesToDraw = new HashSet<>();

        for (NodeVector nodeVector : this.adjacencyList) {
            Point startPoint = getNodePoint(nodeVector.getStartNode());
            Point endPoint = getNodePoint(nodeVector.getEndNode());
            if(endPoint.x != 0 && endPoint.y != 0){
                if(nodeVector.getStartNode() == nodeVector.getEndNode()){
                    drawSelfReferentialLink(g, endPoint);
                } else {
                    drawLink(g, startPoint, endPoint);
                }
                nodesToDraw.add(nodeVector.getEndNode());
            } else {
                addPoint(g, nodeVector.getNodeName());
                drawLink(g, startPoint, this.userVariables.get(nodeVector.getNodeName()));
            }

            addOrders(nodeVector);

            nodesToDraw.add(nodeVector.getStartNode());
        }

        for(NodeAttribute nodeAttribute : nodesToDraw){
            if(nodeAttribute != null){
                drawPoint(g, getNodePoint(nodeAttribute), nodeAttribute.toString());
            }
        }

        drawOrders(g);
    }    
}