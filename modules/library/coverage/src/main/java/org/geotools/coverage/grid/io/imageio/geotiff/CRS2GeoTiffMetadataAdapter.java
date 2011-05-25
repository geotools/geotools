/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio.geotiff;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffCoordinateTransformationsCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffPCSCodes;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffUoMCodes;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.operation.projection.AlbersEqualArea;
import org.geotools.referencing.operation.projection.LambertAzimuthalEqualArea;
import org.geotools.referencing.operation.projection.LambertConformal;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.Mercator;
import org.geotools.referencing.operation.projection.ObliqueMercator;
import org.geotools.referencing.operation.projection.ObliqueStereographic;
import org.geotools.referencing.operation.projection.Orthographic;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.geotools.referencing.operation.projection.Stereographic;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;

/**
 * This class implements a simple reusable adapter to adapt a
 * {@link CoordinateReferenceSystem} into useful Geotiff metadata by mean of
 * {@link GeoTiffIIOMetadataEncoder}.
 * 
 * <p>
 * Since {@link CoordinateReferenceSystem} are essentially immutable this class
 * implements a static pool of {@link CRS2GeoTiffMetadataAdapter} objects that
 * would allow to avoid parsing the same {@link CoordinateReferenceSystem} more
 * than once.
 * 
 * @author Simone Giannecchini
 * 
 * @since 2.2
 * 
 *
 * @source $URL$
 */
public final class CRS2GeoTiffMetadataAdapter {

	/**
	 * The {@link CoordinateReferenceSystem} to be get the metadata from:
	 */
	private CoordinateReferenceSystem crs;

	/**
	 * Constructs a parser using the default set of symbols and factories.
	 */
	public CRS2GeoTiffMetadataAdapter(final CoordinateReferenceSystem crs) {
		this.crs = crs;
	}

	/**
	 * Searches for an EPSG code inside this <code>IdentifiedObject</code>.
	 * 
	 * <p>
	 * It is important to remark that this function should be seen as an hack
	 * hence it may change in the near future. DO not rely on it!
	 * 
	 * @param obj
	 *            An <code>IdentifiedObject</code> to look for an EPSG code
	 *            into.
	 * @return An EPSG numeric code, if one is found, -1 otherwise.
	 */
	private static int getEPSGCode(final IdentifiedObject obj) {
		// looking for an EPSG code
		final Set<? extends Identifier> identifiers = obj.getIdentifiers();
		final Iterator<? extends Identifier> it = identifiers.iterator();
		String code = "";
		while (it.hasNext()) {
			final Identifier identifier = it.next();
			final Citation cite = identifier.getAuthority();
			if (Citations.identifierMatches(cite, "EPSG")) {
				code = identifier.getCode();
				break;
			}
		}

		try {
			return Integer.parseInt(code);

		} catch (Exception e) {

		}
		// an error occurred;
		return -1;
	}

