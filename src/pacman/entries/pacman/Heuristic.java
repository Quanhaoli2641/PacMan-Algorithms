package pacman.entries.pacman;

import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.internal.Node;

/**
 * Created by QuanhaoLi on 10/20/16.
 */
public class Heuristic {

    public static double heuristicFuncForNext (Game game) {

        if (game.gameOver()) {
            if (game.getNumberOfActivePills() == 0 && game.getNumberOfActivePowerPills() == 0) {
                return Double.MAX_VALUE;
            } else {
                return Double.MIN_VALUE;
            }
        }

        double score = game.getScore();

        double pillScore = 0;
        for (int pill : game.getActivePillsIndices()) {
            pillScore += 100 / game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pill);
        }
        for (int pill : game.getActivePowerPillsIndices()) {
            pillScore += 500 / game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pill);
        }

        double ghostScore = 0;
        for (GHOST ghost : GHOST.values()) {
            int ghostCloseness = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                if (ghostCloseness < 20) {
                    ghostScore -= 9999 / (ghostCloseness * ghostCloseness);
                }
            } else if (game.getGhostEdibleTime(ghost) > 0) {
                if (game.wasPowerPillEaten()) {
                    ghostScore += 500 / (ghostCloseness * ghostCloseness);
                } else {
                    ghostScore += 100 / (ghostCloseness * ghostCloseness);
                }
            }
        }


        return score * 10 + pillScore + ghostScore + game.getPacmanNumberOfLivesRemaining() * 1000;
    }
}


