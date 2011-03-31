package org.geotools.api;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Envelope;

public class APIExamples {

/**
 * OpenGIS Envelope Exampels (using ReferencedEnvelope)
 */
private void exampleISOEnvelope() throws Exception {
    // exampleISOEnvelope start
    CoordinateReferenceSystem wsg84 = CRS.decode("EPSG:4326");
    org.opengis.geometry.Envelope envelope = new ReferencedEnvelope(0, 10, 0, 20, wsg84);
    
    double xMin = envelope.getMinimum(0);
    double yMin = envelope.getMinimum(1);
    
    double xMax = envelope.getMaximum(0);
    double yMax = envelope.getMaximum(1);
    
    double width = envelope.getSpan(0);
    double height = envelope.getSpan(1);
    
    double xCenter = envelope.getMedian(0);
    double yCenter = envelope.getMedian(1);
    
    CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
    
    // Direct access to internal upper and lower positions
    DirectPosition lower = envelope.getLowerCorner();
    DirectPosition upper = envelope.getUpperCorner();
    
    // expand to include 15, 30
    upper.setOrdinate(0, Math.max(upper.getOrdinate(0), 15));
    upper.setOrdinate(1, Math.max(upper.getOrdinate(1), 30));
    lower.setOrdinate(0, Math.min(lower.getOrdinate(0), 15));
    lower.setOrdinate(1, Math.min(lower.getOrdinate(1), 30));
    
    // exampleISOEnvelope end
}

private void exampleBoundingBox() throws Exception {
    // exampleBoundingBox start
    CoordinateReferenceSystem wsg84 = CRS.decode("EPSG:4326");
    org.opengis.geometry.BoundingBox bbox = new ReferencedEnvelope(0, 10, 0, 20, wsg84);
    
    double xMin = bbox.getMinX();
    double yMin = bbox.getMinY();
    
    double xMax = bbox.getMaxX();
    double yMax = bbox.getMaxY();
    
    double width = bbox.getWidth();
    double height = bbox.getHeight();
    
    double xCenter = bbox.getMedian(0);
    double yCenter = bbox.getMedian(1);
    
    CoordinateReferenceSystem crs = bbox.getCoordinateReferenceSystem();
    
    // Direct access to internal upper and lower positions
    DirectPosition lower = bbox.getLowerCorner();
    DirectPosition upper = bbox.getUpperCorner();
    
    // expand to include 15, 30
    bbox.include(15, 30);
    
    // exampleBoundingBox end
}

//
// JTS Envelope Examples
//
private void exampleEnvelope() throws Exception {
    // exampleEnvelope start
    com.vividsolutions.jts.geom.Envelope envelope = new Envelope(0, 10, 0, 20);
    double xMin = envelope.getMinX();
    double yMin = envelope.getMinY();
    
    double xMax = envelope.getMaxX();
    double yMax = envelope.getMaxY();
    
    double width = envelope.getWidth(); // assuming axis 0 is easting
    double height = envelope.getHeight(); // assuming axis 1 is nothing
    
    // Expand an existing envelope
    Envelope bbox = new Envelope();
    envelope.expandToInclude( bbox );
    
    // Use
    envelope.covers(5, 10); // inside or on edge!
    envelope.contains(5, 10); // inside only
    
    // Null
    envelope.isNull(); // check if "null" (not storing anything)
    envelope.setToNull();
    
    // exampleEnvelope end
}

private void transformEnvelope() throws Exception {
    // transformEnvelope start
    CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
    
    Envelope envelope = new Envelope(0, 10, 0, 20);
    
    MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
    
    Envelope quick = JTS.transform(envelope, transform);
    
    // Sample 10 points around the envelope
    Envelope better = JTS.transform(envelope, null, transform, 10);
    // transformEnvelope end
}

//
// Referenced Envelope Examples
//
private void exampleReferencedEnvelope() throws Exception {
    // exampleReferencedEnvelope start
    ReferencedEnvelope envelope = new ReferencedEnvelope(0, 10, 0, 20, DefaultGeographicCRS.WGS84);
    
    double xMin = envelope.getMinX();
    double yMin = envelope.getMinY();
    
    double xMax = envelope.getMaxX();
    double yMax = envelope.getMaxY();
    
    double width = envelope.getWidth();
    double height = envelope.getHeight();
    
    double xCenter = envelope.getMedian(0);
    double yCenter = envelope.getMedian(1);
    
    CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
    int dimension = envelope.getDimension();
    
    // Direct access to internal upper and lower positions
    DirectPosition lower = envelope.getLowerCorner();
    DirectPosition upper = envelope.getUpperCorner();
    
    // expand to include 15, 30
    envelope.include(15, 30);
    
    envelope.isEmpty(); // check if storing width and height are 0
    
    envelope.isNull(); // check if "null" (not storing anything)
    envelope.setToNull();
    
    // exampleReferencedEnvelope end
}

private void transformReferencedEnvelope() throws Exception {
    // transformReferencedEnvelope start
    CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
    ReferencedEnvelope envelope = new ReferencedEnvelope(0, 10, 0, 20, sourceCRS);
    
    // Transform using 10 sample points around the envelope
    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
    ReferencedEnvelope result = envelope.transform(targetCRS, true, 10);
    // transformReferencedEnvelope end
}

// 
// Geometry Examples
//
private void transformGeometry() throws Exception {
    // transformGeometry start
    CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
    CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
    
    Envelope envelope = new Envelope(0, 10, 0, 20);
    
    MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
    
    Envelope quick = JTS.transform(envelope, transform);
    
    // Sample 10 points around the envelope
    Envelope better = JTS.transform(envelope, null, transform, 10);
    // transformGeometry end
}
}