	/**
	 * Parses a coordinate reference system.
	 * 
	 * <p>
	 * For the moment we can only encode geographic and projected coordinate
	 * reference systems, we cannot encode the other types like vertical
	 * coordinate reference systems.
	 * 
	 * @throws GeoTiffException
	 */
	public GeoTiffIIOMetadataEncoder parseCoordinateReferenceSystem()
			throws GeoTiffException {
		final GeoTiffIIOMetadataEncoder metadata = new GeoTiffIIOMetadataEncoder();
		// /////////////////////////////////////////////////////////////////////
		//
		// CREATING METADATA AND SETTING BASE FIELDS FOR THEM
		//
		// /////////////////////////////////////////////////////////////////////
		// model type
		final int modelType = (crs instanceof ProjectedCRS) ? 1 : 2;

		// GTModelTypeGeoKey
		metadata
				.addGeoShortParam(GeoTiffConstants.GTModelTypeGeoKey, modelType);

		switch (modelType) {
		// /////////////////////////////////////////////////////////////////////
		//
		// GEOGRAPHIC COORDINATE REFERENCE SYSTEMCREATING METADATA AND SETTING
		// BASE FIELDS FOR THEM
		//
		// /////////////////////////////////////////////////////////////////////
		case GeoTiffGCSCodes.ModelTypeGeographic:
			parseGeoGCS((DefaultGeographicCRS) crs, metadata);
			break;

		// /////////////////////////////////////////////////////////////////////
		//
		// PROJECTED COORDINATE REFERENCE SYSTEMCREATING METADATA AND SETTING
		// BASE FIELDS FOR THEM
		//
		// /////////////////////////////////////////////////////////////////////
		case GeoTiffPCSCodes.ModelTypeProjected:
			parseProjCRS((ProjectedCRS) crs, metadata);
			break;

		default:
			throw new GeoTiffException(
					null,
					"The supplied grid coverage uses an unsupported crs! You are allowed to use only projected and geographic coordinate reference systems",
					null);
		}
		return metadata;

		//
		// if ("VERT_CS".equals(keyword)) {
		// parseVertCS(element);
		// }
		//
		// if ("LOCAL_CS".equals(keyword)) {
		// parseLocalCS(element);
		// }
		//
		// if ("COMPD_CS".equals(keyword)) {
		// parseCompdCS(element);
		// }
		//
		// if ("FITTED_CS".equals(keyword)) {
		// parseFittedCS(element);
		// }

	}

	/**
	 * Parses a "PROJCS" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * PROJCS["<name>", <geographic cs>, <projection>, {<parameter>,}*,
	 *        <linear unit> {,<twin axes>}{,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param projectedCRS
	 *            The parent element.
	 * @param metadata
	 * @return The "PROJCS" element as a {@link ProjectedCRS} object.
	 * @throws ParseException
	 *             if the "GEOGCS" element can't be parsed.
	 */
	private void parseProjCRS(final ProjectedCRS projectedCRS,
			GeoTiffIIOMetadataEncoder metadata) {

		// do we have a code for this pcrs
		final int code = getEPSGCode(projectedCRS);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code)) {
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjectedCSTypeGeoKey,
					code);
			return;
		}

		// user defined projected coordinate reference system.
		// key 3072
		metadata.addGeoShortParam(GeoTiffPCSCodes.ProjectedCSTypeGeoKey, 32767);

		// name of the user defined projected crs
		// key 3073
