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
package org.geotools.imageio.metadata;

/**
 * An {@code <DefinedByConversion>} element in
 * {@linkplain ImageMetadata metadata format}.
 * <p>
 * The method (algorithm or procedure) used to perform the coordinate operation.
 * </p>
 * 
 * <p>
 * <strong>Coordinate Operation:</strong></br> If the relationship between any
 * two coordinate reference systems is known, coordinate tuples can be
 * transformed or converted to another coordinate reference system. The UML
 * model therefore specifies a source and a target coordinate reference system
 * for such coordinate operations.
 * </p>
 * 
 * <p>
 * <strong>Coordinate operation method and parameters:</strong></br> The
 * algorithm used to execute a coordinate operation is defined in the coordinate
 * operation method. Each coordinate operation method uses a number of
 * parameters (although some coordinate conversions use none), and each
 * coordinate operation assigns a value to these parameters.
 * <p>
 * </p>
 * Most parameter values are numeric, but for some coordinate operation methods,
 * notably those implementing a grid interpolation algorithm, the parameter
 * value could be a file name and location (this may be a URI). An example is
 * the NADCON coordinate transformation from NAD 27 to NAD 83 in the USA in
 * which one pair of a series of pairs of grid files is used.
 * <p>
 * </p>
 * It is recommended to make extensive use of identifiers, referencing
 * well-known registers wherever possible. There is as yet no standard way of
 * spelling or even naming the various coordinate operation methods. Client
 * software requesting a coordinate operation to be executed by a coordinate
 * transformation server implementation may therefore ask for a coordinate
 * operation method which this server does not recognize, although a perfectly
 * valid method using a different name may be available. The same holds for
 * coordinate operation parameters used by any coordinate operation method.
 * <p>
 * </p>
 * To facilitate recognition and validation it is recommended that the
 * coordinate operation method formulae be included or referenced in the
 * relevant object, if possible with a worked example.
 * </p>
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/coverage-core/src/main/java/org/geotools/imageio/metadata/DefinedByConversion.java $
 */
public class DefinedByConversion extends IdentifiableMetadataAccessor {

    /**
     * Parameter Values
     * <p>
     * A group of related parameter values. The same group can be repeated more
     * than once in a coordinate operation or higher level ParameterValueGroup,
     * if those instances contain different values of one or more
     * ParameterValues which suitably distinguish among those groups.
     * </p>
     */
    ChildList<ParameterValue> paramValues;
    
    /**
     * Creates a parser for an DefinedByConversion. This constructor should not
     * be invoked directly
     * 
     * @param parent
     *                The set of all OperationMethods.
     * @param index
     *                The DefinedByConversion index for this instance.
     */
    DefinedByConversion(final MetadataAccessor accessor) {
        super(accessor, SpatioTemporalMetadataFormat.MD_SCRS_DEFINED_BY_CONVERSION, SpatioTemporalMetadataFormat.MD_SCRS_DBC_PARAMETERS);
        paramValues = new ChildList.ParameterValues(this);
    }

    /**
     * Sets the {@link DefinedByConversion} for this Coordinate Operation.
     * 
     * @param identification
     *                {@link Identification}
     * @param formula
     *                {@link String} Formula(s) or procedure used by this
     *                operation method. This may be a reference to a
     *                publication. Note that the operation method may not be
     *                analytic, in which case this attribute references or
     *                contains the procedure, not an analytic formula.
     * @param srcDim
     *                {@link String} Number of dimensions in the source CRS of
     *                this operation method.
     * @param tasgetDim
     *                {@link String} Number of dimensions in the target CRS of
     *                this operation method.
     */
    public void setDefinedByConversion(final Identification identification,
            final String formula, final String srcDim, final String targetDim) {
        this.setIdentification(identification);
        this.setString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_FORMULA, formula);
        this.setString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_SRC_DIM, srcDim);
        this.setString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_TARGET_DIM, targetDim);
    }

    /**
     * Returns the {@link DefinedByConversion} formula.
     * 
     * @return formula {@link String}
     */
    public String getFormula() {
        return this.getString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_FORMULA);
    }

    /**
     * Returns the {@link DefinedByConversion} Source Dimensions.
     * 
     * @return formula {@link String}
     */
    public String getSrcDim() {
        return this.getString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_SRC_DIM);
    }

    /**
     * Returns the {@link DefinedByConversion} Target Dimentions.
     * 
     * @return formula {@link String}
     */
    public String getTargetDim() {
        return this.getString(SpatioTemporalMetadataFormat.MD_SCRS_DBC_TARGET_DIM);
    }

    /**
     * Adds a {@link ParameterValue} parameter value to this Coordinate
     * Operation.
     * 
     * @param identification
     *                {@link Identification} The Parameter identificator (name,
     *                alias, identifies, remarks)
     * @param value
     *                {@link String} The value to set.
     * 
     * @return parameterValue {@link ParameterValue}
     */
    public ParameterValue addParameterValue(final Identification identification, final String value) {
        ParameterValue candidate = this.paramValues.addChild();
        candidate.setIdentification(identification);
        candidate.setString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE, value);
        return candidate;
    }

    /**
     * ParameterValue Class
     * <p>
     * A parameter value, ordered sequence of values, or reference to a file of
     * parameter values.
     * </p>
     * 
     * <ul>
     * <li> Numeric value of the coordinate operation parameter with its
     * associated unit of measure. </li>
     * <li> String value of a coordinate operation parameter. A string value
     * does not have an associated unit of measure. </li>
     * <li> Positive integer value of a coordinate operation parameter, usually
     * used for a count. An integer value does not have an associated unit of
     * measure. </li>
     * <li> Boolean value of a coordinate operation parameter. A Boolean value
     * does not have an associated unit of measure. </li>
     * <li> Ordered collection, i.e. sequence, of two or more numeric values of
     * a coordinate operation parameter list, where each value has the same
     * associated unit of measure. </li>
     * <li> Ordered collection, i.e. sequence, of two or more integer values of
     * a coordinate operation parameter list, usually used for counts. These
     * integer values do not have an associated unit of measure. </li>
     * <li> Reference to a file or a part of a file containing one or more
     * parameter values. When referencing a part of a file, that file shall
     * contain multiple identified parts, such as an XML encoded document.
     * Furthermore, the referenced file or part of a file can reference another
     * part of the same or different files, as allowed in XML documents. </li>
     * </ul>
     * 
     * @author Daniele Romagnoli, GeoSolutions
     */
    public static final class ParameterValue extends IdentifiableMetadataAccessor {
        protected ParameterValue(final MetadataAccessor parent, final int index) {
            super(parent);
            selectChild(index);
        }

        /**
         * Creates a parser for an axis.
         * 
         * @param parent
         *                The set of all axis.
         * @param index
         *                The axis index for this instance.
         */
        ParameterValue(final ChildList<ParameterValue> parent, final int index) {
            super(parent);
            selectChild(index);
        }

        public double getValue() {
            return Double.parseDouble(getString(SpatioTemporalMetadataFormat.MD_COMM_ATTRIBUTEVALUE));
        }
    }

    /**
     * Returns the {@link ParameterValue} parameter values at index {@link int}
     * i.
     * 
     * @param index
     *                {@link int} Index of the Parameter group.
     * 
     * @return parameterValue {@link ParameterValue}
     */
    public ParameterValue getParameterValue(int index) {
        return paramValues.getChild(index);
    }
    
    /**
     * 
     */
    public int numParams() {
        return paramValues.childCount();
    }
}
