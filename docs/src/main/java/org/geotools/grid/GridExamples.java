package org.geotools.grid;

import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;

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
}
