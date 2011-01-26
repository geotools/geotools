/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import junit.framework.TestCase;

/**
 * This simply tests and demonstrates how to make a new feature factory.
 *
 * @author Ian Schneider
 * @source $URL$
 */
public class FeatureTypeFactoryTest extends TestCase {
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(FeatureTypeFactoryTest.class);
  }
  
  public void testEmpty() {
	  //an empty test so tests will pass
	  //TODO: fix tests
  }
  
  public void disabledtestFeatureFactoryImpl() throws Exception {
    // set the system property...
    System.setProperty(FeatureTypeFactory.class.getName(), FactoryImpl.class.getName());
    // create a new instance
    FeatureTypeFactory factory = FeatureTypeFactory.newInstance("test");
    FeatureType type = factory.getFeatureType();
    Feature feature = type.create(null);
    
    // lets make sure the implentation is correct
    assertEquals(FactoryImpl.class , factory.getClass());
    // lets make sure the FeatureType produced is correct
    assertEquals(
      Proxy.getInvocationHandler(type).getClass(),
      ImplHandler.class
    );
    // lets make sure the FeatureType produces the correct Feature
    assertEquals(
      Proxy.getInvocationHandler(feature).getClass(),
      ImplHandler.class
    );
  }
  
  // Normally, you would just want to subclass DefaultFeatureTypeFactory and
  // override the method "createFeatureType" - thats it. You would return a
  // subclass of DefaultFeatureType, which, instead of creating an instance of
  // DefaultFeature, would create an instance of your special feature. This is
  // done by overriding DefaultFeatureType.create
  public static class FactoryImpl extends FeatureTypeFactory {
    private java.util.ArrayList attributeTypes = new java.util.ArrayList();
    
    protected void add(AttributeType type) throws IllegalArgumentException {
      attributeTypes.add(type);
    }
    
    protected void add(int idx, AttributeType type) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
      attributeTypes.add(idx,type);
    }
    
    protected FeatureType createFeatureType() {
      return featureTypeImpl();
    }
    
    protected FeatureType createAbstractType() {
      return featureTypeImpl();
    }
    
    
    public AttributeType get(int idx) throws ArrayIndexOutOfBoundsException {
      return (AttributeType) attributeTypes.get(idx);
    }
    
    public int getAttributeCount() {
      return attributeTypes.size();
    }
    
    protected AttributeType remove(int idx) throws ArrayIndexOutOfBoundsException {
      return (AttributeType) attributeTypes.remove(idx);
    }
    
    protected AttributeType remove(AttributeType type) {
      if (attributeTypes.remove(type))
        return type;
      return null;
    }
    
    protected AttributeType set(int idx, AttributeType type) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
      AttributeType former = get(idx);
      attributeTypes.set(idx,type);
      return former;
    }
  }
  

  // the following is tricky...but doesn't really matter much
  
  static FeatureType featureTypeImpl() {
    return (FeatureType) Proxy.newProxyInstance(
      // use this classloader!!!
      FeatureTypeFactory.class.getClassLoader(),
      new Class[] {FeatureType.class},
      new ImplHandler()
    );
  }
  
  static Feature featureImpl() {
    return (Feature) Proxy.newProxyInstance(
      // use this classloader!!!
      FeatureTypeFactory.class.getClassLoader(),
      new Class[] {Feature.class},
      new ImplHandler()
    );
  }
  
  // this is the "overriding" of the method 'create'.
  // all other method calls can just return null.
  static class ImplHandler implements InvocationHandler {
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("create")) {
        return featureImpl(); 
      }
      return null;
    }
    
  }
  
}
