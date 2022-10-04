package managers.filebacked;

import java.io.IOException;

public class ManagerSaveException extends IOException {
    public ManagerSaveException(final String message, IOException e) {
        super(message);
        e.getStackTrace();
    }
}
