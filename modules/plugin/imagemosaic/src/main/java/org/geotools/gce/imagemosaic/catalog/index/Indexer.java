/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="schemas" type="{}schemasType" minOccurs="0"/>
 *         &lt;element name="domains" type="{}domainsType" minOccurs="0"/>
 *         &lt;element name="coverages" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="coverage" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="schema" type="{}schemaType"/>
 *                             &lt;element name="origName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="domains" type="{}domainsType"/>
 *                             &lt;choice>
 *                               &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                               &lt;element name="nameCollector" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;/choice>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="datastore">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence minOccurs="0">
 *                   &lt;element name="database" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="parameters" type="{}parametersType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="collectors" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="collector" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="spi" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="mapped" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="parameters" type="{}parametersType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {
            "schemas",
            "domains",
            "coverages",
            "multipleBandsDimensions",
            "datastore",
            "collectors",
            "parameters"
        })
@XmlRootElement(name = "Indexer")
public class Indexer {

    protected SchemasType schemas;

    protected DomainsType domains;

    protected Indexer.Coverages coverages;

    protected Indexer.MultipleBandsDimensions multipleBandsDimensions;

    protected Indexer.Datastore datastore;

    protected Indexer.Collectors collectors;

    protected ParametersType parameters;

    @XmlTransient
    private File indexerFile;

    /**
     * Gets the value of the schemas property.
     *
     * @return possible object is {@link SchemasType }
     */
    public SchemasType getSchemas() {
        return schemas;
    }

    /**
     * Sets the value of the schemas property.
     *
     * @param value allowed object is {@link SchemasType }
     */
    public void setSchemas(SchemasType value) {
        this.schemas = value;
    }

    /**
     * Gets the value of the domains property.
     *
     * @return possible object is {@link DomainsType }
     */
    public DomainsType getDomains() {
        return domains;
    }

    /**
     * Sets the value of the domains property.
     *
     * @param value allowed object is {@link DomainsType }
     */
    public void setDomains(DomainsType value) {
        this.domains = value;
    }

    /**
     * Gets the value of the coverages property.
     *
     * @return possible object is {@link Indexer.Coverages }
     */
    public Indexer.Coverages getCoverages() {
        return coverages;
    }

    /**
     * Sets the value of the coverages property.
     *
     * @param value allowed object is {@link Indexer.Coverages }
     */
    public void setCoverages(Indexer.Coverages value) {
        this.coverages = value;
    }

    /**
     * Gets the value of the multipleBandsDimensions property.
     *
     * @return possible object is {@link Indexer.MultipleBandsDimensions }
     */
    public Indexer.MultipleBandsDimensions getMultipleBandsDimensions() {
        return multipleBandsDimensions;
    }

    /**
     * Sets the value of the multipleBandsDimensions property.
     *
     * @param value allowed object is {@link Indexer.MultipleBandsDimensions }
     */
    public void setMultipleBandsDimensions(Indexer.MultipleBandsDimensions value) {
        this.multipleBandsDimensions = value;
    }

    /**
     * Gets the value of the datastore property.
     *
     * @return possible object is {@link Indexer.Datastore }
     */
    public Indexer.Datastore getDatastore() {
        return datastore;
    }

    /**
     * Sets the value of the datastore property.
     *
     * @param value allowed object is {@link Indexer.Datastore }
     */
    public void setDatastore(Indexer.Datastore value) {
        this.datastore = value;
    }

    /**
     * Gets the value of the collectors property.
     *
     * @return possible object is {@link Indexer.Collectors }
     */
    public Indexer.Collectors getCollectors() {
        return collectors;
    }

    /**
     * Sets the value of the collectors property.
     *
     * @param value allowed object is {@link Indexer.Collectors }
     */
    public void setCollectors(Indexer.Collectors value) {
        this.collectors = value;
    }

    /**
     * Gets the value of the parameters property.
     *
     * @return possible object is {@link ParametersType }
     */
    public ParametersType getParameters() {
        return parameters;
    }

    /**
     * Sets the value of the parameters property.
     *
     * @param value allowed object is {@link ParametersType }
     */
    public void setParameters(ParametersType value) {
        this.parameters = value;
    }

