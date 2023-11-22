public class Main {
    public static void main(String[] args) {
        ETL etlProcessor = new ETL();
        System.out.println("Initializing ETL data processing...");

        long delayMs = 5000;
        // starting stream generator thread
        Thread streamThread = new Thread(new streamGenerator(etlProcessor, delayMs));
        streamThread.start();

        try {
            // letting stream run for 1 minute
            Thread.sleep(60000);
            // After the condition is met, stop the stream generator
            streamThread.interrupt();
            streamThread.join(); // Wait for the stream generator thread to finish

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ETL processing complete.");
        etlProcessor.printTotalTransactionsProcessed();
    }
}