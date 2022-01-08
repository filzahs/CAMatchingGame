package com.example.MatchingGame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements  AdapterView.OnItemClickListener{

    private String[] imgArray = new String[20];
    int imgCount = 0;
    ProgressBar progressBar;
    TextView progressBarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setMax(20);
        progressBarText = findViewById(R.id.progressbarText);

        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText mUrl = findViewById(R.id.url);
                String urlLink = mUrl.getText().toString();
                parseUrl(urlLink);


            }
        });

    }

    //parse the html page
    //download the images
    // load the imageview
    public void parseUrl(String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup
                            .connect(url)
                            .timeout(10 * 1000)
                            .get();

                    Elements images =
                            doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");

                    for (Element item : images) {
                        String imgLink = item.attr("src");
                        if(imgCount < imgArray.length) {
                            startDownload(imgLink);
                            imgCount++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(progressBar != null) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        progressBar.setProgress(imgCount);
                                        progressBarText.setVisibility(View.VISIBLE);
                                        progressBarText.setText(String.format("Download %d of %d images", imgCount, 20));
                                    }
                                    if(imgCount >= 20) {
                                        progressBar.setVisibility(View.GONE);
                                        progressBarText.setText(String.format("Download completed. Please select 6 images."));
                                    }
                                }
                            });

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void startDownload(String imgLink) {
        String destFileName = UUID.randomUUID().toString() + imgLink.lastIndexOf(".") + 1;
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File destFile = new File(dir, destFileName);
        imgArray[imgCount] = destFileName;

        //create a bg thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                ImageDownloader imgDl = new ImageDownloader();
                if(imgDl.downloadImage(imgLink, destFile)) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //need to use the myadapter to populate view
                            GridView gridView = findViewById(R.id.gridView);
                            if(gridView != null) {
                                gridView.setAdapter(new MyAdapter(MainActivity.this, imgArray));
                                gridView.setOnItemClickListener(MainActivity.this);
                            }

                        }
                    });
                }

            }
        }).start();



    }

    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {

    }


}