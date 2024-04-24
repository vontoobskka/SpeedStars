package speedstars;

public class Timer {
    private long startTime;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public double stop() {
        long endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0; // Convert milliseconds to seconds
    }
}