    /**
     * Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="collector" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="spi" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="mapped" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
            name = "",
            propOrder = {"collector"})
    public static class Collectors {

        protected List<Indexer.Collectors.Collector> collector;

        /**
         * Gets the value of the collector property.
         *
         * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the collector property.
         *
         * <p>For example, to add a new item, do as follows:
         *
         * <pre>
         * getCollector().add(newItem);
         * </pre>
         *
         * <p>Objects of the following type(s) are allowed in the list {@link Indexer.Collectors.Collector }
         */
        public List<Indexer.Collectors.Collector> getCollector() {
            if (collector == null) {
                collector = new ArrayList<>();
            }
            return this.collector;
        }

        /**
         * Java class for anonymous complex type.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="spi" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="mapped" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(
                name = "",
                propOrder = {"spi", "type", "value", "mapped"})
        public static class Collector {

            @XmlElement(required = true)
            protected String spi;

            @XmlElement(required = true)
            protected String type;

            @XmlElement(required = true)
            protected String value;

            @XmlElement(required = true)
            protected String mapped;

            @XmlAttribute(required = true)
            protected String name;

            /**
             * Gets the value of the spi property.
             *
             * @return possible object is {@link String }
             */
            public String getSpi() {
                return spi;
            }

            /**
             * Sets the value of the spi property.
             *
             * @param value allowed object is {@link String }
             */
            public void setSpi(String value) {
                this.spi = value;
            }

            /**
             * Gets the value of the type property.
             *
             * @return possible object is {@link String }
             */
            public String getType() {
                return type;
            }

            /**
             * Sets the value of the type property.
             *
             * @param value allowed object is {@link String }
             */
            public void setType(String value) {
                this.type = value;
            }

            /**
             * Gets the value of the value property.
             *
             * @return possible object is {@link String }
             */
            public String getValue() {
                return value;
            }

            /**
             * Sets the value of the value property.
             *
             * @param value allowed object is {@link String }
             */
            public void setValue(String value) {
                this.value = value;
            }

            /**
             * Gets the value of the mapped property.
             *
             * @return possible object is {@link String }
             */
            public String getMapped() {
                return mapped;
            }

            /**
             * Sets the value of the mapped property.
             *
             * @param value allowed object is {@link String }
             */
            public void setMapped(String value) {
                this.mapped = value;
            }

