package cx.corp.collisionsclub.routes;

import com.google.gson.annotations.SerializedName;
import cx.corp.collisionsclub.ApiResponse;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

public class HashesRoute implements Route {

    private static final List<HashInfo> hashInfo = new ArrayList<>();
    static {
        hashInfo.add(new HashInfo(4, 62978174));
    }

    public static String getRoute() {
        return "/hashes";
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return ApiResponse.ok(hashInfo);
    }

    private static class HashInfo {
        @SerializedName("string_length")
        private final int stringLength;

        @SerializedName("hash")
        private final int hash;

        private HashInfo(int length, int hash) {
            this.stringLength = length;
            this.hash = hash;
        }
    }
}
