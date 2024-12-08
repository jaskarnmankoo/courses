package _backend.framework.models.user;

import _backend.utils.errorHandling.ErrorLogger;

public class UserAccount {

    // Missing: Avatar

    private int userID; // Used to uniquely identify users within the database

    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastname;

    private UserMediaHandler mediaHandler; // Handler that babysits the media a UserAccount has

    public UserAccount(String username, String email, String password, String firstname, String lastname) {
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.mediaHandler = new UserMediaHandler();
    }

    /**
     * Upon creation, users are assigned their own media handler.
     */
    public UserAccount() {
        this.mediaHandler = new UserMediaHandler();
    }

    // GETTERS AND SETTERS
    public void setUserID(int ID) {
        this.userID = ID;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstName() {
        return this.firstname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getLastName() {
        return this.lastname;
    }

    public void setUsername(String username) {
        if (username == null) {
            ErrorLogger.logMessage("Username should not be null!");
        }

        this.username = username;

    }

    public String getUsername() {
        return this.username;
    }

    public void setEmail(String email) {
        if (email == null) {
            ErrorLogger.logMessage("Email should not be null!");
        }

        this.email = email;

    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        if (password == null) {
            ErrorLogger.logMessage("Password should not be null!");
        }

        this.password = password;

    }

    public String getPassword() {
        return this.password;
    }

    public void setUserMediaHandler(UserMediaHandler newUMH) {
        if (newUMH == null) {
            ErrorLogger.logMessage("UserMediaHandler should not be null!");
        }

        this.mediaHandler = newUMH;

    }

    public UserMediaHandler getMediaHandler() {
        return this.mediaHandler;
    }

    @Override
    public String toString() {
        String s = "--UserAccount " + this.getUserID() + "--\n";
        s += "Name: " + this.getUsername() + "\n";
        s += "Email: " + this.getEmail() + "\n";
        s += "Password: " + this.getPassword() + "\n";
        s += "Media List: \n--------\n";

        s += this.mediaHandler;
        s += "\n";

        return s;
    }
}
