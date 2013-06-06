package org.opengeo.gsr.core.geometry;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

public class SpatialReferenceEncoder {
    public static void toJson(SpatialReference sr, JSONBuilder json) {
        if (sr instanceof SpatialReferenceWKID) {
            final SpatialReferenceWKID wkid = (SpatialReferenceWKID) sr;
            final int effectiveWkid = wkid.getWkid() == 3785 ? 102100 : wkid.getWkid(); 
            json.object()
              .key("wkid").value(effectiveWkid)
            .endObject();
        } else if (sr instanceof SpatialReferenceWKT) {
            SpatialReferenceWKT wkt = (SpatialReferenceWKT) sr;
            json.object()
              .key("wkt").value(wkt.getWkt())
            .endObject();
        }
    }

    public static CoordinateReferenceSystem coordinateReferenceSystemFromJSON(JSON json) {
        if (!(json instanceof JSONObject)) {
            throw new JSONException("Spatial Reference must be encoded as JSON Object: was " + json);
        }
        JSONObject obj = (JSONObject) json;
        
        if (obj.containsKey("wkid")) {
            int wkid = obj.getInt("wkid");
            
            try {
                return CRS.decode("EPSG:" + wkid);
            } catch (FactoryException e) {
                throw new JSONException("SRID " + wkid + " does not correspond to any known spatial reference");
            }
        }
        
        if (obj.containsKey("wkt")) {
            String wkt = obj.getString("wkt");
            try {
                return CRS.parseWKT(wkt);
            } catch (FactoryException e) {
                throw new JSONException("wkt value (" + wkt + ") is not valid well-known text", e);
            }
        }
        
        if (obj.containsKey("uri")) {
            // TODO: I'm not sure how to look these up - need to check out how GeoServer does this for WFS requests.
            throw new RuntimeException("Spatial reference specified as URI - decoding these is not yet implemented.");
        }
        
        throw new JSONException("Could not determine spatial reference from JSON: " + json);
    }

    public static CoordinateReferenceSystem fromJson(JSONObject sr) throws FactoryException {
        if (sr.containsKey("wkid")) {
            return CRS.decode("EPSG:" + sr.getString("wkid"));
        } else if (sr.containsKey("wkt")) {
            return CRS.parseWKT(sr.getString("wkid"));
        } else {
            return null;
        }
    }
}
