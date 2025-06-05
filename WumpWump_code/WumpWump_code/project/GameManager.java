package project;

import java.util.*;

import project.CaveSystem.Room;
import project.GameState.DungeonState;
import project.GameState.IState;
import project.GameState.MainMenuState;
import project.GameState.State;
import project.UI.UIManager;

public class GameManager {
   public static Random rand = new Random();
   public static Scanner scan = new Scanner(System.in);

   public static Room activeRoom;

   public static ScoreManager scoreMan;

   public static boolean exitApp = false;

   private static IState currentState;

   private static MainMenuState mainMenuState;
   private static DungeonState dungeonState;

   public static void main(String[] args) {
      UIManager.initSingleton();
      scoreMan = new ScoreManager();

      initializeStates();
      setState(State.MainMenu);

      while (!exitApp) currentState.Run();
   }

   public static void setState(State state) {
      currentState = switch (state) {
         case MainMenu -> mainMenuState;
         case Dungeon -> dungeonState;
      };
      currentState.Init();
   }

   private static void initializeStates() {
      mainMenuState = new MainMenuState();
      dungeonState = new DungeonState();
   }
}