# WriteMetadataXML

Write MetadataXML is a Java application, which reads geographic and quality information from geodata files and
creates metadata files. The application can be used to read OGC Geopackage files (https://www.geopackage.org/),
Esri Shapefiles (https://desktop.arcgis.com/en/arcmap/10.3/manage-data/shapefiles/what-is-a-shapefile.htm),
and ASCII files linked to a Geopackage file via an ID.

The generated metadata is ISO 19115-1:2014 (Geographic information - Metadata https://www.iso.org/standard/53798.html)
and ISO 19157:2013 (Geographic information - Data quality https://www.iso.org/standard/32575.html) compliant.
Thus, the created XML files can be used as inputs for (geograpic) metadata catalogues.

All read metadata is stored in an xml file and a flattened sqlite database. 
The latter serves as an extension point to generate output in other standard compliant formats.

## Structure

## Usage

## Java Libraries
- commons-io 2.7 - Apache 2.0 license
- jdom 2.0.6 - https://github.com/hunterhacker/jdom/blob/master/LICENSE.txt
- sqlite-jdbc 3.32.3 - Apache 2.0 license
- jts-core 1.17.0 - EDL 1.0, EPL 2.0 license
- gt-geopkg 23.1 - LGPL license
- gt-shapefile 23.1 - LGPL license
- gt-epsg-hsql 23.1 - LGPL license
- gson 2.8.6 - Apache 2.0 license

## License

## Contact
Michael Wagner ([michael.wagner@tu-dresden.de](mailto:michael.wagner@tu-dresden.de))