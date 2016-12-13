# Pac-Man-algorithms
*Algorithms to play Pac-man*

Included in this package is the entire Pac-man framework along with the algorithms.

The actual algorithms are linked [here](https://github.com/Quanhaoli2641/Pac-Man-algorithms/tree/master/src/pacman/entries/pacman).

The following algorithms were implemented:
  * [Depth first Search](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/DFS.java)
  * [Breadth first Search](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/BFS.java)
  * [Itterative Deepening](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/ItterativeDeepening.java)
  * [Astar](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/Astar.java)
  * [Hill Climber](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/HillClimbing.java)
  * [Simulated Annealing](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/SimulatedAnnealing.java)
  * [Evolutionary with CrossOver](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/Evolution.java) 
  * [Evolutionary with Mutation](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/Evolution2.java)

In addition to the algorithms mentioned above, the following were also implemented:
 * A [Node](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/NodeTree.java) structure to help create trees for tree-search algorithms.
 * A [Heuristic Function](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/Heuristic.java) to calculate distances for informed algorithms.
 
 Furthermore, a [main](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/entries/pacman/MyPacMan.java) that simply instantiates every class and calls the getMove() function is included (All the algorithms are instantiated, but only one is active at a time. The rest are commented out).

To open this framework, download IntelliJ or any other Java IDE and import the package. To simulate the algorithms, run the [Executor.Java](https://github.com/Quanhaoli2641/Pac-Man-algorithms/blob/master/src/pacman/Executor.java) as the main program.