//		metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, projectedCRS
//				.getName().getCode());

		// projection
		parseProjection(projectedCRS, metadata);

		// geographic crs
		parseGeoGCS((DefaultGeographicCRS) (projectedCRS.getBaseCRS()),
				metadata);

	}

	/**
	 * Parsing ProjectionGeoKey 3074 for a <code>ProjectedCRS</code>.
	 * 
	 * @param projectedCRS
	 *            The <code>ProjectedCRS</code> to parse.
	 * @param metadata
	 */
	private void parseProjection(final ProjectedCRS projectedCRS,
			final GeoTiffIIOMetadataEncoder metadata) {
		// getting the conversion
		final Conversion conversion = projectedCRS.getConversionFromBase();
		final int code = getEPSGCode(conversion);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code)) {
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjectionGeoKey, code);
			return;
		}

		// user defined projection
		// key 3074
		final String conversionName = conversion.getName().getCode();
		metadata.addGeoShortParam(GeoTiffPCSCodes.ProjectionGeoKey, 32767);
		metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, conversionName);

		final OperationMethod method = conversion.getMethod();
		// looking for the parameters
		String name = method.getName().getCode();
		name = name.trim();
		name = name.replace(' ', '_');
		final MathTransform mt = conversion.getMathTransform();
		final MapProjection projTransf;
		if (!(mt instanceof ConcatenatedTransform))
			projTransf = (MapProjection) mt;
		else {
			final ConcatenatedTransform tr = (ConcatenatedTransform) mt;
			final MathTransform m1 = tr.transform1, m2 = tr.transform2;
			if (m1 instanceof MapProjection)
				projTransf = (MapProjection) m1;
			else
				projTransf = (MapProjection) m2;
		}

		// key 3075 and parameters
		parseCoordinateProjectionTransform(projTransf, name, metadata);

		// parse linear unit
		parseLinearUnit(projectedCRS, metadata);

	}

	/**
	 * Parses a linear unit for a <code>ProjectedCRS</code>.
	 * 
	 * @todo complete the list of linear unit of measures and clean the
	 *       exception
	 * @param projectedCRS
	 * @param metadata
	 */
	private void parseLinearUnit(final ProjectedCRS projectedCRS,
			GeoTiffIIOMetadataEncoder metadata) {

		// getting the linear unit
		final Unit linearUnit = CRSUtilities.getUnit(projectedCRS
				.getCoordinateSystem());
		if (linearUnit != null && !SI.METER.isCompatible(linearUnit)) {
			throw new IllegalArgumentException(Errors.format(
					ErrorKeys.NON_LINEAR_UNIT_$1, linearUnit));
		}
		if (SI.METER.isCompatible(linearUnit)) {
			if (SI.METER.equals(linearUnit)) {
				metadata.addGeoShortParam(
						GeoTiffPCSCodes.ProjLinearUnitsGeoKey,
						GeoTiffUoMCodes.Linear_Meter);
				metadata.addGeoDoubleParam(
						GeoTiffPCSCodes.ProjLinearUnitSizeGeoKey, 1.0);
			}
			if (NonSI.NAUTICAL_MILE.equals(linearUnit)) {
				metadata.addGeoShortParam(
						GeoTiffPCSCodes.ProjLinearUnitsGeoKey,
						GeoTiffUoMCodes.Linear_Mile_International_Nautical);
				metadata.addGeoDoubleParam(
						GeoTiffPCSCodes.ProjLinearUnitSizeGeoKey, linearUnit
								.getConverterTo(SI.METER).convert(1));
			}
			if (NonSI.FOOT.equals(linearUnit)) {
				metadata.addGeoShortParam(
						GeoTiffPCSCodes.ProjLinearUnitsGeoKey,
						GeoTiffUoMCodes.Linear_Foot);
				metadata.addGeoDoubleParam(
						GeoTiffPCSCodes.ProjLinearUnitSizeGeoKey, linearUnit
								.getConverterTo(SI.METER).convert(1));
			}
			if (NonSI.YARD.equals(linearUnit)) {
				metadata.addGeoShortParam(
						GeoTiffPCSCodes.ProjLinearUnitsGeoKey,
						GeoTiffUoMCodes.Linear_Yard_Sears);// ??
				metadata.addGeoDoubleParam(
						GeoTiffPCSCodes.ProjLinearUnitSizeGeoKey, linearUnit
								.getConverterTo(SI.METER).convert(1));
			}
		}
	}

	/**
	 * Parses a along with coordinate transformation and its parameters.
	 * 
	 * @param name
	 * @param metadata
	 * 
	 * @param conversion
	 * @throws GeoTiffException
	 */
	private void parseCoordinateProjectionTransform(
			final MapProjection projTransf, final String name,
			GeoTiffIIOMetadataEncoder metadata) {

		final ParameterValueGroup parameters = projTransf.getParameterValues();

		// /////////////////////////////////////////////////////////////////////
		//
		// Transverse Mercator
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof TransverseMercator
				&& name.equalsIgnoreCase("transverse_mercator")) {
			// key 3075
			metadata
					.addGeoShortParam(
							GeoTiffPCSCodes.ProjCoordTransGeoKey,
							GeoTiffCoordinateTransformationsCodes.CT_TransverseMercator);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
					parameters.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(
					GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, parameters
							.parameter("scale_factor").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// Mercator_1SP
		// Mercator_2SP
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof Mercator
				&& (name.equalsIgnoreCase("mercator_1SP") || name
						.equalsIgnoreCase("Mercator_2SP"))) {
			// key 3075
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
					GeoTiffCoordinateTransformationsCodes.CT_Mercator);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			List<GeneralParameterValue> values = parameters.values();
                        for (GeneralParameterValue value : values){
                            if (value instanceof ParameterValue){
                                ParameterValue paramValue = (ParameterValue) value;
                                if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "latitude_of_origin")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjNatOriginLatGeoKey,(paramValue).doubleValue());
                                }else if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "central_meridian")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjNatOriginLongGeoKey,(paramValue).doubleValue());
                                }else if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "scale_factor")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey,(paramValue).doubleValue());
                                }else if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "standard_parallel_1")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjStdParallel1GeoKey,(paramValue).doubleValue());
                                }else if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "false_easting")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjFalseEastingGeoKey,(paramValue).doubleValue());
                                }else if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), "false_northing")) {
                                    metadata.addGeoDoubleParam(
                                            GeoTiffPCSCodes.ProjFalseNorthingGeoKey,(paramValue).doubleValue());
                                }
                            }
                        }
                        
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// Lamber conformal 1sp
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof LambertConformal && name.indexOf("1") != -1) {

			// key 3075
			metadata
					.addGeoShortParam(
							GeoTiffPCSCodes.ProjCoordTransGeoKey,
							GeoTiffCoordinateTransformationsCodes.CT_LambertConfConic_Helmert);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
					parameters.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(
					GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, parameters
							.parameter("scale_factor").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;

		}

		// /////////////////////////////////////////////////////////////////////
		//
		// LAMBERT_CONFORMAL_CONIC_2SP
		// lambert_conformal_conic_2SP_Belgium
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof LambertConformal && name.indexOf("2") != -1) {
			// key 3075
			metadata
					.addGeoShortParam(
							GeoTiffPCSCodes.ProjCoordTransGeoKey,
							GeoTiffCoordinateTransformationsCodes.CT_LambertConfConic_2SP);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
					parameters.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjStdParallel1GeoKey,
					parameters.parameter("standard_parallel_1").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjStdParallel2GeoKey,
					parameters.parameter("standard_parallel_2").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;

		}

		// if (name.equalsIgnoreCase("equidistant_conic")
		// || code == GeoTiffMetadata2CRSAdapter.CT_EquidistantConic) {
		// parameters = mtFactory
		// .getDefaultParameters("equidistant_conic");
		// parameters.parameter("central_meridian").setValue(
		// getOriginLong());
		// parameters.parameter("latitude_of_origin").setValue(
		// getOriginLat());
		// parameters
		// .parameter("standard_parallel_1")
		// .setValue(
		// this
		// .getGeoKeyAsDouble(GeoTiffIIOMetadataDecoder.ProjStdParallel1GeoKey));
		// parameters
		// .parameter("standard_parallel_2")
		// .setValue(
		// this
		// .getGeoKeyAsDouble(GeoTiffIIOMetadataDecoder.ProjStdParallel2GeoKey));
		// parameters.parameter("false_easting").setValue(
		// getFalseEasting());
		// parameters.parameter("false_northing").setValue(
		// getFalseNorthing());
		//
		// return parameters;
		// }

		// /////////////////////////////////////////////////////////////////////
		//
		// stereographic
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof Stereographic
				&& name.equalsIgnoreCase("stereographic")) {

			// key 3075
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
					GeoTiffCoordinateTransformationsCodes.CT_Stereographic);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
					parameters.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(
					GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, parameters
							.parameter("scale_factor").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;

		}

		// /////////////////////////////////////////////////////////////////////
		//
		// polar_stereographic
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof PolarStereographic
				&& name.equalsIgnoreCase("polar_stereographic")) {
			// key 3075
			metadata
					.addGeoShortParam(
							GeoTiffPCSCodes.ProjCoordTransGeoKey,
							GeoTiffCoordinateTransformationsCodes.CT_PolarStereographic);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(
					GeoTiffPCSCodes.ProjStraightVertPoleLongGeoKey, parameters
							.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(
					GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, parameters
							.parameter("scale_factor").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;

		}
		
		// /////////////////////////////////////////////////////////////////////
		//
		// Oblique_Stereographic
		//
		// /////////////////////////////////////////////////////////////////////
		if(projTransf instanceof ObliqueStereographic 
				&& name.equalsIgnoreCase("Oblique_Stereographic")) {
 			metadata.addGeoShortParam(
 					GeoTiffPCSCodes.ProjCoordTransGeoKey,
 					GeoTiffCoordinateTransformationsCodes.CT_ObliqueStereographic);
// 			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);
 			
 			// params			
 			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
 					parameters.parameter("central_meridian").doubleValue());
 			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
 					parameters.parameter("latitude_of_origin").doubleValue());
 			metadata.addGeoDoubleParam(
 					GeoTiffPCSCodes.ProjScaleAtNatOriginGeoKey, parameters
 							.parameter("scale_factor").doubleValue());
 			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
 					parameters.parameter("false_easting").doubleValue());
 			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
 					parameters.parameter("false_northing").doubleValue());
 			return;
 		}
				 		
		// /////////////////////////////////////////////////////////////////////
		//
		// Oblique Mercator
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof ObliqueMercator
				&& (name.equalsIgnoreCase("oblique_mercator") || name
						.equalsIgnoreCase("hotine_oblique_mercator"))) {

			// key 3075
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
					GeoTiffCoordinateTransformationsCodes.CT_ObliqueMercator);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLongGeoKey,
					parameters.parameter("longitude_of_center").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLatGeoKey,
					parameters.parameter("latitude_of_center").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjScaleAtCenterGeoKey,
					parameters.parameter("scale_factor").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjAzimuthAngleGeoKey,
					parameters.parameter("azimuth").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			// rectified grid angle???
			return;

		}
		// /////////////////////////////////////////////////////////////////////
		//
		// albers_Conic_Equal_Area
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof AlbersEqualArea
				&& name.equalsIgnoreCase("albers_Conic_Equal_Area")) {

			// key 3075
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
					GeoTiffCoordinateTransformationsCodes.CT_AlbersEqualArea);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLongGeoKey,
					parameters.parameter("longitude_of_center").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjNatOriginLatGeoKey,
					parameters.parameter("latitude_of_center").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjStdParallel1GeoKey,
					parameters.parameter("standard_parallel_1").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjStdParallel2GeoKey,
					parameters.parameter("standard_parallel_2").doubleValue());
			// rectified grid angle???
			return;

		}

		// /////////////////////////////////////////////////////////////////////
		//
		// Orthographic
		//
		// /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof Orthographic
				&& name.equalsIgnoreCase("Orthographic")) {

			// key 3075
			metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
					GeoTiffCoordinateTransformationsCodes.CT_Orthographic);
