package connect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * This is the view for connect four game.
 * All .png for icons are from a free site いらすとや (<a href="https://www.irasutoya.com">...</a>).
 */
public class ConnectFourView extends JFrame {
  // I'll use a matrix of buttons to represent the connect four game.
  private JButton[][] buttons;
  private JButton restartButton;
  private JLabel headerLabel;

  /**
   * This is the constructor of the view.
   * @param gameName a String representing the name of the game.
   */
  public ConnectFourView(String gameName) {
    // Set the frame for the view.
    super(gameName);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(700, 700);
    this.setLocation(400, 150);
    this.setMinimumSize(new Dimension(700, 700));
    this.setLayout(new BorderLayout());
  }

  /**
   * Prompt user for a valid row and column input.
   *
   * @return an integer list of two integers, the first one is row and the second is column.
   */
  public int[] getValidSize() {
    // Use two spinners to get input.
    JSpinner rowSpinner = new JSpinner(new SpinnerNumberModel(6, 5, 20, 1));
    JSpinner colSpinner = new JSpinner(new SpinnerNumberModel(7, 5, 20, 1));

    // And a panel to hold above two spinners.
    JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
    panel.add(new JLabel("Enter Rows (5~20):"));
    panel.add(rowSpinner);
    panel.add(new JLabel("Enter Columns (5~20):"));
    panel.add(colSpinner);

    // A show confirm dialog with ok and cancel option will have an integer return value,
    // with 0 as Ok and 1 as cancel.
    int result = JOptionPane.showConfirmDialog(null, panel,
        "DIY Your Connect Four Game!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      return new int[]{(Integer) rowSpinner.getValue(), (Integer) colSpinner.getValue()};
    }

    return null;
  }

  /**
   * Set two main panels on the frame.
   * @param row the number of rows of the game.
   * @param column the number of columns of the game.
   */
  public void setGameWindow(int row, int column) {
    // Is it really necessary to check inputs?
    if (row < 4 || column < 4) {
      throw new IllegalArgumentException("Board must be at least 4x4!");
    }

    this.buttons = new JButton[row][column];

    // The header label to display information: player's turn, error message, etc.
    this.headerLabel = new JLabel("Ready? Go! RED's turn!");
    this.headerLabel.setSize(new Dimension(600, 100));
    this.headerLabel.setFont(new Font("Comic", Font.PLAIN, 13));
    this.headerLabel.setLayout(new FlowLayout(FlowLayout.LEFT));

    this.add(headerLabel, BorderLayout.NORTH);

    // The upper panel to hold buttons.
    JPanel upperPanel = new JPanel();
    upperPanel.setSize(new Dimension(600, 500));
    upperPanel.setLayout(new GridLayout(row, column, 1, 1));

    // Make every button non-focusable, and set action command to that column,
    // so that when clicking any button the get action command will return the column.
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        buttons[i][j] = new JButton();
        buttons[i][j].setFocusable(false);
        buttons[i][j].setActionCommand(String.valueOf(j));
        upperPanel.add(buttons[i][j]);
      }
    }

    this.add(upperPanel, BorderLayout.CENTER);

    // The lower panel to hold two buttons: play again and exit.
    JPanel lowerPanel = new JPanel();
    lowerPanel.setSize(new Dimension(600, 100));
    lowerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));

    restartButton = new JButton("Play again!");
    restartButton.setFocusable(false);
    lowerPanel.add(restartButton);

    JButton exitButton = new JButton("Exit game!");
    exitButton.setFocusable(false);
    exitButton.addActionListener(event -> System.exit(0));
    lowerPanel.add(exitButton);

    this.add(lowerPanel, BorderLayout.SOUTH);
  }

  /**
   * Set the contents of the header label.
   * @param message a String representing the message to be displayed.
   */
  public void setHeaderLabelContents(String message) {
    headerLabel.setText(message);
  }

  /**
   * Set every cell/ button with a listener.
   * @param listener the listener to be set for each cell/ button.
   */
  public void setButtonListener(ActionListener listener) {
    for (JButton[] row : buttons) {
      for (JButton button : row) {
        button.addActionListener(listener);
      }
    }
  }

  /**
   * Set a listener to the restart button.
   * @param restartButtonListener the listener to be set for restart button.
   */
  public void setRestartButtonListener(ActionListener restartButtonListener) {
    restartButton.addActionListener(restartButtonListener);
  }

  /**
   * Update all cells/ buttons based on current board state.
   * @param boardState current board state.
   */
  public void updateButtons(Player[][] boardState) {
    // In order to build a jar file, here I used class loader to get the resource.
    ImageIcon orange = new ImageIcon("res/orange.png");
    ImageIcon pink = new ImageIcon("res/pink.png");

    for (int i = 0; i < boardState.length; i++) {
      for (int j = 0; j < boardState[i].length; j++) {
        // If the cell is player red, then set a pink icon to that button.
        // Else if is player yellow, then set an orange icon.
        if (boardState[i][j] == Player.RED) {
          buttons[i][j].setIcon(pink);
        } else if (boardState[i][j] == Player.YELLOW) {
          buttons[i][j].setIcon(orange);
        } else {
          // Otherwise just set the icon to null = the original version.
          buttons[i][j].setIcon(null);
        }
      }
    }
  }

  /**
   * Use a pop-up panel to show the winner.
   * @param winner the winner of the game.
   */
  public void showWinner(Player winner) {
    ImageIcon gameOverIcon;
    if (winner == Player.RED) {
      gameOverIcon = new ImageIcon("res/pink_win.png");
    } else if (winner == Player.YELLOW) {
      gameOverIcon = new ImageIcon("res/orange_win.png");
    } else {
      gameOverIcon = new ImageIcon("res/tie_tsumehodai.png");
    }

    String message = winner == null ? "Tie!" : winner + " Candy Won!";
    JOptionPane.showMessageDialog(
        null, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE, gameOverIcon);
  }
}
