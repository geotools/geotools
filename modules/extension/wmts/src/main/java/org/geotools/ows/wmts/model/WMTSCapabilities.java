/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.ows11.DomainType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.ows11.ValueType;
import net.opengis.ows11.WGS84BoundingBoxType;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.StyleType;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.URLTemplateType;
import org.eclipse.emf.common.util.EList;
import org.geotools.data.ows.Capabilities;
import org.geotools.data.ows.OperationType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.xml.Dimension;
import org.geotools.ows.wms.xml.Extent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.SimpleInternationalString;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Represents a base object for a WMTS getCapabilities response.
 *
 * <p>(Based on existing work by rgould for WMS service)
 *
 * @author ian
 * @author Emanuele Tajariol (etj at geo-solutions dot it)
 * @author Matthias Schulze (LDBV at ldbv dot bayern dot de)
 */
public class WMTSCapabilities extends Capabilities {

    public static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(WMTSCapabilities.class);

    private WMTSRequest request;

    private GeometryFactory gf = new GeometryFactory();

    CapabilitiesType caps;

    private Map<String, WMTSLayer> layerMap = new HashMap<>();

    private List<WMTSLayer> layers = new ArrayList<>(); // cache

    private String[] exceptions = new String[0];

    private List<TileMatrixSet> matrixes = new ArrayList<>();

    private Map<String, TileMatrixSet> matrixSetMap = new HashMap<>();

    private WMTSServiceType type;

    public WMTSCapabilities(CapabilitiesType capabilities) throws ServiceException {
        caps = capabilities;
        setService(new WMTSService(caps.getServiceIdentification()));
        setVersion(caps.getServiceIdentification().getServiceTypeVersion().toString());
        ContentsType contents = caps.getContents();

        // Parse layers
        for (Object l : contents.getDatasetDescriptionSummary()) {

            if (l instanceof LayerType) {
                LayerType layerType = (LayerType) l;
                WMTSLayer layer = parseLayer(layerType);
                layers.add(layer);
                layerMap.put(layer.getName(), layer);

            } else {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Unknown object " + l);
                }
            }
        }

        // Parse TileMatrixSets
        for (TileMatrixSetType tm : contents.getTileMatrixSet()) {
            TileMatrixSet matrixSet = parseMatrixSet(tm);
            matrixes.add(matrixSet);
            matrixSetMap.put(matrixSet.getIdentifier(), matrixSet);
        }

        // set layer SRS - this comes from the tile matrix link
        Set<String> srs = new TreeSet<>();
        Set<CoordinateReferenceSystem> crs = new HashSet<>();
        Map<String, CoordinateReferenceSystem> names = new HashMap<>();
        for (TileMatrixSet tms : matrixes) {

            CoordinateReferenceSystem refSystem = tms.getCoordinateReferenceSystem();
            crs.add(refSystem);
            names.put(tms.getIdentifier(), refSystem);

            srs.add(tms.getCrs());
        }

