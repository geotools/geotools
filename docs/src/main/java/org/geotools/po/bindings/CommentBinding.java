/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.po.bindings;

import javax.xml.namespace.QName;
import org.geotools.po.ObjectFactory;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

public class CommentBinding extends AbstractSimpleBinding {
    ObjectFactory factory;

    public CommentBinding(ObjectFactory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return PO.comment;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return String.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        String comment = (String) value;
        return comment;
    }
}
