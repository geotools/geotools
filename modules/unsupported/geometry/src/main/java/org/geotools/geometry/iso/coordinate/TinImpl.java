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
package org.geotools.geometry.iso.coordinate;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.opengis.geometry.primitive.SurfacePatch;

/**
 * @author Jackson Roehrig & Sanjay Jena
 * 
 * A GM_Tin (Figure 21) is a GM_TriangulatedSurface that uses the Delaunay
 * algorithm or a similar algorithm complemented with consideration for
 * breaklines, stoplines and maximum length of triangle sides (Figure 22). These
 * networks satisfy the Delaunay criterion away from the modifications: For each
 * triangle in the network, the circle passing through its vertexes does not
 * contain, in its interior, the vertex of any other triangle.
 *
 *
 * @source $URL$
 */
public class TinImpl extends TriangulatedSurfaceImpl {

	/**
	 * Stoplines are lines where the local continuity or regularity of the
	 * surface is questionable. In the area of these pathologies, triangles
	 * intersecting a stopline shall be removed from the TIN surface, leaving
	 * holes in the surface. If coincidence occurs on surface boundary
	 * triangles, the result shall be a change of the surface boundary. The
	 * attribute "stopLines" contains all these pathological segments as a set
	 * of line strings.
	 * 
	 * GM_Tin::stopLines : Set<GM_LineString>
	 */
	private ArrayList<LineStringImpl> stopLines = null; /*
														 * ArrayList of
														 * GM_LineSegment
														 */

	/**
	 * Breaklines are lines of a critical nature to the shape of the surface,
	 * representing local ridges, or depressions (such as drainage lines) in the
	 * surface. As such their constituent segments must be included in the TIN
	 * even if doing so violates the Delaunay criterion. The attribute
	 * "breakLines" contains these critical segments as a set of line strings.
	 * 
	 * GM_Tin::breakLines : Set<GM_LineString>
	 */
	private ArrayList<LineStringImpl> breakLines = null; /*
															 * ArrayList of
															 * GM_LineSegment
															 */

	/**
	 * Areas of the surface where the data is not sufficiently dense to assure
	 * reasonable calculations shall be removed by adding a retention criterion
	 * for triangles based on the length of their sides. For any triangle sides
	 * exceeding maximum length, the adjacent triangles to that triangle side
	 * shall be removed from the surface.
	 * 
	 * GM_Tin::maxLength : Distance
	 */
	private double maxLength = Double.NaN;

	@SuppressWarnings("unused")
	private double minLength = Double.NaN;

	/**
	 * The corners of the triangles in the TIN are often referred to as posts.
	 * The attribute "controlPoint" shall contain a set of the GM_Positions used
	 * as posts for this TIN. Since each TIN contains triangles, there must be
	 * at least 3 posts. The order in which these points are given does not
	 * affect the surface that is represented. Application schemas may add
	 * information based on the ordering of the control points to facilitate the
	 * reconstruction of the TIN from the controlPoints.
	 * 
	 * GM_Tin::controlPoint[3..n] : GM_Position
	 * 
	 * NOTE The control points of a TIN are often called "posts."
	 */
	private ArrayList<PositionImpl> controlPoint = null; /*
															 * ArrayList of
															 * GM_Position
															 */

	@SuppressWarnings("unused")
	private ArrayList<LineSegmentImpl> front;

	/**
	 * The constructor for a restricted Delaunay network requires the triangle
	 * corners (posts), breaklines, stoplines, and maximum length of a triangle
	 * side.
	 * 
	 * GM_Tin::GM_Tin(post : Set<GM_Position>, stopLines : Set<GM_LineString>,
	 * breakLines : Set<GM_LineString>, maxLength : Number): GM_Tin
	 * 
	 * @param tin
	 */

