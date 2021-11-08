package am.arkadysahakyan.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * Downloads a file from the Internet.
 * @author Arkady Sahakyan
 */
public class FileDownloader implements Runnable {

    private final byte[] buffer;

    private final URLConnection resourceConnection;

    private State state = State.PROCESS;

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
                if (State.STOPPED.equals(state)) return;
                while (State.PAUSED.equals(state)) {
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

    public synchronized void continueProcess() {
        state = State.PROCESS;
        notify();
    }

    public synchronized void pauseProcess() {
        state = State.PAUSED;
    }

    public synchronized void stopProcess() {
        state = State.STOPPED;
    }

    public Status getStatus() {
        int passed = buffer.length;
        int left = resourceConnection.getContentLength() - passed;
        return new Status(passed, left);
    }

    public State getState() {
        return state;
    }

    private static class Status {
        private final int passed;
        private final int left;

        public Status(int passed, int left) {
            this.passed = passed;
            this.left = left;
        }

        public int getPassed() {
            return passed;
        }

        public int getLeft() {
            return left;
        }
    }

    public static enum State {
        PROCESS, PAUSED, STOPPED
    }
}