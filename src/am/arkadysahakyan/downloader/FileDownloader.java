package am.arkadysahakyan.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * @author Arkady Sahakyan
 */
public class FileDownloader implements Runnable {

    private final byte[] buffer;

    private final URLConnection resourceConnection;

    private boolean pause = false;

    public FileDownloader(byte[] buffer, URLConnection resourceConnection) {
        this.buffer = buffer;
        this.resourceConnection = resourceConnection;
    }

    @Override
    public void run() {
        try(InputStream in = resourceConnection.getInputStream()) {
            int nextPtr = 0;
            int data;
            while ((data = in.read()) != -1) {
                while (pause) {
                    synchronized (this) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                buffer[nextPtr++] = (byte) data;
            }
        } catch (IOException e) {
            System.err.print(e);
        }
    }

    public synchronized void dcontinue() {
        pause = false;
        notify();
    }

    public synchronized void dpause() {
        pause = true;
    }
}
