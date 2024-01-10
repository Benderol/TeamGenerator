package com.project.TeamGenerator.exeptions;

public class EmptyPlayerValue extends PlayerExistsException{
    public EmptyPlayerValue() {
        super("Empty first name or last name");
    }
}
