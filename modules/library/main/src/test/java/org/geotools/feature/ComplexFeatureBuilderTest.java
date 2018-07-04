/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.feature;

import java.util.ArrayList;
import java.util.Collection;
import org.geotools.ExceptionChecker;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;

public class ComplexFeatureBuilderTest {
    private static ComplexAttribute getAMineNameProperty(String name, boolean isPreferred) {
        AttributeBuilder builder = new AttributeBuilder(new LenientFeatureFactoryImpl());
        builder.setType(FakeTypes.Mine.MINENAMETYPE_TYPE);

        builder.add("isPreferred_testId", isPreferred, FakeTypes.Mine.NAME_isPreferred);

        builder.add("mineName_testId", name, FakeTypes.Mine.NAME_mineName);

        final Attribute mineName = builder.build();

        Collection<Attribute> mineNames = new ArrayList<Attribute>();
        mineNames.add(mineName);

        builder.init();
        builder.setType(FakeTypes.Mine.MINENAMEPROPERTYTYPE_TYPE);

        builder.add("MineName_testId", mineNames, FakeTypes.Mine.NAME_MineName);

        return (ComplexAttribute) builder.build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void append_invalidName_throwsIllegalArgumentException() throws Exception {
        // Arrange
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);

        // Act
        try {
            builder.append(new NameImpl("invalid_descriptor_name"), null);
        } catch (IllegalArgumentException iae) {
            ExceptionChecker.assertExceptionMessage(
                    iae,
                    "The name 'invalid_descriptor_name' is not a valid descriptor name for the type 'urn:org:example:MineType'.");
        }
    }

    /** @throws Exception */
    @Test(expected = IllegalArgumentException.class)
    public void append_validNameInvalidValueClass_throwsIllegalArgumentException()
            throws Exception {
        // Arrange
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);

        // Act
        try {
            builder.append(
                    FakeTypes.Mine.NAME_mineName,
                    new AttributeImpl(
                            "Test",
                            FakeTypes.ANYSIMPLETYPE_TYPE,
                            null)); // Passing in simple type instead of a mineName.
        } catch (IllegalArgumentException iae) {
            ExceptionChecker.assertExceptionMessage(
                    iae,
                    "The value provided contains an object of 'class java.lang.Object' but the method expects an object of 'interface java.util.Collection'.");
        }
    }

    @Test
    public void append_validNameValidValue_valueShouldBeAddedToTheMap() {
        // Arrange
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);
        ComplexAttribute mineNameProperty = getAMineNameProperty("mine 1", true);

        // Act
        builder.append(FakeTypes.Mine.NAME_mineName, mineNameProperty);
        Object actualValue = builder.values.get(FakeTypes.Mine.NAME_mineName).get(0);

        // Assert
        Assert.assertSame(mineNameProperty, actualValue);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void append_exceedMaxOccursLimit_throwsIndexOutOfBoundsException() throws Exception {
        // Arrange
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);
        builder.append(FakeTypes.Mine.NAME_mineName, getAMineNameProperty("mine 1", true));
        builder.append(FakeTypes.Mine.NAME_mineName, getAMineNameProperty("mine 2", false));
        builder.append(FakeTypes.Mine.NAME_mineName, getAMineNameProperty("mine 3", false));

        // Act
        try {
            builder.append(
                    FakeTypes.Mine.NAME_mineName,
                    getAMineNameProperty("mine 4", false)); // Add it once too many times.
        } catch (IndexOutOfBoundsException ioobe) {
            ExceptionChecker.assertExceptionMessage(
                    ioobe,
                    "You can't add another object with the name of 'urn:org:example:mineName' because you already have the maximum number (3) allowed by the property descriptor.");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void buildFeature_noLocationSet_throwsIllegalStateException() throws Exception {
        // Arrange
        ComplexFeatureBuilder builder = new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);

        // Deliberately not setting urn:org:example:mineName

        // Act
        try {
            builder.buildFeature("id");
        } catch (IllegalStateException ise) {
            ExceptionChecker.assertExceptionMessage(
                    ise,
                    "Failed to build feature 'urn:org:example:MineType'; its property 'urn:org:example:mineName' requires at least 1 occurrence(s) but number of occurrences was 0.");
        }
    }

    @Test
    public void build_typeIsMineTypeAndAddedDataIsValid_buildsFeature() {
        // Arrange
        ComplexAttribute mineNameProperty = getAMineNameProperty("Sharlston Colliery", true);

        // Act
        ComplexFeatureBuilder complexFeatureBuilder =
                new ComplexFeatureBuilder(FakeTypes.Mine.MINETYPE_TYPE);

        complexFeatureBuilder.append(FakeTypes.Mine.NAME_mineName, mineNameProperty);

        Feature mine = complexFeatureBuilder.buildFeature("er.mine.S0000001");

        // Assert
        Assert.assertEquals(
                "FeatureImpl:MineType<MineType id=er.mine.S0000001>=[ComplexAttributeImpl:MineNamePropertyType=[ComplexAttributeImpl:MineName<MineNameType id=MineName_testId>=[ComplexAttributeImpl:MineNameType=[AttributeImpl:isPreferred<boolean id=isPreferred_testId>=true, AttributeImpl:mineName<string id=mineName_testId>=Sharlston Colliery]]]]",
                mine.toString());
    }
}
