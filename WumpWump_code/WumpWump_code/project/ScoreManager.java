package project;

import java.util.ArrayList;
import java.util.Arrays;

public class ScoreManager {
   public static Score score;
   private ArrayList<Score> topScores;

   public static int goldObtained;

   public ScoreManager() {
      resetScore();
      loadTopScores();
   }

   public void trySaveCurrentScore() {
      if (topScores == null) return;

      topScores.add(score);

      var topScoresArray = topScores.toArray(new Score[topScores.size()]);
      Helper.quickSort(
              topScoresArray,
              0,
              topScoresArray.length - 1,
              (i, pivot) -> topScoresArray[i].getScore() > topScoresArray[pivot].getScore()
      );

      var trimmed = topScoresArray.length > 10
              ? Arrays.copyOf(topScoresArray, 10)
              : topScoresArray;

      topScores = new ArrayList<>(Arrays.asList(trimmed));
      FileManager.write(trimmed, "TopScores");
   }

   public static void resetScore() {
      score = new Score();
      goldObtained = 0;
   }

   public void loadTopScores() {
      var topScoresArray = FileManager.read(Score[].class, "TopScores");

      if (topScoresArray == null) topScores = new ArrayList<>();
      else topScores = new ArrayList<>(Arrays.asList(topScoresArray));
   }

   public String[] getTopScoresText() {
      var scoreTexts = new String[topScores.size()];
      for (var i = 0; i < scoreTexts.length; i++) {
         var score = topScores.get(i);
         scoreTexts[i] = "Player: "+score.player+" Score: "+score.getScore()
                              + " Wumpus "+(score.wumpusKilled ? "slain!" : "escaped!")
                              + " Seed: "+score.seed;
      }
      return scoreTexts;
   }
}