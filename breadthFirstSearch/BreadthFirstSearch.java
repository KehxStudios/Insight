package com.kehxstudios.insight.breadthFirstSearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kehxstudios.insight.GameObject;
import com.kehxstudios.insight.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadth_first_search);

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
                    }
                    actor.addEdge(movie);
                    movie.addEdge(actor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        graph.setStart(graph.getActor("Mickey Rourke"));
        graph.setEnd(graph.getActor("Kevin Bacon"));

        graph.search();
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
