/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *        
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.graph.util.delaunay;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.line.XYNode;

import com.vividsolutions.jts.geom.Coordinate;

/**
 *
 * @author jfc173
 *
 *
 * @source $URL$
 */
public class GraphViewer extends JPanel{
    
    Graph graph;
    Collection nodes;
    double minX, minY;
    int xScaling = 4;
    int yScaling = 4;
    int xOffset = 0;
    int yOffset = 0;
    boolean colorEdges = false;
    Color[] nodeColors = new Color[]{Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.BLACK};
    Vector shortEdges, longEdges, otherEdges;
    
    /** Creates a new instance of GraphViewer */
    public GraphViewer() {  
    }
    
    public void setXScaling(int i){
        xScaling = i;
    }
    
    public int getXScaling(){
        return xScaling;
    }
    
    public void setYScaling(int i){
        yScaling = i;
    }
    
    public int getYScaling(){
        return yScaling;
    }
    
    public void setGraph(Graph gr){
        graph = gr;
        nodes = graph.getNodes();
        Iterator it = nodes.iterator();
        minX = Double.MAX_VALUE;
        minY = Double.MAX_VALUE;
        while (it.hasNext()){
            Object next = it.next();
            if (!(next instanceof XYNode)){
                throw new RuntimeException("I can't draw a node that doesn't have a coordinate.");
            }
            Coordinate coord = ((XYNode) next).getCoordinate();
            if (coord.x < minX){
                minX = coord.x;
            }
            if (coord.y < minY){
                minY = coord.y;
            }
        }        
    }
    
    public void setColorEdges(boolean b){
        colorEdges = b;
    }
    
    public void setShortEdges(Vector l){
        shortEdges = l;
    }
    
    public void setLongEdges(Vector l){
        longEdges = l;
    }
    
    public void setOtherEdges(Vector l){
        otherEdges = l;
    }
    
    public void paintComponent(Graphics g){
        int i = 0;
        xOffset = (int) Math.round(xScaling - minX*xScaling);
        yOffset = (int) Math.round(yScaling - minY*yScaling);
        System.out.println("xOffset is " + xOffset);
        System.out.println("yOffset is " + yOffset);
        Iterator it = nodes.iterator();
        while (it.hasNext()){
            Object next = it.next();
            if (!(next instanceof XYNode)){
                throw new RuntimeException("I can't draw a node that doesn't have a coordinate.");
            }
            Coordinate coord = ((XYNode) next).getCoordinate();
//            g.setColor(nodeColors[i]);
//            i++; //this works if there are no more than 10 nodes.
            g.fillOval((int) Math.round(xOffset+coord.x*xScaling-2), 
                       (int) Math.round(yOffset+coord.y*yScaling-2), 
                       4, 
                       4);          
        }
        
        g.setColor(Color.RED);        
        Collection edges = graph.getEdges();
        Iterator edgeIt = edges.iterator();
        while (edgeIt.hasNext()){
            Edge next = (Edge) edgeIt.next();
            if (!((next.getNodeA() instanceof XYNode) && (next.getNodeB() instanceof XYNode))){
                throw new RuntimeException("I can't draw an edge without endpoint coordinates.");
            }
            Coordinate coordA = ((XYNode) next.getNodeA()).getCoordinate();
            Coordinate coordB = ((XYNode) next.getNodeB()).getCoordinate();
            if (colorEdges){
                if (shortEdges.contains(next)){
                    g.setColor(Color.RED);
                } else if (longEdges.contains(next)){
                    g.setColor(Color.GREEN);
                } else if (otherEdges.contains(next)){
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.YELLOW);
                }                
            }
            g.drawLine((int) Math.round(xOffset+coordA.x*xScaling),
                       (int) Math.round(yOffset+coordA.y*yScaling),
                       (int) Math.round(xOffset+coordB.x*xScaling),
                       (int) Math.round(yOffset+coordB.y*yScaling));
        }
    }
   
}
