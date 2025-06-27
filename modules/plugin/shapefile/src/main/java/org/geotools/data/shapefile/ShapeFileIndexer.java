/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import static org.geotools.data.shapefile.ShapefileIndexerBoundsHelper.createBoundsReader;

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
import org.geotools.api.util.ProgressListener;
import org.geotools.data.shapefile.ShapefileIndexerBoundsHelper.BoundsReader;
import org.geotools.data.shapefile.files.FileWriter;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.files.StorageFile;
import org.geotools.data.shapefile.index.LockTimeoutException;
import org.geotools.data.shapefile.index.TreeException;
import org.geotools.data.shapefile.index.quadtree.Node;
import org.geotools.data.shapefile.index.quadtree.QuadTree;
import org.geotools.data.shapefile.index.quadtree.StoreException;
import org.geotools.data.shapefile.index.quadtree.fs.FileSystemIndexStore;
import org.geotools.data.shapefile.index.quadtree.fs.IndexHeader;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.data.util.NullProgressListener;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Utility class for Shapefile spatial indexing
 *
 * @author Tommaso Nolli
 */
@SuppressWarnings("PMD.SystemPrintln")
class ShapeFileIndexer implements FileWriter {
    private static final Logger LOGGER = Logging.getLogger(ShapeFileIndexer.class);

    private int maxDepth = -1;
    private int leafSize = 16;

