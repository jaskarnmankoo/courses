package _backend.framework.controllers;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import _backend.framework.models.user.UserAccount;
import _backend.utils.database.DBMS;
import _backend.utils.database.GlobalHashMap;
import _backend.utils.database.MediaHashMap;
import _backend.utils.errorHandling.ErrorLogger;

public class UserManager {
    private static UserManager userManager = null;
    public static String ILIR = "Ilir"; // guest
    private UserAccount user;
    private GlobalHashMap globalMediaHash;
    private MediaHashMap publicList, privateList;

    private UserManager(String username) {
        if (username == null) {
            ErrorLogger.logError(new IllegalArgumentException("Invalid Username"));
        }

        // Default connect to SQLite
        Connection conn = DBMS.establishLocalDatabaseConnection("TrackrDatabase");
        List<Map<String, Object>> userInfo = DBMS.queryDatabase(conn, "SELECT * FROM UserAccount WHERE username=?",
                new String[] { username });

        // There should only be one row for the user
        if (userInfo.size() == 0) {
            System.out.println("User not found");
            return;
        }

        String name = (String) userInfo.get(0).get("username");
        String email = (String) userInfo.get(0).get("email");
        String password = (String) userInfo.get(0).get("password");
        String firstname = (String) userInfo.get(0).get("firstname");
        String lastname = (String) userInfo.get(0).get("lastname");

        // Create the usable hashmaps
        this.globalMediaHash = new GlobalHashMap(MediaHashMap.GLOBAL, "TrackrDatabase");
        this.publicList = new MediaHashMap(MediaHashMap.PUBLIC, name);
        this.privateList = new MediaHashMap(MediaHashMap.PRIVATE, name);

        user = new UserAccount(name, email, password, firstname, lastname);
    }

    public static UserManager createInstance(String username) {
        if (userManager == null) {
            userManager = new UserManager(username);
        }

        return userManager;
    }

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager(UserManager.ILIR);
        }

        return userManager;
    }

    public String username() {
        return user.getUsername();
    }

    public String email() {
        return user.getEmail();
    }

    public String firstname() {
        return user.getFirstName();
    }

    public String lastname() {
        return user.getLastName();
    }

    public MediaHashMap getPublicList() {
        return this.publicList;
    }

    public MediaHashMap getPrivateList() {
        return this.privateList;
    }

    public GlobalHashMap getGlobalList() {
        return this.globalMediaHash;
    }

    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Disposes of the singleton instance of UserManager.
     */
    public static void disposeInstance() {
        userManager = null;
    }
}
