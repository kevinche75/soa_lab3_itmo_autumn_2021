package ru.itmo.service;

import javax.ejb.Remote;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Remote
public interface LabWorkI {

    public Response getAllLabWorks(MultivaluedMap<String, String> map);
    public Response getLabWork(String str_id);
    public Response getMinName();
    public Response countPersonalQualitiesMaximum(String str_pqm);
    public Response createLabWork(String xmlStr);
    public Response updateLabWork(String str_id, String xmlStr);
    public Response deleteLabWork(String str_id);
    public Response test();
    public Response getLessMaximumPoint(MultivaluedMap<String, String> map, String maximum_point);
}