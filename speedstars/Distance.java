package speedstars;

public class Distance {
    public static final int[] DISTANCES = {100, 200, 400, 800, 1500}; // Distances in meters
    public static final int MAX_DISTANCE = 1500;

    // Method to get a random distance from the array of realistic distances
    public static int getRandomDistance() {
        int randomIndex = (int) (Math.random() * DISTANCES.length);
        return DISTANCES[randomIndex];
    }
}
