package com.vikavika209.catshow.exception;

public class CatNotFoundException extends RuntimeException{
    public CatNotFoundException(String message) {
        super(message);
    }
}