//			metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);

			// params
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLongGeoKey,
					parameters.parameter("central_meridian").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLongGeoKey,
					parameters.parameter("latitude_of_origin").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
					parameters.parameter("false_easting").doubleValue());
			metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
					parameters.parameter("false_northing").doubleValue());
			return;
		}
		
		// /////////////////////////////////////////////////////////////////////
                //
                // Lambert Azimuthal Equal Area
                //
                // /////////////////////////////////////////////////////////////////////
		if (projTransf instanceof LambertAzimuthalEqualArea
                        && name.equalsIgnoreCase("Lambert_Azimuthal_Equal_Area")) {

                // key 3075
                    metadata.addGeoShortParam(GeoTiffPCSCodes.ProjCoordTransGeoKey,
                                    GeoTiffCoordinateTransformationsCodes.CT_LambertAzimEqualArea);
//                    metadata.addGeoAscii(GeoTiffPCSCodes.PCSCitationGeoKey, name);
    
                    // params
                    metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLatGeoKey,
                                    parameters.parameter("latitude_of_center").doubleValue());
                    metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjCenterLongGeoKey,
                                    parameters.parameter("longitude_of_center").doubleValue());
                    metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseEastingGeoKey,
                                    parameters.parameter("false_easting").doubleValue());
                    metadata.addGeoDoubleParam(GeoTiffPCSCodes.ProjFalseNorthingGeoKey,
                                    parameters.parameter("false_northing").doubleValue());
                    return;
                }
		
		// throw new
		// GeoTiffException(null,"CRS2GeoTiffMetadataAdapter::parseCoordinateTransform::unknown
		// projection transform");

	}

	/**
	 * Parses a "GEOGCS" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * GEOGCS["<name>", <datum>, <prime meridian>, <angular unit>  {,<twin axes>} {,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param geographicCRS
	 *            The parent element.
	 * @param metadata
	 * @return The "GEOGCS" element as a {@link GeographicCRS} object.
	 */
	private void parseGeoGCS(DefaultGeographicCRS geographicCRS,
			GeoTiffIIOMetadataEncoder metadata) {

		// is it one of the EPSG standard GCS?
		final int code = getEPSGCode(geographicCRS);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code)) {
			metadata.addGeoShortParam(GeoTiffGCSCodes.GeographicTypeGeoKey,
					code);
			return;
		}

		// /////////////////////////////////////////////////////////////////////
		//
		// User defined CRS
		//
		// /////////////////////////////////////////////////////////////////////
		// user defined geographic coordinate reference system.
		metadata.addGeoShortParam(GeoTiffGCSCodes.GeographicTypeGeoKey, 32767);

		// get the name of the gcs which will become a citation for the user
		// define crs
		metadata.addGeoAscii(GeoTiffGCSCodes.GeogCitationGeoKey, geographicCRS
				.getName().getCode());

		// geodetic datum
		final DefaultGeodeticDatum datum = (DefaultGeodeticDatum) geographicCRS
				.getDatum();
		parseDatum(datum, metadata);

		// angular unit
		final Unit angularUnit = ((EllipsoidalCS) geographicCRS
				.getCoordinateSystem()).getAxis(0).getUnit();
		parseUnit(angularUnit, 0, metadata);

		// prime meridian
		parsePrimem((DefaultPrimeMeridian) datum.getPrimeMeridian(), metadata);

		// linear unit
