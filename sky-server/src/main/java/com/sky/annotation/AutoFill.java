package com.sky.annotation;

// autofill those public keywords

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // method
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {

//    Nominate
    OperationType value();
}
