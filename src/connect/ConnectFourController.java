package connect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is the controller file of connect 4 game.
 */
public class ConnectFourController {
  private ConnectFourModel model;
  private final ConnectFourView view;

  /**
   * The constructor of the controller.
   * @param view the view of the connect 4 game.
   */
  public ConnectFourController(ConnectFourView view) {
    this.view = view;
  }

  /**
   * Start the game.
   */
  public void go() {
    int[] gameBoardSize = view.getValidSize();
    if (gameBoardSize != null) {
      this.model = new ConnectFourModelImpl(gameBoardSize[0], gameBoardSize[1]);
      view.setGameWindow(gameBoardSize[0], gameBoardSize[1]);
      view.setButtonListener(new ButtonListener());
      view.setRestartButtonListener(new RestartButtonListener());
      view.setVisible(true);
      updateView();
    } else {
      // If user clicked cancel, then quit.
      System.exit(0);
    }
  }

  /**
   * Update the view.
   */
  private void updateView() {
    // Update the view by passing the board state to it.
    view.updateButtons(model.getBoardState());
    if (model.isGameOver()) {
      view.setHeaderLabelContents("Game Over!");
      view.showWinner(model.getWinner());
    }
  }

  /**
   * Create a self-defined button listener.
   */
  private class ButtonListener implements ActionListener {
    /**
     * Invoked when an action occurs.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
      // If the game is not over, react to the button. Otherwise, do nothing.
      if (!model.isGameOver()) {
        // Get the column that is clicked.
        int column = Integer.parseInt(event.getActionCommand());
        // Store the moving information.
        String firstHalfInfo = String.format("%s moved to Column %d!", model.getTurn(), column + 1);

        try {
          // Make a move, set new header info and update the view.
          model.makeMove(column);
          view.setHeaderLabelContents(firstHalfInfo + "\nNow " + model.getTurn() + "'s turn ...");
          updateView();
        } catch (IllegalArgumentException e) {
          // If the move is invalid, set header info and do nothing.
          view.setHeaderLabelContents("Oops! " + e.getMessage());
        }
      }
    }
  }

  /**
   * Create a self-defined restart button listener.
   */
  private class RestartButtonListener implements ActionListener {
    /**
     * Invoked when an action occurs.
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
      model.resetBoard();
      updateView();
      view.setHeaderLabelContents("Game Restarted! RED's turn ...");
    }
  }
}
