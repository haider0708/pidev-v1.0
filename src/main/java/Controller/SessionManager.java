package Controller;

import Model.Patient;

public class SessionManager {
    private static Patient currentSession;
    private static int code;

    public static void saveCode(int newCode) {
        code = newCode;
    }

    public static int getCode() {
        return code;
    }

    public static void startSession(Patient patient) {
        currentSession = patient;
    }

    public static void endSession() {
        currentSession = null;
    }

    public static Patient getCurrentSession() {
        return currentSession;
    }
}
