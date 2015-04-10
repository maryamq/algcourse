import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordNet {
    private Map<String, List<Integer>> allNouns;
    private Map<Integer, String> allKeys;
    private Digraph digraph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsetsFile, String hypernymsFile) {
        if (synsetsFile== null || hypernymsFile == null) {
            throw new NullPointerException("FileNames cannot be  null");
        }
        allNouns = new HashMap<String, List<Integer>>();
        allKeys = new HashMap<Integer, String>();

        int vertexCount = 0;
        try{
            BufferedReader synsetsReader = new BufferedReader(new FileReader(synsetsFile));
            String input = null;
            while ((input=synsetsReader.readLine()) != null) {
                vertexCount++;
                String[] data = input.split(",");
                int key = Integer.parseInt(data[0]);
                String nounSet[] = data[1].split(" ");
                allKeys.put(key, data[1]);
                for (String n: nounSet) {
                    if (!allNouns.containsKey(n)) {
                        allNouns.put(n, new ArrayList<Integer>());
                    }
                    allNouns.get(n).add(key);
                }
            }
            digraph = new Digraph(vertexCount);
            BufferedReader hyperReader = new BufferedReader(new FileReader(hypernymsFile));
            while ((input=hyperReader.readLine()) != null) {
                String[] data = input.split(",");
                int v = Integer.parseInt(data[0]);
                for (int i=1; i < data.length; i++) {
                    digraph.addEdge(v, Integer.parseInt(data[i]));
                }
            }
            sap = new SAP(digraph);

        } catch (IOException e) {

        }


    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return allNouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException("Noun is null");
        }
        return allNouns.containsKey(word);

    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        checkNounIsValid(nounA);
        checkNounIsValid(nounB);
        List<Integer> aList = allNouns.get(nounA);
        List<Integer> bList =  allNouns.get(nounB);
        return sap.length(aList, bList);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        checkNounIsValid(nounA);
        checkNounIsValid(nounB);

        List<Integer> aList = allNouns.get(nounA);
        List<Integer> bList =  allNouns.get(nounB);
        int ancestor = sap.ancestor(aList, bList);
        if (allKeys.containsKey(ancestor)) {
            return allKeys.get(ancestor);
        }
        return null;
    }

    private void checkVertex(int i) {
        if (i < 0 || i > digraph.V()) {
            throw new NullPointerException("Vertex out of range");
        }
    }

    private void checkNounIsValid(String n) {
        if (n == null) {
            throw new NullPointerException("Noun is null");
        }
        if (!allNouns.containsKey(n)) {
            throw new IllegalArgumentException("Noun " + n + " is invalid");
        }
    }


}

