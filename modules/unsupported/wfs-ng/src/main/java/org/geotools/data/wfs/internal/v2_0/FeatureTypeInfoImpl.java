/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.WGS84BoundingBoxType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.OutputFormatListType;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.Loggers;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class FeatureTypeInfoImpl implements FeatureTypeInfo {

    private final FeatureTypeType eType;

    private final WFSConfig config;

    public FeatureTypeInfoImpl(FeatureTypeType eType, WFSConfig config) {
        this.eType = eType;
        this.config = config;
    }

    @Override
    public String getTitle() {
        return eType.getTitle() == null || eType.getTitle().isEmpty()
                ? null
                : String.valueOf(eType.getTitle().get(0).getValue());
    }

    @Override
    public Set<String> getKeywords() {
        @SuppressWarnings("unchecked")
        List<KeywordsType> keywords = eType.getKeywords();
        Set<String> ret;
        if (keywords == null) {
            ret = Collections.emptySet();
        } else {
            ret = new HashSet<String>();
            for (KeywordsType k : keywords) {
                @SuppressWarnings("unchecked")
                List<LanguageStringType> keyword = k.getKeyword();
                for (LanguageStringType ls : keyword) {
                    ret.add(ls.getValue());
                }
            }
            ret.remove(null);
        }
        return ret;
    }

    @Override
    public String getDescription() {
        return eType.getAbstract() == null || eType.getAbstract().isEmpty()
                ? null
                : eType.getAbstract().get(0).getValue();
    }

    @Override
    public String getName() {
        return config.localTypeName(eType.getName());
    }

    public QName getQName() {
        return eType.getName();
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
            Loggers.MODULE.log(
                    Level.WARNING,
                    "Can't transform bounds of " + getName() + " to " + getDefaultSRS(),
                    e);
            nativeBounds = new ReferencedEnvelope(crs);
        } catch (FactoryException e) {
            Loggers.MODULE.log(
                    Level.WARNING,
                    "Can't transform bounds of " + getName() + " to " + getDefaultSRS(),
                    e);
            nativeBounds = new ReferencedEnvelope(crs);
        }
        return nativeBounds;
    }

    @Override
    public String getDefaultSRS() {
        return eType.getDefaultCRS();
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        CoordinateReferenceSystem crs = null;
        String defaultSRS = eType.getDefaultCRS();
        if (null != defaultSRS) {
            try {
                crs = CRS.decode(defaultSRS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return crs;
    }

    @Override
    public ReferencedEnvelope getWGS84BoundingBox() {

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

            ReferencedEnvelope latLonBounds =
                    new ReferencedEnvelope(
                            minLon, maxLon, minLat, maxLat, DefaultGeographicCRS.WGS84);

            return latLonBounds;
        }
        return null;
    }

    @Override
    public List<String> getOtherSRS() {
        return eType.getOtherCRS();
    }

    @Override
    public Set<String> getOutputFormats() {
        final OutputFormatListType outputFormats = eType.getOutputFormats();
        if (null == outputFormats) {
            return Collections.emptySet();
        }

        List<String> ftypeDeclaredFormats = outputFormats.getFormat();
        if (null == ftypeDeclaredFormats || ftypeDeclaredFormats.isEmpty()) {
            return Collections.emptySet();
        }

        return new HashSet<String>(ftypeDeclaredFormats);
    }

    @Override
    public String getAbstract() {
        StringBuffer sb = new StringBuffer();
        for (Object a : eType.getAbstract()) {
            sb.append(a);
            sb.append(" ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}