    private String byteOrder;
    private ShpFiles shpFiles;

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || (args.length - 1) % 2 != 0) {
            usage();
        }

        long start = System.currentTimeMillis();

        ShapeFileIndexer idx = new ShapeFileIndexer();

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
            System.out.println("in " + (System.currentTimeMillis() - start) + "ms.");
            System.out.println();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            usage();
            System.exit(1);
        }
    }

    private static void usage() {
        System.out.println("Usage: ShapeFileIndexer "
                + "-t <QIX> "
                + "[-M <max tree depth>] "
                + "[-b <byte order NL | NM>] "
                + "<shape file>"
                + "[-s <max number of items in a leaf>]");

        System.out.println();

        System.out.println("Options:");
        System.out.println("\t-t Index type: RTREE or QUADTREE");
        System.out.println();
        System.out.println("Following options apllies only to QUADTREE:");
        System.out.println("\t-b byte order to use: NL = LSB; " + "NM = MSB (default)");

        System.exit(1);
    }

    /**
     * Indexes the shapefile denoted by setShapeFileName(String fileName).
     *
     * @param verbose enable/disable printing of dots every 500 indexed records
     * @return The number of indexed records
     */
    public int index(boolean verbose, /*unused*/ ProgressListener listener)
            throws MalformedURLException, IOException, TreeException, StoreException, LockTimeoutException {

        if (this.shpFiles == null) {
            throw new IOException("You have to set a shape file name!");
        }

        int cnt = 0;

        // Temporary file for building...
        StorageFile storage = shpFiles.getStorageFile(ShpFileType.QIX);
        File treeFile = storage.getFile();
        if (maxDepth == -1) {
            maxDepth = computeMaxDepth();
        }

        try (ShapefileReader reader = new ShapefileReader(shpFiles, true, false, new GeometryFactory())) {

            cnt = this.buildQuadTree(reader, treeFile, verbose);
        }

        // Final index file
        storage.replaceOriginal();

        return cnt;
    }

    /**
     * Compute a reasonable index max depth, considering a fully developed 10 levels one already contains 200k index
     * nodes, good for indexing up to 3M features without consuming too much memory
     */
    private int computeMaxDepth() throws IOException {
        int maxDepth;
        try (ShapefileReader reader = new ShapefileReader(shpFiles, true, false, new GeometryFactory())) {
            int features = reader.getCount(0);
            maxDepth = 1;
            int nodes = 1;
            while (nodes * leafSize < features) {
                maxDepth++;
                nodes *= 4;
            }
            if (maxDepth < 10) {
                maxDepth = 10;
            }
            return maxDepth;
        }
    }

    private int buildQuadTree(ShapefileReader reader, File file, boolean verbose) throws IOException, StoreException {
        LOGGER.fine("Building quadtree spatial index with depth " + maxDepth + " for file " + file.getAbsolutePath());

        final byte fileByteOrder = resolveStorageByteOrder();

        int cnt = 0;

        try (IndexFile shpIndex = new IndexFile(shpFiles, false);
                // strategy to speed up optimizeTree()
                BoundsReader boundsHelper = createBoundsReader(reader, shpIndex);
                QuadTree tree = new QuadTree(shpIndex.getRecordCount(), maxDepth, getBounds(reader), shpIndex)) {
            Envelope env = new Envelope();
            while (reader.hasNext()) {
                Record rec = reader.nextRecord();
                env.init(rec.minX, rec.maxX, rec.minY, rec.maxY);
                int recno = cnt++;
                tree.insert(recno, env);
                boundsHelper.insert(recno, env);

                if (verbose && cnt % 1_000 == 0) {
                    System.out.print('.');
                }
                if (cnt % 100_000 == 0) System.out.print('\n');
            }
            if (verbose) System.out.println("done building quadtree");

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Optimizing the tree (this might take some time)");
            }
            if (verbose) System.out.println("Optimizing the tree (this might take some time)");
            optimizeTree(tree, tree.getRoot(), 0, boundsHelper);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Tree optimized");
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                printStats(tree);
            }
            if (verbose) System.out.println("Storing the tree...");
            FileSystemIndexStore store = new FileSystemIndexStore(file, fileByteOrder);
            store.store(tree);
            if (verbose) System.out.println("done");
        }
        return cnt;
    }

    private Envelope getBounds(ShapefileReader reader) {
        ShapefileHeader header = reader.getHeader();
        Envelope bounds = new Envelope(header.minX(), header.maxX(), header.minY(), header.maxY());
        return bounds;
    }

    private byte resolveStorageByteOrder() throws StoreException {
        if (this.byteOrder == null || this.byteOrder.equalsIgnoreCase("NM")) {
            return IndexHeader.NEW_MSB_ORDER;
        }
        if (this.byteOrder.equalsIgnoreCase("NL")) {
            return IndexHeader.NEW_LSB_ORDER;
        }
        throw new StoreException("Asked byte order '" + this.byteOrder + "' must be 'NL' or 'NM'!");
    }

    private Node optimizeTree(QuadTree tree, Node node, int level, BoundsReader reader)
            throws StoreException, IOException {
        // recurse, with a check to avoid too deep recursion due to odd data that has a
        final boolean isLeafNode = node.getNumSubNodes() == 0;
        final boolean isOverFlown = node.getNumShapeIds() > leafSize;
        final int hardMaxDepth = maxDepth * 2;
        final boolean canBeSplit = level < hardMaxDepth;
        if (isLeafNode && isOverFlown && canBeSplit) {
            // ok, we need to split this baby further
            int[] shapeIds = node.getShapesId();
            int numShapesId = node.getNumShapeIds();
            node.clean();

            // get an estimate on how many more levels we need
            int extraLevels = 2;
            int nodes = 4;
            while (nodes * leafSize < numShapesId) {
                extraLevels++;
                nodes *= 4;
            }

            Envelope env = new Envelope();
            for (int i = 0; i < numShapesId; i++) {
                final int recNumber = shapeIds[i];
                reader.read(recNumber, env);
                tree.insert(node, recNumber, env, extraLevels);
            }
        }

        // pack the arrays to use less memory (the optimization often makes the tree grow)
        node.pack();

        // recurse
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            optimizeTree(tree, node.getSubNode(i), level + 1, reader);
        }

        // prune empty subnodes
        for (int i = 0; i < node.getNumSubNodes(); ) {
            Node child = node.getSubNode(i);
            if (child != null && child.getNumShapeIds() == 0 && child.getNumSubNodes() == 0) {
                // empty child, we don't need it, clean it up
                node.removeSubNode(child);
            } else {
                i++;
            }
        }

        // handle degenerate chains, we pop up the nodes to the top by keeping
        // their shape ids _and_ their bounds (as it's the only area that has something)
        if (node.getNumSubNodes() == 1 && node.getNumShapeIds() == 0) {
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
            if (node.getNumShapeIds() > 0) {
                int[] shapeIds = node.getShapesId();
                for (final int recNumber : shapeIds) {
                    reader.expandEnvelope(recNumber, bounds);
                }
            }
            if (node.getNumSubNodes() > 0) {
                for (int i = 0; i < node.getNumSubNodes(); i++) {
                    bounds.expandToInclude(node.getSubNode(i).getBounds());
                }
            }
            node.setBounds(bounds);

            // can we shrink?
            int count = node.getNumShapeIds();
            for (int i = 0; i < node.getNumSubNodes(); i++) {
                Node child = node.getSubNode(i);
                if (child.getNumSubNodes() > 0) {
                    count = Integer.MAX_VALUE;
                    break;
                } else {
                    count += child.getNumShapeIds();
                }
            }
            if (count < leafSize) {
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
        Map<Integer, Integer> stats = new HashMap<>();
        gatherStats(tree.getRoot(), stats);

        List<Integer> nums = new ArrayList<>(stats.keySet());
        Collections.sort(nums);
        LOGGER.log(Level.FINE, "Index statistics");
        for (Integer num : nums) {
            LOGGER.log(Level.FINE, num + " -> " + stats.get(num));
        }
    }

    void gatherStats(Node node, Map<Integer, Integer> stats) throws StoreException {
        int num = node.getNumShapeIds();
        Integer count = stats.get(num);
        if (count == null) {
            stats.put(num, 1);
        } else {
            stats.put(num, count + 1);
        }
        for (int i = 0; i < node.getNumSubNodes(); i++) {
            gatherStats(node.getSubNode(i), stats);
        }
    }

    /** For quad tree this is the max depth. I don't know what it is for RTree */
    public void setMax(int i) {
        maxDepth = i;
    }

    /** @param shpFiles */
    public void setShapeFileName(ShpFiles shpFiles) {
        this.shpFiles = shpFiles;
    }

    /** @param byteOrder The byteOrder to set. */
    public void setByteOrder(String byteOrder) {
        this.byteOrder = byteOrder;
    }

    @Override
    public String id() {
        return getClass().getName();
    }

    public int getLeafSize() {
        return leafSize;
    }

    public void setLeafSize(int leafSize) {
        if (leafSize < 1) {
            throw new IllegalArgumentException("Maximum node leaf size must be a positive integer");
        }
        this.leafSize = leafSize;
    }
}
