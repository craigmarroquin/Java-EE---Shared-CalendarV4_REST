package com.Calendar;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by craig on 4/27/16.
 */
@Path("/users")
public class UserResource {
    UserService userService = new UserService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers(@Context UriInfo uriInfo){
        List<User> user = new ArrayList<User>();
        user = userService.getAllUsers();
        for(User u: user){
            String uri = getURI(uriInfo, u.getUsername());
            u.addLink(uri, "self");
        }
        return user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User addUser(User user){
        User addUser = userService.addUser(user);
        if (addUser.getUserID() == -1) throw new WebApplicationException(Response.Status.CONFLICT); // user duplication
        return addUser;
    }

    @Path("/{username}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("username") String username, @Context UriInfo uriInfo){
        User user = null;
        user = userService.getUser(username);

        String uri = getURI(uriInfo, username);
        user.addLink(uri, "self");

        return user;
    }

    public String getURI(UriInfo uriInfo, String id){
        String uri = uriInfo.getBaseUriBuilder()
                .path(UserResource.class)
                .path(id)
                .build()
                .toString();
        return uri;
    }

}
