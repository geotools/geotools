<?xml version="1.0" encoding="UTF-8"?>

<!-- Created by iant on 11 March 2002, 10:54 -->

<ccg:featureCollection
   xmlns:ccg="http://www.ccg.leeds.ac.uk/gml"
   xmlns:gml="http://www.opengis.net/gml"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://www.ogc.net/gml/base">  
    <ccg:queryExtent>
        <gml:Box gid="1" srsName="http://?/epsg.xml#ESPG:4326">
          <gml:coordinates>
          0,0 30,30
          </gml:coordinates>
        </gml:Box>
    </ccg:queryExtent>
    <ccg:cartographicMember>
        <ccg:lake fid="101">   
            <ccg:name>Blue Lake</ccg:name>
              <gml:Polygon gid="1" srsName="http://?/epsg.xml#ESPG:4326">
              <gml:outerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates>
                    52,18, 66,23, 73,9, 48,6, 52,18
                  </gml:coordinates>
                </gml:LinearRing>
              </gml:outerBoundaryIs>
              <gml:innerBoundaryIs>
                <gml:LinearRing>
                  <gml:coordinates>
                    59,13, 59,18, 67,18, 67,13, 59,13
                  </gml:coordinates>
                </gml:LinearRing>
              </gml:innerBoundaryIs>
            </gml:Polygon>
        </ccg:lake>         
    </ccg:cartographicMember>
   <ccg:cartographicMember>
        <ccg:road fid="102">
            <ccg:name>Route 5</ccg:name>
            <gml:LineString gid="2" srsName="http://?/epsg.xml#ESPG:4326">
                <gml:coordinates>
                    0,18, 10,21, 16,23, 28,26, 44,31 
                </gml:coordinates>
            </gml:LineString>
        </ccg:road>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:road fid="103">
            <ccg:name>Route 5</ccg:name>
            <gml:LineString gid="3" srsName="http://?/epsg.xml#ESPG:4326">
                <gml:coordinates>
                    44,31, 56,34, 70,38 
                </gml:coordinates>
            </gml:LineString>
        </ccg:road>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:road fid="104">
            <ccg:name>Route 5</ccg:name>
            <gml:LineString gid="4" srsName="http://?/epsg.xml#ESPG:4326">
                <gml:coordinates>
                    70,38, 72,48 
                </gml:coordinates>
            </gml:LineString>
        </ccg:road>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:road fid="105">
            <ccg:name>Main Street</ccg:name>
            <gml:LineString gid="5" srsName="http://?/epsg.xml#ESPG:4326">
                <gml:coordinates>
                    70,38, 84,42
                </gml:coordinates>
            </gml:LineString>
        </ccg:road>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:road fid="106">
            <ccg:name>Dirt Road by Green Forest</ccg:name>
            <gml:LineString gid="6" srsName="http://?/epsg.xml#ESPG:4326">
                    <gml:coordinates>
                    28,26, 28,0
                </gml:coordinates>
            </gml:LineString>
        </ccg:road>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:dividedroute fid="101">
            <ccg:name>Route 70</ccg:name>
            <gml:MultiLineString srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
                <gml:lineStringMember>
                    <gml:LineString>
                      <gml:coordinates>
                            10,48, 10,21, 10,0
                      </gml:coordinates>
                    </gml:LineString>
                </gml:lineStringMember>
                <gml:lineStringMember>
                    <gml:LineString>
                      <gml:coordinates>
                            16,0, 16,23, 16,48
                      </gml:coordinates>
                    </gml:LineString>
                </gml:lineStringMember>
            </gml:MultiLineString>
        </ccg:dividedroute>
    </ccg:cartographicMember>
    <ccg:cartographicMember>
        <ccg:forest fid="109">
            <ccg:name>Green Forest</ccg:name>
            <gml:MultiPolygon srsName="http://www.opengis.net/gml/srs/epsg.xml#4326">
                <gml:polygonMember> 
                    <gml:Polygon gid="1" srsName="http://?/epsg.xml#ESPG:4326">
                      <gml:outerBoundaryIs>
                        <gml:LinearRing>
                          <gml:coordinates>
                            28,26, 84,42, 84,0, 28,0, 28,26
                          </gml:coordinates>
                        </gml:LinearRing>
                      </gml:outerBoundaryIs>
                      <gml:innerBoundaryIs>
                        <gml:LinearRing>
                          <gml:coordinates>
                            52,18, 66,23, 73,9, 48,6, 52,18
                          </gml:coordinates>
                        </gml:LinearRing>
                      </gml:innerBoundaryIs>
                    </gml:Polygon>
                </gml:polygonMember>
            </gml:MultiPolygon> 
        </ccg:forest>     
    </ccg:cartographicMember>
</ccg:featureCollection>

