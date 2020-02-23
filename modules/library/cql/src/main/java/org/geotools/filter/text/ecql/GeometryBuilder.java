/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.ecql;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.commons.Result;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.filter.expression.Literal;

/**
 * Builds Geometry
 *
 * <p>This builder is responsible to make the geometry using the elements pushed in the parsing
 * process in the stack.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
abstract class GeometryBuilder {

    private BuildResultStack resultStack;
    private String statement;

    /**
     * New instance of geometry builder
     *
     * @param statement the statement that is parsing
     */
    public GeometryBuilder(final String statement, final BuildResultStack resultStack) {
        assert statement != null;
        assert resultStack != null;

        this.statement = statement;
        this.resultStack = resultStack;
    }

    protected String getStatemet() {
        return this.statement;
    }

    protected GeometryFactory getGeometryFactory() {
        return new GeometryFactory();
    }

    protected BuildResultStack getResultStack() {
        return this.resultStack;
    }

    public Geometry build() throws CQLException {
        throw new UnsupportedOperationException("should be implemented by subclass");
    }

    /** @param idNode Node's identifier specified in the grammar */
    public Geometry build(final int idNode) throws CQLException {
        throw new UnsupportedOperationException("should be implemented by subclass");
    }

    protected Coordinate[] asCoordinate(Stack<Coordinate> stack) {

        int size = stack.size();
        Coordinate[] coordinates = new Coordinate[size];
        int i = 0;
        while (!stack.empty()) {
            coordinates[i++] = (Coordinate) stack.pop();
        }
        return coordinates;
    }

    /**
     * Makes an stack with the geometries indeed by the typeGeom
     *
     * @return an Stack with the required geometries
     */
    protected Stack<Coordinate> popCoordinatesOf(int geomNode) throws CQLException {
        Stack<Coordinate> stack = new Stack<Coordinate>();
        while (!getResultStack().empty()) {

            Result result = getResultStack().peek();

            int node = result.getNodeType();
            if (node != geomNode) {
                break;
            }
            getResultStack().popResult();
            Coordinate coordinate = (Coordinate) result.getBuilt();

            stack.push(coordinate);
        }
        return stack;
    }

    /**
     * Pop the indeed geometry and order the result before delivery the list
     *
     * @param geometryNode geometry required
     * @return a list of indeed geometries
     */
    protected List<Geometry> popGeometry(final int geometryNode)
            throws org.geotools.filter.text.cql2.CQLException {

        List<Geometry> geomList = new LinkedList<Geometry>();
        while (!getResultStack().empty()) {

            Result result = getResultStack().peek();
            if (result.getNodeType() != geometryNode) {
                break;
            }
            getResultStack().popResult();

            Geometry geometry = (Geometry) result.getBuilt();
            geomList.add(geometry);
        }
        Collections.reverse(geomList);

        return geomList;
    }

    /**
     * Pop the indeed geometry and order the result before delivery the list
     *
     * @param geometryNode geometry required
     * @return a list of indeed geometries
     */
    protected List<Geometry> popGeometryLiteral(final int geometryNode) throws CQLException {

        List<Geometry> geomList = new LinkedList<Geometry>();
        while (!getResultStack().empty()) {

            Result result = getResultStack().peek();
            if (result.getNodeType() != geometryNode) {
                break;
            }
            getResultStack().popResult();

            Literal geometry = (Literal) result.getBuilt();
            geomList.add((Geometry) geometry.getValue());
        }
        Collections.reverse(geomList);

        return geomList;
    }
}
