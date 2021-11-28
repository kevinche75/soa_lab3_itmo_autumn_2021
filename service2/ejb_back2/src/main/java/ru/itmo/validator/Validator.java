package ru.itmo.validator;

import lombok.Getter;
import ru.itmo.converter.FieldConverter;
import ru.itmo.stringEntity.Discipline;

import java.io.Serializable;

@Getter
public class Validator implements Serializable {

    public static ValidatorResult validateDiscipline(Discipline strDiscipline){
        ValidatorResult validatorResult = new ValidatorResult();
        FieldConverter.stringConvert(strDiscipline.getName(), "Discipline Name", validatorResult);
        return validatorResult;
    }
}
