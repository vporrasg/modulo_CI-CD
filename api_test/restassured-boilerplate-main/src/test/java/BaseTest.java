import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {

    @Parameters("baseUrl")
    @BeforeClass
    public void setup(@Optional("http://localhost:9000") String baseUrl ) {

        RestAssured.baseURI = baseUrl;
    }

}
