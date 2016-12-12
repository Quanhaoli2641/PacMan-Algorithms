package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;

import java.util.Random;


import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/21/16.
 */
public class Evolution extends Controller<MOVE> {

    private StarterGhosts ghosts = new StarterGhosts();
    private MOVE[] best = new MOVE[5];
    private int index = 0;

    public MOVE[] createPopulation (Game game) {

        MOVE[] population = new MOVE[5];
        Random r = new Random();

        for (int i = 0; i < population.length; i++) {
            Game copy = game.copy();
            int score = copy.getScore();
            MOVE[] moves = copy.getPossibleMoves(copy.getPacmanCurrentNodeIndex());
            int index = r.nextInt(moves.length);
            population[i] = moves[index];
            copy.advanceGame(moves[index], ghosts.getMove());
            if (copy.wasPacManEaten() || copy.getScore() < score) {
                i--;
            }
        }

        return population;
    }

    public MOVE[] createChild (Game game, MOVE[] pop1, MOVE[] pop2) {
        MOVE[] child = new MOVE[5];
        for (int i = 0; i < pop1.length; i++) {
            child[i] = pop1[i];
        }
        for (int j = 0; j < child.length; j++) {
            Game copy = game.copy();
            int score1 = checkMove(copy,child[j]);
            int score2 = checkMove(copy, pop2[j]);
            if (score2 > score1) child[j] = pop2[j];
        }
        return child;
    }

    public int checkMove(Game game, MOVE move) {
        Game copy = game.copy();
        copy.advanceGame(move, ghosts.getMove());
        return (int)Heuristic.heuristicFuncForNext(copy);
    }


    public void createBestChild (Game game) {
        MOVE[] pop1 = createPopulation(game);
        MOVE[] pop2 = createPopulation(game);

        MOVE[] child = createChild(game, pop1,pop2);

        for (int k = 0; k < child.length-1; k++) {
            best[k] = child[k];
        }
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (best[index] == null) createBestChild(game);
        if (best[index] == null) return MOVE.NEUTRAL;
        if (index == 9) {
            index = 0;
            createBestChild(game);
        }
        return best[index++];
    }
}