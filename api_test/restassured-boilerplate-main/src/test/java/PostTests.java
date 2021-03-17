import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Post;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;
import static io.restassured.RestAssured.given;

public class PostTests extends BaseTest {
    private static String resourcePathExam = "/v1/post";
    private static Integer createdPost = 0;

    @BeforeClass
    public void createPost(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePathExam);
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdPost = jsonPathEvaluator.get("id");
    }

    @Test
    public void Create_Post_Success(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePathExam)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Create_Post_Failed(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateInvalidContent());
        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .post(resourcePathExam)
        .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Create_Post_Security(){
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .post(resourcePathExam)
        .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Get_Post_Success(){
        String test = resourcePathExam + "/" + createdPost.toString();
        given()
                .spec(RequestSpecs.generateToken())
                .get(test)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Get_Post_Failed(){
        String test = resourcePathExam + "/-41";
        RequestSpecification requestSpecification = RequestSpecs.generateToken();
        given()
                .spec(requestSpecification)
                .get(test)
        .then()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());

        Response response= given().spec(requestSpecification).get(test);

        String errorMessage ;
        JsonPath jsonPathEvaluator = response.jsonPath();
        errorMessage = jsonPathEvaluator.get("Message");
        Assert.assertEquals(errorMessage,"Post not found");
    }

    @Test
    public void Get_Post_Security(){
        String test = resourcePathExam + "/" + createdPost.toString();
        RequestSpecification requestSpecification = RequestSpecs.generateFakeToken();
        given()
                .spec(requestSpecification)
                .get(test)
        .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

        Response response= given().spec(requestSpecification).get(test);

        String errorMessage ;
        JsonPath jsonPathEvaluator = response.jsonPath();
        errorMessage = jsonPathEvaluator.get("message");
        Assert.assertEquals(errorMessage,"Please login first");
    }

    @Test
    public void Update_Post_Success(){
        String test = resourcePathExam + "/" + createdPost.toString();
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .put(test)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Update_Post_Failed(){
        String test = resourcePathExam + "/-41";
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.generateToken())
                .body(testPost)
                .put(test)
        .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Update_Post_Security(){
        String test = resourcePathExam + "/" + createdPost.toString();
        Post testPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        given()
                .spec(RequestSpecs.generateFakeToken())
                .body(testPost)
                .put(test)
        .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void GetAll_Posts_Success(){
        given()
                .spec(RequestSpecs.generateToken())
                .get("/v1/posts")
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void GetAll_Posts_Security(){
        RequestSpecification requestSpecification = RequestSpecs.generateFakeToken();
        given()
                .spec(requestSpecification)
                .get("/v1/posts")
        .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

        Response response= given().spec(requestSpecification).get("/v1/posts");

        String errorMessage ;
        JsonPath jsonPathEvaluator = response.jsonPath();
        errorMessage = jsonPathEvaluator.get("message");
        Assert.assertEquals(errorMessage,"Please login first");
    }

    @Test(priority = 2)
    public void Delete_Post_Success(){
        given()
                .spec(RequestSpecs.generateToken())
                .delete(resourcePathExam + "/" + createdPost.toString())
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Delete_Post_Failed(){
        String test = resourcePathExam + "/12115";
        RequestSpecification requestSpecification = RequestSpecs.generateToken();
        given()
                .spec(requestSpecification)
                .delete(test)
        .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());

        Response response= given().spec(requestSpecification).delete(test);

        String errorMessage ;
        JsonPath jsonPathEvaluator = response.jsonPath();
        errorMessage = jsonPathEvaluator.get("message");
        Assert.assertEquals(errorMessage,"Post could not be deleted");
    }

    @Test
    public void Delete_Post_Security(){
        String test = resourcePathExam + "/12115";
        RequestSpecification requestSpecification = RequestSpecs.generateFakeToken();
        given()
                .spec(requestSpecification)
                .delete(test)
        .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());

        Response response= given().spec(requestSpecification).delete(test);

        String errorMessage ;
        JsonPath jsonPathEvaluator = response.jsonPath();
        errorMessage = jsonPathEvaluator.get("message");
        Assert.assertEquals(errorMessage,"Please login first");
    }
}
