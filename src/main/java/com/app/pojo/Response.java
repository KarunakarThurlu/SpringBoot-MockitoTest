package com.app.pojo;

import org.springframework.http.HttpStatus;

public class Response<T> {

    private HttpStatus status;
    private T data;
    private String message;

    private Response(){
    }

    public Response(HttpStatus status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
