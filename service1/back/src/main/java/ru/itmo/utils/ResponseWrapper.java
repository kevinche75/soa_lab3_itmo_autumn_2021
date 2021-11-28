package ru.itmo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
public class ResponseWrapper implements Serializable {

    private int code;
    private String payload;

    public ResponseWrapper(String payload){
        code = 200;
        this.payload = payload;
    }

    public ResponseWrapper(int code){
        payload = "";
        this.code = code;
    }
}
