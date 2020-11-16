/**
 * LogicExceptions are used to explain user error from within functions in the Query class.
 * They should be created within a Query function with a relevant description,
 * and thrown to be handled by the calling function.
 *  */
public class LogicException extends Exception { 
    public LogicException(String errorMessage) {
        super(errorMessage);
    }
}
//hello