	// public TinImpl(TinImpl tin) {
	// super(tin);
	// if (tin.breakLines!=null) {
	// this.breakLines = new ArrayList<LineStringImpl>(tin.breakLines);
	// } else {
	// this.breakLines = null;
	// }
	// if (tin.stopLines != null) {
	// this.stopLines = new ArrayList<LineStringImpl>(tin.stopLines);
	// } else {
	// tin.stopLines = null;
	// }
	// this.maxLength = tin.maxLength;
	// this.minLength = tin.minLength;
	// if ( tin.mFront!=null ) {
	// this.mFront = new ArrayList<LineSegmentImpl>(tin.mFront);
	// } else {
	// tin.mFront = null;
	// }
	// this.controlPoint = new ArrayList<PositionImpl>();
	//
	// HashMap<PositionImpl,PositionImpl> hm = new
	// HashMap<PositionImpl,PositionImpl>();
	// for (PositionImpl otherPos : tin.getPosts()) {
	// PositionImpl thisPos = new PositionImpl(otherPos.getDirectPosition());
	// this.controlPoint.add(thisPos);
	// hm.put(otherPos,thisPos);
	// }
	// ArrayList<TriangleImpl> thisTriangles = new ArrayList<TriangleImpl>();
	// for (Triangle otherTri : tin.getTriangles()) {
	// Position[] otherPositions = otherTri.getCorners();
	// Position[] thisPositions = new PositionImpl[3];
	// thisPositions[0] = hm.get(otherPositions[0]);
	// thisPositions[1] = hm.get(otherPositions[1]);
	// thisPositions[2] = hm.get(otherPositions[2]);
	// TriangleImpl thisTriangle =
	// CoordinateFactory.getDefault().createTriangle(null,
	// thisPositions[0].getPosition(),
	// thisPositions[1].getPosition(),
	// thisPositions[2].getPosition());
	// thisTriangles.add(thisTriangle);
	// }
	// this.setTriangles(thisTriangles, null);
	// }
	/**
	 * This constructor delegates the mesh generation to different mesh
	 * generators. GM_Factory transforms the results of the mesh generation into
	 * input parameters
	 * 
	 * @param factory
	 * 
	 * @param surfBdry
	 * @param post
	 * @param stopLines
	 * @param breakLines
	 * @param maxLength
	 * @param triangles
	 */
	public TinImpl(SurfaceBoundaryImpl surfBdry,
			ArrayList<PositionImpl> post, ArrayList<LineStringImpl> stopLines,
			ArrayList<LineStringImpl> breakLines, double maxLength,
			ArrayList<TriangleImpl> triangles) {
		super(surfBdry);
		this.controlPoint = post;
		this.breakLines = breakLines;
		this.stopLines = stopLines;
		this.maxLength = maxLength;
		super.setTriangles(triangles);
	}

	/**
	 * @param factory
	 * @param post
	 * @param stopLines
	 * @param breakLines
	 * @param maxLength
	 */
	public TinImpl(PositionImpl[] post,
			LineStringImpl[] stopLines, LineStringImpl[] breakLines,
			double maxLength) {
		/* Call super class; Triangles will be set later */
		super(null);

		this.controlPoint = new ArrayList<PositionImpl>();
		this.breakLines = new ArrayList<LineStringImpl>();
		this.stopLines = new ArrayList<LineStringImpl>();

		/* Adding posts (Control points) */
		if (post.length >= 3) {
			for (int i = 0; i < post.length; i++) {
				this.controlPoint.add(post[i]);
			}
		} else {
			throw new IllegalArgumentException(
					"At least three control points expected."); //$NON-NLS-1$
		}

		/* Adding Stop Lines */
		for (int i = 0; i < stopLines.length; i++) {
			this.stopLines.add(stopLines[i]);
		}

		/* Adding Break Lines */
		for (int i = 0; i < breakLines.length; i++) {
			this.breakLines.add(breakLines[i]);
		}

		/* Setting maximum length */
		this.maxLength = maxLength;

		/* Create Triangles for Surface */
		// temporary solution, without considering stopLines and breakLines,
		// and without generating intermediate points to smooth the mesh
		// TODO JR
		// GeoMshDelaunayTriangulation dTr = new GeoMshDelaunayTriangulation();
		// dTr.createTriangles(post);
		// GM_Triangle[] triangles = dTr.getTriangles();

		/* Set Triangles for Triangulated Surface (Super class) */
		// JR: SJ, ich habe das übeprüft, es fast alles richtig, nur
		// this.setBoundary(new GM_SurfaceBoundary(patch)) hat Fehler.
		// Man muss aus allen Dreiecken die Boundary ermitteln
		// GM_TriangulatedSurface:setTriangles(GM_Triangle[] triangles)
		// sollte diese Arbeit erledigen
		// TODO JR
		// ArrayList<GM_Triangle> tris = new
		// ArrayList<GM_Triangle>(triangles.length);
		// for (GM_Triangle tri : tris) tris.add(tri);
		// super.setTriangles(tris,null);
		// JR gelöscht: erledigt in setTriangles.
		// /* Set this TIN as Associated Surface for each Surface Patch
		// (=Triangle) */
		// for (int i=0; i<triangles.length; i++) {
		// triangles[i].setAssociatedSurface(this);
		// }
	}

	/**
	 * @return List<LineStringImpl>
	 */
	public List<LineStringImpl> getStopLines() {
		return this.stopLines;
	}

	/**
	 * @return List<LineStringImpl>
	 */
	public List<LineStringImpl> getBreakLines() {
		return this.breakLines;
	}

	/**
	 * @return max length
	 */
	public double getMaxLength() {
		return this.maxLength;
	}

	/**
	 * Returns an Array of the surface patches as GM_Triangle´s
	 * 
	 * @return ArrayList<TriangleImpl>
	 */
	public List<TriangleImpl> getTriangles() {
		List<TriangleImpl> triangles = new ArrayList<TriangleImpl>(); //this.getFeatGeometryFactory().getListFactory().createTriangleList();
		for (SurfacePatch sp : this.getPatches()) {
			triangles.add((TriangleImpl) sp);
		}
		return triangles;
	}

	/**
	 * @return List<PositionImpl>
	 */
	public List<PositionImpl> getPosts() {
		return this.controlPoint;
	}
}
