package org.geotools.data.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.parsers.XmlComplexFeatureParser;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.feature.collection.ComplexFeatureIteratorImpl;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * 
 * @author Adam Brown (Curtin University of Technology)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 */

public class WFSContentComplexFeatureCollection extends BaseFeatureCollection<FeatureType, Feature> {
    private static final Logger LOGGER = Logging
            .getLogger(WFSContentComplexFeatureCollection.class);

    private FeatureType schema;

    private GetFeatureRequest request;

    private QName name;

    private Filter filter;

    public WFSContentComplexFeatureCollection(GetFeatureRequest request, FeatureType schema,
            QName name) throws IOException {

        this.request = request;
        this.name = name;
        this.schema = schema;
    }

    public WFSContentComplexFeatureCollection(GetFeatureRequest request, FeatureType schema,
            QName name, Filter filter) throws IOException {

        this.request = request;
        this.name = name;
        this.schema = schema;
        this.filter = filter;
    }

    @Override
    public FeatureIterator<Feature> features() {

        try {
            InputStream stream = request.getFinalURL().openStream();

            XmlComplexFeatureParser parser = new XmlComplexFeatureParser(stream, schema, name,
                    filter);
            return new ComplexFeatureIteratorImpl(parser);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public FeatureType getSchema() {
        return schema;
    }

    @Override
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {

        try {
            return new WFSContentComplexFeatureCollection(request, schema, name, filter);
        } catch (IOException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
            return null;
        }
    }
}
