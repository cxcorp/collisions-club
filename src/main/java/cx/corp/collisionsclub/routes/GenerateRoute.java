package cx.corp.collisionsclub.routes;

import cx.corp.collisionsclub.ApiResponse;
import cx.corp.collisionsclub.CollisionGenerator;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateRoute implements Route {
    public static String getRoute() {
        return "/generate";
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        Integer count = safeParseInt(req.queryParams("count"));
        Integer stringLength = safeParseInt(req.queryParams("string_length"));

        if (count == null || stringLength == null) {
            res.status(HttpStatus.BAD_REQUEST_400);
            return ApiResponse.error("Missing parameter count or string_length.");
        }

        if (stringLength != 4) {
            res.status(HttpStatus.BAD_REQUEST_400);
            return ApiResponse.error("Parameter string_length must be 4!");
        }

        if (count < 1 || count > 1024) {
            res.status(HttpStatus.BAD_REQUEST_400);
            return ApiResponse.error("Parameter count must be over 1 and under 1024!");
        }

        Random random = ThreadLocalRandom.current();
        if (req.queryParams("seed") != null) {
            Long seed = safeParseLong(req.queryParams("seed"));
            if (seed == null) {
                res.status(HttpStatus.BAD_REQUEST_400);
                return ApiResponse.error("Parameter seed could not be parsed!");
            }
            random = new Random(seed);
        }
        String[] collidingStrings = generateStrings(random, count);
        return ApiResponse.ok(collidingStrings);
    }

    private String[] generateStrings(Random random, int count) {
        CollisionGenerator gen = new CollisionGenerator(random);
        String[] strings = new String[count];
        for (int i = 0; i < count; i++) {
            strings[i] = gen.generate();
        }
        return strings;
    }

    private static Integer safeParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static Long safeParseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
