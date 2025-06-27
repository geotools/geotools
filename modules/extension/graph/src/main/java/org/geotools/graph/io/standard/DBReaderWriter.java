/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.io.standard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

/**
 * An implementation of GraphReaderWriter used for reading and writing graph objects to and from a database. <br>
 * <br>
 * Upon reading, the database is queried using the getQuery() template method, and a representation of the objects to be
 * modelled by the graph are returned through a standard ResultSet. From each tuple in the result set, the object is
 * recreated via the template method readInternal(ResultSet). The object is then passed to an underlying graph generator
 * and the graph components used to model the object are constructed.<br>
 * <br>
 * Upon writing, the graph is read component by component based on set properties. If the NODES property is set, nodes
 * will be written. If the EDGES property is set, edges will be written as well. As each component is processed, it is
 * passed to the repspective template methods writeNode(Statement,Node) and writeEdge(Statement,Edge). The methods then
 * execute a statement to create the database representation of the graph component.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public abstract class DBReaderWriter extends AbstractReaderWriter {

    /** JDBC driver class name key * */
    public static final String DRIVERCLASS = "DRIVERCLASS";

    /** JDBC driver url * */
    public static final String DRIVERURL = "DRIVERURL";

    /** Database server key * */
    public static final String SERVER = "SERVER";

    /** Database port key * */
    public static final String PORT = "PORT";

    /** Database name key * */
    public static final String DBNAME = "DBNAME";

    /** User name key * */
    public static final String USERNAME = "USERNAME";

    /** Table key * */
    public static final String TABLENAME = "TABLENAME";

    /**
     * Performs a graph read by querying the database and processing each tuple returned in the query. As each tuple is
     * processed, the graph components represented by the tuple are created by an underlying GraphGenerator.
     *
     * @see org.geotools.graph.io.GraphReaderWriter#read()
     */
    @Override
    public Graph read() throws Exception {
        // get underlying generator
        GraphGenerator generator = (GraphGenerator) getProperty(GENERATOR);

        // query database to obtain graph information
        try (Connection conn = getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(getQuery())) {
            //    System.out.println(getQuery());

            while (rs.next()) {
                generator.add(readInternal(rs));
            }
        }

        return generator.getGraph();
    }

    /**
     * Performs a write on the graph out to the database. If the NODES property is set, the nodes of the graph will be
     * written, and if the EDGES property is set, the edges of the graph will be written.
     *
     * <p>* @see GraphGenerator#write()
     */
    @Override
    public void write(Graph g) throws Exception {
        // get database connection
        try (Connection conn = getConnection();
                Statement st = conn.createStatement()) {

            // write nodes if property set
            if (getProperty(NODES) != null) {
                for (Node node : g.getNodes()) {
                    writeNode(st, node);
                }
            }

            // write edges if property set
            if (getProperty(EDGES) != null) {
                for (Edge edge : g.getEdges()) {
                    writeEdge(st, edge);
                }
            }
        }
    }

    /**
     * Opens a connection to the database, based on set properties.
     *
     * @return Connection to the database.
     */
    protected Connection getConnection() throws Exception {
        // read database + driver properties
        String driverclass = (String) getProperty(DRIVERCLASS);
        String driverurl = (String) getProperty(DRIVERURL);
        String server = (String) getProperty(SERVER);
        String port = (String) getProperty(PORT);
        String dbname = (String) getProperty(DBNAME);
        String username = (String) getProperty(USERNAME);

        Class.forName(driverclass);
        return DriverManager.getConnection(driverurl + server + ":" + port + "/" + dbname + "?user=" + username);
    }

    /**
     * Template method used to write a node into the database.
     *
     * @param st Statement used to execute write statement.
     * @param node Node to write.
     */
    protected void writeNode(Statement st, Node node) {}

    /**
     * Template method used to write an edge into the database.
     *
     * @param st Statement used to execute write statement.
     * @param edge Edge to write.
     */
    protected void writeEdge(Statement st, Edge edge) {}

    /**
     * Template method which returns the query to execute in order to read a graph from the database.
     *
     * @return SQL query.
     */
    protected abstract String getQuery();

    /**
     * Template method used to create the object represented by a tuple returned by the database query.
     *
     * @param rs ResultSet of query.
     * @return Object represented by current tuple of result set.
     */
    protected abstract Object readInternal(ResultSet rs) throws Exception;
}
