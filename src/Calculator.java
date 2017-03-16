import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class Calculator extends Application {

    private int[] column = {0,0,1,2,0,1,2,0,1,2,0,1,2};
    private int[] row = {4,3,3,3,2,2,2,1,1,1,0,0,0};
    private String[] funcTitles = {"÷", "X", "-", "+", "="};
    private double sto = 0;
    private String lastOp = "";
    private boolean toOp = false;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Calculator");

        BorderPane bp = new BorderPane(); // Controls the main layout

        GridPane grid = new GridPane(); // Controls main buttons
        grid.setAlignment(Pos.TOP_CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        // Set special buttons
        Label res = new Label();
        Button clear = new Button();
        clear.setText("Clr");
        grid.add(clear, 0, 0);
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sto = 0;
                res.setText("0");
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


        // Set opperations
        Button[] ops = new Button[5];
        for (int i = 0; i < ops.length; i++){
            ops[i] = new Button();
            ops[i].setText(funcTitles[i]);
            grid.add(ops[i], 3, i);
            final int toI = i; // Final to fix compiler complaints
            ops[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    switch(lastOp){
                        case "÷":
                            sto /= Double.parseDouble(res.getText());
                            res.setText(trailer(Double.toString(sto)));
                            break;
                        case "X":
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
                    if (toI != 4){ // If not equals
                        toOp = true;
                    }
                    lastOp = ops[toI].getText();
                }
            });
        }


        // Set number 0-9
        Button[] nums = new Button[10];
        for (int i = 0; i < nums.length; i++){
            nums[i] = new Button();
            grid.add(nums[i], column[i], row[i]);
            nums[i].setText(Integer.toString(i));
            final int toI = i; // Final to fix compiler complaints
            nums[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (toOp){
                        res.setText("0");
                        toOp = false;
                    }
                    if (res.getText().equals("0")){
                        res.setText(Integer.toString(toI));
                    } else {
                        res.setText(res.getText() + Integer.toString(toI));
                    }
                }
            });
        }
        ops[4].setId("equal");
        nums[0].setId("zero");
        grid.setColumnSpan(nums[0], 2);
        bp.setTop(res);
        bp.setPadding(new Insets(10,10,10,10));
        grid.setPadding(new Insets(10,0,10,0));
        bp.setCenter(grid);
        grid.setValignment(res, VPos.TOP);
        res.setText("0");
        res.setDisable(true);

        Scene scene = new Scene(bp, 255, 345);
        primaryStage.sizeToScene();
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Calculator.class.getResource("fancy.css").toExternalForm()); // Load our custom CSS
        primaryStage.show(); // Load that sence


    }

    public String trailer(String in){
        return new BigDecimal(in).stripTrailingZeros().toPlainString();
    }

}