package ru.itmo.controller;

import ru.itmo.service.LabWorkI;
import ru.itmo.service.RemoteBeanLookup;
import ru.itmo.utils.ResponseWrapper;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;

@Path("/labworks")
public class LabWorkController {

    private static final String MINIMAL_NAME_FLAG = "min_name";
    private static final String COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG = "count_personal_maximum/{personal_qualities_maximum}";
    private static final String LESS_MAXIMUM_POINT_FLAG = "less_maximum_point/{maximum_point}";

    private LabWorkI service;
    @Context
    ServletContext servletContext;

    public LabWorkController() {
        service = RemoteBeanLookup.lookupRemoteStatelessBean();
    }

    private Response unwrap(ResponseWrapper responseWrapper){
        return Response.status(responseWrapper.getCode()).entity(responseWrapper.getPayload()).build();
    }

    @GET
    public Response getLabWorks(@Context UriInfo ui) {
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        HashMap<String, String> hashMap = new HashMap<>();
        map.forEach((key, value) -> hashMap.put(key, value.get(0)));
        ResponseWrapper responseWrapper = service.getAllLabWorks(hashMap);
        return unwrap(responseWrapper);
    }

    @GET
    @Path("/{id}")
    public Response getLabWork(@PathParam("id") String id){
        return unwrap(service.getLabWork(id));
    }

    @GET
    @Path(LESS_MAXIMUM_POINT_FLAG)
    public Response getLessMaximumPointFlag(@Context UriInfo ui, @PathParam("maximum_point") String maximum_point){
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        HashMap<String, String> hashMap = new HashMap<>();
        map.forEach((key, value) -> hashMap.put(key, value.get(0)));
        return unwrap(service.getLessMaximumPoint(hashMap, maximum_point));
    }

    @GET
    @Path(MINIMAL_NAME_FLAG)
    public Response getMinimalNameLabWork(){
        return unwrap(service.getMinName());
    }

    @GET
    @Path("/test")
    public Response test(){
        ResponseWrapper response = service.test();
        System.out.println(response.getPayload());
        return Response.status(response.getCode()).entity(response.getPayload()).build();
    }


    @GET
    @Path(COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG)
    public Response countPersonalQualitiesMaximumLabWorks(@PathParam("personal_qualities_maximum") String personal_qualities_maximum){
        return unwrap(service.countPersonalQualitiesMaximum(personal_qualities_maximum));
    }

    @POST
    public Response createLabWork(String labWork){
        return unwrap(service.createLabWork(labWork));
    }

    @PUT
    @Path("/{id}")
    public Response changeLabWork(@PathParam("id") String id, String labWork){
        return unwrap(service.updateLabWork(id, labWork));
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLabWork(@PathParam("id") String id){
        return unwrap(service.deleteLabWork(id));
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }
}
