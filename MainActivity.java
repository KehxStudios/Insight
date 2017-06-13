package com.kehxstudios.insight;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.kehxstudios.insight.binaryTree.BinaryTreeActivity;
import com.kehxstudios.insight.breadthFirstSearch.BreadthFirstSearch;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer buttonSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        buttonSound = MediaPlayer.create(MainActivity.this, R.raw.bubble);

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), BinaryTreeActivity.class));
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(getApplicationContext(), BreadthFirstSearch.class));
            }
        });

        ImageButton facebookButton = (ImageButton) findViewById(R.id.facebookButton);
        ImageButton twitterButton = (ImageButton) findViewById(R.id.twitterButton);
        ImageButton gitHubButton = (ImageButton) findViewById(R.id.gitHubButton);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/KehxStudios")));
            }
        });

        twitterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/KehxStudios")));
            }
        });

        gitHubButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSound.start();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KehxStudios")));
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
