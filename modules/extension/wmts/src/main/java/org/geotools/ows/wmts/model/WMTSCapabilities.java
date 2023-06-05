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
import java.util.List;
import java.util.Map;
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
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.xml.Dimension;
import org.geotools.ows.wms.xml.Extent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.logging.Logging;
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

    public static final Logger LOGGER = Logging.getLogger(WMTSCapabilities.class);

    private WMTSRequest request;

    private GeometryFactory gf = new GeometryFactory();

    CapabilitiesType caps;

    private final Map<String, WMTSLayer> layerMap = new HashMap<>();
    private final List<WMTSLayer> layers = new ArrayList<>();
    private final Map<String, TileMatrixSet> matrixSetMap = new HashMap<>();
    private final List<TileMatrixSet> matrixes = new ArrayList<>();

    private String[] exceptions = new String[0];

    private WMTSServiceType type;

    public WMTSCapabilities(CapabilitiesType capabilities) throws ServiceException {
        caps = capabilities;
        setService(new WMTSService(caps.getServiceIdentification(), caps.getServiceProvider()));
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

        // Fill in some layers info from the linked MatrixSets
        for (WMTSLayer wmtsLayer : layers) {
            fillTileMatrixSet(wmtsLayer);
        }

        request = new WMTSRequest();

        parseOperations();
    }

    private void parseOperations() throws ServiceException {
        // some REST capabilities don't fill this in but we need it later!
        OperationType operationType = new OperationType();
        operationType.setGet(null);
        request.setGetCapabilities(operationType);
        OperationsMetadataType operationsMetadata = caps.getOperationsMetadata();
        setType(WMTSServiceType.REST);
        boolean isKVP = false;
        boolean isREST = false;
        if (operationsMetadata != null) {
            for (Object op : operationsMetadata.getOperation()) {
                OperationType opt = operationType;
                net.opengis.ows11.OperationType opx = (net.opengis.ows11.OperationType) op;

                EList dcps = opx.getDCP();
                for (Object item : dcps) {
                    DCPType dcp = (DCPType) item;

                    EList gets = dcp.getHTTP().getGet();
                    for (Object value : gets) {
                        RequestMethodType get = (RequestMethodType) value;
                        try {
                            opt.setGet(new URL(get.getHref()));
                            if (!get.getConstraint().isEmpty()) {
                                for (Object con : get.getConstraint()) {
                                    DomainType dt = (DomainType) con;
                                    AllowedValuesType t = dt.getAllowedValues();
                                    for (Object v : t.getValue()) {
                                        ValueType vt = (ValueType) v;
                                        if (vt.getValue().equalsIgnoreCase("KVP")) {
                                            isKVP = true;
                                        } else if (vt.getValue().equalsIgnoreCase("REST")
                                                || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            isREST = true;
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
                    for (Object o : posts) {
                        RequestMethodType post = (RequestMethodType) o;
                        try {
                            opt.setPost(new URL(post.getHref()));
                            if (!post.getConstraint().isEmpty()) {
                                for (Object con : post.getConstraint()) {
                                    DomainType dt = (DomainType) con;
                                    AllowedValuesType t = dt.getAllowedValues();
                                    for (Object v : t.getValue()) {
                                        ValueType vt = (ValueType) v;
                                        if (vt.getValue().equalsIgnoreCase("KVP")) {
                                            isKVP = true;
                                        } else if (vt.getValue().equalsIgnoreCase("REST")
                                                || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            isREST = true;
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
                if (isREST) { // Given the choice we prefer REST (it's less likely to be broken)
                    setType(WMTSServiceType.REST);
                } else if (isKVP) {
                    setType(WMTSServiceType.KVP);
                }
                if (opx.getName().equalsIgnoreCase("GetCapabilities")) {
                    request.setGetCapabilities(opt);
                } else if (opx.getName().equalsIgnoreCase("GetTile")) {
                    request.setGetTile(opt);
                }
            }
        }
    }

    private void fillTileMatrixSet(WMTSLayer wmtsLayer) {
        // set WGS84 LatLonBBox if not present
        if (wmtsLayer.getLatLonBoundingBox() == null) {
            setLatLonBBox(wmtsLayer);
        }
        ReferencedEnvelope wgs84Env = new ReferencedEnvelope(wmtsLayer.getLatLonBoundingBox());

        Map<String, TileMatrixSetLink> tileMatrixLinks = wmtsLayer.getTileMatrixLinks();
        // add a bbox for every CRS
        for (TileMatrixSetLink tmsLink : tileMatrixLinks.values()) {
            String tmsIdentifier = tmsLink.getIdentifier();
            TileMatrixSet tms = matrixSetMap.get(tmsIdentifier);
            if (tms == null) {
                LOGGER.info(
                        String.format(
                                "WMTS capabilities for layer %s specified a TileMatrixSet link %s that doesn't exist.",
                                wmtsLayer.getName(), tmsIdentifier));
                tileMatrixLinks.remove(tmsIdentifier);
                continue;
            }
            CoordinateReferenceSystem tmsCRS = tms.getCoordinateReferenceSystem();
            wmtsLayer.addSRS(tmsCRS);
            String srs = CRS.toSRS(tmsCRS);

            if (tms.getBbox() != null) {
                wmtsLayer.getBoundingBoxes().put(srs, tms.getBbox());
            } else {
                try {
                    // tileMatrix did not provide bounds, reproject the LatLon ones.
                    wmtsLayer
                            .getBoundingBoxes()
                            .put(srs, new CRSEnvelope(wgs84Env.transform(tmsCRS, true)));

                } catch (TransformException | FactoryException e) {
                    if (LOGGER.isLoggable(Level.INFO))
                        LOGGER.log(
                                Level.INFO,
                                "Not adding CRS " + srs + " for layer " + wmtsLayer.getName(),
                                e);
                }
            }
        }
    }

    // use this if capabilities does not provide LatLon bbox
    // try to reproject one of the TileMatrix CRS or use the CRS bounds
    // to give a LatLonBBox to the layer.
    private void setLatLonBBox(WMTSLayer wmtsLayer) {
        for (TileMatrixSetLink tmsLink : wmtsLayer.getTileMatrixLinks().values()) {
            TileMatrixSet tms = matrixSetMap.get(tmsLink.getIdentifier());
            if (tms.getBbox() != null) {
                // Take the first good bbox
                // TODO: refer a bbox which is natively wgs84
                ReferencedEnvelope re = new ReferencedEnvelope(tms.getBbox());
                try {
                    ReferencedEnvelope wgs84re = re.transform(DefaultGeographicCRS.WGS84, true);
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
            LOGGER.warning("No good Bbox found for layer " + wmtsLayer.getName());
            // throw new ServiceException("No good Bbox found for layer " + l.getName());
            CRSEnvelope latLonBoundingBox = new CRSEnvelope("CRS:84", -180, -90, 180, 90);
            wmtsLayer.setLatLonBoundingBox(latLonBoundingBox);
            wmtsLayer.setBoundingBoxes(latLonBoundingBox);
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

        String name = layerType.getIdentifier().getValue();
        String title =
                layerType.getTitle().size() > 0
                        ? ((LanguageStringType) layerType.getTitle().get(0)).getValue()
                        : name;

        WMTSLayer layer = new WMTSLayer(title);
        layer.setName(name);

        // The Abstract is of Type LanguageStringType, not String. We're choosing the first one.
        if (layerType.getAbstract().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object line : layerType.getAbstract()) {
                if (line instanceof LanguageStringType) {
                    sb.append(((LanguageStringType) line).getValue());
                    break;
                } else {
                    sb.append(line);
                }
            } // end of for
            layer.set_abstract(sb.toString());
        }

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

    /** @return An unmodifiable list of MatrixSets */
    public List<TileMatrixSet> getMatrixSets() {
        return Collections.unmodifiableList(matrixes);
    }

    /** Clears all matrixSet's and populates with the list */
    public void setMatrixSets(List<TileMatrixSet> matrixes) {
        matrixSetMap.clear();
        this.matrixes.clear();
        for (TileMatrixSet tms : matrixes) {
            matrixSetMap.put(tms.getIdentifier(), tms);
            this.matrixes.add(tms);
        }
    }

    public TileMatrixSet getMatrixSet(String identifier) {
        return matrixSetMap.get(identifier);
    }

    /**
     * @param name of the layer
     * @return the WMTS layer
     */
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
