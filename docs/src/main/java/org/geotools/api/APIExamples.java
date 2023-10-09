/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.api;

import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.GeneralPosition;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Envelope;

public class APIExamples {

    /** OpenGIS Envelope Examples (using ReferencedEnvelope) */
    private void exampleGeneralBounds() throws Exception {
        // exampleGeneralBounds start
        CoordinateReferenceSystem wsg84 = CRS.decode("EPSG:4326");

        GeneralPosition lowerPosition = new GeneralPosition(0.0,0.0);
        lowerPosition.setCoordinateReferenceSystem(wsg84);

        GeneralPosition upperPosition = new GeneralPosition(10.0,20.0);
        upperPosition.setCoordinateReferenceSystem(wsg84);

        Bounds bounds = new GeneralBounds(lowerPosition,upperPosition);

        double xMin = bounds.getMinimum(0);
        double yMin = bounds.getMinimum(1);

        double xMax = bounds.getMaximum(0);
        double yMax = bounds.getMaximum(1);

        double width = bounds.getSpan(0);
        double height = bounds.getSpan(1);

        double xCenter = bounds.getMedian(0);
        double yCenter = bounds.getMedian(1);

        CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();

        // Direct access to internal upper and lower positions
        Position lower = bounds.getLowerCorner();
        Position upper = bounds.getUpperCorner();

        // expand to include 15, 30
        lower.setOrdinate(0, Math.min(lower.getOrdinate(0), 15));
        lower.setOrdinate(1, Math.min(lower.getOrdinate(1), 30));
        upper.setOrdinate(0, Math.max(upper.getOrdinate(0), 15));
        upper.setOrdinate(1, Math.max(upper.getOrdinate(1), 30));
        // exampleGeneralBounds end
    }

    private void exampleBoundingBox() throws Exception {
        // exampleBoundingBox start
        CoordinateReferenceSystem wsg84 = CRS.decode("EPSG:4326");
        org.geotools.api.geometry.BoundingBox bbox = new ReferencedEnvelope(0, 10, 0, 20, wsg84);

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
        Position lower = bbox.getLowerCorner();
        Position upper = bbox.getUpperCorner();

        // expand to include 15, 30
        bbox.include(15, 30);

        // exampleBoundingBox end
    }

    //
    // JTS Envelope Examples
    //
    private void exampleEnvelope() throws Exception {
        // exampleEnvelope start
        org.locationtech.jts.geom.Envelope envelope = new Envelope(0, 10, 0, 20);
        double xMin = envelope.getMinX();
        double yMin = envelope.getMinY();

        double xMax = envelope.getMaxX();
        double yMax = envelope.getMaxY();

        double width = envelope.getWidth(); // assuming axis 0 is easting
        double height = envelope.getHeight(); // assuming axis 1 is nothing

        // Expand an existing envelope
        Envelope bbox = new Envelope();
        envelope.expandToInclude(bbox);

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
    // Referenced Envelope Recommended Examples
    //
    private void recommendedReferencedEnvelope() throws Exception {
        // recommendedReferencedEnvelope start
        ReferencedEnvelope envelope =
                ReferencedEnvelope.create(0, 10, 0, 20, DefaultGeographicCRS.WGS84);

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
        Position lower = envelope.getLowerCorner();
        Position upper = envelope.getUpperCorner();

        // expand to include 15, 30
        envelope.include(15, 30);

        envelope.isEmpty(); // check if storing width and height are 0

        envelope.isNull(); // check if "null" (not storing anything)
        envelope.setToNull();

        // recommendedReferencedEnvelope end
    }

    //
    // Referenced Envelope Examples
    //
    private void exampleReferencedEnvelope() throws Exception {
        // exampleReferencedEnvelope start
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(0, 10, 0, 20, DefaultGeographicCRS.WGS84);

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
        Position lower = envelope.getLowerCorner();
        Position upper = envelope.getUpperCorner();

        // expand to include 15, 30
        envelope.include(15, 30);

        envelope.isEmpty(); // check if storing width and height are 0

        envelope.isNull(); // check if "null" (not storing anything)
        envelope.setToNull();

        // exampleReferencedEnvelope end
    }

    //
    // Referenced Envelope 3D Examples
    //
    private void exampleReferencedEnvelope3D() throws Exception {
        // exampleReferencedEnvelope3D start
        ReferencedEnvelope3D envelope =
                new ReferencedEnvelope3D(0, 10, 0, 20, 0, 30, DefaultGeographicCRS.WGS84_3D);

        double xMin = envelope.getMinX();
        double yMin = envelope.getMinY();
        double zMin = envelope.getMinZ();

        double xMax = envelope.getMaxX();
        double yMax = envelope.getMaxY();
        double zMax = envelope.getMaxZ();

        double width = envelope.getWidth();
        double height = envelope.getHeight();
        double depth = envelope.getDepth();

        double xCenter = envelope.getMedian(0);
        double yCenter = envelope.getMedian(1);
        double zCenter = envelope.getMedian(2);

        CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
        int dimension = envelope.getDimension();

        // Direct access to internal upper and lower positions
        Position lower = envelope.getLowerCorner();
        Position upper = envelope.getUpperCorner();

        // expand to include 15, 30, 40
        envelope.include(15, 30, 40);

        envelope.isEmpty(); // check if storing width and height are 0

        envelope.isNull(); // check if "null" (not storing anything)
        envelope.setToNull();

        // exampleReferencedEnvelope3D end
    }

    //
    // Referenced Envelope Static Methods Examples
    //
    private void exampleReferencedEnvelopeStaticMethods() throws Exception {
        // exampleReferencedEnvelopeStaticMethods start

        // can hold both regular ReferencedEnvelope as well as ReferencedEnvelope3D
        ReferencedEnvelope env;
        // can be instance of ReferencedEnvelope3D;
        ReferencedEnvelope original = null;
        // can be 2D or 3D
        CoordinateReferenceSystem crs = null;
        // can be instance of ReferencedEnvelope(3D)
        Bounds opengis_env = null;
        // can be instance of ReferencedEnvelope(3D)
        org.locationtech.jts.geom.Envelope jts_env = null;
        // can be instance of ReferencedEnvelope or ReferencedEnvelope3D
        BoundingBox bbox = null;

        // safely copy ReferencedEnvelope, uses type of original to determine type
        env = ReferencedEnvelope.create(original);

        // safely create ReferencedEnvelope from CRS, uses dimension to determine type
        env = ReferencedEnvelope.create(crs);

        // safely create ReferencedEnvelope from org.geotools.api.geometry.Envelope,
        // uses dimension in Envelope to determine type
        env = ReferencedEnvelope.create(opengis_env, crs);

        // safely create ReferencedEnvelope from org.locationtech.jts.geom.Envelope,
        // uses dimension in Envelope to determine type
        env = ReferencedEnvelope.envelope(jts_env, crs);

        // safely reference org.geotools.api.geometry.Envelope as ReferencedEnvelope
        // --> if it is a ReferencedEnvelope(3D), simply cast it; if not, create a conversion
        env = ReferencedEnvelope.reference(opengis_env);

        // safely reference org.locationtech.jts.geom.Envelope as ReferencedEnvelope
        // --> if it is a ReferencedEnvelope(3D), simply cast it; if not, create a conversion
        env = ReferencedEnvelope.reference(jts_env);

        // safely reference BoundingBox as ReferencedEnvelope
        // --> if it is a ReferencedEnvelope(3D), simply cast it; if not, create a conversion
        env = ReferencedEnvelope.reference(bbox);

        // exampleReferencedEnvelopeStaticMethods end
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
