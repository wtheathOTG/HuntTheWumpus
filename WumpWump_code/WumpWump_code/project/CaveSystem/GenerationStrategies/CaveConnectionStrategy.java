package project.CaveSystem.GenerationStrategies;

import project.CaveSystem.Room;

public interface CaveConnectionStrategy {

    public void execute(Room[] rooms, int totalRooms, int caveWidth, float probability);
}
