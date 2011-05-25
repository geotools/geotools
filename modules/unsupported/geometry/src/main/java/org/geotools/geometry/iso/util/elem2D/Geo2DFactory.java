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
package org.geotools.geometry.iso.util.elem2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 * @source $URL$
 */
public class Geo2DFactory {

	private static class TN {
		Node2D nodeObject;
		Vector<Triangle2D> tri = new Vector<Triangle2D>(6,2);		
		public TN(Object o) {
			nodeObject = (Node2D)o;
		}
		/**
		 * returns the side of the left and of the right triangles
		 * @param n0
		 * @param n1
		 * @return
		 */
		public static int[] getTriangles(Node2D n0, Node2D n1) {
			TN tn0 = (TN)n0.object;
			TN tn1 = (TN)n1.object;
			int side[] = new int[2];
			side[0] = -1;
			side[1] = -1;
			for (Iterator<Triangle2D> it0 = tn0.tri.iterator(); it0.hasNext(); ) {
				Triangle2D t0 = it0.next();
				Node2D p0[] = t0.getPoints();
				for (int i=0; i<3; ++i) {
					if (p0[i]==n0 && p0[(i+1)%3]==n1) {
						side[0] = i;
					}
				}
			}
			for (Iterator<Triangle2D> it1 = tn0.tri.iterator(); it1.hasNext(); ) {
				Triangle2D t1 = it1.next();
				Node2D p1[] = t1.getPoints();
				for (int i=0; i<3; ++i) {
					if (p1[i]==n1 && p1[(i+1)%3]==n0) {
						side[i] = i;
					}
				}
			}
			return side;
		}

	}
	
	public static Edge2D getEdge(Node2D n0, Node2D n1) {
		if (n0==null || n1==null) return null;
		return n0.getEdge(n1);
//		Edge2D edges0[] = n0.getEdges();
//		Edge2D edges1[] = n1.getEdges();
//		if (edges0==null || edges1==null) return null;
//		for (int i=0; i<edges0.length; ++i) {
//			for (int j=0; j<edges1.length; ++j) {
//				if (edges0[i]==edges1[j]) return edges0[i];
// 			}
//		}
//		return null;
	}

	public static Edge2D createEdgeAndNodes(double x0, double y0, double x1, double y1) {
		Node2D n0 = new Node2D(x0,y0);
		Node2D n1 = new Node2D(x1,y1);
		Edge2D edge = new Edge2D(n0,n1);
		n0.linkEdge(edge);
		n1.linkEdge(edge);
		return edge;
	}

	public static Edge2D createEdgeAndNode(Node2D n0, double x1, double y1) {
		Node2D n1 = new Node2D(x1,y1);
		Edge2D edge = new Edge2D(n0,n1);
		n0.linkEdge(edge);
		n1.linkEdge(edge);
		return edge;
	}

	public static Edge2D createEdgeAndNode(double x0, double y0, Node2D n1) {
		Node2D n0 = new Node2D(x0,y0);
		Edge2D edge = new Edge2D(n0,n1);
		n0.linkEdge(edge);
		n1.linkEdge(edge);
		return edge;
	}

	public static Edge2D createEdgeFromNodes(Node2D n0, Node2D n1) {
		if (n0==null || n1==null) return null;
		Edge2D edge = new Edge2D(n0,n1);
		n0.linkEdge(edge);
		n1.linkEdge(edge);
		return edge;
	}

	public static Edge2D createEdge(Node2D nodeFrom, Node2D nodeTo, Simplex2D rightSimplex, Simplex2D leftSimplex) {
		if (nodeFrom==null || nodeTo==null) return null;
		assert nodeFrom.getEdge(nodeTo)==null;
		assert nodeTo.getEdge(nodeFrom)==null;
		
		Edge2D edge = new Edge2D(nodeFrom,nodeTo);
		nodeFrom.linkEdge(edge);
		nodeTo.linkEdge(edge);
		if (rightSimplex!=null) {
			int side = rightSimplex.getSide(nodeFrom,nodeTo);
			if (side==-1) return null;
			rightSimplex.linkEdge(side,edge);
			edge.setSimplex(rightSimplex, Edge2D.RIGHTSIDE);
		}
		if (leftSimplex!=null) {
			int side = leftSimplex.getSide(nodeFrom,nodeTo);
			if (side==-1) return null;
			leftSimplex.linkEdge(side,edge);
			edge.setSimplex(leftSimplex, Edge2D.LEFTSIDE);
		}
		return edge;
	}
	
