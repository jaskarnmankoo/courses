package _backend.commands;

public class CommandUtils {
    /**
     * Generates the correct amount of question marks for the PreparedStatement.
     * Assume there will always be at least 1 question mark.
     */
    public static String generateQuestionmarks(String primary_key, String[] key) {
        String str = "?";

        if (key == null) { // Insert Prepared Statement
            str = "?";

            for (int j = 1; j < primary_key.split(",").length; j++) {
                str += ", ?";
            }
        } else { // Delete Prepared Statement
            str = key[0] + "=?";

            for (int j = 1; j < key.length; j++) {
                str += " AND " + key[j] + "=?";
            }
        }

        return str;
    }

    public static String generateModifyMarks(String[] keys) {
        String str = keys[0] + "=?";

        for (int j = 1; j < keys.length; j++) {
            str += ", " + keys[j] + "=?";
        }

        return str;
    }
}
