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

import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gui.swing.JMapPane;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.renderer3d.Renderer3D;
import org.geotools.renderer3d.Renderer3DImpl;
import org.geotools.renderer3d.utils.canvas3d.FrameListener;
import org.geotools.styling.BasicLineStyle;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;

/**
 * An example of using the 3D map.
 *
 * @author Hans Häggström
 */
public class Show3DMapExample
{

    //======================================================================
    // Public Methods

    //----------------------------------------------------------------------
    // Main Method

    public static void main( String[] args ) throws IOException
    {
        // Create some data
/* DEBUG:
        final ExampleDataGenerator exampleDataGenerator = new ExampleDataGenerator();
        final MapContext exampleMap = exampleDataGenerator.createExampleMap();
*/

        final MapContext exampleMap = createContextFromShapefile( new URL( "file:example_data/countries/countries.shp" ),
                                                                  new URL( "file:example_data/simple_style.sld" ) );

        // Create a 3D renderer
        final Renderer3D renderer3D = new Renderer3DImpl( exampleMap );
        final Component mapView3D = renderer3D.get3DView();

        // Create a 2D renderer with the same data for comparsion
        final StreamingRenderer streamingRenderer = new StreamingRenderer();
        final JMapPane mapView2D = new JMapPane( streamingRenderer, exampleMap );
        mapView2D.setMapArea( exampleMap.getLayerBounds() );
        mapView2D.setState( JMapPane.Pan );
        mapView2D.setCursor( new Cursor( Cursor.MOVE_CURSOR ) );

        // Build and show the rest of the UI
        final JLabel labelFor3DView = new JLabel( "3D map renderer" );
        createUi( wrapInTitledPanel( mapView3D, labelFor3DView ),
                  wrapInTitledPanel( mapView2D, new JLabel( "2D JMapPanel view" ) ) );

        // Show frames per second in a swing label
        renderer3D.addFrameListener( createFpsDisplayer( labelFor3DView, "3D map renderer" ) );
    }

    //======================================================================
    // Private Methods

    private static FrameListener createFpsDisplayer( final JLabel labelFor3DView, final String message )
    {
        return new FrameListener()
        {

            private double time_s = 0;
            private int frames = 0;

            public void onFrame( final double secondsSinceLastFrame )
            {
                time_s += secondsSinceLastFrame;
                frames++;
                if ( time_s > 1 ) // Calculate average fps over one second
                {
                    final int fps = (int) ( frames / time_s );
                    labelFor3DView.setText( message + " (" + fps + " FPS)" );
                    time_s = 0;
                    frames = 0;
                }
            }

        };
    }


    private static JPanel wrapInTitledPanel( Component component, final JLabel label )
    {
        final JPanel panel = new JPanel( new BorderLayout() );
        panel.add( component, BorderLayout.CENTER );
        panel.add( label, BorderLayout.NORTH );

        return panel;
    }


    private static void createUi( final Component view3D, final JComponent view2D )
    {
        final JPanel mainPanel = new JPanel( new BorderLayout() );

        // Add a menu also to demonstrate that the 3D view runs inside swing / awt.
        // TODO: The menu must be converted to a heavyweight awt component to overdraw the 3D awt canvas...  
        // That requires using an AWT Frame, and that makes it hard to add Swing components.
        mainPanel.add( createMenuBar(), BorderLayout.NORTH );

        // Show that menu renders on top of swing component but not awt canvas
        mainPanel.add( new JLabel( "         " ), BorderLayout.WEST );

        // TODO: Do this already at the Renderer3D level if not otherwise solvable.
        // Wrap the 3D canvas in another JPanel, as it seems to have redraw problems otherwise?
        final JPanel view3DHolder = new JPanel( new BorderLayout() );
        view3DHolder.add( view3D, BorderLayout.CENTER );

        // Put 2D and 3D view in a split pane.
        final JSplitPane splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, view3DHolder, view2D );
        splitPane.setDividerLocation( 500 );
        splitPane.setOneTouchExpandable( true );
        mainPanel.add( splitPane, BorderLayout.CENTER );

        showInFrame( mainPanel, "3D Map Demo" );
    }


    private static void showInFrame( final Component view, final String frameTitle )
    {
        final JFrame frame3D = new JFrame( frameTitle );
        final JPanel container = new JPanel( new BorderLayout() );
        container.setPreferredSize( new Dimension( 800, 600 ) );
        frame3D.getContentPane().add( container );
        container.add( view, BorderLayout.CENTER );

        frame3D.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame3D.pack();
        frame3D.setVisible( true );
    }


    private static JMenuBar createMenuBar()
    {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu menu = new JMenu( "Demo" );
        menuBar.add( menu );

        menu.add( new AbstractAction( "Exit" )
        {

            public void actionPerformed( final ActionEvent e )
            {
                System.exit( 0 );
            }

        } );

        return menuBar;
    }


    private static MapContext createContextFromShapefile( final URL shape )
            throws IOException
    {
        return createContextFromShapefile( shape, null );
    }

    private static MapContext createContextFromShapefile( final URL shape, final URL sld )
            throws IOException
    {
        return loadShapefile( shape, loadStyle( sld ) );
    }

    private static Style loadStyle( final URL sld )
            throws IOException
    {
        final Style style;
        if ( sld != null )
        {
            final StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory( null );
            final SLDParser stylereader = new SLDParser( styleFactory, sld );
            final Style[] styles = stylereader.readXML();

            style = styles[ 0 ];
        }
        else
        {
            style = new BasicLineStyle();
        }
        return style;
    }

    private static MapContext loadShapefile( final URL shape, final Style style )
            throws IOException
    {
        final ShapefileDataStore shapefileDataStore = new ShapefileDataStore( shape );

        final FeatureSource featureSource = shapefileDataStore.getFeatureSource();

        CoordinateReferenceSystem crs = featureSource.getSchema().getDefaultGeometry().getCoordinateSystem();
        if ( crs == null )
        {
            crs = DefaultGeographicCRS.WGS84;
        }

        final MapContext context = new DefaultMapContext( crs );
        context.addLayer( featureSource, style );
        context.getLayerBounds();

        return context;
    }

}
