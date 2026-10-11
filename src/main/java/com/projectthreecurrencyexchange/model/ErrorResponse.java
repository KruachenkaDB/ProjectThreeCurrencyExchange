package com.projectthreecurrencyexchange.model;

public class ErrorResponse {
    // подумать над надобностью класса
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