	public static boolean connectEdgeSimplex(Edge2D edge, Simplex2D simplex, int edgeSide) {
		if (edge==null || simplex==null || (edgeSide!=Edge2D.RIGHTSIDE && edgeSide!=Edge2D.LEFTSIDE) ) return false;
		int simplexSide = simplex.getSide(edge.getNode1(), edge.getNode2());
		if (simplexSide==-1) return false;
		simplex.linkEdge(simplexSide,edge);
		edge.setSimplex(simplex,edgeSide);
		return true;
	}

	public static boolean connectSimplexSimplex(Simplex2D simplex0, Simplex2D simplex1) {
		if (simplex0==null || simplex1==null) return false;
		return simplex0.linkSimplex(simplex1);
	}

	public static Object[] splitEdge(Edge2D edge, Point2D p) {

		if (edge.getLeftSimplex()!=null || edge.getRightSimplex()!=null) {
			System.out.println("error connectSimplexSimplex(Simplex2D simplex0, Simplex2D simplex1)");
			return null;
		}
		
		Node2D n1 = edge.getNode1();
		Node2D n2 = edge.getNode2();
		n1.unlinkEdge(edge);
		n2.unlinkEdge(edge);

		Node2D newNode = new Node2D(p);
		edge.setNodes(n1,newNode);
		Edge2D newEdge = new Edge2D(newNode, n2);
		n1.linkEdge(edge);
		n2.linkEdge(newEdge);
		newNode.linkEdge(edge);
		newNode.linkEdge(newEdge);

		newEdge.object = edge.object;
		return new Object[] {newEdge,newNode};
	}
	
	/**
	 * @param edge
	 * @param n1
	 * @param ns
	 */
	private static void connect(Edge2D edge, Node2D n0, Node2D n1) {
		if (edge.hasPoint(n0) && edge.hasPoint(n1)) {
			System.out.println("error connect(Edge2D edge, Node2D n0, Node2D n1)");
		}
		edge.setNodes(n0, n1);
		n0.linkEdge(edge);
		n1.linkEdge(edge);
	}

	/**
	 * removes the edge, maintains the nodes
	 * @param edge
	 */
	public static void removeEdge(Edge2D edge) {
		Node2D n1 = edge.getNode1();
		Node2D n2 = edge.getNode2();
		n1.unlinkEdge(edge);
		n2.unlinkEdge(edge);
	}

	/**
	 * @param p1
	 * @param p2
	 */
	public static void setEdgeNodes(Edge2D e, Node2D n1, Node2D n2) {
		assert (e.getLeftSimplex()==null && e.getRightSimplex()==null);
		assert getEdge(n1,n2)==null;
		Node2D nn1 = e.getNode1();
		Node2D nn2 = e.getNode2();
		if (n1!=nn1) {
			n1.appendEdges(nn1.getEdges());
		}
		if (n2!=nn2) {
			n2.appendEdges(nn2.getEdges());
		}
		e.setNodes(n1,n2);
	}

	public static void setTriangleNodes(Triangle2D tri, Node2D n0, Node2D n1, Node2D n2) {
		tri.point[0] = n0;
		tri.point[1] = n1;
		tri.point[2] = n2;
	}

