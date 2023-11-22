public class streamGenerator implements Runnable {

    private final ETL dataProcessor;
    private final long delayMs;

    public streamGenerator(ETL dataProcessor, long delayMs) {
        this.dataProcessor = dataProcessor;
        this.delayMs = delayMs;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                dataProcessor.readData();
                Thread.sleep(delayMs);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Properly handle the interruption
        }
    }
}
