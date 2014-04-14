/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0;

import static org.geotools.data.wfs.internal.HttpMethod.GET;
import static org.geotools.data.wfs.internal.Loggers.debug;
import static org.geotools.data.wfs.internal.Loggers.trace;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import net.opengis.fes20.AbstractQueryExpressionType;
import net.opengis.fes20.FilterCapabilitiesType;
import net.opengis.fes20.GeometryOperandType;
import net.opengis.fes20.ResourceIdentifierType;
import net.opengis.fes20.SpatialCapabilitiesType;
import net.opengis.fes20.SpatialOperatorType;
import net.opengis.ows11.DCPType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.RequestMethodType;
import net.opengis.wfs20.DescribeFeatureTypeType;
import net.opengis.wfs20.DescribeStoredQueriesType;
import net.opengis.wfs20.FeatureTypeListType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.GetFeatureType;
import net.opengis.wfs20.ListStoredQueriesType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.ResultTypeType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.StoredQueryType;
import net.opengis.wfs20.WFSCapabilitiesType;
import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wfs.impl.WFSServiceInfo;
import org.geotools.data.wfs.internal.AbstractWFSStrategy;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.FeatureTypeInfo;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureRequest.ResultType;
import org.geotools.data.wfs.internal.DescribeStoredQueriesRequest;
import org.geotools.data.wfs.internal.HttpMethod;
import org.geotools.data.wfs.internal.ListStoredQueriesRequest;
import org.geotools.data.wfs.internal.TransactionRequest;
import org.geotools.data.wfs.internal.Versions;
import org.geotools.data.wfs.internal.WFSGetCapabilities;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.factory.Hints;
import org.geotools.filter.capability.FilterCapabilitiesImpl;
import org.geotools.filter.capability.IdCapabilitiesImpl;
import org.geotools.filter.capability.SpatialCapabiltiesImpl;
import org.geotools.filter.capability.SpatialOperatorImpl;
import org.geotools.filter.capability.SpatialOperatorsImpl;
import org.geotools.filter.spatial.ReprojectingFilterVisitor;
import org.geotools.filter.v2_0.FES;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Version;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.Configuration;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 */
public class StrictWFS_2_0_Strategy extends AbstractWFSStrategy {

    private static final List<String> PREFERRED_FORMATS = Collections.unmodifiableList(Arrays
            .asList("text/xml; subtype=gml/3.2", "application/gml+xml; version=3.2", "gml32",
                    "text/xml; subtype=gml/3.1.1", "gml3", "text/xml; subtype=gml/2.1.2", "GML2"));

    private net.opengis.wfs20.WFSCapabilitiesType capabilities;

    private final Map<QName, FeatureTypeType> typeInfos;

	private final QName XSI_String = new QName("http://www.w3.org/2001/XMLSchema-instance", "string");
    
    public StrictWFS_2_0_Strategy() {
        super();
        typeInfos = new HashMap<QName, FeatureTypeType>();
    }

    /*---------------------------------------------------------------------
     * AbstractWFSStrategy methods
     * ---------------------------------------------------------------------*/
    @Override
    public Configuration getFilterConfiguration() {
        return FILTER_2_0_CONFIGURATION;
    }

    @Override
    public Configuration getWfsConfiguration() {
        return WFS_2_0_CONFIGURATION;
    }

    @Override
    protected QName getOperationName(WFSOperationType operation) {
        return new QName(WFS.NAMESPACE, operation.getName());
    }

    /*---------------------------------------------------------------------
     * WFSStrategy methods
     * ---------------------------------------------------------------------*/

    @Override
    public void setCapabilities(WFSGetCapabilities capabilities) {
        net.opengis.wfs20.WFSCapabilitiesType caps = (WFSCapabilitiesType) capabilities
                .getParsedCapabilities();
        this.capabilities = caps;

        typeInfos.clear();
        FeatureTypeListType featureTypeList = this.capabilities.getFeatureTypeList();

        @SuppressWarnings("unchecked")
        List<FeatureTypeType> featureTypes = featureTypeList.getFeatureType();

        for (FeatureTypeType typeInfo : featureTypes) {
            QName name = typeInfo.getName();
            typeInfos.put(name, typeInfo);
        }
    }
    
    @Override
    public WFSServiceInfo getServiceInfo() {
        URL getCapsUrl = getOperationURL(WFSOperationType.GET_CAPABILITIES, GET);
        return new Capabilities200ServiceInfo("http://schemas.opengis.net/wfs/2.0/wfs.xsd",
                getCapsUrl, capabilities);
    }

