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
package org.geotools.caching.grid.spatialindex;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.geotools.caching.spatialindex.Data;
import org.geotools.caching.spatialindex.Shape;
import org.geotools.caching.util.SimpleFeatureMarshaller;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;


/** 
 * Associates data with its shape to be stored in the index.
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 *
 * @source $URL$
 */
public class GridData implements Data<Object>, Externalizable {
    private static final long serialVersionUID = 2435341100521921266L;
    private static SimpleFeatureMarshaller marshaller = new SimpleFeatureMarshaller();
    
    private Shape shape;
    private Object data;		//generally a feature
    private volatile int hashCode = 0;       //cached hashcode for performance 

    public GridData() {
    }

    public GridData(Shape shape, Object data) {
        this.shape = shape;
        this.data = data;
        this.hashCode = 0;
    }
    
    public Object getData() {
        return data;
    }

    public Shape getShape() {
        return shape;
    }
    
    public int hashCode() {
        if (hashCode == 0){
            //int hash = 17;
            //  hash = (37 * hash) + shape.hashCode();
            int hash = 629 + shape.hashCode();	//629 = 17 * 37
            hash = (37 * hash) + data.hashCode();
            this.hashCode = hash;
        }
        return hashCode;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof GridData) {
            return shape.equals(((GridData) o).shape) && data.equals(((GridData) o).data);
        } else {
            return false;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    	try {
    		this.shape = (Shape) in.readObject();
    	} catch (IOException e) {
    		throw e;
    	}

        if (in.readBoolean()) {
            try {
                this.data = getFeatureMarshaller().unmarshall(in);
            } catch ( IllegalAttributeException e) {
                throw (IOException) new IOException().initCause(e);
            } catch (IOException e) {
            	throw e;
            }catch (Exception e){
                throw (IOException) new IOException().initCause(e);
            }
        } else {
        	try {
        		this.data = in.readObject();
        	} catch (IOException e) {
        		throw e;
        	}
        }
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(shape);

        if (data instanceof SimpleFeature) {
            out.writeBoolean(true);
            try{
                getFeatureMarshaller().marshall((SimpleFeature) data, out);
            }catch (Exception ex){
                throw (IOException)new IOException().initCause(ex);
            }
        } else {
            out.writeBoolean(false);
            out.writeObject(data);
        }
    }
    
    public static SimpleFeatureMarshaller getFeatureMarshaller(){
        return GridData.marshaller;
    }
}
