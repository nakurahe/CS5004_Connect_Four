import connect.ConnectFourController;
import connect.ConnectFourView;

/**
 * Run a Connect 4 game with a Swing based GUI.
 */
public class Main {
  /**
   * Run a Connect 4 game interactively.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    ConnectFourView view = new ConnectFourView("~ Let's Battle! Connect 4 ~");
    ConnectFourController controller = new ConnectFourController(view);

    controller.go();
  }
}