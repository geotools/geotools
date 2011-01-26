package org.geotools.demo.grid;

import java.awt.Color;
import java.net.URL;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.Envelopes;
import org.geotools.grid.Grids;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.CRS;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;


/**
 * This example creates a lat-long vector grid, then displays it over a
 * shapefile in a different map projection to illustrate how the grid with
 * 'densified' polygons (additional vertices added to each square) gives
 * a nice approximation of curves in the reprojected display.
 *
 * @author mbedward
 */
public class ReprojectedGridExample {

    public static void main(String[] args) throws Exception {
        URL url = ReprojectedGridExample.class.getResource("/data/shapefiles/oz.shp");
        FileDataStore store = FileDataStoreFinder.getDataStore(url);
        SimpleFeatureSource ozMapSource = store.getFeatureSource();

        // create a grid of squares 10 degrees across
        double gridSize = 10.0;

        // vertex spacing for 'densified' grid polygons
        double vertexSpacing = gridSize / 20;

        // create a bounding envelope that is aligned with our grid size
        ReferencedEnvelope gridBounds = Envelopes.expandToInclude(ozMapSource.getBounds(), gridSize);
        SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, gridSize, vertexSpacing);

        MapContext map = new DefaultMapContext();
        map.setTitle("Vector grid");
        map.setCoordinateReferenceSystem(CRS.decode("EPSG:4462", true));
        map.addLayer(ozMapSource, SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 1.0f));
        map.addLayer(grid, SLD.createPolygonStyle(Color.LIGHT_GRAY, null, 1.0f));

        JMapFrame.showMap(map);
    }
}
