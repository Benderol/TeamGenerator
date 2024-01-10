package com.project.TeamGenerator.exeptions;

public class NotEnoughPlayers extends Exception{
    public NotEnoughPlayers() {
        super("Not enough players were selected");
    }
}
