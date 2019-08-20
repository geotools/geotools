package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.WFSConfig.PreferredHttpMethod;
import org.geotools.ows.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

/** @author Matthias Schulze - Landesamt für Digitalisierung, Breitband und Vermessung */
public class WFSRequestTest {

  @Before
  public void setUp() throws Exception {
    Loggers.RESPONSES.setLevel(Level.FINEST);
  }

  /** Test method for {@link org.geotools.data.wfs.internal.WFSRequest#getFinalURL()}. */
  @Test
  public void testGetFinalURLPost() throws IOException, ServiceException {
    WFSConfig config = new WFSConfig();
    config.preferredMethod = PreferredHttpMethod.HTTP_POST;
    WFSClient client = newClient("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", config);
    // WFSClient client = newClient("XtraServer/GetCapabilities.xml", config);
    WFSRequest request = client.createDescribeFeatureTypeRequest();
    URL url = request.getFinalURL();
    String query = url.getQuery();
    if (query != null && query.contains("=")) {
      Assert.fail("POST-Request should not contain Query String in URL");
    }
  }

  /** Test method for {@link org.geotools.data.wfs.internal.WFSRequest#getFinalURL()}. */
  // @Test
  // public void testGetFinalURLGet() throws IOException, ServiceException {
  // WFSConfig config = new WFSConfig();
  // config.preferredMethod = PreferredHttpMethod.HTTP_GET;
  // WFSClient client = newClient("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", config);
  // WFSRequest request = client.createDescribeFeatureTypeRequest();
  // URL url = request.getFinalURL();
  // String query = url.getQuery();
  // if (query == null || !query.contains("=")) {
  // Assert.fail("GET-Request should contain Query String in URL");
  // }
  // }

  /*
   * final QName typeName = request.getTypeName();
   *
   * Statement does not return name, but null value! This leads to following error:
   *
   * java.lang.IllegalArgumentException: Type name not found: null at
   * org.geotools.data.wfs.internal.v2_0.StrictWFS_2_0_Strategy.getFeatureTypeInfo(StrictWFS_2_0_Strategy.java:204) at
   * org.geotools.data.wfs.internal.AbstractWFSStrategy.buildDescribeFeatureTypeParametersForGET(AbstractWFSStrategy.
   * java:437) at org.geotools.data.wfs.internal.AbstractWFSStrategy.buildUrlGET(AbstractWFSStrategy.java:694) at
   * org.geotools.data.wfs.internal.WFSRequest.getFinalURL(WFSRequest.java:162) at
   * org.geotools.data.wfs.internal.WFSRequestTest.testGetFinalURLGet(WFSRequestTest.java:63) at
   * sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) at
   * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) at
   * sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) at
   * java.lang.reflect.Method.invoke(Method.java:498) at
   * org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50) at
   * org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12) at
   * org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47) at
   * org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17) at
   * org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26) at
   * org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325) at
   * org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78) at
   * org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57) at
   * org.junit.runners.ParentRunner$3.run(ParentRunner.java:290) at
   * org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71) at
   * org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288) at
   * org.junit.runners.ParentRunner.access$000(ParentRunner.java:58) at
   * org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268) at
   * org.junit.runners.ParentRunner.run(ParentRunner.java:363) at
   * org.eclipse.jdt.internal.junit4.runner.JUnit4TestReference.run(JUnit4TestReference.java:89) at
   * org.eclipse.jdt.internal.junit.runner.TestExecution.run(TestExecution.java:41) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:541) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:763) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:463) at
   * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:209)
   */

  private WFSClient newClient(String resource, WFSConfig config)
      throws IOException, ServiceException {
    URL capabilitiesURL = WFSTestData.url(resource);
    HTTPClient httpClient = new SimpleHttpClient();

    WFSClient client = new WFSClient(capabilitiesURL, httpClient, config);
    return client;
  }
}
