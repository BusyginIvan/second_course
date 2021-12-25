package rest.resources;

import database.repositories.PointsRepository;
import database.entities.Point;
import database.exceptions.UserNotFoundException;
import rest.filters.authorization.Authorized;
import rest.json.PointData;

import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/points")
@Authorized
@Produces(MediaType.APPLICATION_JSON)
public class PointsResource {
    @EJB
    private PointsRepository points;

    private String getUsernameFromHeaders(HttpHeaders headers) {
        return headers.getHeaderString("username");
    }

    @GET
    public Response getPoints(@Context HttpHeaders headers) {
        String username = getUsernameFromHeaders(headers);
        List<Point> pointsList = points.getAll(username);
        return Response.ok(toJSONString(pointsList)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPoint(@Context HttpHeaders headers, @Valid PointData pointData) {
        String username = getUsernameFromHeaders(headers);
        Point point = new Point(pointData.getX(), pointData.getY(), pointData.getR());
        try {
            points.add(username, point);
            return Response.ok(toJSONObject(point).toString()).build();
        } catch (UserNotFoundException e) {
            return Response.serverError()
                    .entity("Пользователь не обнаружен в БД, хоть и обладает действительным токеном.")
                    .build();
        }
    }

    @DELETE
    public Response clearPoints(@Context HttpHeaders headers) {
        String username = getUsernameFromHeaders(headers);
        points.clear(username);
        return Response.ok(toJSONString("Данные о точках пользователя " + username + " очищены.")).build();
    }

    private String toJSONString(List<Point> points) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Point point : points)
            arrayBuilder.add(toJSONObject(point));
        return arrayBuilder.build().toString();
    }

    private JsonObject toJSONObject(Point point) {
        return Json.createObjectBuilder()
                .add("x", point.getX())
                .add("y", point.getY())
                .add("r", point.getR())
                .add("result", point.isResult())
                .build();
    }

    private String toJSONString(String message) {
        return Json.createObjectBuilder().add("message", message).build().toString();
    }
}
