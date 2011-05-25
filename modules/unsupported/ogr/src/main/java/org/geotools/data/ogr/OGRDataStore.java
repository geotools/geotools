/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Arrays;
import java.util.Vector;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;
import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.BasicFeatureTypes;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.resources.Classes;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A datastore based on the the <a href="http://www.gdal.org/ogr">OGR</a> spatial data abstraction
 * library
 * 
 * @author Andrea Aime - OpenGeo
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/main/java/org/geotools
 *         /data/ogr/OGRDataStore.java $
 */
public class OGRDataStore extends AbstractDataStore {
    /** C compatible FALSE */
    static final int FALSE = 0;

    /** C compatible TRUE */
    static final int TRUE = 1;
    
    static final FilterCapabilities ATTRIBUTE_FILTER_CAPABILITIES;
    
    static final FilterCapabilities GEOMETRY_FILTER_CAPABILITIES;
    
    static {
        ATTRIBUTE_FILTER_CAPABILITIES = new FilterCapabilities();
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsNotEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsGreaterThan.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsLessThan.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsGreaterThanOrEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsLessThanOrEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(Or.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(And.class);

        // have the capabilities extract any filter that can use geometric intersection 
        // as its base
        GEOMETRY_FILTER_CAPABILITIES = new FilterCapabilities();
        GEOMETRY_FILTER_CAPABILITIES.addType(BBOX.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Contains.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Crosses.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Equals.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Intersects.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Overlaps.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Touches.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Within.class);
    }

    /**
     * The OGRwkbGeometryType enum from ogr_core.h, reduced to represent only 2D classes (I hope the
     * 2.5D will be handled transparently by JTS)
     */
    static final Class[] OGR_GEOM_TYPES = new Class[] { //
    Geometry.class, // wkbUnknown = 0
            Point.class, // wkbPoint = 1
            MultiLineString.class, // wkbLineString = 2,
            MultiPolygon.class, // wkbPolygon = 3,
            MultiPoint.class, // wkbMultiPoint = 4,
            MultiLineString.class, // wkbMultiLineString = 5,
            MultiPolygon.class, // wkbMultiPolygon = 6,
            GeometryCollection.class, // wkbGeometryCollection = 7
    };

    /**
     * The source name that OGR should open and handle
     */
    private String ogrSourceName;

    /**
     * Datastore namespace
     */
    private URI namespace;

    /**
     * OGR driver to be used for the creation of new data sources
     */
    private String ogrDriverName;

    static {
        // perform OGR format registration once
        if (ogr.GetDriverCount() == 0)
            ogr.RegisterAll();
    }

    /**
     * Creates a new OGRDataStore
     * 
     * @param ogrSourceName
     *            a references to the source that needs to be opened. May be a file system path, or
     *            a database reference. See the OGR driver documentation for valid formats of this
     *            string.
     */

    public OGRDataStore(String ogrSourceName, String ogrDriverName, URI namespace)
            throws IOException {
        this.ogrSourceName = ogrSourceName;
        this.ogrDriverName = ogrDriverName;
        this.namespace = (namespace != null) ? namespace : FeatureTypes.DEFAULT_NAMESPACE;
        int update = FALSE;
        if (ogrDriverName == null) {
            DataSource ds = getOGRDataSource(update);
            ds.delete();
        }
    }
    
    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName)
            throws IOException {
        return getFeatureReader(typeName, Query.ALL, false);
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName, 
            Query query) throws IOException {
        return getFeatureReader(typeName, query, false);
    }

