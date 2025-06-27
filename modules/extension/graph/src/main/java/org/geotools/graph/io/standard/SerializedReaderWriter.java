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

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.io.serialization.ValidatingObjectInputStream;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.io.GraphReaderWriter;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

/**
 * An implementation of GraphReaderWriter that uses java serialization to read and write graph objects. During the graph
 * serialization process edges are written to the object output stream. Along with the edges, the two nodes incident to
 * the edge are also written. However, edge adjacency lists of nodes are <B>not</B> written to the output stream in
 * order to prevent deep recursive calls that often result in a stack overflow. Therefore it is important that any
 * implementation of the Node interface declare its edge adjacecny list (if any) as transient in order to support graph
 * serializability. <br>
 * Because edge adjacency lists are not serialized, they must be reconstructed upon deserialization in order to preserve
 * the original graph structure.<br>
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
@SuppressWarnings("BanSerializableRead")
public class SerializedReaderWriter extends AbstractReaderWriter implements FileReaderWriter {

    /**
     * Deserializes the graph and reconstructs the original structure.
     *
     * @see GraphReaderWriter#read()
     */
    @Override
    public Graph read() throws Exception {
        // read builder property
        GraphBuilder builder = (GraphBuilder) getProperty(BUILDER);

        // create file input stream
        try (ValidatingObjectInputStream objin = ValidatingObjectInputStream.builder()
                .setFile((String) getProperty(FILENAME))
                .get()) {

            // only allow graph objects and arrays of graph objects
            objin.accept("org.geotools.graph.structure.*", "[Lorg.geotools.graph.structure.*");

            // read header
            objin.readInt(); // nnodes, not used
            int nedges = objin.readInt();

            // rebuild edge collection, upon reading an edge, at the edge to the
            // adjacency list of each of its nodes
            int count = 0;
            while (count++ < nedges) {
                Edge e = (Edge) objin.readObject();
                e.getNodeA().setVisited(false);
                e.getNodeB().setVisited(false);
                builder.addEdge(e);
            }

            // rebuild node collection
            for (Edge e : builder.getGraph().getEdges()) {
                if (!e.getNodeA().isVisited()) {
                    e.getNodeA().setVisited(true);
                    builder.addNode(e.getNodeA());
                }

                if (!e.getNodeB().isVisited()) {
                    e.getNodeB().setVisited(true);
                    builder.addNode(e.getNodeB());
                }
            }

            // check if object stream is empty, if not, there are nodes of degree 0
            // in the graph
            try {
                Node n;

                while ((n = (Node) objin.readObject()) != null) {
                    builder.addNode(n);
                }
            } catch (EOFException ignore) {
            }
        }

        return builder.getGraph();
    }

    /**
     * Serializes the graph by writing each edge in the graph to an object output stream. If there any nodes of degree 0
     * in the graph, then they are appended to the end of the object output stream.
     *
     * @see GraphReaderWriter#write()
     */
    @Override
    public void write(Graph graph) throws Exception {
        // create file output stream
        try (ObjectOutputStream objout = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream((String) getProperty(FILENAME))))) {

            // create header
            // 1. number of nodes
            // 2. number of edges
            objout.writeInt(graph.getNodes().size());
            objout.writeInt(graph.getEdges().size());

            // write out edges (note: nodes do not write out adjacent edges)
            for (Edge e : graph.getEdges()) {
                objout.writeObject(e);
            }

            // write out any nodes that have no adjacent edges
            for (Node n : graph.getNodesOfDegree(0)) {
                objout.writeObject(n);
            }

            objout.flush();
        }
    }
}
