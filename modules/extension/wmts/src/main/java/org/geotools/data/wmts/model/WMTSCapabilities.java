/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wmts.model;

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
import java.util.logging.Logger;

import org.eclipse.emf.common.util.EList;
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Capabilities;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.AbstractSingleCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.logging.Level;

import net.opengis.ows11.WGS84BoundingBoxType;
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
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.LayerType;
import net.opengis.wmts.v_1.TileMatrixLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLimitsType;
import net.opengis.wmts.v_1.TileMatrixSetLinkType;
import net.opengis.wmts.v_1.TileMatrixSetType;
import net.opengis.wmts.v_1.TileMatrixType;
import net.opengis.wmts.v_1.URLTemplateType;
import org.geotools.data.wms.xml.Dimension;
import org.geotools.data.wms.xml.Extent;
import org.opengis.referencing.crs.GeographicCRS;

/**
 * Represents a base object for a WMTS getCapabilities response.
 *
 * @author Richard Gould, Refractions Research
 * @author Ian Turton
 *
 * @source $URL$
 */
public class WMTSCapabilities extends Capabilities {

    static public final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.wmts");

    private static CoordinateReferenceSystem CRS84;
    static {
        try {
            CRS84 = CRS.decode("CRS:84");
        } catch (FactoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private WMTSRequest request;

    private GeometryFactory gf = new GeometryFactory();

    CapabilitiesType caps;

    private Map<String, WMTSLayer> layerMap = new HashMap<>();

    private List<Layer> layers = new ArrayList<Layer>(); // cache

    private String[] exceptions = new String[0];

    private List<TileMatrixSet> matrixes = new ArrayList<>();
    private Map<String, TileMatrixSet> matrixSetMap = new HashMap<>();

    private WMTSServiceType type;

    /**
     * @param object
     */
    public WMTSCapabilities(CapabilitiesType capabilities) {
        caps = capabilities;
        setService(new WMTSService(caps.getServiceIdentification()));
        setVersion(caps.getServiceIdentification().getServiceTypeVersion().get(0).toString());
        ContentsType contents = caps.getContents();

        for (Object l : contents.getDatasetDescriptionSummary()) {

            if (l instanceof LayerType) {
                LayerType layerType = (LayerType) l;

                String title = ((LanguageStringType) layerType.getTitle().get(0))
                        .getValue();

                WMTSLayer layer = new WMTSLayer(title);
                layer.setName(layerType.getIdentifier().getValue());

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

                @SuppressWarnings("unchecked")
                EList<BoundingBoxType> bboxes = layerType.getBoundingBox();
                Map<String, CRSEnvelope> boundingBoxes = new HashMap<>();
                for (BoundingBoxType bbox : bboxes) {
                    boundingBoxes.put(bbox.getCrs(), bbox2bbox(bbox));
                }

                WGS84BoundingBoxType wgsBBox = null;
                if(! layerType .getWGS84BoundingBox().isEmpty()) {
                    wgsBBox = (WGS84BoundingBoxType) layerType .getWGS84BoundingBox().get(0);
                }
                if (wgsBBox != null) {
                    int y;
                    int x;
                    // in WMTS WGS84 is in lon,lat order -
                    // see https://portal.opengeospatial.org/services/srv_public_issues.php?call=viewIssue&issue_id=898
                    if (CRS.getAxisOrder(CRS84).equals(AxisDirection.NORTH_EAST)) {
                        x = 1;
                        y = 0;
//                        LOGGER.info("exporting bbox " + wgsBBox + "\n as lat/lon");
                    } else {
                        x = 0;
                        y = 1;
//                        LOGGER.info("exporting bbox " + wgsBBox + "\n as lon/lat");
                    }
                    boundingBoxes.put("CRS:84",
                            new CRSEnvelope("CRS:84",
                                    ((Double) wgsBBox.getLowerCorner().get(x)).doubleValue(),
                                    ((Double) wgsBBox.getLowerCorner().get(y)).doubleValue(),
                                    ((Double) wgsBBox.getUpperCorner().get(x)).doubleValue(),
                                    ((Double) wgsBBox.getUpperCorner().get(y)).doubleValue()));

                    layer.setLatLonBoundingBox(boundingBoxes.get("CRS:84"));


                }
                layer.setBoundingBoxes(boundingBoxes);

                EList<URLTemplateType> resourceURL = layerType.getResourceURL();
                if (resourceURL != null && !resourceURL.isEmpty()) {
                    for (URLTemplateType resource : resourceURL) {
                        String template = resourceURL.get(0).getTemplate();
                        String format = resourceURL.get(0).getFormat();
                        // layer.formats.add(format);

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
                        String dimUom = uom == null? "N/A" : uom.getValue();

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

                layers.add(layer);
                layerMap.put(layer.getName(), layer);
            }
        }

        for (TileMatrixSetType tm : contents.getTileMatrixSet()) {
            TileMatrixSet matrixSet = new TileMatrixSet();
            matrixSet.setCRS(tm.getSupportedCRS());
            matrixSet.setIdentifier(tm.getIdentifier().getValue());
            if(tm.getBoundingBox() != null) {
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
                if(matrix.getCrs() == null) {
                    throw new RuntimeException("unable to create CRS " + matrixSet.getCrs());
                }
                List<Double> c = mat.getTopLeftCorner();

                matrix.setTopLeft(gf.createPoint(
                        new Coordinate(c.get(0).doubleValue(), c.get(1).doubleValue())));
                matrixSet.addMatrix(matrix);
            }
            matrixes.add(matrixSet);
            matrixSetMap.put(matrixSet.getIdentifier(), matrixSet);
        }



        // set layer SRS - this comes from the tile matrix link
        Set<String> srs = new TreeSet<>();
        Set<CoordinateReferenceSystem> crs = new HashSet<>();
        Map<String, CoordinateReferenceSystem> names = new HashMap<>();
        for (TileMatrixSet tms : matrixes) {

            try {
                CoordinateReferenceSystem coordinateReferenceSystem = tms
                        .getCoordinateReferenceSystem();
                crs.add(coordinateReferenceSystem);
                names.put(tms.getIdentifier(), coordinateReferenceSystem);
            } catch (FactoryException e) {
                // LOGGER.log(Level.FINER, e.getMessage(), e);
            }

            srs.add(tms.getCrs());
        }


        for (Layer l : layers) {
            WMTSLayer wmtsLayer = (WMTSLayer) l;
            Map<String, TileMatrixSetLink> tileMatrixLinks = wmtsLayer.getTileMatrixLinks();

            if(wmtsLayer.getLatLonBoundingBox() != null) {
                ReferencedEnvelope wgs84Env = new ReferencedEnvelope(wmtsLayer.getLatLonBoundingBox());
                wmtsLayer.getBoundingBoxes().put("EPSG:4326", new CRSEnvelope(wgs84Env));
            } else {
                // if the layer does not provide wgs84bbox, let's assume a bbox from the tilematrixset
                for (TileMatrixSetLink tmsLink : tileMatrixLinks.values()) {
                    TileMatrixSet tms = matrixSetMap.get(tmsLink.getIdentifier());
                    if(tms.getBbox() != null) {
                        // Take the first good bbox
                        // TODO: refer a bbox which is natively wgs84
                        ReferencedEnvelope re = new ReferencedEnvelope(tms.getBbox());
                        try {
                            ReferencedEnvelope wgs84re = re.transform(CRS84, true);
                            wmtsLayer.setLatLonBoundingBox(new CRSEnvelope(wgs84re));
                            break;
                        } catch (Exception ex) {
                            // the RE can't be projected on WGS84, so let's try another one
                            LOGGER.fine("Can't use " + tms.getIdentifier() +" for bbox");
                            continue;
                        }
                    }
                }

                if(wmtsLayer.getLatLonBoundingBox() == null) {
                    // We did not find any good bbox
                    LOGGER.warning("No good BBOX found for layer " + l.getName());
                    throw new RuntimeException("No good BBOX found for layer " + l.getName()); // todo: find a proper exception type
                }
            }

            ReferencedEnvelope wgs84Env = new ReferencedEnvelope(wmtsLayer.getLatLonBoundingBox());

            // add a bbox for every CRS
            for (TileMatrixSetLink tmsLink : tileMatrixLinks.values()) {
                CoordinateReferenceSystem tmsCRS = names.get(tmsLink.getIdentifier());
                wmtsLayer.setPreferredCRS(tmsCRS); // the preferred crs is just an arbitrary one?
                String crsCode = tmsCRS.getName().getCode();

                if(wmtsLayer.getBoundingBoxes().containsKey(crsCode)) {
                    LOGGER.fine("Bbox for " + crsCode + " already exists for layer " + l.getName());
                    continue;
                }

                TileMatrixSet tms = matrixSetMap.get(tmsLink.getIdentifier());
                if(tms.getBbox() != null) {
                    wmtsLayer.getBoundingBoxes().put(crsCode, tms.getBbox());
                }

                // add bboxes
                try {
                    // make safe to CRS bounds
//                    ReferencedEnvelope safeEnv = wgs84Env.intersection(
//                            org.geotools.tile.impl.wmts.WMTSService.getAcceptableExtent(tmsCRS));
//                    wmtsLayer.getBoundingBoxes().put(tmsCRS.getName().getCode(),
//                            new CRSEnvelope(safeEnv.transform(tmsCRS, true)));

                    // making bbox safe may restrict it too much: let's trust in the declaration
                    wmtsLayer.getBoundingBoxes().put(crsCode, new CRSEnvelope(wgs84Env.transform(tmsCRS, true)));
                    wmtsLayer.addSRS(tmsCRS);
                } catch (TransformException | FactoryException e) {
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
                                        } else if (vt.getValue().equalsIgnoreCase("REST") || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            setType(WMTSServiceType.REST);
                                        }
                                    }
                                }
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
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
                                        } else if (vt.getValue().equalsIgnoreCase("REST") || vt.getValue().equalsIgnoreCase("RESTful")) {
                                            setType(WMTSServiceType.REST);
                                        }
                                    }
                                }
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (opx.getName().equalsIgnoreCase("GetCapabilities")) {
                    request.setGetCapabilities(opt);
                } else if (opx.getName().equalsIgnoreCase("GetTile")) {

                    request.setGetTile(opt);
                } else if (opx.getName().equalsIgnoreCase("GetFeatureInfo")) {
                    request.setGetFeatureInfo(opt);
                }
            }
        }

    }

    /**
     * @param type
     */
    private void setType(WMTSServiceType type) {
        this.type = type;

    }

    /**
     * @return the type
     */
    public WMTSServiceType getType() {
        return type;
    }

    /**
     * Access a flat view of the layers available in the WMS.
     * <p>
     * The information available here is the same as doing a top down walk of all the layers available via getLayer().
     *
     * @return List of all available layers
     */
    public List<Layer> getLayerList() {
        return Collections.unmodifiableList(layers);
    }

    private void addChildrenRecursive(List<Layer> layers, Layer layer) {
        if (layer.getChildren() != null) {
            for (Layer child : layer.getChildren()) {
                layers.add(child);
                addChildrenRecursive(layers, child);
            }
        }
    }

    /**
     * The request contains information about possible Requests that can be made against this server, including URLs and formats.
     *
     * @return Returns the request.
     */
    public WMTSRequest getRequest() {
        return request;
    }

    /**
     * @param request The request to set.
     */
    public void setRequest(WMTSRequest request) {
        this.request = request;
    }

    /**
     * Exceptions declare what kind of formats this server can return exceptions in. They are used during subsequent requests.
     */
    public String[] getExceptions() {
        return exceptions;
    }

    public void setExceptions(String[] exceptions) {
        this.exceptions = exceptions;
    }

    /**
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers the layers to set
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * @return the matrixes
     */
    public List<TileMatrixSet> getMatrixSets() {
        return matrixes;
    }

    /**
     * @param matrixes the matrixes to set
     */
    public void setMatrixSets(List<TileMatrixSet> matrixes) {
        this.matrixes = matrixes;
    }

    public TileMatrixSet getMatrixSet(String identifier) {
        return matrixSetMap.get(identifier);
    }
    /**
     * @param string
     * @return
     */
    public WMTSLayer getLayer(String name) {
        return layerMap.get(name);
    }

    public static CRSEnvelope bbox2bbox(BoundingBoxType bbox) {

        return new CRSEnvelope(bbox.getCrs(),
                ((Double) bbox.getLowerCorner().get(0)).doubleValue(),
                ((Double) bbox.getLowerCorner().get(1)).doubleValue(),
                ((Double) bbox.getUpperCorner().get(0)).doubleValue(),
                ((Double) bbox.getUpperCorner().get(1)).doubleValue());
    }
}
