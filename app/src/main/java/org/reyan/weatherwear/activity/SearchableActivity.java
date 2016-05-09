package org.reyan.weatherwear.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.reyan.weatherwear.R;
import org.reyan.weatherwear.domain.City;
import org.reyan.weatherwear.thread.SearchAsyncTask;

import java.net.URLEncoder;
import java.util.List;

public class SearchableActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new SearchAsyncTask(this).execute(URLEncoder.encode(query));
        }

        listView = (ListView) findViewById(R.id.search_list_view);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                City city = (City) parent.getItemAtPosition(position);
                Intent intent = new Intent(SearchableActivity.this, SettingsActivity.class);
                intent.putExtra("id", city.getId());
                intent.putExtra("cityName", city.getCityName());
                intent.putExtra("countryName", city.getCountryName());
                SearchableActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.menu_searchable, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    public void show(List<City> result) {
        // arrayAdapter.clear();
        arrayAdapter.addAll(result);
    }
}