    @Override
    public boolean supports(ResultType resultType) {
        switch (resultType) {
        case RESULTS:
        case HITS:
            return true;
        default:
            return false;
        }
    }

    @Override
    public Version getServiceVersion() {
        return Versions.v2_0_0;
    }

    @Override
    public String getDefaultOutputFormat(WFSOperationType operation) {
        switch (operation) {
        case GET_FEATURE:
            // As per Table 12 in 09-25r1 OGC Web Feature Service WFS 2.0
            return "application/gml+xml; version=3.2";

        case DESCRIBE_FEATURETYPE:
        case DESCRIBE_STORED_QUERIES:
        case LIST_STORED_QUERIES:
        	// Format makes no sense for DescribeFeatureType, DescribeStoredQueries or ListStoredQueries
        	return null;
        
        default:
            throw new UnsupportedOperationException("No outputFormat set up for "+operation);
        }
    }

    private String findExact(List<String> preferredFormats, Set<String> serverFormats) {
        for (String preferred : preferredFormats) {
            for (String serverFormat : serverFormats) {
                if (serverFormat.equalsIgnoreCase(preferred)) {
                    return preferred;
                }
            }
        }
        return null;
    }

    private String findFuzzy(List<String> preferredFormats, Set<String> serverFormats) {
        for (String preferred : preferredFormats) {

            preferred = preferred.toLowerCase();

            for (String serverFormat : serverFormats) {
                if (serverFormat.toLowerCase().startsWith(preferred)) {
                    return preferred;
                }
            }
        }
        return null;
    }

    /**
     * @see WFSStrategy#getFeatureTypeNames()
     */
    @Override
    public Set<QName> getFeatureTypeNames() {
        return new HashSet<QName>(typeInfos.keySet());
    }

    /**
     * @see org.geotools.data.wfs.internal.WFSStrategy#getFeatureTypeInfo(javax.xml.namespace.QName)
     */
    @Override
    public FeatureTypeInfo getFeatureTypeInfo(QName typeName) {
        FeatureTypeType eType = typeInfos.get(typeName);
        if (null == eType) {
            throw new IllegalArgumentException("Type name not found: " + typeName);
        }
        return new FeatureTypeInfoImpl(eType);
    }
    
    @Override
    public FilterCapabilities getFilterCapabilities() {
        FilterCapabilitiesType filterCapabilities = capabilities.getFilterCapabilities();
        
        FilterCapabilitiesImpl ret = new FilterCapabilitiesImpl();
        ret.setId(createIdCapabilities(filterCapabilities));
        ret.setSpatial(createSpatialCapabilities(filterCapabilities));
        
        // TODO: scalar, arithmetic

        return ret;
    }

	private SpatialCapabilities createSpatialCapabilities(FilterCapabilitiesType filterCapabilities) {
		SpatialCapabilitiesType sct = filterCapabilities.getSpatialCapabilities();
		
		SpatialCapabiltiesImpl spatialCapabilities = new SpatialCapabiltiesImpl();
        
		// Geometry operands
        
        
        List<GeometryOperandType> geometryOperandTypes = null;
        
        if (sct != null && sct.getGeometryOperands() != null) {
        	geometryOperandTypes = sct.getGeometryOperands().getGeometryOperand();
        }
        
        if (geometryOperandTypes == null) {
        	geometryOperandTypes = Collections.emptyList();
        }
        
        List<GeometryOperand> geometryOperands = convertGeometryOperands(geometryOperandTypes);
        
        spatialCapabilities.setGeometryOperands(geometryOperands);
        
        
        // Spatial operators
        SpatialOperatorsImpl spatialOperators = new SpatialOperatorsImpl();
        spatialCapabilities.setSpatialOperators(spatialOperators);
        
        Collection<SpatialOperator> ops = new ArrayList<SpatialOperator>();
        
        List<SpatialOperatorType> spatialOperatorTypes = null;
        if (sct != null && sct.getSpatialOperators() != null) {
        	spatialOperatorTypes = sct.getSpatialOperators().getSpatialOperator();
        }
        
        if (spatialOperatorTypes == null) {
        	spatialOperatorTypes = Collections.emptyList();
        }
        
        for (SpatialOperatorType sot : spatialOperatorTypes) {
        	SpatialOperator op;
        	
        	String name = (String)sot.getName();
        	
        	if (sot.getGeometryOperands() != null && sot.getGeometryOperands().getGeometryOperand() != null) {
        		op = new SpatialOperatorImpl(name, convertGeometryOperands(sot.getGeometryOperands().getGeometryOperand()));
        	} else {
        		op = new SpatialOperatorImpl(name);
        	}

        	ops.add(op);
        }
        
        spatialOperators.setOperators(ops);
        
        
		return spatialCapabilities;
	}

