import java.util.Hashtable;
import java.util.Random;

public class KumbiSudoku {

	private short sudokuLength = 9;
	private int[][] sudoku = new int[sudokuLength][sudokuLength];

	Hashtable<Integer, Boolean> sudokuT = new Hashtable<Integer, Boolean>();

	public KumbiSudoku() {

	}

	public void setSudoku(int[][] sudoku) {
		this.sudoku = sudoku;
	}

	public int[][] getSudoku() {
		return sudoku;
	}

	public void initialSudoku() {

		for (int x = 0; x < sudoku[0].length; ++x) {
			for (int y = 0; y < sudoku.length; ++y) {
				sudoku[x][y] = -1;
			}
		}
	}
	
	public void newSudoku(int level) {
		
		initialSudoku();
		solveSudoku();
		
		int counter = 0;
		int x = 0;
		int y = 0;
		
		Random ran = new Random();

		do {
			ran = new Random();
			
			x = ran.nextInt(9);
			y = ran.nextInt(9);
			sudoku[x][y] = -1;
			++counter;
			
		} while(counter < level );
	}

	public boolean guessValue(int i, int j) {

		if (sudoku[i][j] > -1) {
			return true;
		}

		newCheck();

		for (int x = 0; x < 9; ++x) {
			if (sudoku[x][j] > -1) {
				sudokuT.put(sudoku[x][j], true);
			}
		}

		for (int y = 0; y < 9; ++y) {
			if (sudoku[i][y] > -1) {
				sudokuT.put(sudoku[i][y], true);
			}
		}
 
		int min_x = 3 * ((int) (i / 3));
		int max_x = 3 + min_x;
		int min_y = 3 * ((int) (j / 3));
		int max_y = 3 + min_y;

		for (int x = min_x; x < max_x; ++x) {
			for (int y = min_y; y < max_y; ++y) {
				if (sudoku[x][y] > -1) {
					sudokuT.put(sudoku[x][y], true);
				}
			}
		}

		/*
		System.out.print(i + " " + j + " =");
		for (int x = 0; x < 9; ++x) {
			if (sudokuT.get(x) == false) {
				System.out.print(x + ", ");
			}
		}
		*/

		Random rand = new Random();
		int guess = 0;
		int counter = 0;
		do {

			if (counter > 40) {
				return false;
			}
			
			guess = rand.nextInt(9);
			
			++counter;

			
		} while (!isValid(guess));

		//System.out.println(" guess " + guess);
		sudoku[i][j] = guess;
		return true;

	}

	public boolean check(boolean full) {
		
	    boolean valid = true;

		for (int x = 0; x < sudoku[0].length; ++x) {

			newCheck();

			for (int y = 0; y < sudoku.length; ++y) {
                
				KumbiSudokuGui.setValidBoolean(x, y, false);
				
				if (!full && sudoku[x][y] == -1 ){
					continue;
				}

				if (!isValid(sudoku[x][y])) {
					KumbiSudokuGui.setValidBoolean(x, y, true);
					valid = false;
				}

			}

		}

		for (int y = 0; y < sudoku[0].length; ++y) {

			newCheck();

			for (int x = 0; x < sudoku.length; ++x) {
                
				if (!full && sudoku[x][y] == -1 ){
					continue;
				}

				if (!isValid(sudoku[x][y])) {
					KumbiSudokuGui.setValidBoolean(x, y, true);
					valid = false;
				}

			}

		}
		
		
		
		for (int n = 0; n < 9; n+=3) {
			for (int m = 0; m < 9; m+=3) {

				newCheck();
				for (int x = n; x < n + 3; ++x) {
					for (int y = m; y < m + 3; ++y) {
						
						if (!full && sudoku[x][y] == -1 ){
							continue;
						}
						
						if (!isValid(sudoku[x][y])) {
							KumbiSudokuGui.setValidBoolean(x, y, true);
							valid = false;
						}
					}
				}
			}
		}
		
		return valid;
	}

	private void newCheck() {

		for (int i = 0; i < sudokuLength; ++i) {
			sudokuT.put(i, false);
		}

	}

	private boolean isValid(int i) {

		if (i == -1) {
			return false;
		}

		// System.out.println("isValid " + (i) + " " + sudokuT.get(i));

		if (sudokuT.get(i) == false) {
			sudokuT.put(i, true);
			return true;
		}

		return false;

	}

	public boolean solveSudoku() {

		// Random randomGenerator = new Random();
		
		//boolean valid = true;

		int[][] firstSudoku = copySudoku();

		int counter = 0;
		boolean makeNew = false;

		do {
			makeNew = false;
			sudoku = copySudoku(firstSudoku);
			volkan: for (int x = 0; x < sudoku[0].length; ++x) {
				for (int y = 0; y < sudoku.length; ++y) {

					if (!guessValue(x, y)) {
						makeNew = true;
						break volkan;
						
					}
					
				}
			}
			++counter;
			//printSudoku();
		} while ( ( makeNew || !check(true) ) && counter < 600000);
		
		if (counter == 600000) {
			return false;
		}
		
		return true;

		// setSudoku(newSudoku);
		//System.out.println("==================");
		//printSudoku();

	}

	public void printSudoku() {
		for (int x = 0; x < sudoku[0].length; ++x) {

			if (x % 3 == 0) {
				System.out.println("|-------+-------+-------|");
			}

			for (int y = 0; y < sudoku.length; ++y) {

				if (y % 3 == 0) {
					System.out.print("| ");
				}
				System.out.print(sudoku[x][y] + " ");

			}
			System.out.print("|\n");

		}
		System.out.println("|-------+-------+-------|\n");
	}

	private int[][] copySudoku() {

		int[][] newSudoku = new int[sudoku[0].length][sudoku.length];

		for (int x = 0; x < sudoku[0].length; ++x) {
			for (int y = 0; y < sudoku.length; ++y) {

				newSudoku[x][y] = sudoku[x][y];

			}
		}

		return newSudoku;
	}

	private int[][] copySudoku(int[][] sudoku) {

		int[][] newSudoku = new int[sudoku[0].length][sudoku.length];

		for (int x = 0; x < sudoku[0].length; ++x) {
			for (int y = 0; y < sudoku.length; ++y) {

				newSudoku[x][y] = sudoku[x][y];

			}
		}

		return newSudoku;
	}
}
