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

import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.graph.structure.basic.BasicEdge;
import org.geotools.graph.structure.line.XYNode;

/** @author jfc173 */
public class DelaunayEdge extends BasicEdge {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(DelaunayEdge.class);
    Triangle faceA, faceB;

    /** Creates a new instance of DelaunayEdge */
    public DelaunayEdge(XYNode nodeA, XYNode nodeB) {
        super(nodeA, nodeB);
    }

    public void disconnect() {
        this.getNodeA().remove(this);
        this.getNodeB().remove(this);
    }

    public void setFaceA(Triangle t) {
        if (t.equals(faceB)) {
            throw new RuntimeException("Face A must be different from Face B.");
        }
        faceA = t;
    }

    public void setFaceB(Triangle t) {
        if (t.equals(faceA)) {
            throw new RuntimeException("Face A must be different from Face B.");
        }
        faceB = t;
    }

    public boolean hasEndPoint(XYNode node) {
        return node.equals(this.getNodeA()) || node.equals(this.getNodeB());
    }

    public Triangle getOtherFace(Triangle t) {
        if (faceA.equals(t)) {
            return faceB;
        } else if (faceB.equals(t)) {
            return faceA;
        } else {
            LOGGER.warning("Oops.  Input face is " + t);
            LOGGER.warning("Face A is " + faceA);
            LOGGER.warning("Face B is " + faceB);
            throw new RuntimeException(
                    "DelaunayEdge.getOtherFace must receive as input one of the faces bordering that edge.");
        }
    }

    public void setOtherFace(Triangle newT, Triangle oldT) {
        if (faceA.equals(oldT)) {
            if (newT.equals(faceA)) {
                LOGGER.warning("Oops.  Face A is " + faceA + " and new Triangle is " + newT);
                throw new RuntimeException("Face A must be different from Face B.");
            }
            this.setFaceB(newT);
        } else if (faceB.equals(oldT)) {
            if (newT.equals(faceB)) {
                LOGGER.warning("Oops.  Face B is " + faceB + " and new Triangle is " + newT);
                throw new RuntimeException("Face A must be different from Face B.");
            }
            this.setFaceA(newT);
        } else {
            throw new RuntimeException(
                    "DelaunayEdge.setOtherFace must have either faceA or faceB as the oldT parameter.");
        }
    }

    public double getEuclideanDistance() {
        return ((XYNode) this.getNodeA()).getCoordinate().distance(((XYNode) this.getNodeB()).getCoordinate());
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DelaunayEdge
                && (this.getNodeA().equals(((DelaunayEdge) o).getNodeA())
                                && this.getNodeB().equals(((DelaunayEdge) o).getNodeB())
                        || this.getNodeA().equals(((DelaunayEdge) o).getNodeB())
                                && this.getNodeB().equals(((DelaunayEdge) o).getNodeA()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getNodeA(), this.getNodeB());
    }

    @Override
    public String toString() {
        return this.getNodeA().toString() + "--" + this.getNodeB().toString();
    }
}
