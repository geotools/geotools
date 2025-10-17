/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.index;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * Java class for attributeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="attributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attribute" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="collectorRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "attributeType",
        propOrder = {"attribute"})
public class AttributeType {

    @XmlElement(required = true)
    protected String attribute;

    @XmlAttribute
    protected String collectorRef;

    /**
     * Gets the value of the attribute property.
     *
     * @return possible object is {@link String }
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     *
     * @param value allowed object is {@link String }
     */
    public void setAttribute(String value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the collectorRef property.
     *
     * @return possible object is {@link String }
     */
    public String getCollectorRef() {
        return collectorRef;
    }

    /**
     * Sets the value of the collectorRef property.
     *
     * @param value allowed object is {@link String }
     */
    public void setCollectorRef(String value) {
        this.collectorRef = value;
    }
}
