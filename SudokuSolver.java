import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

public class SudokuSolver extends JFrame {
    private static final int GRID_SIZE = 9;

    public SudokuSolver() {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));
        JPanel controlPanel = new JPanel(new BorderLayout());
        int[][] puzzle = new int[GRID_SIZE][GRID_SIZE];

        JTextField[][] textFields = new JTextField[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                textFields[i][j] = new JTextField();
                textFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                textFields[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                textFields[i][j].setPreferredSize(new Dimension(50, 50));
                if (i % 3 == 0 && i != 0) {
                    textFields[i][j].setBorder(createCustomBroderTop());
                    if (j % 3 == 0 && j != 0) {
                        textFields[i][j].setBorder(createCustomBroderLeftTop());
                    }
                } else {
                    if (j % 3 == 0 && j != 0) {
                        textFields[i][j].setBorder(createCustomBroderLeft());
                    } else {
                        textFields[i][j].setBorder(createCustomBroderNormal());
                    }
                }
                gridPanel.add(textFields[i][j]);
            }
        }
        JLabel label = new JLabel("Input your numbers");
        JButton buttonSolve = new JButton("Solve");
        buttonSolve.addActionListener(e -> {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    String value = textFields[i][j].getText();
                    try {
                        puzzle[i][j] = value.isEmpty() ? 0 : Integer.parseInt(value);
                    } catch (NumberFormatException ee) {
                        showErrorMessage("Input only numbers from 1 to 9, and everything else leave empty");
                        return;
                    }
                }
            }
            if (isValidSudoku(puzzle)) {
                solve(puzzle, textFields);
                label.setText("Solved");
            } else {
                showErrorMessage("That's not valid input");
            }
        });
        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(e -> {
            reset(textFields);
            label.setText("Input your numbers");
        });
        controlPanel.add(label, BorderLayout.NORTH);
        controlPanel.add(buttonSolve, BorderLayout.CENTER);
        controlPanel.add(buttonReset, BorderLayout.SOUTH);

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private CompoundBorder createCustomBroderLeft() {
        return BorderFactory.createCompoundBorder(
                createCustomBroderNormal(),
                BorderFactory.createMatteBorder(0, 5, 0, 0, Color.GREEN)
        );
    }

    private CompoundBorder createCustomBroderTop() {
        return BorderFactory.createCompoundBorder(
                createCustomBroderNormal(),
                BorderFactory.createMatteBorder(5, 0, 0, 0, Color.GREEN)
        );
    }

    private CompoundBorder createCustomBroderLeftTop() {
        return BorderFactory.createCompoundBorder(
                createCustomBroderNormal(),
                BorderFactory.createMatteBorder(5, 5, 0, 0, Color.GREEN)
        );
    }

    private CompoundBorder createCustomBroderNormal() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(1, 1, 1, 1),
                BorderFactory.createLineBorder(Color.BLACK)
        );
    }


    private void solve(int[][] grid, JTextField[][] textFields) {

        int[][] solvedGrid = SudokuSolverAlgorithm.solve(grid);

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                textFields[i][j].setText(String.valueOf(solvedGrid[i][j]));
            }
        }
    }

    private void reset(JTextField[][] textFields) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                textFields[i][j].setText("");
            }
        }
    }

    public boolean isValidSudoku(int[][] grid) {
        boolean[][] rowCheck = new boolean[GRID_SIZE][GRID_SIZE];
        boolean[][] colCheck = new boolean[GRID_SIZE][GRID_SIZE];
        boolean[][] boxCheck = new boolean[GRID_SIZE][GRID_SIZE];

        try {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    int num = grid[row][col];
                    if (num < 0 || num > GRID_SIZE) {
                        showErrorMessage("Invalid number entered: " + num);
                        return false;
                    }
                    if (num != 0) {
                        int boxRow = row / 3;
                        int boxCol = col / 3;

                        if (rowCheck[row][num - 1] || colCheck[col][num - 1] || boxCheck[boxRow * 3 + boxCol][num - 1]) {
                            return false;
                        }

                        rowCheck[row][num - 1] = true;
                        colCheck[col][num - 1] = true;
                        boxCheck[boxRow * 3 + boxCol][num - 1] = true;
                    }
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            showErrorMessage("Invalid input size. Expected 9x9 grid.");
            return false;
        }catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter numbers from 1 to 9.");
            return false;
        }

        return true;
    }

    private void showErrorMessage(String message) {
        JLabel label = new JLabel(message);
        JOptionPane.showMessageDialog(this, label, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolver::new);
    }
}
