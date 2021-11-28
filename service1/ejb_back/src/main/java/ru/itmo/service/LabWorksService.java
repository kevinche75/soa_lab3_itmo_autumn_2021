package ru.itmo.service;

import ru.itmo.DAO.LabWorksDAO;
import ru.itmo.converter.FieldConverter;
import ru.itmo.converter.XMLConverter;
import ru.itmo.entity.LabWork;
import ru.itmo.utils.LabWorkParams;
import ru.itmo.utils.LabWorksResult;
import ru.itmo.utils.ResponseWrapper;
import ru.itmo.utils.ServerResponse;
import ru.itmo.validator.Validator;
import ru.itmo.validator.ValidatorResult;

import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

@Stateless
public class LabWorksService implements LabWorkI, Serializable {

    private static final String NAME_PARAM = "name";
    private static final String CREATION_DATE_PARAM = "creationDate";
    private static final String MINIMAL_POINT_PARAM = "minimalPoint";
    private static final String MAXIMAL_POINT_PARAM = "maximumPoint";
    private static final String PERSONAL_QUALITIES_MAXIMUM_PARAM = "personalQualitiesMaximum";
    private static final String DIFFICULTY_PARAM = "difficulty";
    private static final String COORDINATES_X_PARAM = "coordinatesX";
    private static final String COORDINATES_Y_PARAM = "coordinatesY";

    private static final String PAGE_IDX_PARAM = "pageIdx";
    private static final String PAGE_SIZE_PARAM = "pageSize";
    private static final String SORT_FIELD_PARAM = "sortField";

    private static final String PERSON_NAME_PARAM = "authorName";
    private static final String PERSON_WEIGHT_PARAM = "authorWeight";

    private static final String LOCATION_X_PARAM = "locationX";
    private static final String LOCATION_Y_PARAM = "locationY";
    private static final String LOCATION_Z_PARAM = "locationZ";
    private static final String LOCATION_NAME_PARAM = "locationName";

    private XMLConverter xmlConverter;
    private LabWorksDAO dao;

    public LabWorksService() {
        xmlConverter = new XMLConverter();
        dao = new LabWorksDAO();
        System.out.println("Bean launched");
    }

    private LabWorkParams getLabWorksParams(HashMap<String, String> map){

        LabWorkParams params = new LabWorkParams();
        params.setLabWorkParams(
                map.get(NAME_PARAM),
                map.get(CREATION_DATE_PARAM),
                map.get(MINIMAL_POINT_PARAM),
                map.get(MAXIMAL_POINT_PARAM),
                map.get(PERSONAL_QUALITIES_MAXIMUM_PARAM),
                map.get(DIFFICULTY_PARAM),
                map.get(COORDINATES_X_PARAM),
                map.get(COORDINATES_Y_PARAM),
                map.get(PERSON_NAME_PARAM),
                map.get(PERSON_WEIGHT_PARAM),
                map.get(LOCATION_X_PARAM),
                map.get(LOCATION_Y_PARAM),
                map.get(LOCATION_Z_PARAM),
                map.get(LOCATION_NAME_PARAM),
                map.get(PAGE_IDX_PARAM),
                map.get(PAGE_SIZE_PARAM),
                map.get(SORT_FIELD_PARAM)
        );
        return params;
    }

    public ResponseWrapper getInfo(int code, String message) {
        ServerResponse serverResponse = new ServerResponse(message);
        return new ResponseWrapper(code, xmlConverter.toStr(serverResponse));
    }

    public ResponseWrapper getAllLabWorks(HashMap<String, String> map) {

        LabWorkParams params = getLabWorksParams(map);
        return getLabWorks(params);
    }

