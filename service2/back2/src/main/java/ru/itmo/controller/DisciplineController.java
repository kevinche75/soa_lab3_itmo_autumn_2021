package ru.itmo.controller;

import ru.itmo.service.RemoteBeanLookup;
import ru.itmo.service.SecondServiceI;
import ru.itmo.utils.ResponseWrapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/disciplines")
public class DisciplineController {

    private SecondServiceI service;

    public DisciplineController(){
        service = RemoteBeanLookup.lookupRemoteStatelessBean();
    }

    private Response unwrap(ResponseWrapper responseWrapper){
        return Response.status(responseWrapper.getCode()).entity(responseWrapper.getPayload()).build();
    }

    @GET
    public Response getDisciplines(){
        return unwrap(service.getDisciplines());
    }

    @GET
    @Path("/{id}")
    public Response getDiscipline(@PathParam("id")String disciplineId){
        return unwrap(service.getDiscipline(disciplineId));
    }

    @GET
    @Path("/{id}/labworks")
    public Response getDisciplineLabWorks(@PathParam("id") String disciplineId){
        return unwrap(service.getDisciplineLabWorks(disciplineId));
    }

    @POST
    public Response createDiscipline(String strDiscipline){
        return unwrap(service.createDiscipline(strDiscipline));
    }

    @DELETE
    @Path("/{discipline-id}/labwork/{labwork-id}/remove")
    public Response deleteLabWorkFromDiscipline(
            @PathParam("discipline-id") String disciplineId,
            @PathParam("labwork-id") String labWorkId){
        return unwrap(service.removeLabWorkFromDiscipline(disciplineId, labWorkId));
    }

    @POST
    @Path("/{id}/{labwork-id}")
    public Response addLabWorkToDiscipline(@PathParam("id") String disciplineId, @PathParam("labwork-id") String labWorkId){
        return unwrap(service.addLabWorkToDiscipline(disciplineId, labWorkId));
    }

    @OPTIONS
    @Path("{path : .*}")
    public Response options() {
        return Response.ok()
                .build();
    }

//    @SneakyThrows
//    @GET
//    @Path(("/test"))
//    public Response test(){
//        return Response.ok(ServiceDiscovery.getUriFromConsul()).build();
//    }
}
