package ru.itmo.stringEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlRootElement
public class Coordinates implements Serializable {

    @XmlElement
    private String id;

    @XmlElement
    private String x; //Поле не может быть null

    @XmlElement
    private String y;
}
