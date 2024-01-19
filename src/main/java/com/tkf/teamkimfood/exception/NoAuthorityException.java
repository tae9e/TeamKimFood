package com.tkf.teamkimfood.exception;

//권한 관련 예외
public class NoAuthorityException extends RuntimeException{
    public NoAuthorityException(String message) {
        super(message);
    }
}