	private List<GeometryOperand> convertGeometryOperands(
			List<GeometryOperandType> geometryOperandTypes) {
		List<GeometryOperand> geometryOperands = new ArrayList<GeometryOperand>();
        for (GeometryOperandType got : geometryOperandTypes) {
        	GeometryOperand op = GeometryOperand.get(got.getName().getNamespaceURI(), got.getName().getLocalPart());
        	geometryOperands.add(op);
        }
		return geometryOperands;
	}

	private IdCapabilities createIdCapabilities(FilterCapabilitiesType filterCapabilities) {
		IdCapabilitiesImpl idCapabilities = new IdCapabilitiesImpl();
        
		if (filterCapabilities.getIdCapabilities() != null) {
	        for (ResourceIdentifierType rit : filterCapabilities.getIdCapabilities().getResourceIdentifier()) {
	        	QName name = rit.getName();
	        	if (FES.ResourceId.equals(name)) {
	        		idCapabilities.setFID(true);
	        	} else if (name.getNamespaceURI().startsWith("http://www.opengis.net/cat/csw/") &&
	        			name.getLocalPart().equals("RecordId")) {
	        		// FES 2.0 is very unclear about this. See 09-026r1 FES 2.0 7.14.3
	        		idCapabilities.setEid(true);
	        	}
	        }
		}
		return idCapabilities;
	}

	@Override
	protected Map<String, String> buildGetFeatureParametersForGET(
			GetFeatureRequest query) {
		Map<String, String> kvp = null;
		if (query.isStoredQuery()) {
			StoredQueryDescriptionType desc = query.getStoredQueryDescriptionType();
			String storedQueryId = desc.getId();

			kvp = new HashMap<String, String>();

			kvp.put("SERVICE", "WFS");
			kvp.put("VERSION", getVersion());
			kvp.put("REQUEST", "GetFeature");
			kvp.put("STOREDQUERY_ID", storedQueryId);

			Filter originalFilter = query.getFilter();

			

			query.setUnsupportedFilter(originalFilter);
			ParameterType bboxParam = extractStoredQueryFilterBounds(originalFilter, desc);

			if (bboxParam != null) {
				kvp.put(bboxParam.getName(), bboxParam.getValue());
			}


	        if (query.getHints() != null) {
	        	@SuppressWarnings("unchecked")
				Map<String, String> viewParams = (Map<String, String>)query.getHints().get(Hints.VIRTUAL_TABLE_PARAMETERS);
	        	
	        	if (viewParams != null) {
		        	for (ParameterExpressionType p : desc.getParameter()) {
		        		String value = viewParams.get(p.getName());
		        		if (value == null) continue;
		        		
		        		kvp.put(p.getName(), value);
		        	}
	        	}
	        }

		} else {
			kvp = super.buildGetFeatureParametersForGET(query);

			// Very crude
			if (query.getMaxFeatures() != null) {
				String count = kvp.remove("MAXFEATURES");
				kvp.put("COUNT", count);
			}
		}

		return kvp;
	}

    @Override
    protected EObject createDescribeFeatureTypeRequestPost(DescribeFeatureTypeRequest request) {
        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        DescribeFeatureTypeType dft = factory.createDescribeFeatureTypeType();

        Version version = getServiceVersion();
        dft.setService("WFS");
        dft.setVersion(version.toString());
        dft.setHandle(request.getHandle());

        QName typeName = request.getTypeName();
        @SuppressWarnings("unchecked")
        List<QName> typeNames = dft.getTypeName();
        typeNames.add(typeName);

        return dft;
    }

