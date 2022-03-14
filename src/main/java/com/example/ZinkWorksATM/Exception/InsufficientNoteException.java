package com.example.ZinkWorksATM.Exception;

public class InsufficientNoteException extends Exception {
    public InsufficientNoteException(String message){
        super(message);
    }
}
