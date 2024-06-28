import java.util.*;
import java.io.*;

public class Quiz1 {
    public static void main(String[] args) throws IOException {

        Locale.setDefault(Locale.ENGLISH);
        String fileName = args[0];
        Scanner scanner = new Scanner(new File(fileName));

        ArrayList<String> ignoreList = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        readInputFile(scanner, ignoreList, titles);
        scanner.close();

        TreeMap<String, ArrayList<String>> keywordIndex = new TreeMap<>();

        for (String title : titles) {
            String[] words = title.split("\\s+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (!ignoreList.contains(word)) {
                    String[] updated_words = words.clone();
                    updated_words[i] = word.toUpperCase();
                    String updatedTitle = String.join(" ", updated_words);
                    ArrayList<String> titlesList = keywordIndex.get(word.toUpperCase());
                    if (titlesList == null) {
                        titlesList = new ArrayList<>();
                        keywordIndex.put(word.toUpperCase(), titlesList);
                    }
                    titlesList.add(updatedTitle);
                }
            }
        }

        keywordIndex.forEach((keyword, titles_indexed) -> {
            titles_indexed.forEach(System.out::println);
        });
    }
    private static void readInputFile(Scanner scanner, ArrayList<String> ignoreList, ArrayList<String> titles) {
        boolean readingIgnoreList = true;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("...")) {
                readingIgnoreList = false;
                continue;
            }
            ArrayList<String> targetList = readingIgnoreList ? ignoreList : titles;
            targetList.add(line.toLowerCase());
        }
    }
}
