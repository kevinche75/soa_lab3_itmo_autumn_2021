package ru.itmo.validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ValidatorResult implements Serializable {

    private String message = "";
    private boolean status = true;
    private int code;

    public void addMessage(String newMessage){
        this.message += newMessage + "\n";
        this.status = false;
    }
}
