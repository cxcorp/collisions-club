package cx.corp.collisionsclub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import cx.corp.collisionsclub.routes.GenerateRoute;
import cx.corp.collisionsclub.routes.HashesRoute;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

public class BillionClub {

    private static Gson gson;

    public static void main(String... args) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        port(1234);

        after(BillionClub::setContentHeaders);
        notFound(BillionClub::handle404);

        exception(JsonParseException.class, BillionClub::catchJsonParseException);
        exception(Exception.class, BillionClub::catchUncaughtException);

        get(HashesRoute.getRoute(), new HashesRoute(), gson::toJson);
        get(GenerateRoute.getRoute(), new GenerateRoute(), gson::toJson);
    }

    private static void setContentHeaders(Request req, Response res) {
        res.header("Content-Encoding", "gzip");
        res.header("Content-Type", "application/json; charset=utf-8");
    }

    private static Object handle404(Request req, Response res) {
        res.status(HttpStatus.NOT_FOUND_404);
        return gson.toJson(ApiResponse.error("Path \"" + req.pathInfo() + "\" does not exist!"));
    }

    private static void catchJsonParseException(Exception ex, Request req, Response res) {
        res.status(HttpStatus.UNPROCESSABLE_ENTITY_422);
        res.body(gson.toJson(ApiResponse.error("Malformed request.")));
    }

    private static void catchUncaughtException(Exception ex, Request req, Response res) {
        res.status(HttpStatus.SERVICE_UNAVAILABLE_503);
        res.body(gson.toJson(ApiResponse.error("Service unavailable. Please try again later.")));
    }
}