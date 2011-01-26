/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */

package org.geotools.demo.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.geotools.swing.styling.JSimpleStyleDialog;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * This class demonstrates extending JMapFrame to add a menu bar and shapefile handling
 * methods. It also shows the use of {@code DefaultRepository} to manage data stores, and
 * the {@code JFileDataStoreChooser} and {@code JSimpleStyleDialog} classes.
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class ShapefileViewer extends JMapFrame {

    private DefaultRepository repository = new DefaultRepository();
    private MapContext context;
    private String title;
    private File cwd;

    /**
     * Main function. Creates and displays a ShapefileViewer object.
     *
     * @param args ignored presently
     */
    public static void main(String[] args) {
        final ShapefileViewer viewer = new ShapefileViewer("Shapefile viewer");

        File dataDir = new File(ShapefileViewer.class.getResource("/data").getPath());
        viewer.setWorkingDir(dataDir);

        viewer.setSize(800, 600);
        viewer.setVisible(true);
    }

    /**
     * Constructor
     * @param title text to be displayed in the frame's title bar
     */
    public ShapefileViewer(String title) {
        this.title = title;

        enableLayerTable(true);
        enableStatusBar(true);
        enableToolBar(true);
        initComponents();

        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("Open...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    loadShapefile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("Get scale");
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JMapPane mapPane = getMapPane();
                if (mapPane != null) {
                    AffineTransform tr = mapPane.getScreenToWorldTransform();
                    if (tr != null) {
                        System.out.println("x scale: " + tr.getScaleX());
                        System.out.println("Y scale: " + tr.getScaleY());
                        System.out.println("pane area: " + mapPane.getVisibleRect());
                    }
                }
            }
        });
        menu.add(item);


        menuBar.add(menu);
    }

    /**
     * Set the current working directory
     *
     * @param cwd a File object representing the directory; if NULL the user's default
     * directory will be set
     */
    public void setWorkingDir(File cwd) {
        if (!cwd.isDirectory()) {
            throw new IllegalArgumentException("The argument is not a directory: " + cwd.getAbsolutePath());
        }

        this.cwd = cwd;
    }

    /**
     * Add the contents of a shapefile as a new layer. This method
     * simply calls {@linkplain #showOpenShapefileDialog} followed by
     * {@linkplain #addShapefile} with the {@code defaultStyle}
     * argument of the latter method set to {@code true}.
     */
    public void loadShapefile() throws IOException {
        File file = JFileDataStoreChooser.showOpenFile("shp", cwd, null);
        if (file != null) {
            addShapefile(file.toURL(), true);
            setWorkingDir(file.getParentFile());
        }
    }

    /**
     * Open a shapefile and add it to the map layers displayed
     * by the widget, styling the layer as specified by the
     * associated SLD file (same root name as shapefile with .sld
     * extension) if it exists.
     * <p>
     * To add a shapefile with a Style from elsewhere, e.g. one
     * constructed programmitcally, use the
     * {@linkplain #addShapefile(java.net.URL, org.geotools.styling.Style)} method.
     * Alternatively, create a MapLayer object and use the
     * {@linkplain #addLayer} method.
     *
     * @param shapefile URL for the shapefile to add
     * @param defaultStyle specifies what action to take if there is no
     * associated SLD file: fallback to a minimal style (if true) or
     * abort adding the layer (if false)
     *
     * @return true if the layer was added successfully; false otherwise
     *
     * @throws IllegalArgumentException if shapefileURL is null
     */
    public boolean addShapefile(URL shapefileURL, boolean defaultStyle) throws IOException {
        if (shapefileURL == null) {
            throw new IllegalArgumentException("shapefileURL must not me null");
        }
        ShapefileDataStore dstore = null;
        
        DataStore found = repository.dataStore( shapefileURL.toString());
        if( found != null && found instanceof ShapefileDataStore){
        	dstore = (ShapefileDataStore) found;
        }
        else {
	        try {
	            dstore = new ShapefileDataStore(shapefileURL);
	        } catch (MalformedURLException urlEx) {
	            throw new RuntimeException(urlEx);
	        }
	        repository.register( shapefileURL.toString(), dstore );
        }
        /*
         * Before doing anything else we attempt to connect to the 
         * shapefile to check that it exists and is reachable. An
         * IOException will be thrown if this fails.
         */
        dstore.getSchema();

        /*
         * We assume from this point that the shapefile exists and
         * is accessible
         */
        String typeName = dstore.getTypeNames()[0];
        Style style = null;
        URL sldURL = getShapefileSLD(shapefileURL);

        if (sldURL != null) {
            /*
             * The shapefile has an associated SLD file. We read this and
             * use the (first) style for the new layer
             */
            StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
            SLDParser stylereader = new SLDParser(factory, sldURL);
            style = stylereader.readXML()[0];

        } else if (defaultStyle) {
            /*
             * There was no associated SLD file so we attempt to create
             * a minimal style to display the layer
             */
            style = JSimpleStyleDialog.showDialog(this, dstore);
            if (style == null) {
                return false;
            }

        } else {
            /*
             * We are not having a good day...
             */
            return false;
        }

        MapLayer layer = new DefaultMapLayer(dstore.getFeatureSource(typeName), style);
        addLayer(layer);
        return true;
    }

    /**
     * Open a shapefile and add it to the map layers displayed
     * by the widget, rendering the layer with the provided style.
     * <p>
     *
     * @param shapefile URL for the shapefile to add
     * @param style the Syle object to use in rendering this layer
     *
     * @return true if the layer was added successfully; false otherwise
     *
     * @throws IOException if the shapefile could not be accessed
     * @throws IllegalArgumentException if either of the arguments is null
     */
    public boolean addShapefile(URL shapefileURL, Style style) throws IOException {
        if (shapefileURL == null || style == null) {
            throw new IllegalArgumentException("shapefileURL must not be null");
        }
        ShapefileDataStore dstore = null;
        
        DataStore found = repository.dataStore( shapefileURL.toString());
        if( found != null && found instanceof ShapefileDataStore){
        	dstore = (ShapefileDataStore) found;
        }
        else {
	        try {
	            dstore = new ShapefileDataStore(shapefileURL);
	        } catch (MalformedURLException urlEx) {
	            throw new RuntimeException(urlEx);
	        }
	        repository.register( shapefileURL.toString(), dstore );
        }        
        /*
         * Before doing anything else we attempt to connect to the
         * shapefile to check that it exists and is reachable. An
         * IOException will be thrown if this fails.
         */
        dstore.getSchema();

        /*
         * We assume from this point that the shapefile exists and
         * is accessible
         */
        String typeName = dstore.getTypeNames()[0];
        MapLayer layer = new DefaultMapLayer(dstore.getFeatureSource(typeName), style);
        addLayer(layer);
        return true;
    }

    /**
     * Search for a Styled Layer Descriptor (SLD) file associated with the
     * specified shapefile. If the SLD file exists it will be in the same
     * directory as the shapefile, have the same root name, and have
     * .sld as its extension.
     *
     * @param shapefileURL the shapefile for which an SLD file is being sought
     *
     * @return the URL of the SLD file; or null if not found or not accessible
     */
    public URL getShapefileSLD(URL shapefileURL) {
        URL sldURL = null;
        
        File shapefile;
		try {			
			shapefile = new File( shapefileURL.toURI() );
		} catch (URISyntaxException e) {
			shapefile = new File( shapefileURL.getPath() );
		}
        String fileName = shapefile.getName();

        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            File directory = shapefile.getParentFile();
            String sldname1 = fileName.substring(0, lastDot) + ".sld";
            String sldname2 = fileName.substring(0, lastDot) + ".SLD";
            InputStream input=null;
            try {
            	File sldFile1 = new File( directory, sldname1 );
            	File sldFile2 = new File( directory, sldname2 );
            	if( sldFile1.exists() && sldFile1.canRead() ){
            		sldURL = sldFile1.toURL();	
            	}
            	else if( sldFile1.exists() && sldFile1.canRead() ){
            		sldURL = sldFile2.toURL();	
            	}
            	else {
            		/*
                     * The SLD file can't be opened so we return null
                     */
                    return null;
            	}
                /*
                 * Now we check to see if the url that we have created
                 * corresponds to an existing and accessible SLD file.
                 * If it doesn't, this call to openStream() will provoke
                 * an IOException
                 */
                input = sldURL.openStream();
            } catch (MalformedURLException urlEx) {
                throw new RuntimeException(urlEx);
            } catch (IOException ioEx) {
                /*
                 * The SLD file can't be opened so we return null
                 */
                return null;
            }
            finally {
            	if( input != null) {
            		try {
						input.close();
					} catch (IOException e) {
					}
            	}
            }
        }

        return sldURL;
    }

    /**
     * Add a map layer to those displayed. If no {@linkplain org.geotools.map.MapContext}
     * has been set explicitly, a new instance of {@linkplain org.geotools.map.DefaultMapContext}
     * will be created.
     */
    public void addLayer(MapLayer layer) {
        if (context == null) {
            CoordinateReferenceSystem crs = layer.getBounds().getCoordinateReferenceSystem();
            if (crs == null) {
                crs = DefaultGeographicCRS.WGS84;
            }
            context = new DefaultMapContext(crs);
            context.setTitle(title);
            setMapContext(context);
            setRenderer(new StreamingRenderer());
        }

        context.addLayer(layer);
    }

}
