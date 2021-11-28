package ru.itmo.controller;

import ru.itmo.service.RemoteBeanLookup;
import ru.itmo.service.SecondServiceI;
import ru.itmo.utils.ResponseWrapper;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/labworks")
public class LabWorkController {

    private SecondServiceI service;

    public LabWorkController(){
        service = RemoteBeanLookup.lookupRemoteStatelessBean();
    }

    private Response unwrap(ResponseWrapper responseWrapper){
        return Response.status(responseWrapper.getCode()).entity(responseWrapper.getPayload()).build();
    }

    @PUT
    @Path("/{labwork-id}/difficulty/increase/{steps-count}")
    public Response increaseLabWorkDifficulty(
            @PathParam("labwork-id") String labWorkId,
            @PathParam("steps-count") String steps){
        return unwrap(service.increaseLabWorkDifficulty(labWorkId, steps));
    }
}
