geotools dxf module
========

 Geotools DXF Exteded data support

The dxf files have an optional block, that could be added to any entity, the exteded data.
Usualy this block is used to add metadata to entities, like notes, layer specifications, ...
See  http://www.autodesk.com/techpubs/autocad/acad2000/dxf/index.htm section Extended Data

The previous versions of unsupported module "DXF" does not parse these blocks, ignoring the blocks.

Done a pull request for 10-SNAPSHOT

- [ ] Add the all 3 coordinates objects to Map

```JAVA
Example:
//read the file
DXFFeatureReader features = new DXFFeatureReader(file.toURI().toURL(), new String(), "EPSG:" + epsg, geotype, new ArrayList<String>());

//iterate over the parsed features 
while (features.hasNext()) {
  SimpleFeature sf = features.next();
  ...
  for (Property p : sf.getValue()){
      //The new part. Accessing to Extended Data
      //Extended data is in a Map. The only Map in object
      if (p.getValue() instanceof Map){
        //do stuff with extended data
            /**
            * Key set of Map
            * "layerName"
            * "appName"
            * "attributes"
            * "distance"
            * "scaleFactor"
            **/
          p.getValue().get(key);        
        ...
        //1002(see spec) attributes
        p.getValue().get("attributes");
        //do stuff with attributes
        ....
      }
  }

}
```
