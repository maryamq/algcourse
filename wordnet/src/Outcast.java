/**
 * Created by maryamq on 4/3/15.
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;

    }         // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int distance = -1;
        String noun = null;
        for(int i=0; i<nouns.length; i++) {
            int currentDistance = 0;
            String currentNoun = nouns[i];
            for(int j=0; j<nouns.length;j++) {
                int distTo = wordNet.distance(currentNoun, nouns[j]);
                if (distTo == -1) {
                    continue;
                }
                currentDistance += distTo;
            }
            if (currentDistance > distance) {
                distance = currentDistance;
                noun = currentNoun;
            }

        }
        return noun;

    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}