    @Override
    protected EObject createGetFeatureRequestPost(GetFeatureRequest query) throws IOException {
    	final QName typeName = query.getTypeName();
        final FeatureTypeInfoImpl featureTypeInfo = (FeatureTypeInfoImpl)getFeatureTypeInfo(typeName);

        final Wfs20Factory factory = Wfs20Factory.eINSTANCE;

        GetFeatureType getFeature = factory.createGetFeatureType();
        getFeature.setService("WFS");
        getFeature.setVersion(getVersion());

        String outputFormat = query.getOutputFormat();
        getFeature.setOutputFormat(outputFormat);

        getFeature.setHandle(query.getHandle());

        Integer maxFeatures = query.getMaxFeatures();
        if (maxFeatures != null) {
            getFeature.setCount(BigInteger.valueOf(maxFeatures.intValue()));
        }

        ResultType resultType = query.getResultType();
        getFeature.setResultType(ResultType.RESULTS == resultType ? ResultTypeType.RESULTS
                : ResultTypeType.HITS);


        AbstractQueryExpressionType abstractQuery;
        
       
        if (query.isStoredQuery()) {
			StoredQueryDescriptionType desc = query.getStoredQueryDescriptionType();
			String storedQueryId = desc.getId();

	        StoredQueryType storedQuery = factory.createStoredQueryType();
	        storedQuery.setId(storedQueryId);
	        
	        // The query filter must be processed locally
	        query.setUnsupportedFilter(query.getFilter());
	        ParameterType bboxParam = extractStoredQueryFilterBounds(query.getFilter(), desc);

	        // If there is a recognized bbox stored query parameter, add it
	        if (bboxParam != null) {
            	storedQuery.getParameter().add(bboxParam);
            }

	        if (query.getHints() != null) {
	        	@SuppressWarnings("unchecked")
				Map<String, String> viewParams = (Map<String, String>)query.getHints().get(Hints.VIRTUAL_TABLE_PARAMETERS);
	        	
	        	if (viewParams != null) {
		        	
		        	for (ParameterExpressionType p : desc.getParameter()) {
		        		String value = viewParams.get(p.getName());
		        		if (value == null) continue;
		        		
		        		ParameterType param = factory.createParameterType();
		        		param.setName(p.getName());
		        		param.setValue(value);
		        		storedQuery.getParameter().add(param);
		        	}
	        	}
	        }
	        
	        abstractQuery = storedQuery;
	        
        } else {
	        QueryType wfsQuery = factory.createQueryType();
	        wfsQuery.getTypeNames().add(typeName);
	
	        // Lifted from 1.0 / 1.1
	        final Filter supportedFilter;
	        final Filter unsupportedFilter;
	        {
	            final Filter filter = query.getFilter();
	            Filter[] splitFilters = splitFilters(typeName, filter);
	            supportedFilter = splitFilters[0];
	            unsupportedFilter = splitFilters[1];
	        }
	
	        query.setUnsupportedFilter(unsupportedFilter);
	
	        if (!Filter.INCLUDE.equals(supportedFilter)) {
	            wfsQuery.setFilter(supportedFilter);
	        }
	
	        String srsName = query.getSrsName();
	        if (null == srsName) {
	            srsName = featureTypeInfo.getDefaultSRS();
	        }
	        try {
	            wfsQuery.setSrsName(new URI(srsName));
	        } catch (URISyntaxException e) {
	            throw new RuntimeException("Can't create a URI from the query CRS: " + srsName, e);
	        }
	
	        String[] propertyNames = query.getPropertyNames();
	        boolean retrieveAllProperties = propertyNames == null;
	        if (!retrieveAllProperties) {
	            List<QName> propertyName = wfsQuery.getPropertyNames();
	            for (String propName : propertyNames) {
	            	// These get encoded into <fes:AbstractProjectionClause/> elements. Something's missing
	                propertyName.add(new QName(featureTypeInfo.getQName().getNamespaceURI(), propName));
	            }
	        }
	        
	        
	        /*
	         * System.err.println("SortBy is not yet implemented in StrictWFS_2_0_Strategy");
	        SortBy[] sortByList = query.getSortBy();
	        if (sortByList != null) {
	            for (SortBy sortBy : sortByList) {
	                wfsQuery.getSortBy().add(sortBy);
	            }
	        }
	        */
        	abstractQuery = wfsQuery;
        }
        getFeature.getAbstractQueryExpression().add(abstractQuery);
        
        return getFeature;
    }