    protected FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String typeName, 
            Query query, boolean openForUpdate)
            throws IOException {
        DataSource ds = getOGRDataSource(openForUpdate ? TRUE : FALSE);
        Layer layer = getOGRLayer(ds, typeName);
        
        // handle filtering if possible
        SimpleFeatureType schema = getSchema(typeName);
        Filter filter = query.getFilter();
        org.gdal.ogr.Geometry spatialFilter = getSpatialFilter(schema, filter);
        if(spatialFilter != null)
            layer.SetSpatialFilter(spatialFilter);
        String attributeFilter = getAttributeFilter(schema, filter);
        if(attributeFilter != null)
            layer.SetAttributeFilter(attributeFilter);
        
        return new OGRFeatureReader(ds, layer, schema);
    }
    
    /**
     * Parses the Geotools filter and tries to extract an intersecting geometry that
     * can be used as the OGR spatial filter
     * @param schema
     * @param filter
     * @return
     */
    protected org.gdal.ogr.Geometry getSpatialFilter(SimpleFeatureType schema, Filter filter) {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor = new PostPreProcessFilterSplittingVisitor(GEOMETRY_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();
        if(preFilter instanceof BinarySpatialOperator) {
            BinarySpatialOperator bso = ((BinarySpatialOperator) preFilter);
            Expression geomExpression = null;
            if(bso.getExpression1() instanceof PropertyName && bso.getExpression2() instanceof Literal) {
                geomExpression = bso.getExpression2();
            } else if(bso.getExpression1() instanceof Literal && bso.getExpression2() instanceof PropertyName) {
                geomExpression = bso.getExpression1();
            }
            if(geomExpression != null) {
                Geometry geom = geomExpression.evaluate(null, Geometry.class);
                if(geom != null)
                    return new GeometryMapper(geom.getFactory()).parseGTGeometry(geom);
            }
        }
        return null;
    }
    
    /**
     * Parses the GeoTools filter and tries to extract an SQL expression that can
     * be used as the OGR attribute filter
     * @param schema
     * @param filter
     * @return
     */
    protected String getAttributeFilter(SimpleFeatureType schema, Filter filter) {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor = new PostPreProcessFilterSplittingVisitor(ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();
        if(preFilter != Filter.EXCLUDE && preFilter != Filter.INCLUDE) {
            StringWriter writer = new StringWriter();
            FilterToSQL sqlConverter = new FilterToSQL(writer);
            preFilter.accept(sqlConverter, null);
            return writer.getBuffer().toString();
        }
        return null;
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        DataSource ds = getOGRDataSource(FALSE);
        Layer layer = getOGRLayer(ds, typeName);
        if (layer == null) {
            ds.delete();
            throw new IOException("No such type : " + typeName);
        }
        FeatureDefn featureDef = null;
        try {
            featureDef = layer.GetLayerDefn();

            SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
            AttributeTypeBuilder abuilder = new AttributeTypeBuilder();
            builder.setName(typeName);
            builder.setNamespaceURI(namespace);

            // handle the geometry
            CoordinateReferenceSystem crs = getCRS(layer);
            Class geomClass = getGeometryClass(featureDef.GetGeomType());
            abuilder.setName(Classes.getShortName( geomClass ));
            abuilder.setNillable(true);
            abuilder.setCRS(crs);
            abuilder.setBinding(geomClass);
            GeometryType geometryType = abuilder.buildGeometryType();
            builder.add(abuilder.buildDescriptor(
                    BasicFeatureTypes.GEOMETRY_ATTRIBUTE_NAME, geometryType));

//            builder.add("the_geom", geomClass, crs);
            
            // compute a default parent feature type
            if ((geomClass == Point.class) || (geomClass == MultiPoint.class)) {
                builder.setSuperType(BasicFeatureTypes.POINT);
            } else if ((geomClass == Polygon.class) || (geomClass == MultiPolygon.class)) {
                builder.setSuperType(BasicFeatureTypes.POLYGON);
            } else if ((geomClass == LineString.class) || (geomClass == MultiLineString.class)) {
                builder.setSuperType(BasicFeatureTypes.LINE);
            }
            
            // handle the other fields
            for (int i = 1; i < featureDef.GetFieldCount() + 1; i++) {
                FieldDefn fd = featureDef.GetFieldDefn(i - 1);
                builder.length(fd.GetWidth());
                builder.add(fd.GetNameRef(), getFieldClass(fd));
                fd.delete();
            }

            // finally build the geometry type
            return builder.buildFeatureType();
        } catch (FactoryException e) {
            throw new DataSourceException("Could not determine geometry SRS", e);
        } catch (FactoryRegistryException e) {
            throw new DataSourceException("Could not create feature type", e);
        } finally {
            if (featureDef != null)
                featureDef.delete();
            if (layer != null)
                layer.delete();
            if (ds != null)
                ds.delete();
        }

    }

    public String[] getTypeNames() throws IOException {
        DataSource ds;
        try {
            ds = getOGRDataSource(FALSE);
        } catch (IOException e) {
            return new String[0];
        }
        String[] typeNames = new String[ds.GetLayerCount()];
        for (int i = 0; i < typeNames.length; i++) {
            Layer l = ds.GetLayerByIndex(i);
            typeNames[i] = l.GetName();
            l.delete();
        }
        ds.delete();
        return typeNames;
    }

    protected int getCount(Query query) throws IOException {
        if (!Filter.INCLUDE.equals(query.getFilter()))
            return -1;

        DataSource ds = getOGRDataSource(FALSE);
        Layer l = getOGRLayer(ds, query.getTypeName());
        if (l == null)
            throw new IOException("Unknown feature type: " + query.getTypeName());

        // go for the quick computation, return -1 otherwise
        int count = l.GetFeatureCount(FALSE);
        l.delete();
        ds.delete();
        return count;
    }

    protected FeatureWriter createFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        if (supportsInPlaceWrite(typeName)) {
            OGRFeatureReader reader = (OGRFeatureReader) getFeatureReader(typeName, Query.ALL, true);
            return new OGRDirectFeatureWriter(reader);
        } else {
            throw new UnsupportedOperationException(
                    "This file format does not support in place write, "
                            + "can't perform updates/deletes");
        }
    }

    public boolean supportsInPlaceWrite(String typeName) throws IOException {
        // if it's not able to open the datasource in update mode, there's no
        // change it'll be able to support in place write (no need to throw an
        // exception in
        // this case, thus the direct call instead of getOGRDataSource())
        DataSource ds = ogr.OpenShared(ogrSourceName, TRUE);
        if (ds == null)
            return false;
        Layer l = getOGRLayer(ds, typeName);
        boolean retval = l.TestCapability(ogr.OLCDeleteFeature)
                && l.TestCapability(ogr.OLCRandomWrite) && l.TestCapability(ogr.OLCSequentialWrite);
        l.delete();
        ds.delete();
        return retval;
    }

    // given up on this one. Some drivers tell you that you can write on a layer
    // only if you opened in update mode. We could try to create a new data
    // source,
    // but unfortunately there is no way to have a generic ogr name that works
    // for every data store. Finally, not all drivers that do write do support
    // datastore deletion. So, in the end, either we can update directly an OGR
    // datasource content, or we have to create a new one and roll our own
    // mechanism to have it replace the old one (this includes moving and
    // deleting
    // the elements that make up a data store).
    public boolean supportsWriteNewLayer(String typeName) throws IOException {
        DataSource ds = getOGRDataSource(FALSE);
        Layer l = getOGRLayer(ds, typeName);
        boolean retval = ds.TestCapability(ogr.ODsCCreateLayer)
                && l.TestCapability(ogr.OLCSequentialWrite);
        l.delete();
        ds.delete();
        return retval;
    }

    public ReferencedEnvelope getBounds(Query q) throws IOException {
        if (!q.getFilter().equals(Filter.INCLUDE))
            return null;

        DataSource ds = getOGRDataSource(OGRDataStore.FALSE);
        Layer l = getOGRLayer(ds, q.getTypeName());
        try {
            if (l.TestCapability(ogr.OLCFastGetExtent)) {
                double[] bbox = new double[4];
                // TODO: hum... going thru this forcing the computation is anyways
                // faster than loading all the features just for the sake of
                // getting out the bbox.... but we don't have this
                // explicit middle ground in Geotools
                l.GetExtent(bbox, OGRDataStore.FALSE);
                CoordinateReferenceSystem crs = getSchema(q.getTypeName()).getCoordinateReferenceSystem();
                return new ReferencedEnvelope(bbox[0], bbox[1], bbox[2], bbox[3], crs);
            } else {
                return null;
            }
        } finally {
            if(l != null)
                l.delete();
            if(ds != null)
                ds.delete();
        }
    }

    public void createSchema(SimpleFeatureType schema) throws IOException {
        // TODO: add a field to allow approximate definitions
        createSchema(schema, false, null);
    }

    /**
     * Creates a new OGR layer with provided schema and options
     * 
     * @param schema
     *            the geotools schema
     * @param approximateFields
     *            if true, OGR will try to create fields that are approximations of the required
     *            ones when an exact match cannt be provided
     * @param options
     *            OGR data source/layer creation options
     * @throws IOException
     */
    public void createSchema(SimpleFeatureType schema, boolean approximateFields, String[] options)
            throws IOException {
        DataSource ds = null;
        Layer l = null;

        try {
            // either open datasource, or try creating one
            Vector optVector = options != null ? new Vector(Arrays.asList(options)) : null;
            try {
                ds = getOGRDataSource(TRUE);
            } catch (IOException e) {
                if (ogrDriverName != null) {
                    Driver d = ogr.GetDriverByName(ogrDriverName);
                    ds = d.CreateDataSource(ogrSourceName, optVector);
                    d.delete();

                    if (ds == null)
                        throw new IOException("Could not create OGR data source with driver "
                                + ogrDriverName + " and options " + optVector);
                } else {
                    throw new DataSourceException("Driver not provided, and could not "
                            + "open data source neither");
                }
            }

            // get the spatial reference corresponding to the default geometry
            GeometryDescriptor geomType = schema.getGeometryDescriptor();
            int ogrGeomType = getOGRGeometryType(geomType);
            SpatialReference sr = null;
            if (geomType.getCoordinateReferenceSystem() != null) {
                // use tostring to get a lenient wkt translation
                String wkt = geomType.getCoordinateReferenceSystem().toString();
                sr = new SpatialReference(null);
                if (sr.ImportFromWkt(wkt) != 0) {
                    sr = null;
                    LOGGER.warning("OGR could not parse the geometry WKT," + " detailed error is: "
                            + gdal.GetLastErrorMsg() + "\n" + "WKT was: " + wkt);
                }
            }

            // create the layer
            l = ds.CreateLayer(schema.getTypeName(), sr, ogrGeomType, optVector);
            if (l == null) {
                throw new DataSourceException("Could not create the OGR layer: "
                        + gdal.GetLastErrorMsg());
            }

            // create fields
            for (int i = 0; i < schema.getAttributeCount(); i++) {
                AttributeDescriptor ad = schema.getDescriptor(i);
                if (ad == schema.getGeometryDescriptor())
                    continue;

                FieldDefn definition = getOGRFieldDefinition(ad);
                l.CreateField(definition, approximateFields ? TRUE : FALSE);
            }
        } finally {
            if (l != null) {
                l.delete();
            }
            if (ds != null)
                ds.delete();
        }
    }

    // ---------------------------------------------------------------------------------------
    // PRIVATE SUPPORT METHODS
    // ---------------------------------------------------------------------------------------

    private FieldDefn getOGRFieldDefinition(AttributeDescriptor at) throws IOException {
        final Class type = at.getType().getBinding();
        final FieldDefn def;
        // set type, width, precision and justification where:
        // * width is the number of chars needed to format the strings
        // equivalent of
        // the number
        // * precision is the number of chars after decimal pont
        // * justification: right or left (in outputs)
        // TODO: steal code from Shapefile data store to guess eventual size
        // limitations
        if (Boolean.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTString);
            def.SetWidth(5);
        } else if (Byte.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTInteger);
            def.SetWidth(3);
            def.SetJustify(ogr.OJRight);
        } else if (Short.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTInteger);
            def.SetWidth(5);
            def.SetJustify(ogr.OJRight);
        } else if (Integer.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTInteger);
            def.SetWidth(9);
            def.SetJustify(ogr.OJRight);
        } else if (Long.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTInteger);
            def.SetWidth(19);
            def.SetJustify(ogr.OJRight);
        } else if (BigInteger.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTInteger);
            def.SetWidth(32);
            def.SetJustify(ogr.OJRight);
        } else if (BigDecimal.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTReal);
            def.SetWidth(32);
            def.SetPrecision(15);
            def.SetJustify(ogr.OJRight);
        } else if (Float.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTReal);
            def.SetWidth(12);
            def.SetPrecision(7);
            def.SetJustify(ogr.OJRight);
        } else if (Double.class.equals(type) || Number.class.isAssignableFrom(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTReal);
            def.SetWidth(22);
            def.SetPrecision(16);
            def.SetJustify(ogr.OJRight);
        } else if (String.class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTString);
            def.SetWidth(255);
            // TODO: do a serious attempt to cover blob and clob too
        } else if (byte[].class.equals(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTBinary);
            // } else if (java.sql.Date.class.isAssignableFrom(type)) {
            // def = new FieldDefn(at.getLocalName(), ogr.OFTDate);
            // } else if (java.sql.Time.class.isAssignableFrom(type)) {
            // def = new FieldDefn(at.getLocalName(), ogr.OFTTime);
        } else if (java.util.Date.class.isAssignableFrom(type)) {
            def = new FieldDefn(at.getLocalName(), ogr.OFTDateTime);
        } else {
            throw new IOException("Cannot map " + type + " to an OGR type");
        }

        return def;
    }

    /**
     * Tries to open the specified source in either read only or read/write mode
     * 
     * @param update
     *            open read/write if TRUE, otherwise open read only
     * 
     * @return
     * @throws IOException
     */
    DataSource getOGRDataSource(int update) throws IOException {
        DataSource ds = ogr.OpenShared(ogrSourceName, update);
        if (ds == null)
            throw new IOException("OGR could not open '" + ogrSourceName + "'");
        return ds;
    }

    /**
     * Internal utility method, returns a layer if available, otherwise closes the provided
     * datasource and throws and exception stating the feature type is not available
     * 
     * @param ds
     * @param typeName
     * @return
     * @throws IOException
     */
    Layer getOGRLayer(DataSource ds, String typeName) throws IOException {
        Layer l = ds.GetLayerByName(typeName);
        if (l == null) {
            ds.delete();
            throw new IOException("No feature type " + typeName);
        }
        return l;
    }

    /**
     * Turns an ogrFieldType into the best corresponding class we can figure out
     * 
     * @param ogrFieldType
     * @return
     */
    private Class getFieldClass(FieldDefn field) {
        final int ogrFieldType = field.GetFieldType();
        final int width = field.GetWidth();
        if (ogrFieldType < 0)
            throw new IllegalArgumentException("Can't have a negative type");
        if (ogrFieldType == ogr.OFTInteger) {
            if (width <= 9)
                return Integer.class;
            else if (width <= 19)
                return Long.class;
            else
                return BigDecimal.class;
        } else if (ogrFieldType == ogr.OFTReal) {
            if (width < 13)
                return Float.class;
            else
                return Double.class;
        } else if (ogrFieldType == ogr.OFTDate || ogrFieldType == ogr.OFTTime
                || ogrFieldType == ogr.OFTDateTime) {
            // return java.sql.Date.class;
            // } else if (ogrFieldType == ogr.OFTTime) {
            // return java.sql.Time.class;
            // } else if (ogrFieldType == ogr.OFTDateTime) {
            return java.util.Date.class;
        } else {
            return String.class;
        }
    }

    /**
     * Turns an OGR {@link SpatialReference} object into a {@link CoordinateReferenceSystem} one
     * (using WKT parsing)
     * 
     * @param reference
     * @return
     * @throws FactoryException
     */
    private CoordinateReferenceSystem getCRS(Layer layer) throws FactoryException {
        SpatialReference reference = layer.GetSpatialRef();
        if (reference == null)
            return null;

        String[] wkt = new String[1];
        reference.ExportToWkt(wkt);
        reference.delete();
        return CRS.parseWKT(wkt[0]);
    }

    /**
     * Turns a wkbGeometryType into the best corresponding geometry class we can figure out
     * 
     * @param wkbGeometryType
     * @return
     */
    private Class getGeometryClass(int wkbGeometryType) {
        if (wkbGeometryType < 0)
            throw new IllegalArgumentException("Can't have a negative type");
        int mask25d = 0x80000000;
        int unmaskedType = wkbGeometryType & ~mask25d;
        if (wkbGeometryType >= OGR_GEOM_TYPES.length) {
            LOGGER.warning("Could not recognize geometry type " + wkbGeometryType
                    + ". Assuming it's a new type and handling as a string");
            return Geometry.class;
        }
        return OGR_GEOM_TYPES[unmaskedType];
    }

    /**
     * Returns the OGR geometry type constant given a geometry attribute type
     * 
     * @param type
     * @return
     * @throws IOException
     */
    private int getOGRGeometryType(GeometryDescriptor type) throws IOException {
        Class<?> binding = type.getType().getBinding();
        for (int i = 0; i < OGR_GEOM_TYPES.length; i++) {
            if (binding.equals(OGR_GEOM_TYPES[i]))
                return i;
        }
        throw new IOException("Could not map " + binding + " to an OGR geometry type");
    }
    
}
