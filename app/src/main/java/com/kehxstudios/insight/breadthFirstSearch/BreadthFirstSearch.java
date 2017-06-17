package com.kehxstudios.insight.breadthFirstSearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.kehxstudios.insight.R;
import com.kehxstudios.insight.smartRockets.SmartRocketsActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ReidC on 2017-06-12.
 */

public class BreadthFirstSearch extends AppCompatActivity {

    private JSONArray jsonArray;
    private Graph graph;
    private ArrayList<String> actors;

    private Spinner actor1Spinner, actor2Spinner;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadth_first_search);

        actors = new ArrayList<>();

        loadJsonFile();
        graph = new Graph();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Node movie = new Node(jsonObject.getString("title"));
                graph.addMovie(movie);

                JSONArray castArray = jsonObject.getJSONArray("cast");
                for (int j = 0; j < castArray.length(); j++) {
                    Node actor = graph.getActor(castArray.get(j).toString());
                    if (actor == null) {
                        actor = new Node(castArray.get(j).toString());
                        graph.addActor(actor);
                        actors.add(actor.value);
                    }
                    actor.addEdge(movie);
                    movie.addEdge(actor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        actor1Spinner = (Spinner) findViewById(R.id.actor1Spinner);
        actor2Spinner = (Spinner) findViewById(R.id.actor2Spinner);
        searchButton = (Button) findViewById(R.id.searchButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, actors);
        actor1Spinner.setAdapter(adapter);
        actor2Spinner.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                graph.setStart(graph.getActor(actor1Spinner.getSelectedItem().toString()));
                graph.setEnd(graph.getActor(actor2Spinner.getSelectedItem().toString()));

                graph.search();
            }
        });
    }

    private void loadJsonFile() {
        try {
            InputStream is = getResources().openRawResource(R.raw.kevin_bacon);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            JSONObject jsonObject = new JSONObject(new String(buffer, "UTF-8"));
            jsonArray = jsonObject.getJSONArray("movies");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
