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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import org.geotools.graph.build.GraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

/**
 * An implementation of GraphReaderWriter that reads/writes graphs from/to text files. Each line of the text file
 * represents an object to be modelled by the graph. <br>
 * When performing a read, the text file is read line by line. As each line is read it is tokenized based on the
 * delimiter property, and passed to the template method readInternal(StringTokenizer), which must be implemented by a
 * subclass. The method returns the object that is to be represented in the graph. The returned object is then passed to
 * an underlying GraphGenerator which creates the necessary graph components to modell the object.<br>
 * <br>
 * When performing a write, the graph is read component by component based on set properties. If the NODES property is
 * set, nodes will be written. If the EDGES property is set, edges will be written as well. As each component is
 * processed, it is passed to the repspective template methods writeNode(Writer,Node) and writeEdge(Writer,Edge). The
 * methods then write out the text representation of the component.
 *
 * @author Justin Deoliveira, Refractions Research Inc, jdeolive@refractions.net
 */
public abstract class TextfileReaderWriter extends AbstractReaderWriter implements FileReaderWriter {

    /** line delimiter key * */
    public static final String DELIMITER = "DELIMITER";

    /**
     * Performs a read of the text file line by line. As each line is read the corresponding graph components
     * represented by the line of text are created by an underlying GraphGenerator.
     *
     * @see GraphGenerator#read()
     */
    @Override
    public Graph read() throws Exception {
        // get the underlying generator
        GraphGenerator generator = (GraphGenerator) getProperty(GENERATOR);

        // create in the file reader
        try (BufferedReader in = new BufferedReader(new FileReader((String) getProperty(FILENAME)))) {

            // read the delimiter property
            String delim = (String) getProperty(DELIMITER);
            delim = delim != null ? delim : ",";

            // read file line by line passing each line to template method
            String line;
            while ((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, delim);
                generator.add(readInternal(st));
            }
        }

        return generator.getGraph();
    }

    /**
     * Performs a write on the graph out to a text file. If the NODES property is set, the nodes of the graph will be
     * written, and if the EDGES property is set, the edges of the graph will be written.
     *
     * <p>* @see GraphGenerator#write()
     */
    @Override
    public void write(Graph g) throws Exception {
        // create the file writer
        try (BufferedWriter out = new BufferedWriter(new FileWriter((String) getProperty(FILENAME)))) {

            // check NODES property
            if (getProperty(NODES) != null) {
                for (Node node : g.getNodes()) {
                    writeNode(out, node);
                }
            }

            // check EDGES property
            if (getProperty(EDGES) != null) {
                for (Edge edge : g.getEdges()) {
                    writeEdge(out, edge);
                }
            }
        }
    }

    /**
     * Template method for writing the text representation of a node to an text file.
     *
     * @param out The text file writer.
     * @param n The node to write.
     */
    protected void writeNode(Writer out, Node n) {}

    /**
     * Template method for writing the text representation of an edge to an text file.
     *
     * @param out The text file writer.
     * @param e The edge to write.
     */
    protected void writeEdge(Writer out, Edge e) {}

    /**
     * Template method for returning the object represented by a line in the text file being read.
     *
     * @param st The tokenized line read from text file.
     * @return The object represented by the line of text.
     */
    protected abstract Object readInternal(StringTokenizer st);
}
