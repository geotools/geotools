/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.grid.spatialindex.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.caching.grid.spatialindex.GridNode;
import org.geotools.caching.spatialindex.Node;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Storage;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;


/** A storage that stores data in a file on disk.
 * 
 * Create new instances with static factory method <code>DiskStorage.createInstance()</code>
 * or <code>DiskStorage.createInstance(PropertySet)</code>
 * 
 * @author Christophe Rousson <christophe.rousson@gmail.com>, Google SoC 2007 
 *
 *
 * @source $URL$
 */
public class DiskStorage implements Storage {
    public final static String DATA_FILE_PROPERTY = "DiskStorage.DataFile";
    public final static String INDEX_FILE_PROPERTY = "DiskStorage.IndexFile";
    public final static String PAGE_SIZE_PROPERTY = "DiskStorage.PageSize";
    
    protected static Logger logger = org.geotools.util.logging.Logging.getLogger("org.geotools.caching.spatialindex.store");
    
    private int stats_bytes = 0;
    private int stats_n = 0;
    private int page_size;
    private int nextPage = 0;
    
    private File dataFile;		//this is the file that stores the data
    private RandomAccessFile data_file;
    private FileChannel data_channel;
    
    private File indexFile;		//this is the index file that tracks nodes & pages
    
    
    private TreeSet<Integer> emptyPages;				//list of empty pages for reuse
    private HashMap<NodeIdentifier, Entry> pageIndex;	//page index list
   
    private Collection<FeatureType> featureTypes;		//feature types in store
    private ReferencedEnvelope bounds;					//bounds of store
        
    private DiskStorage(File f, int page_size) throws IOException {
        this(f, page_size, new File(f.getCanonicalPath() + ".idx"));
    }

    private DiskStorage(File f, File index_file) throws IOException {
    	this(f, 1000, index_file);
    }
    
    private DiskStorage(File f, int page_size, File index_file) throws IOException {
        this.indexFile = index_file;
        this.page_size = page_size;
        this.dataFile = f;
        this.emptyPages = new TreeSet<Integer>();
        this.pageIndex = new HashMap<NodeIdentifier, Entry>();
        this.featureTypes = new HashSet<FeatureType>();
        
        if (index_file.exists()) {
            try{
                initializeFromIndex();
            }catch (Exception ex){
                //lets clear out any existing info
                this.indexFile.createNewFile();
                this.dataFile.createNewFile();
                
                this.emptyPages = new TreeSet<Integer>();
                this.pageIndex = new HashMap<NodeIdentifier, Entry>();
                this.featureTypes = new HashSet<FeatureType>();
            }
        }   
        
        data_file = new RandomAccessFile(f, "rw");
        data_channel = data_file.getChannel();      
    }

    /** Factory method : create a new Storage of type DiskStorage.
     * 
     * Valid properties are :
     * <ul>
     *   <li>DiskStorage.DATA_FILE_PROPERTY : filename (mandatory) ; overrides given file if index is not provided.
     *   <li>DiskStorage.INDEX_FILE_PROPERTY : filename ;
     *                                         if exists, must be a valid index file
     *                                         and data file must be the valid data file associated with this index.
     *   <li>DiskStorage.PAGE_SIZE_PROPERTY : int, required if INDEX_FILE does not exist, or is not provided.
     * </ul>
     * @param property set
     * @return new instance of DiskStorage
     */
    public static Storage createInstance(Properties pset) {
        try {
            File f = new File(pset.getProperty(DATA_FILE_PROPERTY));

            if (pset.containsKey(INDEX_FILE_PROPERTY)) {
                File index = new File(pset.getProperty(INDEX_FILE_PROPERTY));

                if (index.exists()) {
                    return new DiskStorage(f, index);
                } else {
                    int page_size = Integer.parseInt(pset.getProperty(PAGE_SIZE_PROPERTY));

                    return new DiskStorage(f, page_size, index);
                }
            } else {
                int page_size = Integer.parseInt(pset.getProperty(PAGE_SIZE_PROPERTY));

                return new DiskStorage(f, page_size);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "DiskStorage : error occured when creating new instance : "+e.getMessage(),e);
            return null;
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("DiskStorage : invalid property set.",e);
        }
    }

