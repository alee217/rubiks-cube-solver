import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;

/*
 * Solver for the rubik's cube.
 * Uses A* search algorithm. However, runtime is slow and runs out of memory for
 * more scramble moves. I should try to implement this in Python with a better heuristic.
 */
public class Solver {
	// Size of the PriorityQueue used by A*.
	public static final int QUEUE_SIZE = 7105;
	
	public static void main(String[] args) {
		Cube rubiks = new Cube(3);
		rubiks.r();
		rubiks.l();
		rubiks.u();
		
		/*rubiks.f();
		rubiks.b();
		rubiks.l();
		rubiks.d();
		rubiks.r();
		rubiks.uPrime();
		rubiks.r();
		rubiks.r();
		rubiks.b();
		rubiks.u();
		rubiks.u();
		rubiks.bPrime();
		rubiks.d();
		rubiks.r();
		rubiks.r();
		rubiks.dPrime();
		rubiks.bPrime();
		rubiks.f();
		rubiks.r();
		rubiks.r();
		rubiks.d();
		rubiks.f();
		rubiks.f();
		rubiks.b();
		rubiks.l();
		rubiks.l();
		rubiks.bPrime();
		rubiks.u();
		rubiks.u();
		rubiks.f();
		rubiks.f();
		rubiks.r();
		rubiks.r();
		rubiks.uPrime();*/
		System.out.println(aStar(rubiks));
		System.out.println(rubiks);
	}
	
	/*
	 * Returns null if there are no solutions which shouldn't happen
	 * The Object[] is a pair of move and cube state in that order. Be
	 * sure to cast them before using.
	 */
	public static List<String> aStar(Cube start) {
		Queue<Object[]> fringe = new PriorityQueue<Object[]>(QUEUE_SIZE, new StateComparator());
		Set<Cube> visited = new HashSet<Cube>();
		Set<Cube> expanded = new HashSet<Cube>();
		Map<Object[], Object[]> childToParent = new HashMap<Object[], 
				                                            Object[]>();
		fringe.add(new Object[] {"", start});
		visited.add(start);
		while (!fringe.isEmpty()) {
			System.out.println(fringe.size());
			Object[] curr = fringe.remove();
			Cube currCube = (Cube) curr[1];
			if (currCube.isSolved()) {
				List<String> result = new LinkedList<String>();
				Object[] tempCurr = curr;
				while (!start.equals((Cube) tempCurr[1])) {
					result.add((String) tempCurr[0]);
					tempCurr = childToParent.get(tempCurr);
				}
				Collections.reverse(result);
				return result;
			}
			if (!expanded.contains(currCube)) {
				expanded.add(currCube);
				Map<String, Cube> nextStates = currCube.getNextStates();
				for (String nextMove : nextStates.keySet()) {
					Cube nextCube = nextStates.get(nextMove);
					if (!visited.contains(nextCube)) {
						nextCube.setCost(currCube.getCost() + 1);
						nextCube.setWeight(nextCube.getCost() + nextCube.getHeuristicCost());
						visited.add(nextCube);
						Object[] nextState = {nextMove, nextCube};
						fringe.add(nextState);
						childToParent.put(nextState, curr);
					}
				}
			}
		}
		return null;
	}
	
	// Used by the priority queue to compare the values of two nodes of the A*.
	private static class StateComparator implements Comparator<Object[]> {
		
		/*
		 * negative integer, zero, or positive integer as the first
		 * argument is less than, equal to, or greater than the second.
		 */
		public int compare(Object[] first, Object[] second) {
			Cube firstCube = (Cube) first[1];
			Cube secondCube = (Cube) second[1];
			return firstCube.getWeight() - secondCube.getWeight();
		}
	}
}
