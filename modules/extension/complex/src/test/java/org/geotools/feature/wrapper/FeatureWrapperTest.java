/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.feature.wrapper;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Collection;
import org.geotools.ExceptionChecker;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.data.complex.feature.wrapper.FeatureWrapper;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.FakeTypes.Mine;
import org.geotools.feature.FeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.filter.identity.GmlObjectIdImpl;
import org.junit.Assert;
import org.junit.Test;

public class FeatureWrapperTest {
    private static Feature getFeature() {
        // AttributeImpl:mineName<string id=mineName_1>=Pieces of Eight -
        // Admiral Hill
        Attribute mineName =
                new AttributeImpl(
                        "Pieces of Eight - Admiral Hill",
                        Mine.mineNAME_DESCRIPTOR,
                        new GmlObjectIdImpl("mineName"));

        // AttributeImpl:isPreferred<boolean id=isPreferred_1>=true,
        Attribute isPreferred =
                new AttributeImpl(
                        true, Mine.ISPREFERRED_DESCRIPTOR, new GmlObjectIdImpl("isPreferred"));

        Collection<Property> MineNameTypeProperties = new ArrayList<>();
        MineNameTypeProperties.add(mineName);
        MineNameTypeProperties.add(isPreferred);

        // ComplexAttributeImpl:MineNameType=
        ComplexAttribute MineNameType =
                new ComplexAttributeImpl(MineNameTypeProperties, Mine.MINENAMETYPE_TYPE, null);

        Collection<Property> MineNameProperties = new ArrayList<>();
        MineNameProperties.add(MineNameType);

        // ComplexAttributeImpl:MineName<MineNameType id=MINENAMETYPE_TYPE_1>=
        ComplexAttribute MineName =
                new ComplexAttributeImpl(
                        MineNameProperties,
                        Mine.MINENAME_DESCRIPTOR,
                        new GmlObjectIdImpl("MineName"));

        Collection<Property> MineNamePropertyProperties = new ArrayList<>();
        MineNamePropertyProperties.add(MineName);

        // ComplexAttributeImpl:MineNamePropertyType=
        ComplexAttribute MineNamePropertyType =
                new ComplexAttributeImpl(
                        MineNamePropertyProperties, Mine.MINENAMEPROPERTYTYPE_TYPE, null);

        Collection<Property> MineProperties = new ArrayList<>();
        MineProperties.add(MineNamePropertyType);

        // FeatureImpl:MineType<MineType id=Mine>=
        Feature mine =
                new FeatureImpl(MineProperties, Mine.MINETYPE_TYPE, new FeatureIdImpl("Mine"));

        return mine;
    }

    @Test
    public void wrap_validFeature_returnsWrappedFeature() throws Exception {
        // Arrange
        Feature mine = getFeature();

        // Act
        MineType wrappedMine = FeatureWrapper.wrap(mine, MineType.class);

        // Assert
        Assert.assertEquals(
                "Pieces of Eight - Admiral Hill",
                wrappedMine.MineNameProperties.get(0).MineName.mineName);
    }

    @Test
    public void wrap_validFeature_canAccessPathDefinedVariables() throws Exception {
        // Arrange
        Feature mine = getFeature();

        // Act
        MineType wrappedMine = FeatureWrapper.wrap(mine, MineType.class);

        // Assert
        Assert.assertEquals("Pieces of Eight - Admiral Hill", wrappedMine.firstName);
    }

    @Test(expected = InvalidClassException.class)
    public void wrap_invalidFeatureCannotResolvePath_throwsInvalidClassException()
            throws Exception {
        // Arrange
        Collection<Property> properties = new ArrayList<>();
        Feature mine =
                new FeatureImpl(properties, Mine.MINETYPE_TYPE, new FeatureIdImpl("Invalid mine."));

        // Act
        try {
            FeatureWrapper.wrap(mine, MineType.class);
        } catch (InvalidClassException ice) {
            ExceptionChecker.assertExceptionMessage(
                    ice,
                    "Unable to wrap attribute in class 'class org.geotools.feature.wrapper.MineType'. Reference to mineName could not be found in the attribute.");
        }
    }

