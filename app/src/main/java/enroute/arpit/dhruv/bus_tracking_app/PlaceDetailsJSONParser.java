package enroute.arpit.dhruv.bus_tracking_app;

/**
 * Created by Dhruv on 25-Jul-15.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceDetailsJSONParser {

    /** Receives a JSONObject and returns a list */
    public List<HashMap<String,String>> parse(JSONObject jObject){

        Double lat = Double.valueOf(0);
        Double lng = Double.valueOf(0);

        HashMap<String, String> hm = new HashMap<String, String>();
        List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();

        try {

            lat = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
            lng = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");

        } catch (JSONException e) {
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

        hm.put("lat", Double.toString(lat));
        hm.put("lng", Double.toString(lng));

        list.add(hm);
        return list;
    }
}
