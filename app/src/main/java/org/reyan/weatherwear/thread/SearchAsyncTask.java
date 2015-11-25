package org.reyan.weatherwear.thread;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reyan.weatherwear.activity.SearchableActivity;
import org.reyan.weatherwear.domain.City;
import org.reyan.weatherwear.service.JSONService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reyan on 11/7/15.
 */
public class SearchAsyncTask extends AsyncTask<String, Void, List<City>> {

    private SearchableActivity searchableActivity;
    private ProgressDialog progressDialog;

    public SearchAsyncTask(SearchableActivity searchableActivity) {
        this.searchableActivity = searchableActivity;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(searchableActivity);
        progressDialog.show();
    }

    @Override
    protected List<City> doInBackground(String... params) {
        List<City> result = new ArrayList<City>();
        try {
            JSONArray jsonArray = JSONService
                    .getJSONObject(params[0], JSONService.SEARCH_CITY)
                    .getJSONArray("RESULTS");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String id = item.getString("l");
                double latitude = item.getDouble("lat");
                double longitude = item.getDouble("lon");
                String cityName = item.getString("name");
                String countryName = item.getString("c");
                String type = item.getString("type");
                if ("city".equals(type)) {
                    result.add(new City(id, latitude, longitude, cityName, countryName));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onPostExecute(List<City> result) {
        progressDialog.dismiss();
        searchableActivity.show(result);
    }
}
