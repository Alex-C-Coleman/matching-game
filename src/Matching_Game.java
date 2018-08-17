import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Here is the pseudocode:
 *
 * class MessageLog extends ScrollPane{
 *     properties -> vBox
 *
 *     Constructor :
 *
 *     MessageLog(){
 *              create a ScrollPane with an internal vBox to display messages to user
 *     }
 *
 *     method addMessage(){
 *        takes String message
 *
 *        makes Label using message
 *
 *        sets Label to vBox
 *     }
 * }
 *
 * class GameSquare extends Rectangle{
 *     properties -> int x;
 *                   int y;
 *                   Color color;
 *
 *
 *       Constructor:
 *
 *          GameSquare(int x, int y, Color color){
 *                      create a rectangle with height & width = 100;
 *                      fill with color;
 *                      
 *                     set GameSquare properties to passed properties
 *          }
 *
 *          method brighten{
 *              sets fill to GOLD
 *          }
 *
 *          method correctGuess{
 *              sets fill to GREEN.brighter()
 *          }
 *
 *         method incorrectGuess{
 *             sets fill to RED.brighter()
 *         }
 *
 *         method resetColor{
 *             sets fill to original color
 *         }
 *
 *
 * }
 *
 * class GameBoard extends Pane{
 *
 *                   propeties -> int round;
 *                                List<GameSquare> currentPattern;
 *                                int currentGuessCount;
 *                                int n;
 *                                MessageLog messageLog;
 *                                Button nextRoundButton;
 *                                boolean allowedToGuess;
 *                                List<GameSquare> children;
 *
 *                    Constructor:
 *
 *                    GameBoard(int n, MessageLog messageLog, Button nextRoundButton){
 *                             sets n, messageLog, and nextRoundButton to passed properties
 *                    }
 *
 *                    method generateAndShowPattern{
 *                          use messageLog to inform user of roundStarting
 *                          disable nextRoundButton
 *                          generatePattern(3 + (round/3);
 *                          
 *                          create a timeline and use GameSquare.brighten to brighten the GameSquares in
 *                          currentPattern in the correct order, with small pauses in between flashes
 *                    }
 *
 *                    method generatePattern{
 *                         takes length
 *                         fill currentPattern with length amount of random GameSquares from children;
 *                    }
 *
 *                    method handleClickEvent{
 *                          takes MouseEvent e
 *                          if (boolean allowedToGuess = true){
 *                             determine if GameSquare clicked is correct GameSquare in pattern
 *                             if correct{
 *                                  display correctGuess() animation
 *                                  correctGuessCount ++
 *                                  if(correct & finished(currentGuessCount == currentPatter.size){
 *                                          inform user they won
 *                                          currentLevel ++
 *                                          generate new pattern
 *                                          allowedToGuess = false
 *                                          enable nextRoundButton
 *                                          
 *                                  } 
 *                             } else{
 *                             display gameOver()
 *                             inform user they lost
 *                             currentLevel = 1
 *                             allowedToGuess = false
 *                             enable nextRoundButton
 *                          }
 *                    }
 *
 *                    method populateGameBoard{
 *                          fills GameBoard up with GameSquares
 *                    }
 *
 *                    method gameOver{
 *
 *                       performs a specific animation to display when game is over
 *                    }
 *
 * }
 *
 * class Matching_Game extends Application{
 *
 *      properties -> VBox controlsAndInfo;
 *                    GridPane grid;
 *                    Buttons button1, button2, button3, nextRoundButton;
 *                    MessageLog messsageLog;
 *                    GameBoard gameBoard;
 *
 *
 *        method start{
 *              add everything to scene with desired formatting
 *              call registerEventHandler (button, 2), (button2, 3), (button3, 4) 
 *              nextRoundButton.registerEventHandler so that generateAndShowPattern is called when button is clicked
 *              add scene to primaryStage
 *              run primaryStage
 *        }
 *
 *        method registerEventHandler{
 *            takes a Button and int n
 *
 *            sets Button on action so that the mouseBeing clicked will create a new
 *            GameSquare of n x n
 *        }
 *
 *
 *
 * }
 *
 *
 */

public class Matching_Game extends Application {

    public static void main(String[] args){
        launch(Matching_Game.class, args);
    }


    private VBox controlsAndInfo = new VBox();

    private GridPane grid = new GridPane();

    private Button button = new Button("2 x 2");
    private Button button2 = new Button("3 x 3");
    private Button button3 = new Button("4 x 4");
    private Button nextRoundButton = new Button("Start round 1");

    private MessageLog messageLog = new MessageLog();

    private GameBoard currentGameBoard = new GameBoard(3, messageLog, nextRoundButton);

    @Override
    public void start(Stage primaryStage) {

        messageLog.addMessage(
                "This is a pattern-matching game.\n\nThe game squares will flash gold in a specific order, " +
                        "and to win the round you must click the squares in the order they were flashed. " +
                        "\n\nGood luck!\n\nPress 'Start round' to start game");



        // For each button, set font to 20, and bind the button widthProperty to controlsAndInfo widthProperty
        for(Button b: new Button[]{button, button2, button3, nextRoundButton}) {
            b.setFont(new Font(20));
            b.prefWidthProperty().bind(controlsAndInfo.widthProperty());
        }

        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        cc1.setPercentWidth(30); // ColumnConstraint for controlsAndInfo, 30% of grid
        cc2.setPercentWidth(70); // ColumnConstraint for currentGameBoard, 70% of grid


        grid.getColumnConstraints().addAll(cc1, cc2);

        grid.addColumn(0, controlsAndInfo);
        grid.addColumn(1, currentGameBoard);

        
        registerEventHandler(button, 2);
        registerEventHandler(button2, 3);
        registerEventHandler(button3, 4);

        // When nextRoundButton is clicked, generateAndShowPattern
        nextRoundButton.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> currentGameBoard.generateAndShowPattern());

        // Set scene
        Scene scene = new Scene(grid, 700, 700 * .7);


        VBox.getVgrow(messageLog);
        messageLog.setPrefHeight(1000);
        controlsAndInfo.getChildren().setAll(button, button2, button3, nextRoundButton, messageLog);
        controlsAndInfo.setSpacing(12);

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     *
     * @param b -> button to add EventHandler to
     * @param n -> size of grid to change to
     *          
     */
    private void registerEventHandler(Button b, int n){

        // registerEventHandler to passed Button
        b.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            grid.getChildren().remove(currentGameBoard); // remove gameBoard to be replace from grid
            currentGameBoard = new GameBoard(n, messageLog, nextRoundButton); // create a new gameBoard with n x n dimensions
            grid.add(currentGameBoard, 1,0); // add new GameBoard back to grid
            messageLog.addMessage("\nBoard re-sized. Game starting over.\n\nPress 'Start round' to start game"); // Inform user of the gird being resized
        });
        
    }
}


class GameBoard extends Pane {
    private int round = 1; // Starting round = 1
    private List<GameSquare> currentPattern = new ArrayList<>();
    private int currentGuessCount = 0;

    private int n; // number of rows/columns -> n x n matrix
    private MessageLog messageLog;

    private Button nextRoundButton;
    private boolean allowedToGuess = false; // Users cannot guess by default

    private List<GameSquare> children; // A list containing each GameSquare in GameBoard

    GameBoard(int n, MessageLog messageLog, Button nextRoundButton) {
        super();
        this.n = n;
        this.messageLog = messageLog;
        this.nextRoundButton = nextRoundButton;


        nextRoundButton.setDisable(false); // nextRoundButton can be clicked
        nextRoundButton.setText("Start round 1"); // nextRoundButton default text

        // create the GameSquares and place them in the correct locations
        populateGameBoard();
    }

    void generateAndShowPattern() {

        nextRoundButton.setDisable(true); // disable the nextRoundButton
        messageLog.addMessage("Starting round " + round); // change nextRoundButton to represent nextRound


        generatePattern(3 + (round/3)); // Default length is 3, increase pattern length after two round(i.e on round 3, 6, etc)


        Timeline timeline = new Timeline();

        // Set notches in time(since the beginning of the timeLine);
        for(int i = 0; i < currentPattern.size(); i++) {
            final int time = i * 500; // Time between each item in pattern flashing(in millis)
            GameSquare focused = currentPattern.get(i);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time), e -> focused.brighten())); // Brighten
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time + 300), e-> focused.resetColor())); // Reset back to original color
        }

        timeline.setCycleCount(1);
        timeline.setOnFinished( e -> allowedToGuess = true); // allow user to guess when pattern is finished showing
        timeline.setDelay(Duration.millis(300));
        timeline.play(); // Play animation
    }

    private void generatePattern(int length){

        Random r = new Random();

        currentGuessCount = 0;
        currentPattern.clear();

        // Generate a random pattern of GameSquares
        for(int i = 0; i < length; i++) {
            GameSquare random = children.get(r.nextInt(children.size()));
            currentPattern.add(random);
        }
    }

    private void handleClickEvent(MouseEvent e) {

        if(allowedToGuess) {
            GameSquare clicked = (GameSquare)e.getSource(); // Only GameSquares have this set up as an eventHandler

            if(currentPattern.get(currentGuessCount) == clicked) {
                // We clicked the right one!
                currentGuessCount++; // Increase the correctGuessCounter


                Timeline timeline = new Timeline();
                // At the beginning, set GameSquare back to original color(done in case user clicks the same square
                // correctly twice in a row, before timeline has finished
                timeline.getKeyFrames().add(new KeyFrame(Duration.ZERO, a -> clicked.resetColor()));
                // Flash green at 100 millis
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), a -> clicked.correctGuess()));
                // Set back to original color at 400 millis
                timeline.getKeyFrames().add(new KeyFrame(Duration.millis(400), a -> clicked.resetColor()));
                timeline.play();

                if(currentGuessCount == currentPattern.size()){
                    this.messageLog.addMessage("You win! Press 'Start round' to move forward.");
                    round++;
                    nextRoundButton.setText("Start round " + round);
                    nextRoundButton.setDisable(false); // User can now click nextRoundButton
                    currentPattern.clear();
                    allowedToGuess = false; // User cannot currently guess pattern

                }
            } else {
                gameOver(clicked); // Call gameOver method

                this.messageLog.addMessage("Game over.");

                allowedToGuess = false;  // User cannot currently guess pattern
                currentPattern.clear();
                round = 1;
                nextRoundButton.setText("Start round 1");
                nextRoundButton.setDisable(false); // User can now click nextRoundButton
                currentPattern.clear();

            }
        }
    }

    private void populateGameBoard(){

        List<GameSquare> newChildren = new ArrayList<>();
        Random random = new Random();
        // Generate random color more in the blue range
        Color color = Color.rgb(random.nextInt(50), random.nextInt(50), (random.nextInt(200) + 50), .99);


        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                // Create a GameSquare with a random color
                GameSquare gameSquare = new GameSquare(i, j, color);

                // Add it to our list keeping track of all the squares
                newChildren.add(gameSquare);

                // Add it to the pane
                super.getChildren().add(gameSquare);

                // Bindings to make the width based on the size of the pane
                gameSquare.widthProperty().bind(this.widthProperty().divide(n).subtract(5));
                gameSquare.heightProperty().bind(this.heightProperty().divide(n).subtract(5));

                // Bindings to make each square rests in the right location in the pane
                gameSquare.layoutXProperty().bind(this.widthProperty().divide(n).multiply(gameSquare.x));
                gameSquare.layoutYProperty().bind(this.heightProperty().divide(n).multiply(gameSquare.y));

                gameSquare.setOnMouseClicked(this::handleClickEvent);
            }
        }

        children = newChildren;
    }

    /**
     *
     * @param incorrectSquare -> GameSquare that user clicked which was incorrect and ended the game
     *
     * Animation to display once game is over
     */
    private void gameOver(GameSquare incorrectSquare){


        Timeline timeline = new Timeline();

        int interval = 100; // Time between each GameSquare turning red(in millis)

        // Set notches in time(since the beginning of the timeLine);
        for(int i = 0; i < children.size(); i++) {
            // Time between frames, based on i so that delay between red flashes occurs
            final int time = i * interval;

            // We want the sequence to start at the GameSquare that was incorrectly clicked, and go to
            // the one right before it, so the GameSquare we flash is children.indexOf(incorrectSquare) + i)
            // % children.size(), so OutOfBounds error does not occur
            GameSquare focused = children.get((children.indexOf(incorrectSquare) + i) % children.size());

            // Flash red
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time), e -> focused.incorrectGuess()));
            // Reset back to original color(250 millis after flashed red)
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time + 250), e-> focused.resetColor()));
        }

        for(int i = 0; i < children.size(); i++) {
            int time = children.size() * interval + 500; // Time after every individual GameSquare has flashed red + 500 millis
            GameSquare focused = children.get(i);
            // Flash everything red at same time
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time), e -> focused.incorrectGuess()));

            // Reset everything back to original color(1000 millis after flashed red)
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(time + 1000), e-> focused.resetColor()));
        }


        timeline.setCycleCount(1);
        timeline.setDelay(Duration.millis(300)); // So animation does not start right away
        timeline.play();
    }
}