            /**
             * Gets the value of the name property.
             *
             * @return possible object is {@link String }
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value allowed object is {@link String }
             */
            public void setName(String value) {
                this.name = value;
            }
        }
    }

    /**
     * Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="coverage" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="schema" type="{}schemaType"/>
     *                   &lt;element name="origName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="domains" type="{}domainsType"/>
     *                   &lt;choice>
     *                     &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                     &lt;element name="nameCollector" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;/choice>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
            name = "",
            propOrder = {"coverage"})
    public static class Coverages {

        @XmlElement(required = true)
        protected List<Indexer.Coverages.Coverage> coverage;

        /**
         * Gets the value of the coverage property.
         *
         * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the coverage property.
         *
         * <p>For example, to add a new item, do as follows:
         *
         * <pre>
         * getCoverage().add(newItem);
         * </pre>
         *
         * <p>Objects of the following type(s) are allowed in the list {@link Indexer.Coverages.Coverage }
         */
        public List<Indexer.Coverages.Coverage> getCoverage() {
            if (coverage == null) {
                coverage = new ArrayList<>();
            }
            return this.coverage;
        }

        /**
         * Java class for anonymous complex type.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.
         *
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="schema" type="{}schemaType"/>
         *         &lt;element name="origName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="domains" type="{}domainsType"/>
         *         &lt;choice>
         *           &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *           &lt;element name="nameCollector" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;/choice>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(
                name = "",
                propOrder = {"schema", "origName", "domains", "name", "nameCollector", "parameters"})
        public static class Coverage {

            @XmlElement(required = true)
            protected SchemaType schema;

            @XmlElement(required = true)
            protected String origName;

            @XmlElement(required = true)
            protected DomainsType domains;

            protected String name;

            protected String nameCollector;

            protected ParametersType parameters;

            /**
             * Gets the value of the schema property.
             *
             * @return possible object is {@link SchemaType }
             */
            public SchemaType getSchema() {
                return schema;
            }

            /**
             * Sets the value of the schema property.
             *
             * @param value allowed object is {@link SchemaType }
             */
            public void setSchema(SchemaType value) {
                this.schema = value;
            }

            /**
             * Gets the value of the origName property.
             *
             * @return possible object is {@link String }
             */
            public String getOrigName() {
                return origName;
            }

            /**
             * Sets the value of the origName property.
             *
             * @param value allowed object is {@link String }
             */
            public void setOrigName(String value) {
                this.origName = value;
            }

            /**
             * Gets the value of the domains property.
             *
             * @return possible object is {@link DomainsType }
             */
            public DomainsType getDomains() {
                return domains;
            }

            /**
             * Sets the value of the domains property.
             *
             * @param value allowed object is {@link DomainsType }
             */
            public void setDomains(DomainsType value) {
                this.domains = value;
            }

            /**
             * Gets the value of the name property.
             *
             * @return possible object is {@link String }
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value allowed object is {@link String }
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the nameCollector property.
             *
             * @return possible object is {@link String }
             */
            public String getNameCollector() {
                return nameCollector;
            }

            /**
             * Sets the value of the nameCollector property.
             *
             * @param value allowed object is {@link String }
             */
            public void setNameCollector(String value) {
                this.nameCollector = value;
            }

            /** Returns coverage specific parameters. */
            public ParametersType getParameters() {
                return parameters;
            }

            /** Sets the coverage specific parameters */
            public void setParameters(ParametersType parameters) {
                this.parameters = parameters;
            }
        }
    }

    /**
     * Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence minOccurs="0">
     *         &lt;element name="database" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="parameters" type="{}parametersType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
            name = "",
            propOrder = {"database", "parameters"})
    public static class Datastore {

        protected String database;

        protected ParametersType parameters;

        /**
         * Gets the value of the database property.
         *
         * @return possible object is {@link String }
         */
        public String getDatabase() {
            return database;
        }

        /**
         * Sets the value of the database property.
         *
         * @param value allowed object is {@link String }
         */
        public void setDatabase(String value) {
            this.database = value;
        }

        /**
         * Gets the value of the parameters property.
         *
         * @return possible object is {@link ParametersType }
         */
        public ParametersType getParameters() {
            return parameters;
        }

        /**
         * Sets the value of the parameters property.
         *
         * @param value allowed object is {@link ParametersType }
         */
        public void setParameters(ParametersType value) {
            this.parameters = value;
        }
    }

    public File getIndexerFile() {
        return indexerFile;
    }

    public void setIndexerFile(File indexerFile) {
        this.indexerFile = indexerFile;
    }

    /**
     * Java class for anonymous complex type.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="multipleBandsDimension" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                   &lt;element name="bandsNames" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(
            name = "",
            propOrder = {"multipleBandsDimension"})
    public static class MultipleBandsDimensions {

        @XmlElement(required = true)
        protected List<Indexer.MultipleBandsDimensions.MultipleBandsDimension> multipleBandsDimension;

        /**
         * Gets the value of the multipleBandsDimension property.
         *
         * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the multipleBandsDimension property.
         *
         * <p>For example, to add a new item, do as follows:
         *
         * <pre>
         *    getMultipleBandsDimension().add(newItem);
         * </pre>
         *
         * <p>Objects of the following type(s) are allowed in the list
         * {@link Indexer.MultipleBandsDimensions.MultipleBandsDimension }
         */
        public List<Indexer.MultipleBandsDimensions.MultipleBandsDimension> getMultipleBandsDimension() {
            if (multipleBandsDimension == null) {
                multipleBandsDimension = new ArrayList<>();
            }
            return this.multipleBandsDimension;
        }

        /**
         * Java class for anonymous complex type.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *         &lt;element name="bandsNames" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(
                name = "",
                propOrder = {"name", "bandsNames"})
        public static class MultipleBandsDimension {

            @XmlElement(required = true)
            protected String name;

            @XmlElement(required = true)
            protected String bandsNames;

            /**
             * Gets the value of the name property.
             *
             * @return possible object is {@link String }
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             *
             * @param value allowed object is {@link String }
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the bandsNames property.
             *
             * @return possible object is {@link String }
             */
            public String getBandsNames() {
                return bandsNames;
            }

            /**
             * Sets the value of the bandsNames property.
             *
             * @param value allowed object is {@link String }
             */
            public void setBandsNames(String value) {
                this.bandsNames = value;
            }
        }
    }
}
