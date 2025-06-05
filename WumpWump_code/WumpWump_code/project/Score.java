package project;

import java.io.Serializable;

public class Score implements Serializable {
    public String player;
    public long seed;

    public int turnNum = 0;
    public int goldCoins = 0;
    public int arrows = 3;
    public boolean wumpusKilled = false;

    public void tryAddGold() {
        if (ScoreManager.goldObtained < 100) {
            goldCoins++;
            ScoreManager.goldObtained++;
        }
    }
    public boolean tryRemoveGold() {
        if (goldCoins == 0) return false;
        goldCoins--;
        return true;
    }
    public boolean tryRemoveArrows() { return (--arrows > 0); }
    public int getScore() {
        return 100 - turnNum + goldCoins + arrows * 5 + (wumpusKilled ? 50 : 0);
    }

    @Override
    public String toString() {
        return   "player: "+player
                +" seed: "+seed
                +" turnNum: "+turnNum
                +" goldCoins: "+goldCoins
                +" arrows: "+arrows
                +" wumpusKilled: "+wumpusKilled;
    }
}
