# IP Review

 - Jody Garnett, August 7th, 2006

 - Adrian Custer, June 2008


STATUS: DIRTY

 - Code is clean, 
 - Sample data dirty: http://jira.codehaus.org/browse/GEOT-1894
 - validation.dto schema "probably needs a note" (see below)

```
org.geotools.validation
```

 - Outlines the validaiton framework and infrastructure.

```
org.geotools.validation.attributes
```

 - Attribute validaiton, aka what SQL is good at checking.


```
org.geotools.validation.dto
```

 - Beans use dto hold data enroute from xml to memory ...

 - schema probably needs a note


```
org.geotools.validation.[network|relate|spatial]
```

 - Remaining validation test, nothing that exciting.


```
org.geotools.validation.xml
```

```
org.geotools.validation.attributes (test)
org.geotools.validation.relate (test)
```

Bleah, the horribe memory leak test, the first brute force abuse of geotools datastore api.


## Communication

Brent Owens sent the following email to the list on April 17th, 2006:

> I have nearly completed the IP check on the validation module of
> Geotools.
> 
> All the headers have been verified and corrected. I have also received
> word from Paul Ramsey (Refractions Research) that all the code in the
> module has been given to the community.
> 
> Here is a list of files that had their headers changed (some had duplicate headers):
>
> ```
>  DefaultFeatureResults.java
>  DefaultFeatureValidation.java
>  DefaultIntegrityValidation.java
>  FeatureValidation.java
>  IntegrityValidation.java
>  PlugIn.java
>  Validation.java
>  ValidationBeanInfo.java
>  ValidationResults.java
>  Validator.java
>  TempFeatureResults.java
>  FeatureValidationTest.java
>  IntegrityValidationTest.java
>  RoadNetworkValidationResults.java
>  RoadValidationResults.java
>  UniqueFIDIntegrityValidation.java
>  ValidationPlugInTester.java
>  ValidationProcessorTest.java
>  GazetteerNameValidationBeanInfoTest.java
>  GazetteerNameValidationTest.java
>  NullZeroValidationTest.java
>  RangeFeatureValidationTest.java
>  XMLReaderTest.java
>  validation/relate/*
>  PlugInDTO.java
>  TestDTO.java
>  TestSuiteDTO.java
>  IsValidGeometryValidation.java
>  LineCoveredByPolygonValidation.java
>  LineNoSelfOverlappingValidation.java
>  LinesNotIntersectValidation.java
>  ArgHelper.java
>  ReaderUtils.java
>  ValidationException.java
>  WriterUtils.java
>  XMLReader.java
> ```
> 
> Brent Owens
> (The Open Planning Project)


Pertti Tapola points out that the following files contain GPL copyright
notices from GeoServer, often in addition to the GeoTool LGPL header:

* TO\_RESOLVE: This can be removed as per [Chris Holmes's
statement.](http://www.mail-archive.com/geotools-devel@lists.sourceforge.net/msg02396.html)

```
org.geotools.validation.attributes.EqualityValidation.java
org.geotools.validation.attributes.NullZeroValidation.java
org.geotools.validation.attributes.RangeValidation.java
org.geotools.validation.attributes.UniqueFIDValidation.java
org.geotools.validation.attributes.UniquityValidation.java
org.geotools.validation.DefaultFeatureValidation.java
org.geotools.validation.DefaultIntegrityValidation.java
org.geotools.validation.dto.PlugInDTO.java
org.geotools.validation.dto.TestDTO.java
org.geotools.validation.dto.TestSuiteDTO.java
org.geotools.validation.FeatureValidation.java
org.geotools.validation.IntegrityValidation.java
org.geotools.validation.PlugIn.java
org.geotools.validation.relate.ContainsIntegrity.java
org.geotools.validation.relate.ContainsIntegrityBeanInfo.java
org.geotools.validation.relate.CrossesIntegrity.java
org.geotools.validation.relate.CrossesIntegrityBeanInfo.java
org.geotools.validation.relate.DisjointIntegrity.java
org.geotools.validation.relate.DisjointIntegrityBeanInfo.java
org.geotools.validation.relate.IntersectsIntegrity.java
org.geotools.validation.relate.IntersectsIntegrityBeanInfo.java
org.geotools.validation.relate.OverlapsIntegrity.java
org.geotools.validation.relate.OverlapsIntegrityBeanInfo.java
org.geotools.validation.relate.RelateIntegrity.java
org.geotools.validation.relate.RelateIntegrityBeanInfo.java
org.geotools.validation.relate.RelationIntegrity.java
org.geotools.validation.relate.RelationIntegrityBeanInfo.java
org.geotools.validation.relate.SpatialTestCase.java
org.geotools.validation.relate.TouchesIntegrity.java
org.geotools.validation.relate.TouchesIntegrityBeanInfo.java
org.geotools.validation.relate.WithinIntegrity.java
org.geotools.validation.relate.WithinIntegrityBeanInfo.java
org.geotools.validation.spatial.IsValidGeometryValidation.java
org.geotools.validation.spatial.LineCoveredByPolygonValidation.java
org.geotools.validation.spatial.LineNoSelfOverlappingValidation.java
org.geotools.validation.spatial.LinesNotIntersectValidation.java org.geotools.validation.Validation.java
org.geotools.validation.ValidationBeanInfo.java
org.geotools.validation.ValidationResults.java
org.geotools.validation.Validator.java
org.geotools.validation.xml.ArgHelper.java
org.geotools.validation.xml.ReaderUtils.java
org.geotools.validation.xml.ValidationException.java
org.geotools.validation.xml.WriterUtils.java org.geotools.validation.xml.XMLReader.java
ext.validation.test.org.geotools.validation.attributes.NullZeroValidationTest.java
ext.validation.test.org.geotools.validation.attributes.RangeFeatureValidationTest.java
ext.validation.test.org.geotools.validation.FeatureValidationTest.java
ext.validation.test.org.geotools.validation.IntegrityValidationTest.java
ext.validation.test.org.geotools.validation.relate.OverlapsIntegrityTest.java
ext.validation.test.org.geotools.validation.RoadNetworkValidationResults.java
ext.validation.test.org.geotools.validation.RoadValidationResults.java
ext.validation.test.org.geotools.validation.UniqueFIDIntegrityValidation.java
ext.validation.test.org.geotools.validation.ValidationPlugInTester.java
ext.validation.test.org.geotools.validation.ValidationProcessorTest.java
ext.validation.test.org.geotools.validation.xml.XMLReaderTest.java
```