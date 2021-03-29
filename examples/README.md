# Example Datasets for metadataFromGeodata

In this folder are some basic geodata file examples that can be used for try out metadataFromGeodata. All files contain dummy data with no meaning, although in the right format.

- `exampleGeo.gpkg`: simple GeoPackage with two layers, that could be interpreted as nation (level_1) and municipality (level_2); WGS84 is used; polygons can overlap
- `exampleGeoETRS.gpkg`: the same as `exampleGeo.gpkg`, but in the appropriate ETRS89 reference system
- `exampleGeoPointLine.gpkg`: GeoPackage with one points layer and one lines layer in WGS84
- `exampleGeoShp.*`: Esri Shape file with (overlapping) polygons
- `exampleGeoShpETRS.*`: the same as `exampleGeoShp`, but in the appropriate ETRS89 reference system
- `exampleGeoTable.csv`: comma separated values file as a content container for `exampleGeo.gpkg`; mapping from content to spatial entities works via combinations of geoID and ahID in both resources; *id* is a unique number, *tabID* defines the source of the entry and is an example of a column to be ignored, *geoID* is the source geometry, *ahID* is an administrative id, *commodityID* is the thematic id, *year* is the time variable, *planted*, *harvested*, *production*, *yield*, *headcount* are arbitrary content columns to be interpreted
- `exampleIDCommodity.csv`: commodity mapping for `exampleGeoTable.csv`
- `rasterExample.tif`: simple GeoTIFF with one band; uses WGS84
- `rasterExampleETRS.tif`: the same as `rasterExample.tif`, but in the appropriate ETRS89 reference system
- `rasterExampleLarge.tif`: via interpolation disaggregated version of `rasterExample.tif`