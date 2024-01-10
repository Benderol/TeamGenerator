package com.project.TeamGenerator.exeptions;

public class PlayerExistsException extends Exception{

    public PlayerExistsException() {
        super("This player already exists");
    }
    public PlayerExistsException(String message) {
        super(message);
    }
}
