package com.userService.Exception;

public class RecordNotFoundException extends RuntimeException{

    public RecordNotFoundException(String message){
        super(message);
    }
}