    @Test(expected = InvalidClassException.class)
    public void wrap_invalidFeatureMissingAttribute_throwsInvalidClassException() throws Exception {
        // AttributeImpl:isPreferred<boolean id=isPreferred_1>=true,
        Attribute isPreferred =
                new AttributeImpl(
                        true, Mine.ISPREFERRED_DESCRIPTOR, new GmlObjectIdImpl("isPreferred"));

        Collection<Property> MineNameTypeProperties = new ArrayList<>();
        // Deliberately neglect to add: MineNameTypeProperties.add(mineName);
        MineNameTypeProperties.add(isPreferred);

        // ComplexAttributeImpl:MineNameType=
        ComplexAttribute MineNameType =
                new ComplexAttributeImpl(MineNameTypeProperties, Mine.MINENAMETYPE_TYPE, null);

        Collection<Property> MineNameProperties = new ArrayList<>();
        MineNameProperties.add(MineNameType);

        // ComplexAttributeImpl:MineName<MineNameType id=MINENAMETYPE_TYPE_1>=
        ComplexAttribute MineName =
                new ComplexAttributeImpl(
                        MineNameProperties,
                        Mine.MINENAME_DESCRIPTOR,
                        new GmlObjectIdImpl("MineName"));

        Collection<Property> MineNamePropertyProperties = new ArrayList<>();
        MineNamePropertyProperties.add(MineName);

        // ComplexAttributeImpl:MineNamePropertyType=
        ComplexAttribute MineNamePropertyType =
                new ComplexAttributeImpl(
                        MineNamePropertyProperties, Mine.MINENAMEPROPERTYTYPE_TYPE, null);

        Collection<Property> MineProperties = new ArrayList<>();
        MineProperties.add(MineNamePropertyType);

        // FeatureImpl:MineType<MineType id=Mine>=
        Feature mine =
                new FeatureImpl(
                        MineProperties, Mine.MINETYPE_TYPE, new FeatureIdImpl("Invalid Mine"));

        // Act
        try {
            FeatureWrapper.wrap(mine, MineType2.class);
        } catch (InvalidClassException ice) {
            ExceptionChecker.assertExceptionMessage(
                    ice,
                    "Unable to wrap attribute in class 'class org.geotools.feature.wrapper.MineNameType'. urn:org:example:mineName could not be found in the attribute.");
        }
    }

    @Test(expected = InvalidClassException.class)
    public void wrap_invalidFeatureMissingMineNameTypeProperty_throwsInvalidClassException()
            throws Exception {
        // Arrange
        Collection<Property> MineNameProperties = new ArrayList<>();
        // MineNameProperties.add(MineNameType); Deliberately not adding this.

        // ComplexAttributeImpl:MineName<MineNameType id=MINENAMETYPE_TYPE_1>=
        ComplexAttribute MineName =
                new ComplexAttributeImpl(
                        MineNameProperties,
                        Mine.MINENAME_DESCRIPTOR,
                        new GmlObjectIdImpl("MineName"));

        Collection<Property> MineNamePropertyProperties = new ArrayList<>();
        MineNamePropertyProperties.add(MineName);

        // ComplexAttributeImpl:MineNamePropertyType=
        ComplexAttribute MineNamePropertyType =
                new ComplexAttributeImpl(
                        MineNamePropertyProperties, Mine.MINENAMEPROPERTYTYPE_TYPE, null);

        Collection<Property> MineProperties = new ArrayList<>();
        MineProperties.add(MineNamePropertyType);

        // FeatureImpl:MineType<MineType id=Mine>=
        Feature mine =
                new FeatureImpl(
                        MineProperties, Mine.MINETYPE_TYPE, new FeatureIdImpl("Invalid Mine"));

        // Act
        try {
            FeatureWrapper.wrap(mine, MineType2.class);
        } catch (InvalidClassException ice) {
            ExceptionChecker.assertExceptionMessage(
                    ice,
                    "Unable to wrap attribute in class 'org.geotools.feature.wrapper.MineNamePropertyType'. 'urn:org:example:MineName' doesn't have required property 'urn:org:example:MineNameType'.");
        }
    }

    @Test(expected = InvalidClassException.class)
    public void wrap_invalidFeatureMissingMineName_throwsInvalidClassException() throws Exception {
        // Arrange
        Collection<Property> MineNamePropertyProperties = new ArrayList<>();
        // MineNamePropertyProperties.add(MineName); // Deliberately not adding
        // this.

        // ComplexAttributeImpl:MineNamePropertyType=
        ComplexAttribute MineNamePropertyType =
                new ComplexAttributeImpl(
                        MineNamePropertyProperties, Mine.MINENAMEPROPERTYTYPE_TYPE, null);

        Collection<Property> MineProperties = new ArrayList<>();
        MineProperties.add(MineNamePropertyType);

        // FeatureImpl:MineType<MineType id=Mine>=
        Feature mine =
                new FeatureImpl(
                        MineProperties, Mine.MINETYPE_TYPE, new FeatureIdImpl("Invalid Mine"));

        // Act
        try {
            FeatureWrapper.wrap(mine, MineType2.class);
        } catch (InvalidClassException ice) {
            ExceptionChecker.assertExceptionMessage(
                    ice,
                    "Unable to wrap attribute in class 'org.geotools.feature.wrapper.MineNamePropertyType'. 'urn:org:example:MineNamePropertyType' doesn't have required property 'urn:org:example:MineName'.");
        }
    }
}
