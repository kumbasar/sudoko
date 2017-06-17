import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class KumbiSudokuGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2215979184657923953L;
	private final Font font = new Font("Verdana", Font.BOLD, 15);
	private final String TITLE_VERSION = "Kumbidoku - volkan.kumbasar@gmail.com";
	private final Pattern p = Pattern.compile("[1-9]");

	private JTextField[][] sudokuField = new JTextField[9][9];
	private JButton solveButton;
	private JButton blankButton;
	private JButton newButton;
	private Border sudokuBorder;
	private boolean actionYES = false;
	private int[][] sudokuArray = new int[sudokuField.length][sudokuField.length];
	private static boolean[][] sudokuValid = new boolean[9][9];
	private KumbiSudoku sudoku;
	private int level = 50;

	public KumbiSudokuGui() {

		super();

		setTitle(TITLE_VERSION);

		sudoku = new KumbiSudoku();

		getContentPane().setLayout(new BorderLayout());

		JPanel buttonPane = new JPanel(new GridLayout(1, 2));

		JRadioButton easyButton = new JRadioButton("Easy");
		easyButton.setActionCommand("30");
		easyButton.addActionListener(this);
		JRadioButton mediumButton = new JRadioButton("Medium");
		mediumButton.setActionCommand("" + level);
		mediumButton.setSelected(true);
		mediumButton.addActionListener(this);
		JRadioButton hardButton = new JRadioButton("Hard");
		hardButton.setActionCommand("80");
		hardButton.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(easyButton);
		group.add(mediumButton);
		group.add(hardButton);

		JPanel levelPanel = new JPanel();
		levelPanel.add(easyButton);
		levelPanel.add(mediumButton);
		levelPanel.add(hardButton);

		newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sudoku.newSudoku(level);
				setSudoku(sudoku.getSudoku());
				solveButton.setEnabled(true);
			}
		});

		solveButton = new JButton("Solve");
		solveButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				sudoku.setSudoku(sudokuArray);

				solveButton.setEnabled(false);

				if (!sudoku.check(false)) {
					return;
				}

				blankButton.setEnabled(false);
				newButton.setEnabled(false);

				long time = System.currentTimeMillis();
				boolean valid = sudoku.solveSudoku();
				time = System.currentTimeMillis() - time;

				System.out
						.println("\n\nsolve time = " + time + " milliseconds");

				blankButton.setEnabled(true);
				newButton.setEnabled(true);

				if (valid) {
					setSudoku(sudoku.getSudoku());
					// repaint();
					solveButton.setEnabled(true);
				} else {
					solveButton.setEnabled(false);
				}

			}
		});

		blankButton = new JButton("Blank");
		blankButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				sudoku.initialSudoku();
				setSudoku(sudoku.getSudoku());
				solveButton.setEnabled(true);
			}
		});

		buttonPane.add(solveButton);
		buttonPane.add(blankButton);
		buttonPane.add(newButton);

		JPanel sudokuPane = new JPanel(new GridLayout(sudokuField[0].length,
				sudokuField.length));

		for (int x = 0; x < sudokuField[0].length; ++x) {
			for (int y = 0; y < sudokuField.length; ++y) {

				sudokuField[x][y] = new JTextField("");
				sudokuField[x][y].setHorizontalAlignment(JTextField.CENTER);
				sudokuField[x][y].setFont(font);
				sudokuField[x][y].setForeground(Color.black);
				sudokuField[x][y].addKeyListener(new KeyAdapter() {

					public void keyReleased(KeyEvent e) {

						char input = e.getKeyChar();
						String inputStr = ((JTextField) e.getSource())
								.getText();

						// System.out.println("dd"+((JTextField)
						// e.getSource()).getLocation());

						if (input == '\b' && inputStr.length() == 0) {
							performCheck(e);
						}

						if (input == '\n' || input == '\t') {

							((JTextField) e.getSource()).setText(inputStr
									.trim());
							if (inputStr.length() == 0) {
								performCheck(e);
							}
							return;
						}

						// System.out.println("vv" + inputStr + "vv");

						if (inputStr.length() > 1) {
							// System.out.println("fff:" + inputStr);
							((JTextField) e.getSource()).setText(inputStr
									.substring(0, 1));
							// System.out.println("fff:" +
							// inputStr.substring(0,1));
							return;
						}

						else if (inputStr.length() == 1) {
							// System.out.println(input + "");

							Matcher m = p.matcher(input + "");
							if (!m.find()) {
								/*
								 * System.out.println("gggg" + ((JTextField)
								 * e.getSource()) .getText() + "gggg");
								 */
								// sudokuField[x][y].setText("");
								((JTextField) e.getSource()).setText("");
							} else {
								performCheck(e);
							}

						}
					}
				});
				sudokuField[x][y].addMouseListener(new MouseAdapter() {

					public void mousePressed(MouseEvent e) {

					}

					public void mouseEntered(MouseEvent e) {
						((JTextField) e.getSource()).setFocusable(true);

						sudokuBorder = ((JTextField) e.getSource()).getBorder();
						((JTextField) e.getSource()).setBorder(BorderFactory
								.createEtchedBorder(EtchedBorder.RAISED));
					}

					public void mouseExited(MouseEvent e) {
						if (!actionYES) {
							((JTextField) e.getSource())
									.setBorder(sudokuBorder);

						} else {
							actionYES = false;
						}

					}

				});

				if ((x > -1 && x < 3) || (x > 5)) {
					if ((y > -1 && y < 3) || (y > 5)) {
						sudokuField[x][y].setBackground(Color.gray);
					}
				} else if (x > 2 && x < 6 && y > 2 && y < 6) {
					sudokuField[x][y].setBackground(Color.gray);
				}

				sudokuPane.add(sudokuField[x][y]);
			}
		}

		getContentPane().add(sudokuPane, BorderLayout.CENTER);
		getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		getContentPane().add(levelPanel, BorderLayout.PAGE_START);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 400);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);

		sudoku.newSudoku(level);
		setSudoku(sudoku.getSudoku());

	}

	public void setSudoku(int[][] sudokuArray) {

		for (int x = 0; x < sudokuField[0].length; ++x) {
			for (int y = 0; y < sudokuField.length; ++y) {

				this.sudokuArray[x][y] = sudokuArray[x][y];
				sudokuValid[x][y] = false;

				if (sudokuArray[x][y] == -1) {
					sudokuField[x][y].setText("");
					sudokuField[x][y].setEditable(true);
					sudokuField[x][y].setBorder(BorderFactory
							.createLineBorder(Color.BLACK));
					sudokuField[x][y].setForeground(Color.BLACK);
				} else {
					sudokuField[x][y].setText((sudokuArray[x][y] + 1) + "");
					sudokuField[x][y].setEditable(false);
					sudokuField[x][y].setBorder(BorderFactory
							.createLineBorder(Color.BLACK));
					sudokuField[x][y].setForeground(Color.BLACK);
				}

			}
		}
	}

	public int[][] getSudoku() {
		return sudokuArray;
	}

	public void performCheck(KeyEvent e) {

		actionYES = true;

		for (int x = 0; x < sudokuField[0].length; ++x) {
			for (int y = 0; y < sudokuField.length; ++y) {
				if (sudokuField[x][y].getText().trim().equals("")) {
					sudokuArray[x][y] = -1;
				} else {
					sudokuArray[x][y] = Integer.parseInt(sudokuField[x][y]
							.getText().trim()) - 1;
				}
			}
		}

		sudoku.setSudoku(sudokuArray);

		boolean valid = sudoku.check(false);

		for (int x = 0; x < sudokuField[0].length; ++x) {
			for (int y = 0; y < sudokuField.length; ++y) {
				if (sudokuValid[x][y]) {
					sudokuField[x][y].setBorder(BorderFactory
							.createLineBorder(Color.red));
					sudokuField[x][y].setForeground(Color.red);
				} else {
					sudokuField[x][y].setBorder(BorderFactory
							.createLineBorder(Color.BLACK));
					sudokuField[x][y].setForeground(Color.black);
				}
			}
		}

		if (!valid) {
			solveButton.setEnabled(false);
			((JTextField) e.getSource()).setBorder(BorderFactory
					.createLineBorder(Color.red));
			((JTextField) e.getSource()).setForeground(Color.red);
		} else {

			if (sudoku.check(true)) {
				solveButton.setEnabled(false);
				// JOptionPane.showMessageDialog(this,
				// "You are a big cat, marry a mini panda!!");
				JOptionPane.showMessageDialog(this, "You are a big cat!!");
			} else {

				solveButton.setEnabled(true);
			}

		}
	}

	public static void setValidBoolean(int x, int y, boolean value) {
		sudokuValid[x][y] = value;
	}

	public void actionPerformed(ActionEvent e) {

		level = Integer.parseInt(e.getActionCommand());
		sudoku.newSudoku(level);
		setSudoku(sudoku.getSudoku());
		solveButton.setEnabled(true);
	}

}
