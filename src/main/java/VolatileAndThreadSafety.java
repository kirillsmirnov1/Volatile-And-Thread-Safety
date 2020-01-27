import java.util.concurrent.atomic.AtomicInteger;

public class VolatileAndThreadSafety {

    static AtomicInteger threadsInWork = new AtomicInteger(0);
    static SharedObject sharedObject = new SharedObject();
    static int threads = 10000;
    static int repetitions = 10000;

    static boolean allThreadsStarted = false;

    public static void main(String[] args) {

        for(int i = 0; i < threads; ++i){
            new ThreadWithSharedObject(i).start();
            threadsInWork.incrementAndGet();
        }

        allThreadsStarted = true;

    }

    static void threadFinished(){
        if(threadsInWork.decrementAndGet() == 0 && allThreadsStarted) {

            String expectedNumber = "" + threads*repetitions;
            String actualNumber = String.format("%" + expectedNumber.length() + "d", sharedObject.getCount());

            System.out.println("\nexpectation: " + expectedNumber +
                               "\nreality:     " + actualNumber);
        }
    }

    static class ThreadWithSharedObject extends Thread{
        private int threadNumber;

        ThreadWithSharedObject(int threadNumber){
            this.threadNumber = threadNumber;
        }

        @Override
        public void run() {
            System.out.println(threadNumber + " thread start");

            for(int i = 0; i < repetitions; ++i){
                sharedObject.incrementCount();
            }

            threadFinished();
        }
    }

    static class SharedObject {
        private volatile int count = 0;

        public void incrementCount() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
