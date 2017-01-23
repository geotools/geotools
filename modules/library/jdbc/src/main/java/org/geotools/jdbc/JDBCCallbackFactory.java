/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 */
package org.geotools.jdbc;

/**
 * JDBC callback factory. 
 * <p>
 *   This interface is used to "inject" callbacks into the various JDBC classes. 
 * </p>
 */
public interface JDBCCallbackFactory {

  /**
   * Null callback factory. 
   */
  JDBCCallbackFactory NULL = new JDBCCallbackFactory() {
    @Override
    public String getName() {
      return "null";
    }

    @Override
    public JDBCReaderCallback createReaderCallback() {
      return JDBCReaderCallback.NULL;
    }
  };

  /**
   * Callback factory name.
   */
  String getName();

  /**
   * Creates a callback for {@link JDBCFeatureReader}.
   */
  JDBCReaderCallback createReaderCallback();
}
