package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.AggressiveGhosts;
import pacman.game.Game;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

import java.util.Random;
import java.math.*;

/**
 * Created by QuanhaoLi on 10/20/16.
 */

public class SimulatedAnnealing extends Controller<MOVE> {

    private AggressiveGhosts ghosts = new AggressiveGhosts();
    private static double t = Double.MAX_VALUE;

    private Random rand = new Random();

    @Override
    public MOVE getMove(Game game, long timeDue) {

       MOVE nextMove = MOVE.NEUTRAL;
        double indexVal = Heuristic.heuristicFuncForNext(game);
        MOVE[] moves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

        int i = rand.nextInt(moves.length - 1);
        Game copy = game.copy();
        copy.advanceGame(moves[i], ghosts.getMove());

        t -= (double)(copy.getScore() + copy.getNumberOfActivePills() + copy.getNumberOfActivePowerPills());

        double value = Heuristic.heuristicFuncForNext(copy);

        if (copy.wasPacManEaten()) {
            return moves[i].opposite();
        }
        else {
            if (value > indexVal) {
                nextMove = moves[i];
                indexVal = value;
            }
            else {
                double dif = value - indexVal;
                double f = Math.exp(dif/t);
                double p = rand.nextDouble();
                if (f > p) {
                    nextMove = moves[i];
                }
            }

        }
        return nextMove;

    }
}



