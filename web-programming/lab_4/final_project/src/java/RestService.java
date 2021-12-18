import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@ApplicationPath("/rest")
@Path("/")
public class RestService extends Application {
    @EJB(name="Database")
    private Database database;

    private MultivaluedMap<String, String> checkHeaders(HttpHeaders httpHeaders) {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        if (!headers.containsKey("login") || !headers.containsKey("password"))
            throw new BadRequestException();
        return headers;
    }

    private String checkUser(HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = checkHeaders(httpHeaders);
        if (!database.authorization(headers.getFirst("login"), headers.getFirst("password")))
            throw new NotAuthorizedException(401);
        return headers.getFirst("login");
    }

    @GET
    @Path("registration")
    public boolean registration(@Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = checkHeaders(httpHeaders);
        return database.registration(headers.getFirst("login"), headers.getFirst("password"));
    }

    @GET
    @Path("authorization")
    public boolean authorization(@Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = checkHeaders(httpHeaders);
        return  database.authorization(headers.getFirst("login"), headers.getFirst("password"));
    }

    @POST
    @Path("addPoint")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Point post(
            @Context HttpHeaders httpHeaders,
            @DefaultValue("") @FormParam("x") Float x,
            @DefaultValue("") @FormParam("y") Float y,
            @DefaultValue("") @FormParam("r") Float r
    ) throws Exception {
        String login = checkUser(httpHeaders);
        Point point = new Point(x, y, r);
        if (point.valid()) {
            database.addPoint(login, point);
            return point;
        }
        throw new BadRequestException();
    }

    @GET
    @Path("getPoints")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Point> getPoints(@Context HttpHeaders httpHeaders) throws Exception {
        String login = checkUser(httpHeaders);
        return database.getPoints(login);
    }

    @DELETE
    @Path("clearPoints")
    public void clearPoints(@Context HttpHeaders httpHeaders) throws Exception {
        String login = checkUser(httpHeaders);
        database.clearPoints(login);
    }
}
