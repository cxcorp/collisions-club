package cx.corp.collisionsclub;

public class ApiResponse {
    private final Object data;
    private final String error;

    private ApiResponse(Object data, String error) {
        this.data = data;
        this.error = error;
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(data, null);
    }

    public static ApiResponse error(String errorMessage) {
        return new ApiResponse(null, errorMessage);
    }
}