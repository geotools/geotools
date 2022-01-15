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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Java class for domainsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="domainsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="domain" type="{}domainType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "domainsType",
        propOrder = {"domain"})
public class DomainsType {

    protected List<DomainType> domain;

    /**
     * Gets the value of the domain property.
     *
     * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
     * modification you make to the returned list will be present inside the JAXB object. This is
     * why there is not a <CODE>set</CODE> method for the domain property.
     *
     * <p>For example, to add a new item, do as follows:
     *
     * <pre>
     * getDomain().add(newItem);
     * </pre>
     *
     * <p>Objects of the following type(s) are allowed in the list {@link DomainType }
     */
    public List<DomainType> getDomain() {
        if (domain == null) {
            domain = new ArrayList<>();
        }
        return this.domain;
    }
}