    /** Default factory method : create a new Storage of type DiskStorage,
     * with page size set to default 1000 bytes, and data file is a new temporary file.
     *
     * @return new instance of DiskStorage with default parameters.
     */
    public static Storage createInstance() {
        try {
            return new DiskStorage(File.createTempFile("storage", ".tmp"), 1000);
        } catch (IOException e) {
            logger.log(Level.WARNING,
                "DiskStorage : error occured when creating new instance : " + e);

            return null;
        }
    }

    /**
     * Removes all entries from the disk store and clears the
     * associated feature types.
     */
    public synchronized void clear() {
        for (Iterator<java.util.Map.Entry<NodeIdentifier, Entry>> it = pageIndex.entrySet().iterator();it.hasNext();) {
            java.util.Map.Entry<NodeIdentifier, Entry> next = it.next();
            Entry e = next.getValue();
            int n = 0;
            while (n < e.pages.size()) {
            	emptyPages.add(e.pages.get(n));
            	n++;
            }
            it.remove();
        }
    }

    /**
     * Gets a particular node
     */
    public synchronized Node get(NodeIdentifier id) {
        Node node = null;
                
        Entry e = pageIndex.get(id);
        if (e == null) {
        	return null;
        }
        byte[] data = new byte[e.length];
        readData(data, e);
        try {
            node = readNode(data, id);
        } catch (IOException e1) {
        	throw new IllegalStateException(e1);
        } catch (ClassNotFoundException e1) {
        	throw new IllegalStateException(e1);
        } catch (Exception ex){
            logger.log(Level.WARNING, "Error reading node.", ex);
        }
        return node;
    }
    
    /*
     * Reads data from the file into data array
     */
    private void readData(byte[] data, Entry e) {
    	ByteBuffer buffer = ByteBuffer.allocate(page_size);
    	int page = 0;
        int rem = data.length;
        int len = 0;
        int next = 0;
        int index = 0;
        while (next < e.pages.size()) {	//for each page
        	page = e.pages.get(next);
        	len = (rem > page_size) ? page_size : rem;

        	try {
        		buffer.clear();
        		data_channel.position(page * page_size);
        		int bytes_read = data_channel.read(buffer);
        		if (bytes_read != page_size) {
        			throw new IllegalStateException("Data file might be corrupted.");
        		}

        		buffer.rewind();
        		buffer.get(data, index, len);
        		rem -= bytes_read;
        		index += bytes_read;
        		next++;
        	} catch (IOException io) {
        		throw new IllegalStateException(io);
        	}
        }
    }
    
    /* 
     * Converts an array of bytes into a node 
     */
    private Node readNode(byte[] data, NodeIdentifier id) throws IOException, ClassNotFoundException {
    	ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Node node = null;
        try{
            node = (Node) ois.readObject();
        }finally{
            ois.close();
            bais.close();            
        }
        id = findUniqueInstance(id);
        node.setIdentifier(id);
        return node;
    }

    /**
     * Adds a node to the store.
     */
    public synchronized void put(Node n) {
        byte[] data = null;
        try {
            data = writeNode(n);
        } catch (IOException e1) {
            logger.log(Level.SEVERE, "Cannot put data in DiskStorage : " + e1);
            return;
        }

        Entry e = new Entry(n.getIdentifier());
        Entry old = null;

        if (pageIndex.containsKey(e.id)) {
            old = pageIndex.get(e.id);
            if (old == null) {
                // problem
                throw new IllegalStateException("old entry null");
            }
        } else {
        	if (!pageIndex.containsKey(e.id)) {
        		pageIndex.put(e.id, null); // advertise we created a new entry        	} else {
        		old = pageIndex.get(e.id);
        	}
        }
        writeData(data, e, old);
        pageIndex.put(e.id, e);
    }
    