/**
 * An individual square in the GameBoard
 */

class GameSquare extends Rectangle{

    private Color color;
    int x; // Between 0 - GameBoard.n
    int y; // Between 0 - GameBoard.n

    GameSquare(int x, int y, Color color){

       super(100, 100);

       super.setFill(color);
       this.color = color;
       this.x = x;
       this.y = y;

    }

    void brighten() {
        super.setFill(Color.GOLD);
    }

    void correctGuess(){
        super.setFill(Color.GREEN.brighter());
    }

    void incorrectGuess(){
        super.setFill(Color.RED.brighter());
    }

    void resetColor() {
        super.setFill(color);
    }
}

/**
 * Used to give info to the user
 */
class MessageLog extends ScrollPane {

    private VBox vBox = new VBox(); // vBox which MessageLog will be appended to

    MessageLog(){
        super();
        super.setContent(vBox);
        vBox.setSpacing(8); // Set spacing between messages

        // Automatically scroll down
        this.vvalueProperty().bind(vBox.heightProperty());
        this.setFitToWidth(true);
    }

    /**
     *
     * @param message -> message to add to vBox
     */
    void addMessage(String message) {
        Label label = new Label(message);
        label.setWrapText(true);
        label.setFont(new Font(21));
        vBox.getChildren().add(label);
    }
}
