import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isWordEnd;
    List<WordWeightPair> wordWeights;

    public TrieNode() {
        children = new HashMap<>();
        isWordEnd = false;
        wordWeights = new ArrayList<>();
    }
}

class WordWeightPair {
    long weight;
    String word;

    public WordWeightPair(long weight, String word) {
        this.weight = weight;
        this.word = word;
    }
}
class Trie {
    TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insertWord(String word, long weight) {
        TrieNode currentNode = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (!currentNode.children.containsKey(ch)) {
                currentNode.children.put(ch, new TrieNode());
            }
            currentNode = currentNode.children.get(ch);
            currentNode.wordWeights.add(new WordWeightPair(weight, word));
        }
        currentNode.isWordEnd = true;
    }

    public List<WordWeightPair> getWordsWithPrefix(String prefix) {
        TrieNode currentNode = root;
        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (!currentNode.children.containsKey(ch)) {
                return Collections.emptyList();
            }
            currentNode = currentNode.children.get(ch);
        }
        return new ArrayList<>(currentNode.wordWeights);
    }
}

public class Quiz4 {

    public static void main(String[] args) {


        String databaseFilePath = args[0];
        String queryFilePath = args[1];

        Trie trie = new Trie();

        try (BufferedReader dbReader = new BufferedReader(new FileReader(databaseFilePath))) {
            String line = dbReader.readLine();
            if (line != null) {
                int numberOfEntries = Integer.parseInt(line.trim());
                for (int i = 0; i < numberOfEntries; i++) {
                    line = dbReader.readLine();
                    if (line != null) {
                        String[] parts = line.split("\t");
                        long weight = Long.parseLong(parts[0]);
                        String word = parts[1].toLowerCase();
                        trie.insertWord(word, weight);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        try (BufferedReader queryReader = new BufferedReader(new FileReader(queryFilePath))) {
            String line;
            while ((line = queryReader.readLine()) != null) {
                String[] queryParts = line.split("\t");
                if (queryParts.length == 2) {
                    String query = queryParts[0].toLowerCase();
                    int limit = Integer.parseInt(queryParts[1]);

                    List<WordWeightPair> results = trie.getWordsWithPrefix(query);
                    results.sort((a, b) -> Long.compare(b.weight, a.weight));

                    System.out.println("Query received: \"" + query + "\" with limit " + limit + ". Showing results:");
                    if (limit == 0 || results.isEmpty()) {
                        System.out.println("No results.");
                    } else {
                        for (int i = 0; i < Math.min(limit, results.size()); i++) {
                            System.out.println("- " + results.get(i).weight + " " + results.get(i).word);
                        }
                    }
                } else {
                    System.err.println("Invalid query format: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

    }
}
