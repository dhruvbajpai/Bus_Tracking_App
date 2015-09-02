package enroute.pallavi.chugh.bus_tracking_app.Classes;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Dhruv on 01-Sep-15.
 */
public class MyJSONParse {

    public static String DistanceInKm(String res)
    {
        String distance=null;

        try {
            JSONObject jsonObject = new JSONObject(res);
            //abc = jsonObject.getString("destination_addresses");

            JSONArray jo = jsonObject.getJSONArray("rows");

            JSONObject j = jo.getJSONObject(0);
            JSONArray param = j.getJSONArray("elements");
            JSONObject elements = param.getJSONObject(0);
            JSONObject d_time = elements.getJSONObject("duration");
            JSONObject d_distance = elements.getJSONObject("distance");
             distance = d_distance.getString("text");

            String time = d_time.getString("text");
            String t_value = d_time.getString("value");
            String d_value = d_distance.getString("value");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return distance;
    }
    public static String TimeInMins(String res)
    {
        String time=null;

        try {
            JSONObject jsonObject = new JSONObject(res);
            //abc = jsonObject.getString("destination_addresses");

            JSONArray jo = jsonObject.getJSONArray("rows");

            JSONObject j = jo.getJSONObject(0);
            JSONArray param = j.getJSONArray("elements");
            JSONObject elements = param.getJSONObject(0);
            JSONObject d_time = elements.getJSONObject("duration");
            JSONObject d_distance = elements.getJSONObject("distance");

            time = d_time.getString("text");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return time;
    }
    public static String TimeVal(String res)
    {
        String t_val=null;

        try {
            JSONObject jsonObject = new JSONObject(res);
            //abc = jsonObject.getString("destination_addresses");

            JSONArray jo = jsonObject.getJSONArray("rows");

            JSONObject j = jo.getJSONObject(0);
            JSONArray param = j.getJSONArray("elements");
            JSONObject elements = param.getJSONObject(0);
            JSONObject d_time = elements.getJSONObject("duration");

            t_val = d_time.getString("value");

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return t_val;
    }
    public static String Dist_val(String res)
    {
        String d_value=null;

        try {
            JSONObject jsonObject = new JSONObject(res);
            //abc = jsonObject.getString("destination_addresses");

            JSONArray jo = jsonObject.getJSONArray("rows");

            JSONObject j = jo.getJSONObject(0);
            JSONArray param = j.getJSONArray("elements");
            JSONObject elements = param.getJSONObject(0);
            JSONObject d_time = elements.getJSONObject("duration");
            JSONObject d_distance = elements.getJSONObject("distance");

             d_value = d_distance.getString("value");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return d_value;
    }
}
