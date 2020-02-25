import java.util.Arrays;

public class Cube {
	// Stores the colors of the rubik's cube, use as reference for the order of faces in the cube array.
	private static final String COLORS[] = {"white", "green", "red", "orange", "blue", "yellow"};
	
	private int dimension; // Dimmension of the rubik's cube.
	private int[][][] cube; // Cube stored as 6 2-d arrays of dim x dim sized face.
	
	public Cube(int dim) {
		dimension = dim;
		cube = new int[6][dim][dim];
		for (int i = 0; i < cube.length; i++) {
			for (int j = 0; j < cube[0].length; j++) {
				for (int k = 0; k < cube[0].length; k++) {
					cube[i][j][k] = i;
				}
			}
		}
		for (int[][] faces : cube) {
			faces[0][0] = 0;
		}
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
	
	public void l() {
		moveRL(new int[] {4, 5, 1, 0}, 3, 0, true);
	}
	
	public void lPrime() {
		moveRL(new int[] {0, 1, 5, 4}, 3, 0, false);
	}
	
	public void f() {
		//moveFB(new int[] {0, 3, 5, 2}, 1, true);
		int[] faces = new int[] {0, 3, 5, 2};
		int temp[] = {cube[faces[0]][2][0], cube[faces[0]][2][1],
			      cube[faces[0]][2][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][2][i] = cube[faces[1]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][0][i] = cube[faces[2]][0][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][0][i] = cube[faces[3]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][2][i] = temp[i];
		}
		
		cube[1] = rotateClockwise(cube[1], true);
	}
	
	public void fPrime() {
		//moveFB(new int[] {0, 2, 5, 3}, 1, false);
		int[] faces = new int[] {0, 2, 5, 3};
		int temp[] = {cube[faces[0]][2][0], cube[faces[0]][2][1],
			      cube[faces[0]][2][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][2][i] = cube[faces[1]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][2][i] = cube[faces[2]][0][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][0][i] = cube[faces[3]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][2][i] = temp[i];
		}
		
		cube[1] = rotateClockwise(cube[1], false);
	}
	
	private void moveFB(int[] faces, int side, boolean isClockwise) {
		int temp[] = {cube[faces[0]][2][0], cube[faces[0]][2][1],
			      cube[faces[0]][2][2]};
		for (int i = 0; i < dimension; i++) {
			cube[faces[0]][2][i] = cube[faces[1]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[1]][0][i] = cube[faces[2]][0][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[2]][0][i] = cube[faces[3]][2][i];
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][2][i] = temp[i];
		}
		
		cube[side] = rotateClockwise(cube[side], isClockwise);
	}
	
	/*
	 *  Do a move
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
		
		// Re-orient the right side.
		/*int tempNum = cube[2][0][0];
		cube[2][0][0] = cube[2][2][0];
		cube[2][2][0] = cube[2][2][2];
		cube[2][2][2] = cube[2][0][2];
		cube[2][0][2] = tempNum;
		
		tempNum = cube[2][0][1];
		cube[2][0][1] = cube[2][1][0];
		cube[2][1][0] = cube[2][2][1];
		cube[2][2][1] = cube[2][1][2];
		cube[2][1][2] = tempNum;*/
	}
	
	/*
	 *  Do a counterclockwise move.
	 */
	/*private void movePrime() {
		int[] faces = {4, 5, 1, 0};
		int temp[] = {cube[faces[0]][0][2], cube[faces[0]][1][2], cube[faces[0]][2][2]};
		for (int f = 1; f < faces.length; f++) {
			for (int i = 0; i < dimension; i++) {
				cube[faces[f-1]][i][2] = cube[faces[f]][i][2];
			}
		}
		for (int i = 0; i < dimension; i++) {
			cube[faces[faces.length - 1]][i][2] = temp[i];
		}
		
		cube[2] = rotateClockwise(cube[2], false);
		
		// Re-orient the right side.
		/*int tempNum = cube[2][0][0];
		cube[2][0][0] = cube[2][0][2];
		cube[2][0][2] = cube[2][2][2];
		cube[2][2][2] = cube[2][2][0];
		cube[2][2][0] = tempNum;
		
		tempNum = cube[2][0][1];
		cube[2][0][1] = cube[2][1][2];
		cube[2][1][2] = cube[2][2][1];
		cube[2][2][1] = cube[2][1][0];
		cube[2][1][0] = tempNum;
	}*/
	
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
