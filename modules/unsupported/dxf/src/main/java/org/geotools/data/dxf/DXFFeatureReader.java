/*
 * $Id: DXFFeatureReader.java Matthijs $
 */
package org.geotools.data.dxf;

import org.geotools.data.dxf.parser.DXFParseException;
import com.vividsolutions.jts.geom.Geometry;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.ArrayList;
import java.net.URL;
import org.geotools.data.GeometryType;
import org.geotools.data.dxf.entities.DXFEntity;
import org.geotools.data.dxf.entities.DXFInsert;
import org.geotools.data.dxf.entities.DXFText;
import org.geotools.data.dxf.parser.DXFUnivers;
import org.geotools.data.dxf.parser.DXFLineNumberReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


import org.apache.commons.io.input.CountingInputStream;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Matthijs Laan, B3Partners
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/dxf/src/main/java/org/geotools/data/dxf/DXFFeatureReader.java $
 */
public class DXFFeatureReader implements FeatureReader {

    private static final Log log = LogFactory.getLog(DXFFeatureReader.class);
    private SimpleFeatureType ft;
    private Iterator<DXFEntity> entityIterator;
    private GeometryType geometryType = null;
    private SimpleFeature cache;
    private DXFUnivers theUnivers;
    private ArrayList dxfInsertsFilter;
    private int featureID = 0;

    public DXFFeatureReader(URL url, String typeName, String srs, GeometryType geometryType, ArrayList dxfInsertsFilter) throws IOException, DXFParseException {
        CountingInputStream cis = null;
        DXFLineNumberReader lnr = null;

        try {
            cis = new CountingInputStream(url.openStream());
            lnr = new DXFLineNumberReader(new InputStreamReader(cis));
            theUnivers = new DXFUnivers(dxfInsertsFilter);
            theUnivers.read(lnr);
        } catch (IOException ioe) {
            log.error("Error reading data in datastore: ", ioe);
            throw ioe;
        } finally {
            if (lnr != null) {
                lnr.close();
            }
            if (cis != null) {
                cis.close();
            }
        }

        // Set filter point, line, polygon, defined in datastore typenames
        updateTypeFilter(typeName, geometryType, srs);
    }

    public void updateTypeFilter(String typeName, GeometryType geometryType, String srs) {
        this.geometryType = geometryType;
        entityIterator = theUnivers.theEntities.iterator();

        try {
            createFeatureType(typeName, srs);
        } catch (DataSourceException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    private void createFeatureType(String typeName, String srs) throws DataSourceException {
        CoordinateReferenceSystem crs = null;
        try {
            crs = CRS.decode(srs);
        } catch (Exception e) {
            throw new DataSourceException("Error parsing CoordinateSystem srs: \"" + srs + "\"");
        }

        int SRID = -1;
        if (crs != null) {
            try {
                Set ident = crs.getIdentifiers();
                if ((ident != null && !ident.isEmpty())) {
                    String code = ((NamedIdentifier) ident.toArray()[0]).getCode();
                    SRID = Integer.parseInt(code);
                }
            } catch (Exception e) {
                log.error("SRID could not be determined from crs!");
            }
        }
        log.info("SRID used by SimpleFeature reader: " + SRID);


        try {

            SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
            ftb.setName(typeName);
            ftb.setSRS(srs);

            ftb.add("the_geom", Geometry.class);
            ftb.add("name", String.class);
            ftb.add("key", String.class);
            ftb.add("urlLink", String.class);
            ftb.add("lineType", String.class);
            ftb.add("color", String.class);
            ftb.add("layer", String.class);
            ftb.add("thickness", Double.class);
            ftb.add("rotation", Double.class);
            ftb.add("visible", Integer.class);
            ftb.add("entryLineNumber", Integer.class);
            ftb.add("parseError", Integer.class);
            ftb.add("error", String.class);

            ft = ftb.buildFeatureType();
        
        } catch (Exception e) {
            throw new DataSourceException("Error creating SimpleFeatureType: " + typeName, e);
        }
    }

    public SimpleFeatureType getFeatureType() {
        return ft;
    }

    public SimpleFeature next() throws IOException, IllegalAttributeException, NoSuchElementException {
        return cache;
    }

    public boolean hasNext() throws IOException {
        if (!entityIterator.hasNext()) {
            return false;
        } else {
            Geometry g = null;
            DXFEntity entry = null;
            try {
                entry = null;
                boolean passedFilter = false;
                do {
                    entry = (DXFEntity) entityIterator.next();
                    passedFilter = passedFilter(entry);
                } while (!passedFilter && entityIterator.hasNext());

                if (passedFilter) {
                    g = entry.getGeometry();

                    cache = SimpleFeatureBuilder.build(ft, new Object[]{
                                g,
                                entry.getName(),
                                entry.getKey(),
                                entry.getUrlLink(),
                                entry.getLineTypeName(),
                                entry.getColorRGB(),
                                entry.getRefLayerName(),
                                new Double(entry.getThickness()),
                                ((entry instanceof DXFText) ? new Double(((DXFText) entry)._rotation) : new Double(0.0)), // Text rotation
                                new Integer(entry.isVisible() ? 1 : 0),
                                new Integer(entry.getStartingLineNumber()),
                                new Integer(entry.isParseError() ? 1 : 0),
                                entry.getErrorDescription()
                            }, Integer.toString(featureID++));

                    return true;
                } else {
                    // No next features found
                    return false;
                }
            } catch (IllegalAttributeException ex) {
                log.error(ex.getLocalizedMessage() + "\n" + entry.getErrorDescription());
                return false;
            }
        }
    }

    /**
     * Check if geometry of entry is equal to filterType
     *
     * @param entry     SimpleFeature from iterator; entry to check it'serviceInfo geometryType from
     * @return          if entry.getType equals geometryType
     */
    private boolean passedFilter(DXFEntity entry) {
        // Entries who are null can never be wanted and will never pass the filter

        if (entry == null) {
            return false;
        } else {
            /**
             * Check if type of geometry is equal to geometryType of filter
             * If true, this entry should be added to the table
             */
            boolean isEqual = entry.getType().equals(geometryType) || (geometryType.equals(GeometryType.ALL) && !entry.getType().equals(GeometryType.UNSUPPORTED));

            try {
                // Filter invalid geometries
                if (!entry.getGeometry().isValid()) {
                    // Only display message for own SimpleFeatureType, otherwise it will be displayed for every typename
                    if (isEqual) {
                        log.info("Invalid " + entry.getType() + " found while parsing table");
                    }
                    return false;
                }

                // Skip entryErrors from Inserts
                if (entry.isParseError()) {
                    if (entry instanceof DXFInsert) {
                        return false;
                    }
                }

            } catch (Exception ex) {
                log.error("Skipping geometry; problem with " + entry.getName() + ": " + ex.getLocalizedMessage());
                return false;
            }

            return isEqual;
        }
    }

    public ServiceInfo getInfo() {
        DefaultServiceInfo serviceInfo = new DefaultServiceInfo();
        serviceInfo.setTitle("DXF FeatureReader");
        serviceInfo.setDescription(theUnivers == null ? "Univers is null" : theUnivers.getInfo());

        return serviceInfo;
    }

    public void close() throws IOException {
    }
}
