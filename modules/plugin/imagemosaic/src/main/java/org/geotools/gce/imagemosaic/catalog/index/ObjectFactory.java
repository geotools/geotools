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

import jakarta.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the
 * generated package.
 *
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content.
 * The Java representation of XML content can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory methods for each of these are provided in
 * this class.
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package:
     * generated
     */
    public ObjectFactory() {}

    /** Create an instance of {@link AttributeType } */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /** Create an instance of {@link Indexer.Coverages } */
    public Indexer.Coverages createIndexerCoverages() {
        return new Indexer.Coverages();
    }

    /** Create an instance of {@link SchemasType } */
    public SchemasType createSchemasType() {
        return new SchemasType();
    }

    /** Create an instance of {@link Indexer.Coverages.Coverage } */
    public Indexer.Coverages.Coverage createIndexerCoveragesCoverage() {
        return new Indexer.Coverages.Coverage();
    }

    /** Create an instance of {@link ParametersType } */
    public ParametersType createParametersType() {
        return new ParametersType();
    }

    /** Create an instance of {@link Indexer.Collectors.Collector } */
    public Indexer.Collectors.Collector createIndexerCollectorsCollector() {
        return new Indexer.Collectors.Collector();
    }

    /** Create an instance of {@link DomainType } */
    public DomainType createDomainType() {
        return new DomainType();
    }

    /** Create an instance of {@link ParametersType.Parameter } */
    public ParametersType.Parameter createParametersTypeParameter() {
        return new ParametersType.Parameter();
    }

    /** Create an instance of {@link Indexer.Collectors } */
    public Indexer.Collectors createIndexerCollectors() {
        return new Indexer.Collectors();
    }

    /** Create an instance of {@link Indexer } */
    public Indexer createIndexer() {
        return new Indexer();
    }

    /** Create an instance of {@link DomainsType } */
    public DomainsType createDomainsType() {
        return new DomainsType();
    }

    /** Create an instance of {@link SchemaType } */
    public SchemaType createSchemaType() {
        return new SchemaType();
    }

    /** Create an instance of {@link Indexer.Datastore } */
    public Indexer.Datastore createIndexerDatastore() {
        return new Indexer.Datastore();
    }
}
