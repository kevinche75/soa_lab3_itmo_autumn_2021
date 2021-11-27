package ru.itmo.controller;

import ru.itmo.service.LabWorkI;
import ru.itmo.service.RemoteBeanLookup;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

    @GET
    public Response getLabWorks(@Context UriInfo ui) {
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        return service.getAllLabWorks(map);
    }

    @GET
    @Path("/{id}")
    public Response getLabWork(@PathParam("id") String id){
        return service.getLabWork(id);
    }

    @GET
    @Path(LESS_MAXIMUM_POINT_FLAG)
    public Response getLessMaximumPointFlag(@Context UriInfo ui, @PathParam("maximum_point") String maximum_point){
        MultivaluedMap<String, String> map = ui.getQueryParameters();
        return service.getLessMaximumPoint(map, maximum_point);
    }

    @GET
    @Path(MINIMAL_NAME_FLAG)
    public Response getMinimalNameLabWork(){
        return service.getMinName();
    }

    @GET
    @Path("/test")
    public Response test(){
        return service.test();
    }

    @GET
    @Path("/test_2")
    public String t(){
        System.out.println("test");
        return "t";
    }

    @GET
    @Path(COUNT_PERSONAL_QUALITIES_MAXIMUM_FLAG)
    public Response countPersonalQualitiesMaximumLabWorks(@PathParam("personal_qualities_maximum") String personal_qualities_maximum){
        return service.countPersonalQualitiesMaximum(personal_qualities_maximum);
    }

    @POST
    public Response createLabWork(String labWork){
        return service.createLabWork(labWork);
    }

    @PUT
    @Path("/{id}")
    public Response changeLabWork(@PathParam("id") String id, String labWork){
        return service.updateLabWork(id, labWork);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteLabWork(@PathParam("id") String id){
        return service.deleteLabWork(id);
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }
}