//		final Unit linearUnit = datum.getEllipsoid().getAxisUnit();
//		parseUnit(linearUnit, 1, metadata);

	}

	/**
	 * Parses a "DATUM" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * DATUM["<name>", <spheroid> {,<to wgs84>} {,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param datum
	 *            The parent element.
	 * @param metadata
	 * @param meridian
	 *            the prime meridian.
	 * @return The "DATUM" element as a {@link GeodeticDatum} object.
	 */
	private void parseDatum(final DefaultGeodeticDatum datum,
			GeoTiffIIOMetadataEncoder metadata) {

		// looking for an EPSG code
		final int code = getEPSGCode(datum);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code)) {
			metadata.addGeoShortParam(GeoTiffGCSCodes.GeogGeodeticDatumGeoKey,
					code);
			return;
		}

		/**
		 * user defined datum
		 */
		// set the datum as user defined
		metadata.addGeoShortParam(GeoTiffGCSCodes.GeogGeodeticDatumGeoKey,
				32767);

		// set the name
//		metadata.addGeoAscii(GeoTiffGCSCodes.GeogCitationGeoKey, datum
//				.getName().getCode());

		parseSpheroid((DefaultEllipsoid) datum.getEllipsoid(), metadata);

	}

	/**
	 * Parses a "SPHEROID" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * SPHEROID["<name>", <semi-major axis>, <inverse flattening> {,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param metadata
	 * 
	 * @param parent
	 *            The parent element.
	 * @return The "SPHEROID" element as an {@link Ellipsoid} object.
	 */
	private void parseSpheroid(final DefaultEllipsoid ellipsoid,
			GeoTiffIIOMetadataEncoder metadata) {

		final int code = getEPSGCode(ellipsoid);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code)) {

			metadata
					.addGeoShortParam(GeoTiffGCSCodes.GeogEllipsoidGeoKey, code);
			return;
		}

		// user defined ellipsoid
		metadata.addGeoShortParam(GeoTiffGCSCodes.GeogEllipsoidGeoKey, 32767);
		// setting the name
