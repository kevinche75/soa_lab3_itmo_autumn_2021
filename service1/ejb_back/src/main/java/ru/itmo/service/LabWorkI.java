package ru.itmo.service;

import ru.itmo.utils.ResponseWrapper;

import javax.ejb.Remote;
import java.util.HashMap;

@Remote
public interface LabWorkI {

    public ResponseWrapper getAllLabWorks(HashMap<String, String> map);
    public ResponseWrapper getLabWork(String str_id);
    public ResponseWrapper getMinName();
    public ResponseWrapper countPersonalQualitiesMaximum(String str_pqm);
    public ResponseWrapper createLabWork(String xmlStr);
    public ResponseWrapper updateLabWork(String str_id, String xmlStr);
    public ResponseWrapper deleteLabWork(String str_id);
    public ResponseWrapper test();
    public ResponseWrapper getLessMaximumPoint(HashMap<String, String> map, String maximum_point);
}
