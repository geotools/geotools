package org.geotools.grid;

import java.awt.Color;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Polygon;
import java.util.Arrays;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.hexagon.HexagonOrientation;
import org.geotools.grid.hexagon.Hexagons;
import org.geotools.grid.oblong.Oblongs;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLineDef;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class GridExamples {
    
private void exampleSquareGrid() {
    // exampleSquareGrid start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(
            110.0, 150.0, -45.0, -5.0, DefaultGeographicCRS.WGS84);
    
    SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, 10.0);
    
    // exampleSquareGrid end
}

private void exampleDensifiedSquareGrid() {
    // exampleDensifiedSquareGrid start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(
            110, 160, -45, -8, DefaultGeographicCRS.WGS84);
    
    double squareWidth = 10.0;

    // max distance between vertices
    double vertexSpacing = squareWidth / 20;
    
    SimpleFeatureSource grid = Grids.createSquareGrid(gridBounds, squareWidth, vertexSpacing);

    // exampleDensifiedSquareGrid end
}
    
private void exampleHexagonalGrid() {
    // exampleHexagonalGrid start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(0, 100, 0, 100, null);

    // length of each hexagon edge
    double sideLen = 5.0;
    SimpleFeatureSource grid = Grids.createHexagonalGrid(gridBounds, sideLen);

    // exampleHexagonalGrid end
}

private void exampleCustomFeatureType() {
    // exampleCustomFeatureType start
    SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
    typeBuilder.setName("hextype");
    typeBuilder.add("hexagon", Polygon.class, (CoordinateReferenceSystem)null);
    typeBuilder.add("color", Color.class);
    SimpleFeatureType TYPE = typeBuilder.buildFeatureType();

    final ReferencedEnvelope bounds = new ReferencedEnvelope(0, 100, 0, 100, null);

    GridFeatureBuilder builder = new GridFeatureBuilder(TYPE) {
        @Override
        public void setAttributes(GridElement element, Map<String, Object> attributes) {
            PolygonElement polyEl = (PolygonElement) element;
            int g = (int) (255 * polyEl.getCenter().x / bounds.getWidth());
            int b = (int) (255 * polyEl.getCenter().y / bounds.getHeight());
            attributes.put("color", new Color(0, g, b));
        }
    };
    
    // Pass the GridFeatureBuilder object to the createHexagonalGrid method
    // (the -1 value here indicates that we don't need densified polygons)
    final double sideLen = 5.0;
    SimpleFeatureSource grid = Grids.createHexagonalGrid(bounds, sideLen, -1, builder);

    // exampleCustomFeatureType end
}

private void exampleIntersection() throws Exception {
    // exampleIntersection start
    
    // Load the outline of Australia from a shapefile
    URL url = getClass().getResource("oz.shp");
    FileDataStore dataStore = FileDataStoreFinder.getDataStore(url);
    SimpleFeatureSource ozMapSource = dataStore.getFeatureSource();

    // Set the grid size (1 degree) and create a bounding envelope
    // that is neatly aligned with the grid size
    double sideLen = 1.0;
    ReferencedEnvelope gridBounds =
            Envelopes.expandToInclude(ozMapSource.getBounds(), sideLen);

    // Create a feature type
    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
    tb.setName("grid");
    tb.add(GridFeatureBuilder.DEFAULT_GEOMETRY_ATTRIBUTE_NAME,
            Polygon.class, gridBounds.getCoordinateReferenceSystem());
    tb.add("id", Integer.class);
    SimpleFeatureType TYPE = tb.buildFeatureType();

    // Build the grid the custom feature builder class
    GridFeatureBuilder builder = new IntersectionBuilder(TYPE, ozMapSource);
    SimpleFeatureSource grid = Grids.createHexagonalGrid(gridBounds, sideLen, -1, builder);

    // exampleIntersection end
}

private void exampleHexagonOrientation() {
    // exampleHexagonOrientation start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(0, 100, 0, 100, null);
    double sideLen = 5.0;
    GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
    SimpleFeatureSource grid = Hexagons.createGrid(
            gridBounds, sideLen, HexagonOrientation.ANGLED, builder);

    // exampleHexagonOrientation end
}

private void exampleOblong() {
    // exampleOblong start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(0, 100, 0, 100, null);
    double width = 10.0;
    double height = 5.0;
    GridFeatureBuilder builder = new DefaultGridFeatureBuilder();
    SimpleFeatureSource grid = Oblongs.createGrid(gridBounds, width, height, builder);
    
    // exampleOblong end
}

private void exampleMajorMinorLines() {
    // exampleMajorMinorLines start
    ReferencedEnvelope gridBounds = new ReferencedEnvelope(
            110.0, 150.0, -45.0, -5.0, DefaultGeographicCRS.WGS84);
    
    /*
     * Line definitions: 
     * major lines at 10 degree spacing are indicated by level = 2
     * minor lines at 2 degree spacing are indicated by level = 1
     * (level values are arbitrary; only rank order matters)
     */
    List<OrthoLineDef> lineDefs = Arrays.asList(
            // vertical (longitude) lines
            new OrthoLineDef(LineOrientation.VERTICAL, 2, 10.0),
            new OrthoLineDef(LineOrientation.VERTICAL, 1, 2.0),
    
            // horizontal (latitude) lines
            new OrthoLineDef(LineOrientation.HORIZONTAL, 2, 10.0),
            new OrthoLineDef(LineOrientation.HORIZONTAL, 1, 2.0));
    
    // Specify vertex spacing to get "densified" polygons
    double vertexSpacing = 0.1;
    SimpleFeatureSource grid = Lines.createOrthoLines(gridBounds, lineDefs, vertexSpacing);
    
    // exampleMajorMinorLines end
}

}
