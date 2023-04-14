import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RestAssuredTest {

    public static void main(String[] args) {
        Response response = RestAssured.get("https://rickandmortyapi.com/api/location");
        JsonPath jsonPath = new JsonPath(response.asString());
        jsonPath.getList("results.dimension").forEach(System.out::println);
    }

}
