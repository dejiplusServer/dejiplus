/**
 * dejiplus - Package: syam.dejiplus.exception Created: 2012/12/03 2:38:30
 */
package syam.dejiplus.exception;

/**
 * CommandException (CommandException.java)
 * 
 * @author syam(syamn)
 */
public class CommandException extends Exception {
    private static final long serialVersionUID = 4458258938924399334L;

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
