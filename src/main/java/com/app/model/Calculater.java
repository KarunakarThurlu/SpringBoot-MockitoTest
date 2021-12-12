package com.app.model;

import java.util.stream.IntStream;

public class Calculater {

    public int add(int a,int b){
        return a+b;
    }
    public int mul(int a,int b){
        return a*b;
    }
    public int subtract(int a,int b){
        return a-b;
    }

    public Boolean primeNumberCheck(Integer number){
        return IntStream.range(2,number).noneMatch(p->number%p==0);
    }
}