        // Fill in some layers info from the linked MatrixSets
        for (Layer l : layers) {
            WMTSLayer wmtsLayer = (WMTSLayer) l;
            Map<String, TileMatrixSetLink> tileMatrixLinks = wmtsLayer.getTileMatrixLinks();

            if (wmtsLayer.getLatLonBoundingBox() != null) {
                ReferencedEnvelope wgs84Env =
                        new ReferencedEnvelope(wmtsLayer.getLatLonBoundingBox());
                wmtsLayer.getBoundingBoxes().put("EPSG:4326", new CRSEnvelope(wgs84Env));
            } else {
                // if the layer does not provide wgs84bbox, let's assume a bbox
                // from the tilematrixset
                for (TileMatrixSetLink tmsLink : tileMatrixLinks.values()) {
                    TileMatrixSet tms = matrixSetMap.get(tmsLink.getIdentifier());
                    if (tms.getBbox() != null) {
                        // Take the first good bbox
                        // TODO: refer a bbox which is natively wgs84
                        ReferencedEnvelope re = new ReferencedEnvelope(tms.getBbox());
                        try {
                            ReferencedEnvelope wgs84re =
                                    re.transform(DefaultGeographicCRS.WGS84, true);
                            wmtsLayer.setLatLonBoundingBox(new CRSEnvelope(wgs84re));
                            break;
                        } catch (Exception ex) {
                            // the RE can't be projected on WGS84,
                            // so let's try another one
                            if (LOGGER.isLoggable(Level.FINE))
                                LOGGER.fine(
                                        "Can't use "
                                                + tms.getIdentifier()
                                                + " for bbox: "
                                                + ex.getMessage());
                            continue;
                        }
                    }
                }

                if (wmtsLayer.getLatLonBoundingBox() == null) {
                    // We did not find any good bbox
                    LOGGER.warning("No good Bbox found for layer " + l.getName());
                    // throw new ServiceException("No good Bbox found for layer " + l.getName());
                    CRSEnvelope latLonBoundingBox = new CRSEnvelope("CRS:84", -180, -90, 180, 90);
                    wmtsLayer.setLatLonBoundingBox(latLonBoundingBox);
                    wmtsLayer.setBoundingBoxes(latLonBoundingBox);
                }
            }

            ReferencedEnvelope wgs84Env = new ReferencedEnvelope(wmtsLayer.getLatLonBoundingBox());

            // add a bbox for every CRS
            for (TileMatrixSetLink tmsLink : tileMatrixLinks.values()) {
                CoordinateReferenceSystem tmsCRS = names.get(tmsLink.getIdentifier());
                wmtsLayer.setPreferredCRS(tmsCRS); // the preferred crs is just
                // an arbitrary one?
                String crsCode = tmsCRS.getName().getCode();

                if (wmtsLayer.getBoundingBoxes().containsKey(crsCode)) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine(
                                "Bbox for " + crsCode + " already exists for layer " + l.getName());
                    continue;
                }

                TileMatrixSet tms = matrixSetMap.get(tmsLink.getIdentifier());
                if (tms.getBbox() != null) {
                    wmtsLayer.getBoundingBoxes().put(crsCode, tms.getBbox());
                }

                // add bboxes
                try {
                    // make safe to CRS bounds
                    // making bbox safe may restrict it too much: let's trust in
                    // the declaration
                    wmtsLayer
                            .getBoundingBoxes()
                            .put(crsCode, new CRSEnvelope(wgs84Env.transform(tmsCRS, true)));
                    wmtsLayer.addSRS(tmsCRS);
                } catch (TransformException | FactoryException e) {
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.info("Not adding CRS " + crsCode + " for layer " + l.getName());
                }
            }
        }

        request = new WMTSRequest();
        // some REST capabilities don't fill this in but we need it later!
        OperationType operationType = new OperationType();
        operationType.setGet(null);
        request.setGetCapabilities(operationType);
        OperationsMetadataType operationsMetadata = caps.getOperationsMetadata();
        setType(WMTSServiceType.REST);
        if (operationsMetadata != null) {
            for (Object op : operationsMetadata.getOperation()) {
                OperationType opt = operationType;
                net.opengis.ows11.OperationType opx = (net.opengis.ows11.OperationType) op;

                EList dcps = opx.getDCP();
                for (int i = 0; i < dcps.size(); i++) {
                    DCPType dcp = (DCPType) dcps.get(i);

                    EList gets = dcp.getHTTP().getGet();
                    for (int j = 0; j < gets.size(); j++) {
                        RequestMethodType get = (RequestMethodType) gets.get(j);
                        try {
                            opt.setGet(new URL(get.getHref()));
                            if (!get.getConstraint().isEmpty()) {
                                for (Object con : get.getConstraint()) {
                                    DomainType dt = (DomainType) con;
                                    AllowedValuesType t = dt.getAllowedValues();
                                    for (Object v : t.getValue()) {
                                        ValueType vt = (ValueType) v;
                                        if (vt.getValue().equalsIgnoreCase("KVP")) {
                                            setType(WMTSServiceType.KVP);
                                        } else if (vt.getValue().equalsIgnoreCase("REST")
                                                || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            setType(WMTSServiceType.REST);
                                        }
                                    }
                                }
                            }
                        } catch (MalformedURLException e) {
                            throw new ServiceException(
                                    "Error parsing WMTS operation URL: " + e.getMessage());
                        }
                    }
                    EList posts = dcp.getHTTP().getPost();
                    for (int j = 0; j < posts.size(); j++) {
                        RequestMethodType post = (RequestMethodType) posts.get(j);
                        try {
                            opt.setPost(new URL(post.getHref()));
                            if (!post.getConstraint().isEmpty()) {
                                for (Object con : post.getConstraint()) {
                                    DomainType dt = (DomainType) con;
                                    AllowedValuesType t = dt.getAllowedValues();
                                    for (Object v : t.getValue()) {
                                        ValueType vt = (ValueType) v;
                                        if (vt.getValue().equalsIgnoreCase("KVP")) {
                                            setType(WMTSServiceType.KVP);
                                        } else if (vt.getValue().equalsIgnoreCase("REST")
                                                || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            setType(WMTSServiceType.REST);
                                        }
                                    }
                                }
                            }
                        } catch (MalformedURLException e) {
                            throw new ServiceException(
                                    "Error parsing WMTS operation URL: " + e.getMessage());
                        }
                    }
                }

                if (opx.getName().equalsIgnoreCase("GetCapabilities")) {
                    request.setGetCapabilities(opt);
                } else if (opx.getName().equalsIgnoreCase("GetTile")) {
                    request.setGetTile(opt);
                }
            }
        }
    }

    private TileMatrixSet parseMatrixSet(TileMatrixSetType tm)
            throws RuntimeException, IllegalArgumentException, ServiceException {
        TileMatrixSet matrixSet = new TileMatrixSet();
        matrixSet.setCRS(tm.getSupportedCRS());
        matrixSet.setWellKnownScaleSet(tm.getWellKnownScaleSet());
        matrixSet.setIdentifier(tm.getIdentifier().getValue());
        if (tm.getBoundingBox() != null) {
            matrixSet.setBbox(bbox2bbox(tm.getBoundingBox()));
        }
        for (TileMatrixType mat : tm.getTileMatrix()) {
            TileMatrix matrix = new TileMatrix();

            matrix.setIdentifier(mat.getIdentifier().getValue());
            matrix.setDenominator(mat.getScaleDenominator());
            matrix.setMatrixHeight(mat.getMatrixHeight().intValue());
            matrix.setMatrixWidth(mat.getMatrixWidth().intValue());
            matrix.setTileHeight(mat.getTileHeight().intValue());
            matrix.setTileWidth(mat.getTileWidth().intValue());
            matrix.setParent(matrixSet);
            if (matrix.getCrs() == null) {
                throw new ServiceException(
                        "MatrixSet "
                                + tm.getIdentifier().getValue()
                                + ": unable to create CRS "
                                + matrixSet.getCrs());
            }
            List<Double> c = mat.getTopLeftCorner();

            matrix.setTopLeft(gf.createPoint(new Coordinate(c.get(0), c.get(1))));
            matrixSet.addMatrix(matrix);
        }
        return matrixSet;
    }

    private WMTSLayer parseLayer(LayerType layerType) {
        String title = ((LanguageStringType) layerType.getTitle().get(0)).getValue();
        WMTSLayer layer = new WMTSLayer(title);
        layer.setName(layerType.getIdentifier().getValue());

        // The Abstract is of Type LanguageStringType, not String.
        StringBuilder sb = new StringBuilder();
        for (Object line : layerType.getAbstract()) {
            if (line instanceof LanguageStringType) {
                sb.append(((LanguageStringType) line).getValue());
            } else {
                sb.append(line);
            }
        } // end of for
        layer.set_abstract(sb.toString());

        EList<TileMatrixSetLinkType> tmsLinks = layerType.getTileMatrixSetLink();
        for (TileMatrixSetLinkType linkType : tmsLinks) {
            TileMatrixSetLink link = new TileMatrixSetLink();
            link.setIdentifier(linkType.getTileMatrixSet());
            TileMatrixSetLimitsType limits = linkType.getTileMatrixSetLimits();
            if (limits != null) {
                for (TileMatrixLimitsType tmlt : limits.getTileMatrixLimits()) {
                    TileMatrixLimits tml = new TileMatrixLimits();
                    tml.setTileMatix(tmlt.getTileMatrix());
                    tml.setMinCol(tmlt.getMinTileCol().longValue());
                    tml.setMaxCol(tmlt.getMaxTileCol().longValue());
                    tml.setMinRow(tmlt.getMinTileRow().longValue());
                    tml.setMaxRow(tmlt.getMaxTileRow().longValue());
                    link.addLimit(tml);
                }
            }
            layer.addTileMatrixLink(link);
        }
        layer.getFormats().addAll(layerType.getFormat());
        layer.getInfoFormats().addAll(layerType.getInfoFormat());
        EList<StyleType> styles = layerType.getStyle();
        List<StyleImpl> sList = new ArrayList<>();
        for (StyleType styleType : styles) {
            StyleImpl style = new StyleImpl();
            style.setName(styleType.getIdentifier().getValue());
            StringBuilder t = new StringBuilder();
            for (Object title1 : styleType.getTitle()) {
                t.append(title1.toString());
            }
            style.setTitle(new SimpleInternationalString(t.toString()));
            style.setDefault(styleType.isIsDefault());
            if (styleType.isIsDefault()) {
                layer.setDefaultStyle(style);
            }
            sList.add(style);
        }
        layer.setStyles(sList);
        @SuppressWarnings("unchecked")
        EList<BoundingBoxType> bboxes = layerType.getBoundingBox();
        Map<String, CRSEnvelope> boundingBoxes = new HashMap<>();
        for (BoundingBoxType bbox : bboxes) {
            boundingBoxes.put(bbox.getCrs(), bbox2bbox(bbox));
        }
        WGS84BoundingBoxType wgsBBox = null;
        if (!layerType.getWGS84BoundingBox().isEmpty()) {
            wgsBBox = (WGS84BoundingBoxType) layerType.getWGS84BoundingBox().get(0);
        }
        if (wgsBBox != null) {
            int y;
            int x;
            // in WMTS WGS84 is in lon,lat order - see
            // https://portal.opengeospatial.org/services/srv_public_issues.php?call=viewIssue&issue_id=898
            if (CRS.getAxisOrder(DefaultGeographicCRS.WGS84).equals(CRS.AxisOrder.NORTH_EAST)) {
                x = 1;
                y = 0;
            } else {
                x = 0;
                y = 1;
            }
            boundingBoxes.put(
                    "CRS:84",
                    new CRSEnvelope(
                            "CRS:84",
                            (Double) wgsBBox.getLowerCorner().get(x),
                            (Double) wgsBBox.getLowerCorner().get(y),
                            (Double) wgsBBox.getUpperCorner().get(x),
                            (Double) wgsBBox.getUpperCorner().get(y)));

            layer.setLatLonBoundingBox(boundingBoxes.get("CRS:84"));
        }
        layer.setBoundingBoxes(boundingBoxes);
        EList<URLTemplateType> resourceURL = layerType.getResourceURL();
        if (resourceURL != null && !resourceURL.isEmpty()) {
            for (URLTemplateType resource : resourceURL) {
                String template = resource.getTemplate();
                String format = resource.getFormat();

                layer.putResourceURL(format, template);
            }
        }
        EList<DimensionType> dimensionList = layerType.getDimension();
        if (dimensionList != null && !dimensionList.isEmpty()) {
            for (DimensionType dimensionType : dimensionList) {
                CodeType identifierType = dimensionType.getIdentifier();
                String dimIdentifier = identifierType.getValue();
                String dimDefault = dimensionType.getDefault();

                DomainMetadataType uom = dimensionType.getUOM();
                String dimUom = uom == null ? "N/A" : uom.getValue();

                Dimension d = new Dimension(dimIdentifier, dimUom);
                d.setUnitSymbol(dimensionType.getUnitSymbol());
                d.setCurrent(dimensionType.isCurrent());

                // TODO: improve values encoding
                String dimValues = String.join(",", dimensionType.getValue());

                Extent e = new Extent(dimIdentifier, dimDefault, false, false, dimValues);
                d.setExtent(e);

                layer.getLayerDimensions().add(d);
            }
        }
        return layer;
    }

    /** @param type */
    private void setType(WMTSServiceType type) {
        this.type = type;
    }

    /** @return the type */
    public WMTSServiceType getType() {
        return type;
    }

    /**
     * Access a flat view of the layers available in the WMTS.
     *
     * @return Unmodifiable List of all available layers
     */
    public List<WMTSLayer> getLayerList() {
        return Collections.unmodifiableList(layers);
    }

    /**
     * The request contains information about possible Requests that can be made against this
     * server, including URLs and formats.
     *
     * @return Returns the request.
     */
    public WMTSRequest getRequest() {
        return request;
    }

    /** @param request The request to set. */
    public void setRequest(WMTSRequest request) {
        this.request = request;
    }

    /**
     * Exceptions declare what kind of formats this server can return exceptions in. They are used
     * during subsequent requests.
     */
    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    /** @return the matrixes */
    public List<TileMatrixSet> getMatrixSets() {
        return matrixes;
    }

    /** @param matrixes the matrixes to set */
    public void setMatrixSets(List<TileMatrixSet> matrixes) {
        this.matrixes = matrixes;
    }

    public TileMatrixSet getMatrixSet(String identifier) {
        return matrixSetMap.get(identifier);
    }

    /** */
    public WMTSLayer getLayer(String name) {
        return layerMap.get(name);
    }

    public static CRSEnvelope bbox2bbox(BoundingBoxType bbox) {

        return new CRSEnvelope(
                bbox.getCrs(),
                (Double) bbox.getLowerCorner().get(0),
                (Double) bbox.getLowerCorner().get(1),
                (Double) bbox.getUpperCorner().get(0),
                (Double) bbox.getUpperCorner().get(1));
    }
}