    /*
     * converts a node to a byte array
     */
    private byte[] writeNode(Node n) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        try{
            oos.writeObject(n);
            byte[] data = baos.toByteArray();
            stats_bytes += data.length;
            stats_n++;
            return data;
        }finally{
            oos.close();
            baos.close();
        }
    }

    /*
     * writes a btye array to particular pages
     */
    private void writeData(byte[] data, Entry e, Entry old) {
        ByteBuffer buffer = ByteBuffer.allocate(page_size);
        e.length = data.length;

        int rem = data.length;
        int page;
        int len;
        int index = 0;
        int next = 0;

        while (rem > 0) {
        	if ((old != null) && (next < old.pages.size())) {
        		page = old.pages.get(next);
        		next++;
        	} else if (!emptyPages.isEmpty()) {
        		synchronized (emptyPages) {
        			Integer i = emptyPages.first();
        			page = i.intValue();

        			if (!emptyPages.remove(i)) {
        				throw new RuntimeException("buggy here !!!!");
        			}
        		}
        	} else {
        		page = nextPage++;
        	}

        	len = (rem > page_size) ? page_size : rem;
        	buffer.clear();
        	buffer.put(data, index, len);

        	try {
        		buffer.rewind();
        		data_channel.position(page * page_size);
        		data_channel.write(buffer);
        	} catch (IOException io) {
        		throw new IllegalStateException(io);
        	}

        	rem -= len;
        	index += len;
        	e.pages.add(new Integer(page));
        }

        if (old != null) { // don't forget to recycle pages
        	while (next < old.pages.size()) {
        		emptyPages.add(new Integer(old.pages.get(next)));
        		next++;
        	}
        }
    }

    /**
     * Removes a node from the store.
     */
    public synchronized void remove(NodeIdentifier id) {
        Entry e = pageIndex.get(id);
        if (e == null) {
            // problem
            throw new IllegalArgumentException("Invalid identifier " + id.toString());
        }

        int next = 0;
        while (next < e.pages.size()) {
        	emptyPages.add(new Integer(e.pages.get(next)));
        	next++;
        }
        pageIndex.remove(id);
    }

    /**
     * Disposes of the store. 
     * <p>This flushes all data and closes file handles</p>
     */
    public synchronized void dispose(){
        flush();
        try{
            this.data_channel.close();
            this.data_file.close();
        }catch (Exception ex){
            logger.log(Level.WARNING, "Error disposing of disk storage", ex);
        }
    }
    
    /**
     * Writes the index file.
     * <p>This does not close the data files.</p>
     */
    public void flush() {
        try {
            FileOutputStream os = new FileOutputStream(indexFile);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            try {
                oos.writeInt(this.page_size);
                oos.writeInt(this.nextPage);
                oos.writeObject(this.emptyPages);
                oos.writeObject(this.pageIndex);
                oos.writeObject(this.bounds);

                oos.writeInt(this.featureTypes.size());
                for( Iterator<FeatureType> iterator = featureTypes.iterator(); iterator.hasNext(); ) {
                    FeatureType type = (FeatureType) iterator.next();
                    String rep = DataUtilities.spec((SimpleFeatureType) type);
                    oos.writeObject(type.getName().getNamespaceURI() + "." + type.getName().getLocalPart());
                    oos.writeObject(rep);
                }
            } finally {
                oos.close();
                os.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Cannot close DiskStorage normally : " + e, e);
        }
    }
    
    /**
     * Initializes the store from the index file.
     * 
     * @throws IOException
     */
    protected void initializeFromIndex() throws IOException {
        FileInputStream is = new FileInputStream(indexFile);
        ObjectInputStream ois = new ObjectInputStream(is);
        try {
            this.page_size = ois.readInt();
            this.nextPage = ois.readInt();
            
            this.emptyPages = (TreeSet<Integer>) ois.readObject();
            this.pageIndex = (HashMap<NodeIdentifier, Entry>) ois.readObject();
            this.bounds = (ReferencedEnvelope)ois.readObject();
            
            int featuretypesize = ois.readInt();
            this.featureTypes = new HashSet<FeatureType>();
            for(int i = 0; i < featuretypesize; i++){
                String name = (String)ois.readObject();
                String rep = (String)ois.readObject();
                try {
                    FeatureType ft = DataUtilities.createType(name, rep);
                    featureTypes.add(ft);
                } catch (SchemaException e) {
                    logger.log(Level.WARNING, "Error initializing feature types from store.", e);
                }
            }
        } catch (ClassNotFoundException e) {
            throw (IOException) new IOException().initCause(e);
        } finally {
            ois.close();
            is.close();
        }
    }

    public Properties getPropertySet() {
        Properties pset = new Properties();
        try {
            pset.setProperty(STORAGE_TYPE_PROPERTY, DiskStorage.class.getCanonicalName());
            pset.setProperty(DATA_FILE_PROPERTY, dataFile.getCanonicalPath());
            pset.setProperty(INDEX_FILE_PROPERTY, indexFile.getCanonicalPath());
            pset.setProperty(PAGE_SIZE_PROPERTY, new Integer(page_size).toString());
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error while creating DiskStorage property set : " + e);
        }

        return pset;
    }


    public NodeIdentifier findUniqueInstance(NodeIdentifier id) {
        if (pageIndex.containsKey(id)) {
            return pageIndex.get(id).id;
        } else {
            return id;
        }
    }

    void logPageAccess(int page, int length) throws IOException {
        File log = new File("log/" + page + ".log");
        FileWriter fw = new FileWriter(log, true);
        try{
            fw.write(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " writing " + length + " bytes.\n");
        }finally{
            fw.close();
        }
    }

    void logGet() throws IOException {
        FileWriter getlog = new FileWriter("log/get.log", true);
        try{
            getlog.write(Thread.currentThread().getName() + " : " + System.currentTimeMillis() + "\n");
        }finally{
            getlog.close();
        }
    }

    void writeReadable(Node n, int page) {
        try {
            FileWriter fw = new FileWriter("log/" + page + ".node");
            try{
                fw.write(((GridNode) n).toReadableText());
            }finally{
                fw.close();
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error writing node.", e);
        }
    }

    /**
     * Adds a feature type to the store.
     */
    public void addFeatureType( FeatureType ft ) {
        featureTypes.add(ft);
    }

    /**
     * Gets the feature types supported by the store.
     */
    public Collection<FeatureType> getFeatureTypes() {
        return Collections.unmodifiableCollection(this.featureTypes);
    }
    
    /**
     * Clears all feature types associated with store
     */
    public void clearFeatureTypes(){
        this.featureTypes.clear();
    }
    
    /**
     * Sets the bounds of the store
     */
    public void setBounds(ReferencedEnvelope bounds){
        this.bounds = bounds;
    }
    
    /**
     * Get the bounds of data in the store.
     */
    public ReferencedEnvelope getBounds(){
        return this.bounds;
    }
}

/**
 * This is a class to track the pages a particular
 * node is written to. 
 * 
 */
class Entry implements Serializable {
    private static final long serialVersionUID = -9013786524696213884L;
    protected int length = 0;
    protected NodeIdentifier id;
    protected ArrayList<Integer> pages = new ArrayList<Integer>();

    Entry(NodeIdentifier id) {
        this.id = id;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Id : " + id);
        sb.append(", Length : " + length);

        for (Iterator<Integer> it = pages.iterator(); it.hasNext();) {
            sb.append("\n    page = " + it.next());
        }

        return sb.toString();
    }
}

