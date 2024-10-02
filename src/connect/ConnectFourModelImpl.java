package connect;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Model component in the MVC architecture of a Connect Four game. This class defines the core
 * functionalities required to manage the game's state, including initializing the game board,
 * handling player moves, and checking for win/draw conditions.
 */
public class ConnectFourModelImpl implements ConnectFourModel {
  private Player[][] board;
  private final int rows;
  private final int columns;
  private int playerTurn = 0;

  /**
   * Constructor for the ConnectFourModelImpl class.
   *
   * @param rows    the number of rows in the game board
   * @param columns the number of columns in the game board
   * @throws IllegalArgumentException if the board is too small (smaller than 4x4)
   */
  public ConnectFourModelImpl(int rows, int columns) throws IllegalArgumentException {
    if (rows < 4 || columns < 4) {
      throw new IllegalArgumentException("Board must be at least 4x4");
    }
    this.rows = rows;
    this.columns = columns;
    initializeBoard();
  }

  @Override public void initializeBoard() {
    this.board = new Player[rows][columns];
    // Board starts empty
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        this.board[i][j] = null;
      }
    }
  }

  @Override public void makeMove(int column) throws IllegalArgumentException {
    if (column < 0 || column >= columns) {
      throw new IllegalArgumentException("The column is out of bounds");
    } else if (this.board[0][column] != null) {
      throw new IllegalArgumentException("The column is full");
    } else {
      int row = 0;
      while (row < rows && this.board[row][column] == null) {
        row++;
      }
      this.board[row - 1][column] = this.getTurn();
      this.playerTurn++;
    }
  }

  @Override public Player getTurn() {
    if (this.isGameOver()) {
      return null;
    }
    if (this.playerTurn % 2 == 0) {
      return Player.RED;
    } else {
      return Player.YELLOW;
    }
  }

  @Override public Player getWinner() {
    // Check horizontally for winner
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns - 3; j++) {
        Player firstCell = this.board[i][j];
        if (Objects.nonNull(firstCell) && firstCell == this.board[i][j + 1]
            && firstCell == this.board[i][j + 2] && firstCell == this.board[i][j + 3]) {
          return firstCell;
        }
      }
    }
    // Check vertically for winner
    for (int j = 0; j < columns; j++) {
      for (int i = 0; i < rows - 3; i++) {
        Player firstCell = this.board[i][j];
        if (Objects.nonNull(firstCell) && firstCell == this.board[i + 1][j]
            && firstCell == this.board[i + 2][j] && firstCell == this.board[i + 3][j]) {
          return firstCell;
        }
      }
    }
    // Check diagonally up left to down right for winner
    for (int i = 0; i < rows - 3; i++) {
      for (int j = 0; j < columns - 3; j++) {
        Player firstCell = this.board[i][j];
        if (Objects.nonNull(firstCell) && firstCell == this.board[i + 1][j + 1]
            && firstCell == this.board[i + 2][j + 2] && firstCell == this.board[i + 3][j + 3]) {
          return firstCell;
        }
      }
    }
    // Check diagonally down left to up right for winner
    for (int i = rows - 1; i >= 3; i--) {
      for (int j = 0; j < columns - 3; j++) {
        Player firstCell = this.board[i][j];
        if (Objects.nonNull(firstCell) && firstCell == this.board[i - 1][j + 1]
            && firstCell == this.board[i - 2][j + 2] && firstCell == this.board[i - 3][j + 3]) {
          return firstCell;
        }
      }
    }
    return null;
  }

  @Override public boolean isGameOver() {
    // If there is a winner, the game is over
    if (this.getWinner() != null) {
      return true;
    }

    // If there is any empty space, the game is not over
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        if (this.board[i][j] == null) {
          return false;
        }
      }
    }

    // If there is no winner and no empty spaces, the game is over
    return true;
  }

  @Override public void resetBoard() {
    initializeBoard();
    this.playerTurn = 0;
  }

  @Override public Player[][] getBoardState() {
    Player[][] boardCopy = new Player[rows][columns];
    for (int i = 0; i < rows; i++) {
      System.arraycopy(this.board[i], 0, boardCopy[i], 0, columns);
    }
    return boardCopy;
  }

  @Override public String toString() {
    // Using Java stream API to save code:
    return Arrays.stream(getBoardState()).map(
            row -> " " + Arrays.stream(row).map(p -> p == null ? " " : p.getDisplayName())
                .collect(Collectors.joining(" | ")))
        .collect(Collectors.joining("\n---------------------------\n"));
  }
}