    private ResponseWrapper getLabWorks(LabWorkParams params){
        if (!params.getValidatorResult().isStatus()) {
            return getInfo(400, params.getValidatorResult().getMessage());
        }
        try {
            LabWorksResult labWorksResult = dao.getAllLabWorks(params);
            return new ResponseWrapper(xmlConverter.toStr(labWorksResult));
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getLabWork(String str_id) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "LabWork Id", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            Optional<LabWork> labWork = dao.getLabWork(id);
            if (labWork.isPresent()) {
                return new ResponseWrapper(xmlConverter.toStr(labWork.get()));
            } else {
                return getInfo(404, "No labWork with such id: " + id);
            }
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper getMinName() {
        try {
            LabWork lab = dao.getMinName();
            if (lab == null) {
                return getInfo( 404, "No labs");
            }
            return new ResponseWrapper(xmlConverter.toStr(xmlConverter.toStr(lab)));
        } catch (Exception e) {
            return getInfo( 500, "Server error, try again");
        }
    }

    public ResponseWrapper countPersonalQualitiesMaximum(String str_pqm) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long pqm = FieldConverter.longConvert(str_pqm, "LabWork Personal Qualities Maximum", validatorResult);
        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }
        try {
            Long count = dao.countPQM(pqm);
            if (count != null) {
                return getInfo(200, "Count of labs with PQM: " + str_pqm + " is: " + count);
            } else {
                return getInfo(500, "Server error, try again");
            }
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper createLabWork(String xmlStr) {
        try {
            ru.itmo.stringEntity.LabWork stringLabWork = xmlConverter.fromStr(xmlStr, ru.itmo.stringEntity.LabWork.class);
            ValidatorResult validatorResult = Validator.validateLabWork(stringLabWork);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            LabWork labWork = xmlConverter.fromStr(xmlStr, LabWork.class);
            Long id = dao.createLabWork(labWork);
            return new ResponseWrapper(200);
//            return Response.ok().build();
        } catch (JAXBException e) {
            return getInfo(400, "Unknown data structure");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper updateLabWork(String str_id, String xmlStr) {
        try {
            ru.itmo.stringEntity.LabWork stringLabWorkUpdate = xmlConverter.fromStr(xmlStr, ru.itmo.stringEntity.LabWork.class);
            ValidatorResult validatorResult = Validator.validateLabWork(stringLabWorkUpdate);
            Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);
            Validator.validateCreationDate(stringLabWorkUpdate, validatorResult);
            Validator.validateId(stringLabWorkUpdate, validatorResult);
            if (!validatorResult.isStatus()) {
                return getInfo(400, validatorResult.getMessage());
            }
            LabWork labWorkUpdate = xmlConverter.fromStr(xmlStr, LabWork.class);
            Optional<LabWork> lab = dao.getLabWork(id);
            if (lab.isPresent()) {
                LabWork labWorkPresent = lab.get();
                labWorkPresent.update(labWorkUpdate);
                dao.updateLabWork(labWorkPresent);
                return new ResponseWrapper(200);
            } else
                return getInfo(404, "No LabWork with such id: " + labWorkUpdate.getId());
        } catch (JAXBException e) {
            return getInfo(400, "Can't understand data structure");
        } catch (Exception e) {
            return getInfo(500, "Server error, try again");
        }
    }

    public ResponseWrapper deleteLabWork(String str_id) {
        ValidatorResult validatorResult = new ValidatorResult();
        Long id = FieldConverter.longConvert(str_id, "Delete id", validatorResult);

        if (!validatorResult.isStatus()) {
            return getInfo(400, validatorResult.getMessage());
        }

        boolean result = dao.deleteLabWork(id, validatorResult);
        if (!result)
            return getInfo(validatorResult.getCode(), validatorResult.getMessage());
        else
            return new ResponseWrapper(200);
    }

    @Override
    public ResponseWrapper test() {
        return new ResponseWrapper(200, "Success");
    }

    @Override
    public ResponseWrapper getLessMaximumPoint(HashMap<String, String> map, String maximum_point) {
        LabWorkParams params = getLabWorksParams(map);
        params.setLessMaximalPointFlag(true);
        params.setMaximumPoint(maximum_point);
        return getLabWorks(params);
    }
}
