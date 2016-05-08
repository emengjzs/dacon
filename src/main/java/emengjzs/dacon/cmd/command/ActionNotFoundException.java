package emengjzs.dacon.cmd.command;

public class ActionNotFoundException extends RuntimeException {

    public ActionNotFoundException(String action) {
        super("Command action " + action + " not found!");
    }

}
