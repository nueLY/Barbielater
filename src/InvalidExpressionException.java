public class InvalidExpressionException extends Exception{

    InvalidExpressionException(){
        super("Invalid Expression");
    }
    InvalidExpressionException(String message){
        super(message);
    }
}
