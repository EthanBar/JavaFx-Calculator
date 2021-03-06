import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;

public class Calculator extends Application {

    private int[] column = {0,0,1,2,0,1,2,0,1,2,0,1,2}; // Grid positons
    private int[] row = {4,3,3,3,2,2,2,1,1,1,0,0,0};
    private String[] funcTitles = {"÷", "×", "-", "+", "="};
    private double sto = 0; // What we are calculating agianst
    private String lastOp = ""; // What operation we are waiting to do
    private boolean updateRes = false; // Update the result next button press?
    private boolean updateHis = false; // Update the history next function press?

    public static void main(String[] args){launch(args);}

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Calculator");

        BorderPane bp = new BorderPane(); // Controls the grids below
        GridPane top = new GridPane();
        GridPane grid = new GridPane(); // Controls main buttons
        grid.setHgap(10);
        grid.setVgap(10);

        // Set special buttons
        Label res = new Label();
        Label history = new Label();
        Button clear = new Button();
        res.setText("0");
        clear.setText("Clr");
        grid.add(clear, 0, 0);
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sto = 0;
                res.setText("0");
                history.setText("");
            }
        });
        Button dot = new Button();
        dot.setText(".");
        grid.add(dot, 2, 4);
        dot.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!res.getText().contains(".")){
                    res.setText(res.getText() + ".");
                }
            }
        });
        Button back = new Button();
        back.setText("Del");
        grid.add(back, 1, 0);
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (res.getText().length() > 1) {
                    res.setText(res.getText().substring(0, res.getText().length() - 1)); // Remove last char of string
                } else {
                    res.setText("0");
                }
            }
        });


        // Set opperation buttons
        Button[] ops = new Button[5];
        for (int i = 0; i < ops.length; i++){
            ops[i] = new Button();
            ops[i].setText(funcTitles[i]);
            grid.add(ops[i], 3, i);
            final int toI = i; // Final to fix compiler complaints
            ops[i].setOnAction(new EventHandler<ActionEvent>() {
                boolean updateLate = false;
                @Override
                public void handle(ActionEvent event) {
                    if (toI != 4){ // If not equals
                        if (!updateHis){
                            history.setText(res.getText() + " " + ops[toI].getText());
                        } else {
                            updateLate = true;
                        }
                        updateHis = true;
                        updateRes = true;
                        // Animations are here. Fade in, and slide in from the right
                        history.setText(res.getText() + " " + ops[toI].getText());
                        history.setTranslateX(20f);
                        FadeTransition ft = new FadeTransition(Duration.millis(500), history);
                        ft.setFromValue(0.0);
                        ft.setToValue(0.7);
                        ft.setCycleCount(1); // Only do the animation once
                        TranslateTransition tt = new TranslateTransition(Duration.millis(400), history);
                        tt.setByX(-20f);
                        tt.setCycleCount(1);
                        tt.play();
                        ft.play();
                    } else {
                        history.setText("");
                        updateHis = false;
                    }
                    switch(lastOp){
                        case "÷":
                            sto /= Double.parseDouble(res.getText());
                            res.setText(trailer(Double.toString(sto)));
                            break;
                        case "×":
                            sto *= Double.parseDouble(res.getText());
                            res.setText(trailer(Double.toString(sto)));
                            break;
                        case "-":
                            sto -= Double.parseDouble(res.getText());
                            res.setText(trailer(Double.toString(sto)));
                            break;
                        case "+":
                            sto += Double.parseDouble(res.getText());
                            res.setText(trailer(Double.toString(sto)));
                            break;
                        default:
                            if (toI != 4){ // If not equals
                                sto = Double.parseDouble(res.getText());
                                res.setText("0");
                            }
                    }
                    if (updateLate){
                        System.out.println("yet");
                        history.setText(res.getText() + " " + ops[toI].getText());
                    }
                    lastOp = ops[toI].getText();
                }
            });
        }


        // Set numbers 0-9
        Button[] nums = new Button[10];
        for (int i = 0; i < nums.length; i++){
            nums[i] = new Button();
            grid.add(nums[i], column[i], row[i]);
            nums[i].setText(Integer.toString(i));
            final int toI = i; // Final to fix compiler complaints
            nums[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (updateRes){
                        res.setText("0");
                        updateRes = false;
                    }
                    if (res.getText().equals("0")){
                        res.setText(Integer.toString(toI));
                    } else {
                        res.setText(res.getText() + Integer.toString(toI));
                    }
                }
            });
        }
        // CSS ids
        ops[4].setId("equal");
        clear.setId("equal");
        history.setId("history");
        nums[0].setId("zero");
        res.setId("res");

        // Positioning
        grid.setColumnSpan(nums[0], 2);
        top.add(history, 0, 0);
        top.add(res, 1, 0 );
        bp.setTop(top);
        bp.setPadding(new Insets(10,5,10,10));
        grid.setPadding(new Insets(10,0,10,0));
        bp.setCenter(grid);
        grid.setValignment(res, VPos.TOP);

        // Set scene
        Scene scene = new Scene(bp, 250, 348);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Calculator.class.getResource("fancy.css").toExternalForm()); // Load our custom CSS
        primaryStage.show(); // Load that sence


    }

    public String trailer(String in){
        return new BigDecimal(in).stripTrailingZeros().toPlainString();
    }

}
