package exceptions;

import java.io.IOException;

public class InvalidTaskException extends RuntimeException {
    public InvalidTaskException(String message, IOException e) {
        super(message);
    }
}