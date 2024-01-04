import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Calculator extends Application {

    public static void main(String[] args) {
        launch();
    }

    private long operand1 = 0L, operand2 = 0L, result = 0L;
    private TextField textFieldExpression;
    private String operator = "+";
    private GridPane mainGrid;
    private Label statusLabel;
    private boolean lastClickOnOperator = false;


    @Override
    public void start(Stage stage){

        Scene scene;
        Image iconImage = new Image("Images/Barbie-Movie-Icon-Vector.png");
        try {
            scene = new Scene(createMainGrid(),345,400);
        } catch (InvalidExpressionException e) {
            throw new RuntimeException(e);
        }
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("appStyle.css")).toExternalForm());
        stage.getIcons().add(iconImage);
        // STAGE TITLE
        stage.setTitle("BARBIELATER");
        stage.setScene(scene);
        stage.show();
    }


    /***
     * Method used to create the main gridPane
     * @return GridPane
     *
     * @author NuelyFurtado
     * @since 2023-10-17
     */

    private GridPane createMainGrid() throws InvalidExpressionException {
        // create gridPane
        mainGrid = new GridPane();
        mainGrid.setPadding(new Insets(10));
        // create buttons
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);

        // CREATE TEXTFIELD TO DISPLAY RESULT
        textFieldExpression = createTextField();
        textFieldExpression.getStyleClass().add("text-field");
        textFieldExpression.setDisable(true);
        mainGrid.add(textFieldExpression, 0,0);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        mainGrid.add(grid,0,1);

        statusLabel = createLabel();
        mainGrid.add(statusLabel,0,2);
        // SET SPACE BETWEEN THE ELEMENTS IN THE MAIN GRId
        mainGrid.setVgap(15);

        Button btn9 = createNumberButton("9");
        grid.add(btn9,2,0);

        Button btn8 = createNumberButton("8");
        grid.add(btn8,1,0);

        Button btn7 = createNumberButton("7");
        grid.add(btn7,0,0);

        Button btn6 = createNumberButton("6");
        grid.add(btn6,2,1);

        Button btn5 = createNumberButton("5");
        grid.add(btn5,1,1);

        Button btn4 = createNumberButton("4");
        grid.add(btn4,0,1);

        Button btn3 = createNumberButton("3");
        grid.add(btn3,2,2);

        Button btn2 = createNumberButton("2");
        grid.add(btn2,1,2);

        Button btn1 = createNumberButton("1");
        grid.add(btn1,0,2);

        Button btn0 = createNumberButton("0");
        btn0.setMaxWidth(9999);
        grid.add(btn0,0,3,2,1);

        Button btnClear = createClearButton();
        grid.add(btnClear,2,3);

        Button btnSum = createOperator("+");
        grid.add(btnSum,3,0);

        Button btnSub = createOperator("-");
        grid.add(btnSub,3,1);

        Button btnMult = createOperator("*");
        grid.add(btnMult,3,2);

        Button btnDiv = createOperator("/");
        grid.add(btnDiv,3,3);

        // EQUAL BUTTON
        Image icon1 = new Image("Images/Barbie-Icon-Vector.png",30,30,
                                 true,true);

        Button btnEqual = createEqualButton(icon1);
        btnEqual.getStyleClass().add("myEqual");
        grid.add(btnEqual,4,0,1,4);


        return mainGrid;
    }


    /***
     * Method used to create operator button.
     * @param strOperator String not null
     * @return Button
     *
     * @author NuelyFurtado
     * @since 2023-10-19
     */
    private Button createOperator(String strOperator){
        Button operatorButton = new Button(strOperator);
        operatorButton.setPrefWidth(60);
        operatorButton.setMaxHeight(60);
        //Check how does the below line of code work.
        operatorButton.setOnAction(operatorButtonClick_Handler);
        return operatorButton;
    }

    /**
     * Method used to set action once numeric buttons are actioned.
     * @since 2023-10-11
     */
    private final EventHandler<ActionEvent> numericButtonClick_Handler = e->{
        statusLabel.setText("Ready");
        Button btn = (Button)e.getSource();
        if (lastClickOnOperator){
            textFieldExpression.setText(btn.getText());
        }
        else{
            textFieldExpression.setText(textFieldExpression.getText()+btn.getText());
        }
        lastClickOnOperator = false;
    };

    /***
     * Method used to set action when an operator button is clicked.
     * @since 2023-10-11
     */
    private final EventHandler<ActionEvent> operatorButtonClick_Handler = event->{
        Button btn = (Button)event.getSource();
        try{
            lastClickOnOperator = true;
            operator = btn.getText();
            if (!textFieldExpression.getText().isBlank()){
                operand1 = Long.parseLong(textFieldExpression.getText());
                textFieldExpression.setText(textFieldExpression.getText());
            }
            else{
                throw new InvalidExpressionException();
            }

        } catch (InvalidExpressionException e){
            statusLabel.setText("Invalid Expression my friend!");
        }
    };

    /***
     * Method used to set action when the eq button is clicked.
     * @since 2023-10-11
     */
    private final EventHandler<ActionEvent> equalButtonClick_Handler = event -> {
        try {
            if (!textFieldExpression.getText().isBlank() && !lastClickOnOperator) {
                operand2 = Long.parseLong(textFieldExpression.getText());
                long result = calculateResult(operand1, operand2, operator);
                textFieldExpression.setText(String.valueOf(result));
            } else {
                throw new InvalidExpressionException();
            }

        } catch (InvalidExpressionException e) {
            statusLabel.setText("Invalid Expression my friend!");
        }
    };

    /**
     * Method used to create numeric buttons and set in action.
     * @param buttonValue String not null
     * @return Button
     *
     * @author NuelyFurtado
     * @since 2023-10-11
     */
    private Button createNumberButton(String buttonValue){
        Button numericButton = new Button(buttonValue);
        numericButton.setPrefWidth(60);
        numericButton.setPrefHeight(60);
        numericButton.setOnAction(numericButtonClick_Handler);

        return numericButton;
    }

    /***
     * Method used to create a textField.
     * @return TextField
     *
     * @author NuelyFurtado
     * @since 2023-10-11
     */
    private TextField createTextField(){
        TextField textField = new TextField();
        textField.setPrefHeight(60);
        return textField;
    }


    /**
     * Method used to create Equal button and set in action
     * @return Button
     *
     * @author NuelyFurtado
     * @since 2023-10-11
     */
    private Button createEqualButton(Image icon){
        Button equalButton = new Button("",new ImageView(icon));
        equalButton.setMaxHeight(9999);
        equalButton.setPrefWidth(60);
        equalButton.setOnAction(equalButtonClick_Handler);
        return equalButton;
    }

    
    /**
     * Method used to create clear button and set action.
     * @return Button
     *
     * @author NuelyFurtado
     * @since 2023-10-11
     */
    private Button createClearButton(){
        Button clearButton = new Button("C");
        clearButton.setPrefWidth(60);
        clearButton.setMaxHeight(60);
        clearButton.setOnAction(event -> {

            statusLabel.setText("Ready");
            textFieldExpression.setText("");

        });
        return clearButton;
    }


    private Label createLabel(){
        return new Label("Ready");
    }

    /**
     * Method used to calculate the result.
     * @param operand1 int
     * @param operand2 int
     * @param operator String not null
     * @return int
     *
     * @author NuelyFurtado
     * @since 2023-10-11
     */
    private long calculateResult(long operand1, long operand2, String operator){

        switch (operator){
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                if (operand2 == 0){
                    return 0;
                } else {
                    return operand1 / operand2;
                }
            default:
                return 0;

        }

    }

}
