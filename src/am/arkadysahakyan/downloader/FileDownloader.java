package am.arkadysahakyan.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader implements Runnable {

    private final int[] buffer;

    private final URL target;

    private int next = 0;

    public FileDownloader(int[] buffer, URL target) {
        this.buffer = buffer;
        this.target = target;
    }


    @Override
    public void run() {
        URLConnection urlConnection = null;
        try {
            urlConnection = target.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(InputStream in = urlConnection.getInputStream()) {
            int data;
            while ((data = in.read()) != -1) {
                buffer[next++] = data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
