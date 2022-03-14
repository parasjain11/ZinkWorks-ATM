package com.example.ZinkWorksATM.Exception;

public class UnAuthorizedUserException extends Exception{
	public UnAuthorizedUserException(String message){
        super(message);
    }
}
