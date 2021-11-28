package ru.itmo.service;

import ru.itmo.DAO.DisciplineDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.Difficulty;
import ru.itmo.entity.Discipline;
import ru.itmo.stringEntity.LabWork;
import ru.itmo.utils.DisciplineResult;
import ru.itmo.utils.LabWorksResult;
import ru.itmo.utils.ResponseWrapper;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.Validator;
import ru.itmo.validator.ValidatorResult;

import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class SecondService implements SecondServiceI, Serializable {

    private XMLConverter xmlConverter;
    private DisciplineDAO dao;
    private Map<Integer, String> difficultyMap;

    public SecondService() {
        xmlConverter = new XMLConverter();
        dao = new DisciplineDAO();
        difficultyMap = Arrays
                .stream(Difficulty.values())
                .collect(Collectors.toMap(Difficulty::ordinal, Difficulty::toString));
    }

    public ResponseWrapper getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return new ResponseWrapper(code, xmlConverter.toStr(serverResponse));
    }


    public ResponseWrapper getDisciplines(){
        try {
            DisciplineResult disciplineResult = dao.getAllDisciplines();
            return new ResponseWrapper(xmlConverter.toStr(disciplineResult));
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getDiscipline(String stringId){
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            Long id = FieldConverter.longConvert(stringId, "Discipline Id", validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Optional<Discipline> discipline = dao.getDiscipline(id);
            if (discipline.isPresent()){
                return new ResponseWrapper(xmlConverter.toStr(discipline.get().toUnrealDiscipline()));
            } else {
                return getInfo(400, String.format("No discipline with id: %s", stringId));
            }
        } catch (Exception e){
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper createDiscipline(String xmlString){
        try {
            ru.itmo.stringEntity.Discipline stringDiscipline = xmlConverter.fromStr(xmlString, ru.itmo.stringEntity.Discipline.class);
            ValidatorResult validatorResult = Validator.validateDiscipline(stringDiscipline);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Discipline discipline = stringDiscipline.toRealDiscipline();
            Long id = dao.createDiscipline(discipline);

            return new ResponseWrapper(200);
        }catch (PersistenceException e){
            return getInfo(400, "Discipline has already existed");
        } catch (JAXBException e) {
            return getInfo(400, "Unknown data structure");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getDisciplineLabWorks(String stringDisciplineId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        LabWorksResult result = null;
        try {
            result = dao.getDisciplineLabWorks(id);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        }
        return new ResponseWrapper(xmlConverter.toStr(result));
    }

    public ResponseWrapper addLabWorkToDiscipline(String stringDisciplineId, String stringLabWorkId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        Long labWorkId = FieldConverter.longConvert(stringLabWorkId, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            dao.addLabWork(id, labWorkId);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        } catch (EntityExistsException e){
            return getInfo(400, e.getMessage());
        }
        return getInfo(200, "Success");
    }

    public ResponseWrapper removeLabWorkFromDiscipline(String stringDisciplineId, String stringLabWorkId){
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(stringDisciplineId, "Discipline Id", validatorResult);
        Long labWorkId = FieldConverter.longConvert(stringLabWorkId, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            dao.deleteLabWorkFromDiscipline(id, labWorkId);
        } catch (EntityNotFoundException e){
            return getInfo(400, e.getMessage());
        }
        return new ResponseWrapper(200);
    }

    public ResponseWrapper increaseLabWorkDifficulty(String id, String stringSteps) {
        try {
            ValidatorResult validatorResult = new ValidatorResult();
            FieldConverter.longConvert(id, "Discipline Id", validatorResult);
            Integer steps = FieldConverter.intConvert(stringSteps, "Steps", validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            Response labWorkResponse = dao.getTarget().path(id).request().accept(MediaType.APPLICATION_XML).get();
            if (labWorkResponse.getStatus() != 200){
                ServerResponse serverResponse = labWorkResponse.readEntity(ServerResponse.class);
                return getInfo(labWorkResponse.getStatus(), serverResponse.getMessage());
            }
            String strLab = labWorkResponse.readEntity(String.class);
            LabWork labWork = xmlConverter.fromStr(strLab, LabWork.class);
            int difficulty = Difficulty.valueOf(labWork.getDifficulty()).ordinal();
            int resultDifficulty = difficulty + steps;
            if (!difficultyMap.containsKey(resultDifficulty)){
                return getInfo(400, "Increased Difficulty out of bounds");
            }
            labWork.setDifficulty(difficultyMap.get(resultDifficulty));
            Response changeDifficultyResponse = dao.getTarget().path(id).request().accept(MediaType.APPLICATION_XML).put(Entity.entity(labWork, MediaType.APPLICATION_XML));
            if (changeDifficultyResponse.getStatus() != 200){
                ServerResponse serverResponse = changeDifficultyResponse.readEntity(ServerResponse.class);
                return getInfo(changeDifficultyResponse.getStatus(), serverResponse.getMessage());
            }
        } catch (Exception e){
            e.printStackTrace();
            return getInfo(500, "Server error, try again");
        }
        return getInfo(200, "Success");
    }

    @Override
    public ResponseWrapper test() {
        return new ResponseWrapper(200, "Success");
    }
}