	public static void setTopology(ArrayList<Triangle2D> triangles, ArrayList<Edge2D> edges) {

		if (edges == null) edges = new ArrayList<Edge2D>();
		if (triangles == null) triangles = new ArrayList<Triangle2D>();

		// unlink eventually linked triangles, link the nodes of each triangle
		// with its triangle, create a temporary list node-triangles and insert
		// the triangles associated to each node
		for (Iterator<Triangle2D> it = triangles.iterator(); it.hasNext();) {
			Triangle2D tri = it.next();
			tri.linkSimplex(0,null);
			tri.linkSimplex(1,null);
			tri.linkSimplex(2,null);
			Node2D n[] = tri.getPoints();
			for (int i=0; i<3; ++i) {
				n[i].linkSimplex(tri);
				if ( !(n[i].object instanceof TN) ) n[i].object = new TN(n[i].object);
				((TN)n[i].object).tri.add(tri);
			}
		}

		// set the triangle-triangle association for triangles sharing two nodes
		for (Iterator<Triangle2D> it = triangles.iterator(); it.hasNext();) {
			Triangle2D tri = it.next();
			Node2D n[] = tri.getPoints();
			for (int i=0; i<3; ++i) {
				if (tri.hasNeighbour(i)) continue;
				TN tn = ((TN)n[i].object);
				for (Iterator<Triangle2D> itt = tn.tri.iterator(); itt.hasNext(); ) {
					Triangle2D t = itt.next();
					if ( t == tri ) continue;
					int side = t.getSide(n[i],n[(i+1)%3]);
					if ( side != -1 )  {
						tri.linkSimplex(i,t);
						t.linkSimplex(side,tri);
						break;
					}
				}
			}
		}

		// edge-triangle associations
		for (Iterator<Edge2D> it = edges.iterator(); it.hasNext();) {
			Edge2D e = it.next();
			Node2D n0 = e.getNode1();
			Node2D n1 = e.getNode2();
			TN tn0 = (TN)n0.object;
			TN tn1 = (TN)n1.object;
			int side0 = -1;
			int side1 = -1;
			Triangle2D tri0 = null;
			Triangle2D tri1 = null;
			for (Iterator<Triangle2D> it0 = tn0.tri.iterator(); it0.hasNext(); ) {
				Triangle2D t0 = it0.next();
				Node2D p0[] = t0.getPoints();
				for (int i=0; i<3; ++i) {
					if (p0[i]==n0 && p0[(i+1)%3]==n1) {
						side0 = i;
						tri0 = t0;
						break;
					}
				}
				if (tri0!=null) break;
			}
			for (Iterator<Triangle2D> it1 = tn1.tri.iterator(); it1.hasNext(); ) {
				Triangle2D t1 = it1.next();
				Node2D p1[] = t1.getPoints();
				for (int i=0; i<3; ++i) {
					if (p1[i]==n1 && p1[(i+1)%3]==n0) {
						side1 = i;
						tri1 = t1;
						break;
					}
				}
				if (tri1!=null) break;
			}
			
			assert ( (side0!=-1) && (side1!=-1) );
			
			if (side0!=-1) {
				e.surfaceLeft = tri0;
				tri0.linkEdge(side0,e);
			}
			if (side1!=-1) {
				e.surfaceRight = tri1;
				tri1.linkEdge(side1,e);
			}
		}
		
		// now the edges. An edge-node connection will prevail over triangle-triangle associations
		for (Iterator<Edge2D> it = edges.iterator(); it.hasNext();) {
			Edge2D e = it.next();
			e.surfaceRight = null;
			e.surfaceLeft = null;
			Node2D n1 = e.getNode1();
			Node2D n2 = e.getNode2();
			n1.linkEdge(e);
			n2.linkEdge(e);
		}

		// reset the temporary list of triangles connected to each node.
		// Reestablish the former object values of each node
		for (Iterator<Triangle2D> it = triangles.iterator(); it.hasNext();) {
			Triangle2D tri = it.next();
			Node2D n[] = tri.getPoints();
			for (int i=0; i<3; ++i) {
				if ( (n[i].object instanceof TN) ) n[i].object = ((TN)n[i].object).nodeObject;
			}
		}
	}

	/**
	 * the nodes will be merged. The edges from the second node (n1) will be
	 * connected from n1 and connected to n0
	 * 
	 * @param n0
	 * @param n1
	 */
	public static void mergeNodes(Node2D n0, Node2D n1) {
		Edge2D edges[] = n1.getEdges();
		for (int i = 0; i < edges.length; ++i ) {
			Edge2D e = edges[i];
			n1.unlinkEdge(e);
			if ( !n0.hasEdge(e) ) n0.linkEdge(e);
		}
	}

	/**
	 * @param post
	 * @return
	 */
	public static Node2D createNode(Point2D p) {
		return new Node2D(p);
	}

	/**
	 * @param x
	 * @param y
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	public static Triangle2D createTriangleAndNodes(double x0, double y0, double x1, double y1, double x2, double y2) {
		Node2D n0 = new Node2D(x0,y0);
		Node2D n1 = new Node2D(x1,y1);
		Node2D n2 = new Node2D(x2,y2);
		return new Triangle2D(n0,n1,n2);
	}


}
