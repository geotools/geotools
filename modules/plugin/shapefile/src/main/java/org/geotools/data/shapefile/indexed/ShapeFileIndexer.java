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
package org.geotools.data.shapefile.indexed;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.shapefile.FileWriter;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StorageFile;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.index.LockTimeoutException;
import org.geotools.index.TreeException;
import org.geotools.index.quadtree.Node;
import org.geotools.index.quadtree.QuadTree;
import org.geotools.index.quadtree.StoreException;
import org.geotools.index.quadtree.fs.FileSystemIndexStore;
import org.geotools.index.quadtree.fs.IndexHeader;
import org.geotools.util.NullProgressListener;
import org.geotools.util.logging.Logging;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Utility class for Shapefile spatial indexing
 * 
 * @author Tommaso Nolli
 *
 * @source $URL$
 */
public class ShapeFileIndexer implements FileWriter {
    private static final Logger LOGGER = Logging.getLogger(ShapeFileIndexer.class);
    
    private int max = -1;
    private int leafSize = 16;

	private String byteOrder;
    private boolean interactive = false;
    private ShpFiles shpFiles;

    public static void main(String[] args) throws IOException {
        if ((args.length < 1) || (((args.length - 1) % 2) != 0)) {
            usage();
        }

        long start = System.currentTimeMillis();

        ShapeFileIndexer idx = new ShapeFileIndexer();
        idx.interactive = true;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-t")) {
                // idx.setIdxType(IndexType.valueOf(args[++i]));
                // just skip it for backwards compatibility
                i++;
            } else if (args[i].equals("-M")) {
                idx.setMax(Integer.parseInt(args[++i]));
            } else if (args[i].equals("-s")) {
                idx.setLeafSize(Integer.parseInt(args[++i]));
            } else if (args[i].equals("-b")) {
                idx.setByteOrder(args[++i]);
            } else {
                if (!args[i].toLowerCase().endsWith(".shp")) {
                    System.out.println("File extension must be '.shp'");
                    System.exit(1);
                }

                idx.setShapeFileName(new ShpFiles(args[i]));
            }
        }

        try {
            System.out.print("Indexing ");

            int cnt = idx.index(true, new NullProgressListener());
            System.out.println();
            System.out.print(cnt + " features indexed ");
            System.out.println("in " + (System.currentTimeMillis() - start)
                    + "ms.");
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
            usage();
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("Usage: ShapeFileIndexer " + "-t <QIX> "
                + "[-M <max tree depth>] "
                + "[-b <byte order NL | NM>] " + "<shape file>"
                + "[-s <max number of items in a leaf>]");

        System.out.println();

        System.out.println("Options:");
        System.out.println("\t-t Index type: RTREE or QUADTREE");
        System.out.println();
        System.out.println("Following options apllies only to QUADTREE:");
        System.out.println("\t-b byte order to use: NL = LSB; "
                + "NM = MSB (default)");

        System.exit(1);
    }

    /**
     * Index the shapefile denoted by setShapeFileName(String fileName) If when
     * a thread starts, another thread is indexing the same file, this thread
     * will wait that the first thread ends indexing; in this case <b>zero</b>
     * is reurned as result of the indexing process.
     * 
     * @param verbose
     *                enable/disable printing of dots every 500 indexed records
     * @param listener
     *                DOCUMENT ME!
     * 
     * @return The number of indexed records (or zero)
     * 
     * @throws MalformedURLException
     * @throws IOException
     * @throws TreeException
     * @throws StoreException
     *                 DOCUMENT ME!
     * @throws LockTimeoutException
     */
    public int index(boolean verbose, ProgressListener listener)
            throws MalformedURLException, IOException, TreeException,
            StoreException, LockTimeoutException {
        
        if (this.shpFiles == null) {
            throw new IOException("You have to set a shape file name!");
        }

        int cnt = 0;

        ShapefileReader reader = null;

        // Temporary file for building...
        StorageFile storage = shpFiles.getStorageFile(ShpFileType.QIX);
        File treeFile = storage.getFile();

        try {
            reader = new ShapefileReader(shpFiles, true, false, new GeometryFactory());
            
            if(max == -1) {
                // compute a reasonable index max depth, considering a fully developed
                // 10 levels one already contains 200k index nodes, good for indexing up
                // to 3M features without consuming too much memory
                int features = reader.getCount(0);
                max = 1;
                int nodes = 1;
                while(nodes * leafSize < features) {
                    max++;
                    nodes *= 4;
                }
                if(max < 10) {
                    max = 10;
                }
                
                reader.close();
                reader = new ShapefileReader(shpFiles, true, false, new GeometryFactory());
            }
            
            cnt = this.buildQuadTree(reader, treeFile, verbose);
        } finally {
            if (reader != null)
                reader.close();
        }

        // Final index file
        storage.replaceOriginal();

        return cnt;
    }

    private int buildQuadTree(ShapefileReader reader, File file, boolean verbose)
            throws IOException, StoreException {
        LOGGER.fine("Building quadtree spatial index with depth " +  max + " for file " + file.getAbsolutePath());
        
        byte order = 0;

        if ((this.byteOrder == null) || this.byteOrder.equalsIgnoreCase("NM")) {
            order = IndexHeader.NEW_MSB_ORDER;
        } else if (this.byteOrder.equalsIgnoreCase("NL")) {
            order = IndexHeader.NEW_LSB_ORDER;
        } else {
            throw new StoreException("Asked byte order '" + this.byteOrder
                    + "' must be 'NL' or 'NM'!");
        }

        IndexFile shpIndex = new IndexFile(shpFiles, false);
        QuadTree tree = null;
        int cnt = 0;
        int numRecs = shpIndex.getRecordCount();
        ShapefileHeader header = reader.getHeader();
        Envelope bounds = new Envelope(header.minX(), header.maxX(), header
                .minY(), header.maxY());

        tree = new QuadTree(numRecs, max, bounds, shpIndex);
        try {
            Record rec = null;

            while (reader.hasNext()) {
                rec = reader.nextRecord();
                tree.insert(cnt++, new Envelope(rec.minX, rec.maxX, rec.minY,
                        rec.maxY));

                if (verbose && ((cnt % 1000) == 0)) {
                    System.out.print('.');
                }
                if (cnt % 100000 == 0)
                    System.out.print('\n');
            }
            if (verbose)
                System.out.println("done");
            FileSystemIndexStore store = new FileSystemIndexStore(file, order);
            
            if(leafSize > 0) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Optimizing the tree (this might take some time)");
                }
                optimizeTree(tree, tree.getRoot(), 0, reader, shpIndex);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Tree optimized");
                }
            }
            
            if(LOGGER.isLoggable(Level.FINE)) {
                printStats(tree);
            }
            store.store(tree);
        } finally {
            tree.close();
        }
        return cnt;
    }
    
    private Node optimizeTree(QuadTree tree, Node node, int level, ShapefileReader reader, IndexFile index) throws StoreException, IOException {
        // recurse, with a check to avoid too deep recursion due to odd data that has a
        if(node.getNumShapeIds() > leafSize && node.getNumSubNodes() == 0 && level < max * 2) {
            // ok, we need to split this baby further
            int[] shapeIds = node.getShapesId();
            int numShapesId = node.getNumShapeIds();
            node.clean();
            
            // get an estimate on how many more levels we need
            int extraLevels = 2;
            int nodes = 4;
            while(nodes * leafSize < numShapesId) {
                extraLevels++;
                nodes *= 4;
            }
            
            for (int i = 0; i < numShapesId; i++) {
                final int shapeId = shapeIds[i];
                int offset = index.getOffsetInBytes(shapeId);
                reader.goTo(offset);
                Record rec = reader.nextRecord();
                Envelope env = new Envelope(rec.minX, rec.maxX, rec.minY, rec.maxY);
                tree.insert(node, shapeId, env, extraLevels);
            }
        }
        
        // pack the arrays to use less memory (the optimization often makes the tree grow)
        node.pack();
        
        // recurse 
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            optimizeTree(tree, node.getSubNode(i), level + 1, reader, index);
        }
        
        // prune empty subnodes
        for (int i = 0; i < node.getNumSubNodes();) {
            Node child = node.getSubNode(i);
            if(child != null && child.getNumShapeIds() == 0 && child.getNumSubNodes() == 0) {
                // empty child, we don't need it, clean it up
                node.removeSubNode(child);
            } else {
                i++;
            }
        }
        
        // handle degenerate chains, we pop up the nodes to the top by keeping
        // their shape ids _and_ their bounds (as it's the only area that has something)
        if(node.getNumSubNodes() == 1 && node.getNumShapeIds() == 0) {
            Node subnode = node.getSubNode(0);
            node.clearSubNodes();
            node.setShapesId(subnode);
            node.setBounds(subnode.getBounds());
            for (int i = 0; i < subnode.getNumSubNodes(); i++) {
                node.addSubNode(subnode.getSubNode(i));
            }
        } else {
            // limit this node area to the effective child area
            Envelope bounds = new Envelope();
            if(node.getNumShapeIds() > 0) {
                int[] shapeIds  = node.getShapesId();
                for (int i = 0; i < shapeIds.length; i++) {
                    final int shapeId = shapeIds[i];
                    int offset = index.getOffsetInBytes(shapeId);
                    reader.goTo(offset);
                    Record rec = reader.nextRecord();
                    Envelope env = new Envelope(rec.minX, rec.maxX, rec.minY, rec.maxY);
                    bounds.expandToInclude(env);
                }
            }
            if(node.getNumSubNodes() > 0) {
                for (int i = 0; i < node.getNumSubNodes(); i++) {
                    bounds.expandToInclude(node.getSubNode(i).getBounds());
                }
            }
            node.setBounds(bounds);
            
            // can we shrink?
            int count = node.getNumShapeIds();
            for (int i = 0; i < node.getNumSubNodes(); i++) {
                Node child = node.getSubNode(i);
                if(child.getNumSubNodes() > 0) {
                    count = Integer.MAX_VALUE;
                    break;
                } else {
                    count += child.getNumShapeIds();
                }
            }
            if(count < leafSize) {
                for (int i = 0; i < node.getNumSubNodes(); i++) {
                    Node child = node.getSubNode(i);
                    int[] shapesId = child.getShapesId();
                    for (int j = 0; j < child.getNumShapeIds(); j++) {
                        node.addShapeId(shapesId[j]);
                    }
                }
                node.clearSubNodes();
            }
        }
        
        return node;
    }

    private void printStats(QuadTree tree) throws StoreException {
       Map<Integer, Integer> stats = new HashMap<Integer, Integer>();
       gatherStats(tree.getRoot(), stats);
       
       List<Integer> nums = new ArrayList<Integer>(stats.keySet());
       Collections.sort(nums);
       LOGGER.log(Level.FINE, "Index statistics");
       for (Integer num : nums) {
           LOGGER.log(Level.FINE, num + " -> "  + stats.get(num));
       }
       
    }

    void gatherStats(Node node, Map<Integer, Integer> stats) throws StoreException  {
        int num = node.getNumShapeIds();
        Integer count = stats.get(num);
        if(count == null) {
            stats.put(num, 1);
        } else {
            stats.put(num, count + 1);
        }
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            gatherStats(node.getSubNode(i), stats);
        }
    }

    /**
     * For quad tree this is the max depth. I don't know what it is for RTree
     * 
     * @param i
     */
    public void setMax(int i) {
        max = i;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param shpFiles
     */
    public void setShapeFileName(ShpFiles shpFiles) {
        this.shpFiles = shpFiles;
    }


    /**
     * DOCUMENT ME!
     * 
     * @param byteOrder
     *                The byteOrder to set.
     */
    public void setByteOrder(String byteOrder) {
        this.byteOrder = byteOrder;
    }

    public String id() {
        return getClass().getName();
    }
    
    public int getLeafSize() {
		return leafSize;
	}

	public void setLeafSize(int leafSize) {
		this.leafSize = leafSize;
	}
}
