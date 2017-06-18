package com.kehxstudios.insight;

import android.content.Intent;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kehxstudios.insight.aStarPathfinding.AStarPathfindingActivity;
import com.kehxstudios.insight.binaryTree.BinaryTreeActivity;
import com.kehxstudios.insight.breadthFirstSearch.BreadthFirstSearchActivity;
import com.kehxstudios.insight.evolutionarySteering.EvolutionarySteeringActivity;
import com.kehxstudios.insight.login.LoginActivity;
import com.kehxstudios.insight.mazeGenerator.MazeGeneratorActivity;
import com.kehxstudios.insight.smartRockets.SmartRocketsActivity;
import com.kehxstudios.insight.starfield.StarfieldActivity;
import com.kehxstudios.insight.terrainGeneration.TerrainGenerationActivity;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer buttonSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        buttonSound = MediaPlayer.create(MainActivity.this, R.raw.bubble);

        TextView textView = (TextView) findViewById(R.id.title);
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button binaryTreeButton = (Button) findViewById(R.id.binaryTreeButton);
        Button breadthFirstSearchButton = (Button) findViewById(R.id.breadthFirstSearchButton);
        Button smartRocketsButton = (Button) findViewById(R.id.smartRocketsButton);
        Button mazeGeneratorButton = (Button) findViewById(R.id.mazeGeneratorButton);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button aStarPathfinding = (Button) findViewById(R.id.aStarPathfindingButton);
        Button evolutionarySteering = (Button) findViewById(R.id.evolutionarySteeringButton);
        Button starfield = (Button) findViewById(R.id.starfieldButton);
        Button terrainGeneration = (Button) findViewById(R.id.terrainGenerationButton);

        binaryTreeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), BinaryTreeActivity.class));
            }
        });

        breadthFirstSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), BreadthFirstSearchActivity.class));
            }
        });

        smartRocketsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), SmartRocketsActivity.class));
            }
        });

        mazeGeneratorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), MazeGeneratorActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        aStarPathfinding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), AStarPathfindingActivity.class));
            }
        });

        evolutionarySteering.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), EvolutionarySteeringActivity.class));
            }
        });

        starfield.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), StarfieldActivity.class));
            }
        });

        terrainGeneration.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), TerrainGenerationActivity.class));
            }
        });



        ImageButton facebookButton = (ImageButton) findViewById(R.id.facebookButton);
        ImageButton twitterButton = (ImageButton) findViewById(R.id.twitterButton);
        ImageButton meButton = (ImageButton) findViewById(R.id.meButton);
        ImageButton gitHubButton = (ImageButton) findViewById(R.id.gitHubButton);
        ImageButton linkedinButton = (ImageButton) findViewById(R.id.linkedinButton);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/KehxStudios")));
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/KehxStudios")));
            }
        });

        meButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.KehxStudios.com")));
            }
        });

        gitHubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KehxStudios")));
            }
        });

        linkedinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/christopher-reid-86b1ba132/")));
            }
        });
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

}
