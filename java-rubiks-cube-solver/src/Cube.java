import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
 * The state representation of a rubik's cube.
 */
public class Cube implements Comparable<Cube> {
	// Stores the colors of the rubik's cube, use as reference for the order of faces in the cube array.
	private static final String COLORS[] = {"W", "G", "R", "O", "B", "Y"};

	private int dimension; // Dimension of the rubik's cube.
	private int[][][] cube; // Cube stored as 6 2-d arrays of dim x dim sized face.
	private int[][][] solvedCube; // An array representing a solved cube.
	private int cost; // the cost to do one move
	private int weight; // the g + h of the A* algorithm;

	public Cube(int dim) {
		this.dimension = dim;
		this.cube = new int[6][dim][dim];
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[0].length; j++) {
				for (int k = 0; k < cube[0].length; k++) {
					cube[i][j][k] = i;
				}
			}
		}
		solvedCube = deepCopy(cube);
	}
	
	/*
	 * This constructor can be used to create a Cube given an array
	 * representation of a rubik's cube.
	 */
	public Cube(int dim, int[][][] newCube, int[][][] solvedCube) {
		this.dimension = dim;
		this.cube = deepCopy(newCube);
		this.solvedCube = solvedCube;		
	}
	
	/*
	 * Gets all possible Cube states after one move.
	 */
	public Map<String, Cube> getNextStates() {
		Map<String, Cube> ret = new HashMap<String, Cube>();
		
		ret.put("r", new Cube(3, cube, solvedCube));
		ret.get("r").r();
		ret.put("rPrime", new Cube(3, cube, solvedCube));
		ret.get("rPrime").rPrime();
		ret.put("l", new Cube(3, cube, solvedCube));
		ret.get("l").l();
		ret.put("lPrime", new Cube(3, cube, solvedCube));
		ret.get("lPrime").lPrime();
		ret.put("u", new Cube(3, cube, solvedCube));
		ret.get("u").u();
		ret.put("uPrime", new Cube(3, cube, solvedCube));
		ret.get("uPrime").uPrime();
		ret.put("d", new Cube(3, cube, solvedCube));
		ret.get("d").d();
		ret.put("dPrime", new Cube(3, cube, solvedCube));
		ret.get("dPrime").dPrime();
		ret.put("f", new Cube(3, cube, solvedCube));
		ret.get("f").f();
		ret.put("fPrime", new Cube(3, cube, solvedCube));
		ret.get("fPrime").fPrime();
		ret.put("b", new Cube(3, cube, solvedCube));
		ret.get("b").b();
		ret.put("bPrime", new Cube(3, cube, solvedCube));
		ret.get("bPrime").bPrime();
		
		return ret;
	}
	
	/*
	 * Checks if the rubik's cube is solved.
	 */
	public boolean isSolved() {
		return Arrays.deepEquals(cube, solvedCube);
	}
	
	/*
	 * Helper method to copy a 3-d array i.e. the cube representation.
	 */
	private int[][][] deepCopy(int[][][] arr) {
		int[][][] ret = new int[6][dimension][dimension];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				ret[i][j] = arr[i][j].clone();
			}
		}
		return ret;
	}

	/*
	 *  Do a r move on the cube.
	 */
	public void r() {
		moveRL(new int[] {0, 1, 5, 4}, 2, 2, true);
	}

	/*
	 * Do a r' move on the cube.
	 */
	public void rPrime() {
		moveRL(new int[] {4, 5, 1, 0}, 2, 2, false);
	}

	/*
	 * Do a l move.
	 */
	public void l() {
		moveRL(new int[] {4, 5, 1, 0}, 3, 0, true);
	}

	/*
	 * Do a l' move.
	 */
	public void lPrime() {
		moveRL(new int[] {0, 1, 5, 4}, 3, 0, false);
	}

	/*
	 * Do a f move.
	 */
	public void f() {
		int[] faces = new int[] {0, 3, 5, 2};
		int temp[] = {cube[faces[0]][2][0], cube[faces[0]][2][1],
			      cube[faces[0]][2][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][2][i] = cube[faces[1]][2-i][2];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][i][2] = cube[faces[2]][0][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][0][i] = cube[faces[3]][2-i][0];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[3]][i][0] = temp[i];
		}

		cube[1] = rotateClockwise(cube[1], true);
	}

	/*
	 * Do a f' move.
	 */
	public void fPrime() {
		int[] faces = new int[] {0, 2, 5, 3};
		int temp[] = {cube[faces[0]][2][0], cube[faces[0]][2][1],
			      cube[faces[0]][2][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][2][i] = cube[faces[1]][i][0];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][i][0] = cube[faces[2]][0][2-i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][0][i] = cube[faces[3]][i][2];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][i][2] = temp[2-i];
		}

		cube[1] = rotateClockwise(cube[1], false);
	}

	/*
	 * Do a b move.
	 */
	public void b() {
		int[] faces = new int[] {0, 2, 5, 3};
		int temp[] = {cube[faces[0]][0][0], cube[faces[0]][0][1],
						cube[faces[0]][0][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][0][i] = cube[faces[1]][i][2];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][i][2] = cube[faces[2]][2][2-i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][2][i] = cube[faces[3]][i][0];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][i][0] = temp[2-i];
		}

		cube[4] = rotateClockwise(cube[4], true);
	}

	/*
	 * Do a b' move.
	 */
	public void bPrime() {
		int[] faces = new int[] {0, 3, 5, 2};
		int temp[] = {cube[faces[0]][0][0], cube[faces[0]][0][1],
			      cube[faces[0]][0][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][0][i] = cube[faces[1]][2-i][0];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][i][0] = cube[faces[2]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][2][i] = cube[faces[3]][2-i][2];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[3]][i][2] = temp[i];
		}

		cube[4] = rotateClockwise(cube[4], false);
	}

	/*
	 * Do a u move.
	 */
	public void u() {
		int[] faces = new int[] {1, 2, 4, 3};
		moveUD(faces, 0, 0, true);
	}
	
	/*
	 * Do a u' move.
	 */
	public void uPrime() {
		int[] faces = new int[] {1, 3, 4, 2};
		moveUD(faces, 0, 0, false);
	}
	
	/*
	 * Do a d move.
	 */
	public void d() {
		int[] faces = new int[] {1, 3, 4, 2};
		moveUD(faces, 5, 2, true);
	}
	
	/*
	 * Do a d' move.
	 */
	public void dPrime() {
		int[] faces = new int[] {1, 2, 4, 3};
		moveUD(faces, 5, 2, false);
	}
	
	/*
	 * Do a U or D move
	 */
	private void moveUD(int[] faces, int side, int rowChange, boolean isClockwise) {
		int temp[] = {cube[faces[0]][rowChange][0], cube[faces[0]][rowChange][1],
			      cube[faces[0]][rowChange][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][rowChange][i] = cube[faces[1]][rowChange][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][rowChange][i] = cube[faces[2]][2-rowChange][2-i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][2-rowChange][2-i] = cube[faces[3]][rowChange][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[3]][rowChange][i] = temp[i];
		}

		cube[side] = rotateClockwise(cube[side], isClockwise);
	}

	/*
	 *  Do a R move or L move
	 *  True for clockwise and false for counterclockwise
	 */
	private void moveRL(int[] faces, int side, int changedCol, boolean isClockwise) {
		//int[] faces = {0, 1, 5, 4};
		int temp[] = {cube[faces[0]][0][changedCol], cube[faces[0]][1][changedCol],
				      cube[faces[0]][2][changedCol]};
		for (int f = 1; f < faces.length; f++) {
			for (int i = 0; i < dimension; i++) {
				cube[faces[f-1]][i][changedCol] = cube[faces[f]][i][changedCol];
			}
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][i][changedCol] = temp[i];
		}

		cube[side] = rotateClockwise(cube[side], isClockwise);
	}

	/*
	 * Do a rotation
	 * true for clockwise and false for counterclockwise rotation.
	 */
	public int[][] rotateClockwise(int[][] arr, boolean isClockwise) {
		int[][] ret = new int[arr.length][arr.length];
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				if (isClockwise) {
					ret[i][j] = arr[arr.length - j - 1][i];
				} else {
					ret[i][j] = arr[j][arr.length - i - 1];
				}
			}
		}
		return ret;
	}
	
	/*
	 * Gets the cube array. 
	 * Could make the Cube object mutable.
	 * Be careful when using this method.
	 */
	private int[][][] getCubeRep() {
		return this.cube;
	}
	
	/*
	 * Gets the cost of the current state.
	 */
	public int getCost() {
		return this.cost;
	}
	
	/*
	 * Sets the cost of the current state.
	 */
	public void setCost(int newCost) {
		this.cost = newCost;
	}
	
	/*
	 * Gets the weight of the current state.
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/*
	 * Sets the weight of the current state.
	 */
	public void setWeight(int newWeight) {
		this.weight = newWeight;
	}
	
	/*
	 * Counts the number of misplaced color.
	 * I would need to set up a move database in order to find
	 * solutions with reasonable memory usage and time.
	 */
	public int getHeuristicCost() {
		int misplacedCount = 0;
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[0].length; j++) {
				for (int k = 0; k < cube[0].length; k++) {
					if (cube[i][j][k] != i) {
						misplacedCount++;
					}
				}
			}
		}
		return misplacedCount;
	}

	/*
	 * Overrides compareTo. Used to compare two cubes.
	 */
	@Override
	public int compareTo(Cube other) {
		return this.cost - other.getCost();
	}
	
	/*
	 * Overrides equals. Used to compare two cubes.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Cube))
			return false;
		Cube c = (Cube) o;
		return Arrays.deepEquals(this.cube, c.getCubeRep());
	}
	
	/*
	 * Overrides hashCode(). Used by hashing functions.
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[0].length; j++) {
				for (int k = 0; k < cube[0].length; k++) {
					hash = 31 * hash + (i + 1) * cube[i][j][k];
				}
			}
		}
		//hash = 31 * hash + Arrays.deepHashCode(this.cube);
		return hash;
	}
	
	/*
	 * Overrides toString.
	 */
	@Override
	public String toString() {
		String ret = "";
		for (int[][] side : cube) {
			for (int[] row : side) {
				for (int col : row) {
					ret += COLORS[col] + " ";
				}
				ret += "\n";
			}
			ret += "---------\n";
		}
		return ret;
	}
}
