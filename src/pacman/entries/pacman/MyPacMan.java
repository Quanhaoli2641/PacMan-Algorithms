package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import static java.lang.Math.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;

	public MOVE getMove(Game game, long timeDue) 
	{
		//Place your game logic here to play the game as Ms Pac-Man

		DFS d = new DFS (20);
		//BFS d = new BFS();
		//ItterativeDeepening d = new ItterativeDeepening(10);
		//Astar d = new Astar();
		//HillClimbing d = new HillClimbing();
		//SimulatedAnnealing d = new SimulatedAnnealing();
		//Evolution d = new Evolution();
		//Evolution2 d = new Evolution2();
		return d.getMove(game, timeDue);
	}

}