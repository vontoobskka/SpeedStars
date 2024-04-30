package speedstars;

public class Timer implements Runnable {
    private static final int TARGET_FPS = 60; // Target frames per second
    private static final long TARGET_FRAME_TIME = 1000000000 / TARGET_FPS; // Target frame time in nanoseconds

    private boolean running = false;

    public Timer(int i, Object object) {
		// TODO Auto-generated constructor stub
	}

	public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastUpdateTime = System.nanoTime();
        long lastRenderTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateTime = now - lastUpdateTime;
            lastUpdateTime = now;

            // Update game logic
            update(updateTime);

            // Render game graphics
            render();

            // Calculate sleep time to achieve target frame rate
            long renderTime = System.nanoTime() - lastRenderTime;
            long sleepTime = (TARGET_FRAME_TIME - renderTime) / 1000000;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lastRenderTime = System.nanoTime();
        }
    }

    private void update(long updateTime) {
        // Update game logic here
    }

    private void render() {
        // Render game graphics here
    }
}
