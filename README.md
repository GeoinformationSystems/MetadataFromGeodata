# metadataFromGeodata
The metadataFromGeodata is a Java application, which reads geographic and quality information from geodata files and
creates metadata files. The application can be used to read OGC Geopackage files (https://www.geopackage.org/),
Esri Shapefiles (https://desktop.arcgis.com/en/arcmap/10.3/manage-data/shapefiles/what-is-a-shapefile.htm),
and ASCII (csv) files linked to a Geopackage file.

The generated metadata is ISO 19115-1:2014 (Geographic information - Metadata https://www.iso.org/standard/53798.html)
and ISO 19157:2013 (Geographic information - Data quality https://www.iso.org/standard/32575.html) compliant.
Thus, the created XML files can be used as inputs for (geographic) metadata catalogues.

A xml file stores all read metadata and additionally a flattened sqlite database is generated. 
The latter serves as an extension point to generate output in other standard compliant formats.


## Usage
Use the IDE of your choice and build, e.g., a jar file. The package needs one argument - the filename of a property file.
This file must contain all information to run the main method. An example is given in config/propertiesExample.txt.

*Minimum content of a property file*:
- `profile = filenameProfile.json` provides the path (absolute or relative to executing path + file name) to a Json profile
  containing the used metadata elements for all packages under `org.geokur` plus their obligation. Metadata elements that
  are not used in the profile can be instantiated, but a warning will occur. If a class is instantiated, but misses mandatory
  elements, a warning will occur.
- `geodata = filenameGeodata.xxx` is the file name to geospatial data for metadata extraction
- `outXML = filenameOut.xml` denotes file name for output in xml format. All extracted metadata follow the nested structure
  given in ISO 19115 and ISO 19157. If the file exists it is replaced without asking.
- `outDB = filenameOut.db` is the file name for a flattened view on metadata in SQLite format.

In the case of an *ascii file in csv format* more content in the property file shall be provided:
- `geodataReference = filenameGeodata.gpkg` gives the file name of the geodata that serves as geographic base data for
  the recent ascii file. Currently only geopackage format is allowed.
- `geoTableName = tablename` denotes an arbitrary long list of tables in the geopackage to be interpreted.
- `geoColNameJoin = columnName` an arbitrary long list of column headers in the geopackage to form a concatenated string.
  This string is used to identify unambiguous elements within the tables in the geopackage.
- `asciiColNameJoin = columnName` means the same like geoColNameJoin, but for the column names in the ascii file. The
  order of the entries must be the same in geoColNameJoin and asciiColNameJoin.
- `asciiColNameDefine = columnName` these entries designate the column name for definition of temporal or thematic contents.
  Further analysis of data quality use these columns.
- `descriptionAsciiColNameDefine = thematic/temporal` gives the description whether columns in asciiColNameDefine are of
  thematic or temporal type. The order of asciiColNameDefine and descriptionAsciiColNameDefine must be given accordingly.
- `asciiColNameIgnore = columnName` indicates a column to be ignored. Multiple entries with asciiColNameIgnore are possible.


## Structure
The metadataFromGeodata project is structured into several packages:
- `org.geokur` contains the main method in the class MetadataGenerator.
- `org.geokur.generateMetadata` contains central classes for each supported geospatial format (Geopackage, Asciidata, Shape).
- `org.geokur.ISO191xxProfile` contains classes and exceptions for profiling the metadata schema for individual use cases.
- `org.geokur.ISO19103Schema` contains classes from ISO 19103:2015 (geographic information - conceptual schema language).
  Only a small set of classes is included (Coordinate, DirectPosition, Record, RecordType). These classes are necessary for
  correct instantiating classes from ISO 19115 and ISO 19157.
- `org.geokur.ISO19107Schema` contains classes from ISO 19107:2019 (geographic information - spatial schema). These classes
  describe the representation and manipulation of spatial characteristics of geographic entities. Only classes ISO 19115
  and ISO 19157 refer to are included.
- `org.geokur.ISO19111Schema` contains classes from ISO 19111:2019 (geographic information - referencing by coordinates).
  Included are only classes ISO 19115 and ISO 19157 refer to.
- `org.geokur.ISO19115Schema` contains classes from ISO 19115-1:2014 (geographic information - metadata). Using these classes
  provides a standardized way for metadata representation.
- `org.geokur.ISO19115_2Schema` contains classes from ISO 19115-2:2019 (geographic information - metadata - Extension for
  acquisition and processing). These classes extend ISO 19115-1 in regard to specific requirements for observational data.
- `org.geokur.ISO19157Schema` contains classes from ISO 19157:2013 (geographic information - data quality). These provide
  comprehensive information about data quality of the described dataset. Referring to comparable data quality measures
  the data user can evaluate the dataset's fitness for the specific use.
  
  
## Output File Structure
*xml file*
- the root element is `DS_DataSet` according to ISO 19115
- all namespaces from the ISO norms are declared
- next element `has` is a field of class `DS_DataSet` and leads to the next element
- `MD_Metadata` indicates one geospatial resource; shape files allow only one resource, geopackages enable an arbitrary
  number of resources and thus `MD_Metadata` elements
  
*SQLite database*
- the database consists of several tables: datasets, namespaces, properties, and a metadata linking table plus metadata
  content table for each geospatial dataset (one in case of shape, possibly multiple in case of geopackage or csv)
- datasets table lists all geospatial datasets
- namespaces contains all used namespaces
- metadata linking table (e.g., exampleGeo_gpkg_1) shows the nested structure of metadata elements and fields and links to metadata
  content via metadata_id
- metadata content table (e.g., exampleGeo_gpkg_1_metadata) summarizes all metadata content
- properties allows future extensions
  

## Java Libraries
- commons-io 2.7 - Apache 2.0 license
- jdom 2.0.6 - https://github.com/hunterhacker/jdom/blob/master/LICENSE.txt
- sqlite-jdbc 3.32.3 - Apache 2.0 license
- jts-core 1.17.0 - EDL 1.0, EPL 2.0 license
- gt-geopkg 23.1 - LGPL license
- gt-shapefile 23.1 - LGPL license
- gt-epsg-hsql 23.1 - LGPL license
- gson 2.8.6 - Apache 2.0 license


## Known Limits/Bugs
- only geopackages are allowed as geographic source for ascii files
- currently coordinate reference system other than EPSG:4326 are not working due to failing transformations


## License
The metadataFromGeodata project is licensed under the GNU LGPL. 


## Contact
Michael Wagner ([michael.wagner@tu-dresden.de](mailto:michael.wagner@tu-dresden.de))