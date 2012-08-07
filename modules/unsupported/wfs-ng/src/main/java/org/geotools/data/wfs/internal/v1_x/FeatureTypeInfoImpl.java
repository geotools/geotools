package org.geotools.data.wfs.internal.v1_x;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.XMLConstants;

import net.opengis.ows10.WGS84BoundingBoxType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.OutputFormatListType;

import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class FeatureTypeInfoImpl implements FeatureTypeInfo {

    private final FeatureTypeType eType;

    public FeatureTypeInfoImpl(FeatureTypeType eType) {
        this.eType = eType;
    }

    @Override
    public String getTitle() {
        return eType.getTitle();
    }

    @Override
    public Set<String> getKeywords() {
        @SuppressWarnings("unchecked")
        List<String> keywords = eType.getKeywords();
        Set<String> ret;
        if (keywords == null) {
            ret = Collections.emptySet();
        } else {
            ret = new HashSet<String>(keywords);
            ret.remove(null);
        }
        return ret;
    }

    @Override
    public String getDescription() {
        return eType.getAbstract();
    }

    @Override
    public String getName() {
        return eType.getName().getLocalPart();
    }

    @Override
    public URI getSchema() {
        String namespaceURI = eType.getName().getNamespaceURI();
        try {
            return XMLConstants.NULL_NS_URI.equals(namespaceURI) ? null : new URI(namespaceURI);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    public ReferencedEnvelope getBounds() {
        ReferencedEnvelope wgs84Bounds = getWGS84BoundingBox();
        CoordinateReferenceSystem crs = getCRS();
        if (null == crs) {
            return wgs84Bounds;
        }

        ReferencedEnvelope nativeBounds;
        try {
            nativeBounds = wgs84Bounds.transform(crs, true);
        } catch (TransformException e) {
            Loggers.MODULE.log(Level.WARNING, "Can't transform bounds of " + getName() + " to "
                    + getDefaultSRS(), e);
            nativeBounds = new ReferencedEnvelope(crs);
        } catch (FactoryException e) {
            Loggers.MODULE.log(Level.WARNING, "Can't transform bounds of " + getName() + " to "
                    + getDefaultSRS(), e);
            nativeBounds = new ReferencedEnvelope(crs);
        }
        return nativeBounds;
    }

    @Override
    public String getDefaultSRS() {
        return eType.getDefaultSRS();
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        CoordinateReferenceSystem crs = null;
        String defaultSRS = eType.getDefaultSRS();
        if (null != defaultSRS) {
            try {
                boolean forceLongitudFirst = defaultSRS.startsWith("EPSG:");
                crs = CRS.decode(defaultSRS, forceLongitudFirst);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return crs;
    }

    @Override
    public ReferencedEnvelope getWGS84BoundingBox() {

        @SuppressWarnings("unchecked")
        List<WGS84BoundingBoxType> bboxList = eType.getWGS84BoundingBox();
        if (bboxList != null && bboxList.size() > 0) {
            WGS84BoundingBoxType bboxType = bboxList.get(0);
            @SuppressWarnings("unchecked")
            List<Double> lowerCorner = bboxType.getLowerCorner();
            @SuppressWarnings("unchecked")
            List<Double> upperCorner = bboxType.getUpperCorner();
            double minLon = (Double) lowerCorner.get(0);
            double minLat = (Double) lowerCorner.get(1);
            double maxLon = (Double) upperCorner.get(0);
            double maxLat = (Double) upperCorner.get(1);

            ReferencedEnvelope latLonBounds = new ReferencedEnvelope(minLon, maxLon, minLat,
                    maxLat, DefaultGeographicCRS.WGS84);

            return latLonBounds;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getOtherSRS() {
        return eType.getOtherSRS();
    }

    @Override
    public Set<String> getOutputFormats() {
        final OutputFormatListType outputFormats = eType.getOutputFormats();
        if (null == outputFormats) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        List<String> ftypeDeclaredFormats = outputFormats.getFormat();
        if (null == ftypeDeclaredFormats || ftypeDeclaredFormats.isEmpty()) {
            return Collections.emptySet();
        }

        return new HashSet<String>(ftypeDeclaredFormats);
    }

}
