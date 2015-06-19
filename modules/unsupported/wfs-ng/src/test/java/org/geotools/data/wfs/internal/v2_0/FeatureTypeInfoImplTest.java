package org.geotools.data.wfs.internal.v2_0;

import junit.framework.Assert;
import net.opengis.wfs20.AbstractType;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Factory;
import net.opengis.wfs20.Wfs20Package;
import net.opengis.wfs20.impl.FeatureTypeTypeImpl;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.geotools.data.wfs.internal.v2_0.storedquery.ParameterMappingDefaultValue;
import org.geotools.data.wfs.internal.v2_0.storedquery.ParameterMappingExpressionValue;
import org.geotools.data.wfs.internal.v2_0.storedquery.ParameterTypeFactory;
import org.geotools.data.wfs.internal.v2_0.storedquery.StoredQueryConfiguration;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.opengis.filter.Filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link FeatureTypeInfoImpl}.
 *
 * @author Jan Venstermans
 */
public class FeatureTypeInfoImplTest {

	private FeatureTypeType fttMock;
    private FeatureTypeInfoImpl featureType;

    @Before
    public void setup() {
        // mock featureType
        fttMock = Mockito.mock(FeatureTypeType.class);
        featureType = new FeatureTypeInfoImpl(fttMock);
    }

    // One parameter, no view params, no mappings => no parameters
    @Test
    public void testGetAbstractWhenFeatureTypeTypeAbstractIsEmpty() {
		when(fttMock.getAbstract()).thenReturn(ECollections.<AbstractType>emptyEList());
		String abstractExpected = "";

		String abstractResult = featureType.getAbstract();

		Assert.assertEquals(abstractExpected, abstractResult);
	}

}
