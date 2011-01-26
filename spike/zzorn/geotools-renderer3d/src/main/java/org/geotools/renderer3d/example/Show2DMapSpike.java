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
package org.geotools.renderer3d.example;

import com.vividsolutions.jts.geom.Envelope;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gui.swing.*;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


/**
 * Copied and edited from geotools examples.
 */
public class Show2DMapSpike
        implements ActionListener
{

    //======================================================================
    // Non-Private Fields

    JFrame frame;
    JMapPane myMapPane;
    JToolBar jtb;
    JLabel text;
    final JFileChooser jfc = new JFileChooser();

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Main Method

    /**
     * @param args
     */
    public static void main( String[] args ) throws Exception
    {
        final Show2DMapSpike mapViewer = new Show2DMapSpike();

        //MapContext context = loadMapContextFromCommandLine( args, mapViewer );

        final ExampleDataGenerator exampleDataGenerator = new ExampleDataGenerator();
        final MapContext context = exampleDataGenerator.createExampleMap();

        mapViewer.setMapContext( context );
    }

    private static MapContext loadMapContextFromCommandLine( final String[] args, final Show2DMapSpike mapViewer )
            throws Exception
    {
        MapContext context = null;
        if ( args.length == 0 || !args[ 0 ].toLowerCase().endsWith( ".shp" ) )
        {
            System.out.println( "java org.geotools.gui.swing.MapViewer shapefile.shp" );
            System.out.println( "Notes:" );
            System.out.println( " Any provided shapefile.prj file or shapefile.sld will be used" );
            System.exit( 0 );
        }
        else
        {
            String pathname = args[ 0 ];
            URL shape = aquireURL( pathname );
            if ( shape == null )
            {
                System.err.println( "Could not find shapefile: " + pathname );
                System.exit( 1 );
            }
            String filepart = pathname.substring( 0, pathname.lastIndexOf( "." ) );
            URL sldFile = aquireURL( filepart + ".sld" );
            if ( sldFile == null )
            {
                System.err.println( "Could not find sld file: " + filepart + ".sld" );
                System.exit( 1 );
            }

            context = mapViewer.load( shape, sldFile );


        }
        return context;
    }

    //----------------------------------------------------------------------
    // Constructors

    public Show2DMapSpike()
    {
        frame = new JFrame( "My Map Viewer" );
        frame.setBounds( 20, 20, 450, 200 );
        frame.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        Container content = frame.getContentPane();
        myMapPane = new JMapPane();
        //myMapPane.addZoomChangeListener(this);
        content.setLayout( new BorderLayout() );
        jtb = new JToolBar();

        JButton load = new JButton( "Load file" );
        load.addActionListener( this );
        jtb.add( load );
        Action zoomIn = new ZoomInAction( myMapPane );
        Action zoomOut = new ZoomOutAction( myMapPane );
        Action pan = new PanAction( myMapPane );
        Action select = new SelectAction( myMapPane );
        Action reset = new ResetAction( myMapPane );
        jtb.add( zoomIn );
        jtb.add( zoomOut );
        jtb.add( pan );
        jtb.addSeparator();
        jtb.add( reset );
        jtb.addSeparator();
        jtb.add( select );
        final JButton button = new JButton();
        button.setText( "CRS" );
        button.setToolTipText( "Change map prjection" );
        button.addActionListener( new ActionListener()
        {

            public void actionPerformed( ActionEvent e )
            {
                String code = JOptionPane.showInputDialog( button, "Coordinate Reference System:", "EPSG:4326" );
                if ( code == null )
                {
                    return;
                }
                try
                {
                    CoordinateReferenceSystem crs = CRS.decode( code );
                    setCRS( crs );
                }
                catch ( Exception fe )
                {
                    fe.printStackTrace();
                    JOptionPane.showMessageDialog( button,
                                                   fe.getMessage(),
                                                   fe.getClass().toString(),
                                                   JOptionPane.ERROR_MESSAGE );
                    return;
                }
            }

        } );
        jtb.add( button );

        content.add( jtb, BorderLayout.NORTH );

        //JComponent sp = myMapPane.createScrollPane();
        myMapPane.setSize( 400, 200 );
        content.add( myMapPane, BorderLayout.CENTER );

        content.doLayout();
        frame.setVisible( true );
    }

    //----------------------------------------------------------------------
    // Static Methods

    public static URL aquireURL( String target )
    {
        if ( new File( target ).exists() )
        {
            try
            {
                return new File( target ).toURI().toURL();
            }
            catch ( MalformedURLException e )
            {
            }
        }
        try
        {
            return new URL( target );
        }
        catch ( MalformedURLException e )
        {
            return null;
        }
    }

    //----------------------------------------------------------------------
    // ActionListener Implementation

    public void actionPerformed( ActionEvent e )
    {
        int returnVal = jfc.showOpenDialog( frame );
        if ( returnVal == JFileChooser.APPROVE_OPTION )
        {
            String pathname = jfc.getSelectedFile().getAbsolutePath();
            URL shape = aquireURL( pathname );
            if ( shape == null )
            {
                JOptionPane.showMessageDialog( frame,
                                               "could not find file \"" + pathname + "\"",
                                               "Could not find file",
                                               JOptionPane.ERROR_MESSAGE );
                System.err.println( "Could not find shapefile: " + pathname );
                return;
            }
            String filepart = pathname.substring( 0, pathname.lastIndexOf( "." ) );
            URL sld = aquireURL( filepart + ".sld" );
            if ( sld == null )
            {
                JOptionPane.showMessageDialog( frame,
                                               "could not find SLD file \"" + filepart + ".sld\"",
                                               "Could not find SLD file",
                                               JOptionPane.ERROR_MESSAGE );
                System.err.println( "Could not find sld file: " + filepart + ".sld" );
                return;
            }
            try
            {
                setMapContext( load( shape, sld ) );
            }
            catch ( Exception e1 )
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    private void setMapContext( final MapContext context )
    {
        //        myMapPane.setHighlightLayer( context.getLayer( 0 ) );

        myMapPane.setMapArea( new Envelope( 0, 0, 100, 100 ) );
        myMapPane.setRenderer( createRenderer() );
        myMapPane.setContext( context );

//        myMapPane.getRenderer().addLayer(new RenderedMapScale());
        frame.repaint();
        frame.doLayout();
    }

    //----------------------------------------------------------------------
    // Other Public Methods

    /**
     * Method used to set the current map projection.
     *
     * @param crs A new CRS for the mappnae.
     */
    public void setCRS( CoordinateReferenceSystem crs )
    {
        myMapPane.getContext().setAreaOfInterest( myMapPane.getContext().getAreaOfInterest(), crs );
        myMapPane.setReset( true );
        myMapPane.repaint();
    }


    public MapContext load( URL shape, URL sld ) throws Exception
    {
        ShapefileDataStore ds = new ShapefileDataStore( shape );

        FeatureSource fs = ds.getFeatureSource();
        com.vividsolutions.jts.geom.Envelope env = fs.getBounds();
        myMapPane.setMapArea( env );
        StyleFactory factory = CommonFactoryFinder.getStyleFactory( null );

        SLDParser stylereader = new SLDParser( factory, sld );
        org.geotools.styling.Style[] style = stylereader.readXML();

        CoordinateReferenceSystem crs = fs.getSchema().getDefaultGeometry().getCoordinateSystem();
        if ( crs == null )
        {
            crs = DefaultGeographicCRS.WGS84;
        }
        MapContext context = new DefaultMapContext( crs );
        context.addLayer( fs, style[ 0 ] );

        context.getLayerBounds();
        return context;

    }

    private MapContext createContextFromShapefile( final URL shape, final URL sld )
            throws IOException
    {
        ShapefileDataStore ds = new ShapefileDataStore( shape );

        FeatureSource fs = ds.getFeatureSource();
        com.vividsolutions.jts.geom.Envelope env = fs.getBounds();
        myMapPane.setMapArea( env );
        StyleFactory factory = CommonFactoryFinder.getStyleFactory( null );

        SLDParser stylereader = new SLDParser( factory, sld );
        org.geotools.styling.Style[] style = stylereader.readXML();

        CoordinateReferenceSystem crs = fs.getSchema().getDefaultGeometry().getCoordinateSystem();
        if ( crs == null )
        {
            crs = DefaultGeographicCRS.WGS84;
        }
        MapContext context = new DefaultMapContext( crs );
        context.addLayer( fs, style[ 0 ] );

        context.getLayerBounds();
        return context;
    }

    private GTRenderer createRenderer()
    {
        GTRenderer renderer;
        if ( false )
        {
            renderer = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put( "memoryPreloadingEnabled", Boolean.TRUE );
            renderer.setRendererHints( hints );
        }
        else
        {
            renderer = new StreamingRenderer();
            HashMap hints = new HashMap();
            hints.put( "memoryPreloadingEnabled", Boolean.FALSE );
            renderer.setRendererHints( hints );
        }
        return renderer;
    }

}

   
