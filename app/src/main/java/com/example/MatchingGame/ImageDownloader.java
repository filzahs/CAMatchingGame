package com.example.MatchingGame;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader {
    protected Boolean downloadImage(String imgURL, File destfile) {
        try{
            URL url = new URL(imgURL);
            URLConnection conn = url.openConnection();

            InputStream in = conn.getInputStream();
            FileOutputStream out = new FileOutputStream(destfile);

            byte[] buf = new byte[1024];
            int bytesRead = -1;
            while((bytesRead = in.read(buf)) != -1) {
                out.write(buf, 0, bytesRead);
            }
            out.close();
            in.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

}
