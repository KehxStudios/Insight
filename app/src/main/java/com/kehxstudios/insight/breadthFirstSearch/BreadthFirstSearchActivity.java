/*******************************************************************************
 * Copyright 2017 See AUTHORS file.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/

package com.kehxstudios.insight.breadthFirstSearch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kehxstudios.insight.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 *
 */

public class BreadthFirstSearchActivity extends AppCompatActivity {

    private JSONArray jsonArray;
    private ArrayList<String> actors;

    private Spinner actor1Spinner, actor2Spinner;
    private Button searchButton;
    private TextView resultsText;

    public Node start, end;
    public ArrayList<Node> movieNodes;
    public ArrayList<Node> actorNodes;
    private ArrayList<Node> queue;

    private ArrayList<String> resultPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breadth_first_search);

        actors = new ArrayList<>();
        movieNodes = new ArrayList<>();
        actorNodes = new ArrayList<>();
        start = null;
        end = null;

        loadJsonFile();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Node movie = new Node(jsonObject.getString("title"));
                movieNodes.add(movie);

                JSONArray castArray = jsonObject.getJSONArray("cast");
                for (int j = 0; j < castArray.length(); j++) {
                    Node actor = getActor(castArray.get(j).toString());
                    if (actor == null) {
                        actor = new Node(castArray.get(j).toString());
                        actorNodes.add(actor);
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
        resultsText = (TextView) findViewById(R.id.breadth_resultsText);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, actors);
        actor1Spinner.setAdapter(adapter);
        actor2Spinner.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setStart(getActor(actor1Spinner.getSelectedItem().toString()));
                setEnd(getActor(actor2Spinner.getSelectedItem().toString()));

                resultsText.setText(search());
            }
        });
    }

    public void setStart(Node node) {
        if (actorNodes.contains(node)) {
            start = node;
        } else {
            start = null;
            Log.d("graph", "actor not found");
        }
    }

    public void setEnd(Node node) {
        if (actorNodes.contains(node)) {
            end = node;
        } else {
            end = null;
            Log.d("graph", "actor not found");
        }
    }

    private Node getActor(String actor) {
        for (Node node : actorNodes) {
            if (node.value.equals(actor)) {
                return node;
            }
        }
        return null;
    }

    private void reset() {
        for (Node node : actorNodes) {
            node.parent = null;
            node.searched = false;
        }
        for (Node node : movieNodes) {
            node.parent = null;
            node.searched = false;
        }
    }

    public String search() {
        if (start == null || end == null || start == end) {
            Log.d("search", "start or end is null or the same");
            return "Error";
        }
        reset();
        start.searched = true;
        queue = new ArrayList<>();
        queue.add(start);

        while(queue.size() > 0) {
            Node current = queue.get(0);
            if (current == end) {
                return printSearchResults(current);
            } else {
                for (int i = 0; i < current.edges.size(); i++) {
                    if (!current.edges.get(i).searched) {
                        current.edges.get(i).searched = true;
                        current.edges.get(i).parent = current;
                        queue.add(current.edges.get(i));
                    }
                }
            }
            queue.remove(current);
        }
        return "Not Found";
    }


    public String printSearchResults(Node node) {
        resultPath = new ArrayList<>();
        resultPath.add(0, node.value);
        Node current = node;
        while (current.parent != null) {
            current = current.parent;
            resultPath.add(0, current.value);
        }
        String result = "";
        for (String path : resultPath) {
            result += path + "\n";
        }
        return result;
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
