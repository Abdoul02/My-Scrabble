import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class MainApp {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter file full path:");

        String filePath = scanner.nextLine();

        long startTime = System.nanoTime();
        readUsingJava8(filePath);
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds : " + timeElapsed / 1000000);
    }

    //33.45 minutes
    private static void readUsingJava8(String filePath) throws Exception {
        String word = "", mostSeven = "";
        int count, maxCount = 0, countSeven, maxSevenChar = 0;
        ArrayList<String> words = new ArrayList<>();
        int max = Integer.MIN_VALUE;
        String highestScoreWord = "";

        Stream<String> lines = Files.lines(Paths.get(filePath));
        lines.forEach(line -> {
                    String[] string = line.toUpperCase().split("([,.\\s]+)");
                    words.addAll(Arrays.asList(string));
                }
        );

        for (int i = 0; i < words.size(); i++) {
            count = 1;
            countSeven = 1;

            for (int j = i + 1; j < words.size(); j++) {
                if (words.get(i).equals(words.get(j))) {
                    count++;
                    if (words.get(i).length() == 7) {
                        countSeven++;
                    }
                }
            }

            if (count > maxCount) {
                maxCount = count;
                word = words.get(i);
            }

            if (countSeven > maxSevenChar) {
                maxSevenChar = countSeven;
                mostSeven = words.get(i);
            }
            if (getScore(words.get(i)) > max) {
                max = getScore(words.get(i));
                highestScoreWord = words.get(i);
            }
        }
        System.out.println("Most frequent word: " + word.toLowerCase() + " occurred " + maxCount + " times");
        System.out.println("Most frequent 7-character word: " + mostSeven.toLowerCase() + " occurred " + maxSevenChar + " times");
        System.out.println("Highest scoring word(s) (according to Scrabble): " + highestScoreWord.toLowerCase() + " with a score of " + max);
    }

    //38.37
    private static void readInputStream(String filePath) throws IOException {
        String line, word = "", mostSeven = "";
        int count, maxCount = 0, countSeven, maxSevenChar = 0;
        int max = Integer.MIN_VALUE;
        String highestScoreWord = "";

        ArrayList<String> words = new ArrayList<>();

        try (FileInputStream inputStream = new FileInputStream(filePath); Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] string = line.toLowerCase().split("([,.\\s]+)");
                words.addAll(Arrays.asList(string));
            }
            for (int i = 0; i < words.size(); i++) {
                count = 1;
                countSeven = 1;

                for (int j = i + 1; j < words.size(); j++) {
                    if (words.get(i).equals(words.get(j))) {
                        count++;
                        if (words.get(i).length() == 7) {
                            countSeven++;
                        }
                    }
                }
                //If maxCount is less than count then store value of count in maxCount
                //and corresponding word to variable word
                if (count > maxCount) {
                    maxCount = count;
                    word = words.get(i);
                }

                if (countSeven > maxSevenChar) {
                    maxSevenChar = countSeven;
                    mostSeven = words.get(i);
                }

                if (getScore(words.get(i)) > max) {
                    max = getScore(words.get(i));
                    highestScoreWord = words.get(i);
                }
            }
            System.out.println("Most frequent word: " + word + " occurred " + maxCount + " times");
            System.out.println("Most frequent 7-character word: " + mostSeven + " occurred " + maxSevenChar + " times");
            System.out.println("Highest scoring word(s) (according to Scrabble): " + highestScoreWord + " with a score of " + max);
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
    }

    private static int getScore(String word) {
        char[] arrayChar = word.toCharArray();
        int score = 0;

        for (Character letter : arrayChar) {
            score += letterValue(letter);
        }
        return score;
    }

    static int letterValue(char letter) {
        switch (letter) {
            case 'G':
            case 'D':
                return 2;

            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;

            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;

            case 'K':
                return 5;

            case 'J':
            case 'X':
                return 8;

            case 'Q':
            case 'Z':
                return 10;

            default:
                return 1;
        }
    }
}
