package com.project.FitSync.exceptions;

public class ActivityNotFoundException extends RuntimeException{
    public ActivityNotFoundException(String meassage, Long id){
        super(meassage +id);
    }
}
