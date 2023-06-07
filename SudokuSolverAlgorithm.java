public class SudokuSolverAlgorithm {
    private static final int GRID_SIZE = 9;

    public static int[][] solve(int[][] puzzle) {
        if (solveSudoku(puzzle)) {
            return puzzle;
        } else {
            return new int[GRID_SIZE][GRID_SIZE];
        }
    }

    private static boolean solveSudoku(int[][] puzzle) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (puzzle[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValidMove(puzzle, row, col, num)) {
                            puzzle[row][col] = num;

                            if (solveSudoku(puzzle)) {
                                return true;
                            } else {
                                puzzle[row][col] = 0;
                            }
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isValidMove(int[][] puzzle, int row, int col, int num) {
        for (int i = 0; i < GRID_SIZE; i++) {
            if (puzzle[row][i] == num || puzzle[i][col] == num) {
                return false;
            }
        }

        int gridRow = (row / 3) * 3;
        int gridCol = (col / 3) * 3;
        for (int i = gridRow; i < gridRow + 3; i++) {
            for (int j = gridCol; j < gridCol + 3; j++) {
                if (puzzle[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }
}
