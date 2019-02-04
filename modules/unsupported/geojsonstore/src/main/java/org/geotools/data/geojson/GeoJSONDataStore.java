package org.geotools.data.geojson;
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class GeoJSONDataStore extends org.geotools.data.store.ContentDataStore
    implements FileDataStore {
  URL url;

  SimpleFeatureType schema;

  protected Name typeName;

  public GeoJSONDataStore(URL url) throws IOException {
    this.url = url;
  }

  @Override
  protected List<Name> createTypeNames() throws IOException {

    String name = FilenameUtils.getBaseName(url.toExternalForm());
    // could hard code features in here?
    typeName = new NameImpl(name);

    return Collections.singletonList(typeName);
  }

  GeoJSONReader read() throws IOException {
    GeoJSONReader reader = null;

    reader = new GeoJSONReader(url);

    return reader;
  }

  @Override
  protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
    // We can only really write to local files
    String scheme = url.getProtocol();
    String host = url.getHost();
    if ("file".equalsIgnoreCase(scheme) && (host == null || host.isEmpty())) {
      GeoJSONFeatureStore geoJSONFeatureStore = new GeoJSONFeatureStore(entry, Query.ALL);
      return geoJSONFeatureStore;
    } else {
      GeoJSONFeatureSource geoJSONFeatureSource = new GeoJSONFeatureSource(entry, Query.ALL);
      return geoJSONFeatureSource;
    }
  }

  @Override
  public void createSchema(SimpleFeatureType featureType) throws IOException {
    this.schema = featureType;
  }

  @Override
  public SimpleFeatureType getSchema() throws IOException {
    // TODO Auto-generated method stub
    return this.schema;
  }

  @Override
  public void updateSchema(SimpleFeatureType featureType) throws IOException {
    this.schema = featureType;
  }

  @Override
  public SimpleFeatureSource getFeatureSource() throws IOException {
    // TODO Auto-generated method stub
    return new GeoJSONFeatureSource(new ContentEntry(this, typeName), Query.ALL);
  }

  @Override
  public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader() throws IOException {
    // TODO Auto-generated method stub
    return new GeoJSONFeatureReader(new ContentState(new ContentEntry(this, typeName)), Query.ALL);
  }

  @Override
  public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
      Filter filter, Transaction transaction) throws IOException {

    return super.getFeatureWriter(getTypeName(), filter, transaction);
  }

  /** @return */
  private String getTypeName() {
    return typeName.getLocalPart();
  }

  @Override
  public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(Transaction transaction)
      throws IOException {
    return super.getFeatureWriter(getTypeName(), transaction);
  }

  @Override
  public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(
      Transaction transaction) throws IOException {
    return super.getFeatureWriterAppend(getTypeName(), transaction);
  }

  /** @return the url */
  protected URL getUrl() {
    return url;
  }

  /** @param url the url to set */
  protected void setUrl(URL url) {
    this.url = url;
  }
}