    /**
     * Searches for a bounding box parameter in the stored query parameter list. If one is found
     * and there is an spatial envelope for the filter, the envelope bounds are filled into
     * the stored query parameter.
     * 
	 * TODO: should use layer metadata to figure out the correct parameter, currently 
	 * it just uses naive heuristics (the name must match 'bbox', ignoring case).
	 * 
	 * @param filter
	 * @param desc
	 * @return
     */
	private ParameterType extractStoredQueryFilterBounds(Filter filter,
			StoredQueryDescriptionType desc) {

		final Wfs20Factory factory = Wfs20Factory.eINSTANCE;
		ParameterType param = null;
		
		ParameterExpressionType bboxParameter = null;

		// A more generic way to configure this would be nice. BBOX is such an usual suspect
		// that this should be fine though.
		for (ParameterExpressionType pet : desc.getParameter()) {
			if (pet.getName().equalsIgnoreCase("bbox")) {
				if (pet.getType() == null || XSI_String.equals(pet.getType())) {
					bboxParameter = pet;
				}
				break;
			}
		}
		
		boolean flip = true;
		// But if there is a bbox parameter, we can use that!
		if (bboxParameter != null) {
		    Envelope bbox = new ReferencedEnvelope();
		    bbox = (Envelope) filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
		    
		    if (bbox != null && !bbox.isNull()) {

				param = factory.createParameterType();
				StringBuffer sb = new StringBuffer();
				if (!flip) {
					sb.append(bbox.getMinX());
					sb.append(',');
					sb.append(bbox.getMinY());
					sb.append(',');
					sb.append(bbox.getMaxX());
					sb.append(',');
					sb.append(bbox.getMaxY());
				} else {
					sb.append(bbox.getMinY());
					sb.append(',');
					sb.append(bbox.getMinX());
					sb.append(',');
					sb.append(bbox.getMaxY());
					sb.append(',');
					sb.append(bbox.getMaxX());
				}
				param.setName(bboxParameter.getName());
				param.setValue(sb.toString());
		    }
		}
		return param;
	}

    @Override
    protected EObject createListStoredQueriesRequestPost(
    		ListStoredQueriesRequest request) throws IOException {
    	final Wfs20Factory factory = Wfs20Factory.eINSTANCE;
    	
    	ListStoredQueriesType ret = factory.createListStoredQueriesType();

    	return ret;
    }
    
    @Override
    protected EObject createDescribeStoredQueriesRequestPost(
    		DescribeStoredQueriesRequest request) throws IOException {
    	final Wfs20Factory factory = Wfs20Factory.eINSTANCE;
    	
    	DescribeStoredQueriesType ret = factory.createDescribeStoredQueriesType();
    	
    	ret.getStoredQueryId().addAll(request.getStoredQueryIds());
    	
    	return ret;
    }
    
    @Override
    protected EObject createTransactionRequest(TransactionRequest request) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected String getOperationURI(WFSOperationType operation, HttpMethod method) {
        OperationsMetadataType omt = this.capabilities.getOperationsMetadata();
        omt.getOperation();

        trace("Looking operation URI for ", operation, "/", method);

        List<OperationType> operations = capabilities.getOperationsMetadata().getOperation();
        for (OperationType op : operations) {
            if (!operation.getName().equals(op.getName())) {
                continue;
            }
            List<DCPType> dcpTypes = op.getDCP();
            if (null == dcpTypes) {
                continue;
            }
            for (DCPType d : dcpTypes) {
                List<RequestMethodType> methods;
                if (HttpMethod.GET.equals(method)) {
                    methods = d.getHTTP().getGet();
                } else {
                    methods = d.getHTTP().getPost();
                }
                if (null == methods || methods.isEmpty()) {
                    continue;
                }

                String href = methods.get(0).getHref();
                debug("Returning operation URI for ", operation, "/", method, ": ", href);
                return href;
            }
        }

        debug("No operation URI found for ", operation, "/", method);
        return null;
    }

    @Override
    public Set<String> getServerSupportedOutputFormats(WFSOperationType operation) {
    	OperationsMetadataType omt = this.capabilities.getOperationsMetadata();
    	omt.getOperation();
    	
    	trace("Looking suppoerted output formats for ", operation);

        List<OperationType> operations = capabilities.getOperationsMetadata().getOperation();
        for (OperationType op : operations) {
            if (!operation.getName().equals(op.getName())) {
                continue;
            }
            
            for (Object o : op.getParameter()) {
            	System.out.println(o);
            }
            
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getServerSupportedOutputFormats(QName typeName, WFSOperationType operation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getClientSupportedOutputFormats(WFSOperationType operation) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean supportsTransaction(QName typeName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Set<String> getSupportedCRSIdentifiers(QName typeName) {
        // TODO Auto-generated method stub
        return null;
    }
}
