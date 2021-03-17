import helpers.DataHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;

public class CommentTests extends BaseTest {
    private static String resourcePathExam = "/v1/comment/";
    private static String postID = "1162";
    private static Integer createdCommentID = 0;

    @BeforeClass
    public void createComment(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        Response response = given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .post(resourcePathExam + postID);
        JsonPath jsonPathEvaluator = response.jsonPath();
        createdCommentID = jsonPathEvaluator.get("id");
    }

    @Test
    public void Create_Comment_Success(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        System.out.println("Created comment============>" + testPost.getName()+","+testPost.getComment());

        given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .post(resourcePathExam + postID)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Create_Comment_Failed(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .post(resourcePathExam+"-42")
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Create_Comment_Security(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuthWrong())
                .body(testPost)
                .post(resourcePathExam+ postID)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void GetAll_Comments_Success(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .get("/v1/comments/"+ postID)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void GetAll_Comments_Security(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.basicAuthWrong())
                .body(testPost)
                .get("/v1/comments/"+ postID)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Get_Comment_Success(){
        given()
                .spec(RequestSpecs.basicAuth())
                .get("/v1/comment/"+ postID+"/"+createdCommentID)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Get_Comment_Failed(){
        given()
                .spec(RequestSpecs.basicAuth())
                .get("/v1/comment/"+ postID+"/-42")
                .then()
                .statusCode(404)
                .spec(ResponseSpecs.defaultSpec());
        //AD COMMENT NOT FOUND
    }

    @Test
    public void Get_Comment_Security(){
        given()
                .spec(RequestSpecs.basicAuthWrong())
                .get("/v1/comment/"+ postID+"/"+createdCommentID)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Update_Comment_Success(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        System.out.println("Created comment============>" + testPost.getName()+","+testPost.getComment());

        given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .put(resourcePathExam + postID + "/" + createdCommentID)
        .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Update_Comment_Failed(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        System.out.println("Created comment============>" + testPost.getName()+","+testPost.getComment());

        given()
                .spec(RequestSpecs.basicAuth())
                .body(testPost)
                .put(resourcePathExam + postID + "/-42")
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Update_Comment_Security(){
        Comment testPost = new Comment(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());
        System.out.println("Created comment============>" + testPost.getName()+","+testPost.getComment());

        given()
                .spec(RequestSpecs.basicAuthWrong())
                .body(testPost)
                .put(resourcePathExam + postID + "/" + createdCommentID)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test(priority = 2)
    public void Delete_Comment_Success(){
        given()
                .spec(RequestSpecs.basicAuth())
                .delete(resourcePathExam + postID + "/" + createdCommentID)
                .then()
                .statusCode(200)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Delete_Comment_Failed(){
        given()
                .spec(RequestSpecs.basicAuth())
                .delete(resourcePathExam + postID + "/-42")
                .then()
                .statusCode(406)
                .spec(ResponseSpecs.defaultSpec());
    }

    @Test
    public void Delete_Comment_Security(){
        given()
                .spec(RequestSpecs.basicAuthWrong())
                .delete(resourcePathExam + postID + "/" + createdCommentID)
                .then()
                .statusCode(401)
                .spec(ResponseSpecs.defaultSpec());
    }




}