//		metadata.addGeoAscii(GeoTiffGCSCodes.GeogCitationGeoKey, ellipsoid
//				.getName().getCode());

		// setting semimajor axis
		metadata.addGeoDoubleParam(GeoTiffGCSCodes.GeogSemiMajorAxisGeoKey,
				ellipsoid.getSemiMajorAxis());

		// setting inverse flattening
		metadata.addGeoDoubleParam(GeoTiffGCSCodes.GeogInvFlatteningGeoKey,
				ellipsoid.getInverseFlattening());

	}

	/**
	 * Parses a "PRIMEM" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * PRIMEM["<name>", <longitude> {,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param metadata
	 * 
	 * @param parent
	 *            The parent element.
	 * @param angularUnit
	 *            The contextual unit.
	 * @return The "PRIMEM" element as a {@link PrimeMeridian} object.
	 */
	private void parsePrimem(final DefaultPrimeMeridian pm,
			GeoTiffIIOMetadataEncoder metadata) {
		// looking for an EPSG code
		final int code = getEPSGCode(pm);
		if (GeoTiffIIOMetadataEncoder.isTiffUShort(code))
			metadata.addGeoShortParam(GeoTiffGCSCodes.GeogPrimeMeridianGeoKey,
					code);
		else {
			// user defined
			metadata.addGeoShortParam(GeoTiffGCSCodes.GeogPrimeMeridianGeoKey,
					32767);

			// citation
//			metadata.addGeoAscii(GeoTiffGCSCodes.GeogCitationGeoKey, pm
//					.getName().getCode());

			// longitude
			metadata.addGeoDoubleParam(
					GeoTiffGCSCodes.GeogPrimeMeridianLongGeoKey, pm
							.getGreenwichLongitude());
		}

	}

	/**
	 * Parses an "UNIT" element. This element has the following pattern:
	 * 
	 * <blockquote><code>
	 * UNIT["<name>", <conversion factor> {,<authority>}]
	 * </code></blockquote>
	 * 
	 * @param unit
	 *            The parent element.
	 * @param unit
	 *            The contextual unit. Usually {@link SI#METRE} or
	 *            {@link SI#RADIAN}.
	 * @param metadata
	 * @return The "UNIT" element as an {@link Unit} object.
	 * @todo Authority code is currently ignored. We may consider to create a
	 *       subclass of {@link Unit} which implements {@link IdentifiedObject}
	 *       in a future version.
	 */
	private void parseUnit(Unit unit, int model,
			GeoTiffIIOMetadataEncoder metadata) {

		// final UnitFormat unitFormat = UnitFormat.getStandardInstance();
		// user defined
		metadata.addGeoShortParam(
				model == 0 ? GeoTiffGCSCodes.GeogAngularUnitsGeoKey
						: GeoTiffPCSCodes.ProjLinearUnitsGeoKey, 32767);

		// preparing the string to write here

		// citation
//		metadata.addGeoAscii(GeoTiffGCSCodes.GeogCitationGeoKey, unit
//				.toString());// unitFormat.labelFor(unit)

		Unit base = null;
		if (SI.METER.isCompatible(unit)) {
			base = SI.METER;
		} else if (SI.SECOND.isCompatible(unit)) {
			base = SI.SECOND;
		} else if (SI.RADIAN.isCompatible(unit)) {
			if (!Unit.ONE.equals(unit)) {
				base = SI.RADIAN;
			}
		}
		if (base != null) {
			metadata.addGeoDoubleParam(
					model == 0 ? GeoTiffGCSCodes.GeogAngularUnitSizeGeoKey
							: GeoTiffGCSCodes.GeogLinearUnitSizeGeoKey, unit
							.getConverterTo(base).convert(1));
		} else
			metadata.addGeoDoubleParam(
					model == 0 ? GeoTiffGCSCodes.GeogAngularUnitSizeGeoKey
							: GeoTiffGCSCodes.GeogLinearUnitSizeGeoKey, 1);
	}

}
