OGC(r) WMTS 1.0.0 - ReadMe.txt
======================================

OGC Web Map Tile Service (WMTS) Interface Standard
-----------------------------------------------------------------------

More information on the OGC WMTS standard may be found at
 http://www.opengeospatial.org/standards/wmts

The most current schema are available at http://schemas.opengis.net/ .

-----------------------------------------------------------------------

2014-11-06  Joan Maso
  * wmts/1.0/profiles/wmts-simple: added WMTS Simple Profile 1.0.0 (OGC 13-082r2)

2012-07-21  Kevin Stegemoller
  * v1.0: WARNING XLink change is NOT BACKWARD COMPATIBLE.
  * Changed OGC XLink (xlink:simpleLink) to W3C XLink (xlink:simpleAttrs)
    per an approved TC and PC motion during the Dec. 2011 Brussels meeting.
    See http://www.opengeospatial.org/blog/1597 
  * v1.0: Per 11-025, all leaf documents of a namespace shall retroactively
    and explicitly require/add an <include/> of the all-components schema.
  * v1.0: Updated xsd:schema/@version to 1.0.1 (06-135r11 s#13.4)
  * v1.0: Included wmts.xsd as the all-components document (06-135r11 #14)
  * v1.0: Removed date from xs:schema/xs:annotation/xs:appinfo
  * v1.0: Update copyright
  * v1.0: xsd:annontation removed from wsdl files

2010-05-04  Kevin Stegemoller 
 * v1.0: Published wmts/1.0.0 as wmts/1.0 from OGC 07-057r7
 * v1.0: These documents were validated with:
   + XSV Validator version 3.1.1
   + Xerces-c validator version 2.8.0
   + libxml2 validator version 2.7.3
   + AltovaXML 2009
   + MSXML parser 4.0 sp2.
     -- Joan Maso

-----------------------------------------------------------------------

Policies, Procedures, Terms, and Conditions of OGC(r) are available
  http://www.opengeospatial.org/ogc/legal/ .

Copyright (c) 2012 Open Geospatial Consortium.

-----------------------------------------------------------------------
