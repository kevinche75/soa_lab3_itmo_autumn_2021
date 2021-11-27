package ru.itmo.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
public class ServerResponse implements Serializable {

    @XmlElement
    String message;
}
