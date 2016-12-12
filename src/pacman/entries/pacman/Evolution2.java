package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.NearestPillPacMan;
import pacman.controllers.examples.StarterGhosts;

import java.util.LinkedList;
import java.util.Random;


import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/22/16.
 */
public class Evolution2 extends Controller<MOVE>{

    private StarterGhosts ghosts = new StarterGhosts();
    private MOVE[] best = new MOVE[10];
    private int index = 0;

    public MOVE[] createPopulation (Game game) {

        MOVE[] population = new MOVE[10];
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

    public void mutatePopulation (Game game) {
        best = createPopulation(game);
        double bestScore = 0;
        for (int i = 0; i < best.length-1; i++) {
            for (MOVE move : MOVE.values()) {

                if (FindScore(game, move) > bestScore) {
                    best[i] = move;
                    bestScore = FindScore(game, move);
                }
            }
            bestScore = 0;
        }
    }

    public double FindScore(Game game, MOVE move) {
        Game copy = game.copy();
        copy.advanceGame(move,ghosts.getMove());
        return Heuristic.heuristicFuncForNext(copy);
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (best[index] == null) mutatePopulation(game);
        if (best[index] == null) return MOVE.NEUTRAL;
        if (index == 9){
            index = 0;
            mutatePopulation(game);
        }
        return best[index++];
    }



}
