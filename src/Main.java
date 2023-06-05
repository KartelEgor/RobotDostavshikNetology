import java.util.*;


public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new TreeMap<>();
    public static final int routes = 1000;

    public static void main(String[] args) {
        threadsRun();
        printResult();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static void threadsRun() {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < routes; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                int countOf_R = countOfR(route);

                synchronized (sizeToFreq) {
                    Integer freq = sizeToFreq.getOrDefault(countOf_R, 0);
                    sizeToFreq.put(countOf_R, freq + 1);
                }
            });
            thread.start();
            threadList.add(thread);
        }

        try {
            for (Thread thread : threadList) thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countOfR(String str) {
        int count = 0;
        for (char element : str.toCharArray()) {
            if (element == 'R') count++;
        }
        return count;
    }

    public static void printResult() {
        Map.Entry<Integer, Integer> maxResult = sizeToFreq.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        System.out.printf("Самое частое количество повторений: %s (встречалось %s раз) \n", maxResult.getKey(), maxResult.getValue());
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            if (entry == maxResult) continue;
            System.out.printf(" - %s (%s раз) \n", entry.getKey(), entry.getValue());
        }
    }
}
