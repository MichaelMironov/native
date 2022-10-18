package api.in.reqres;

import api.in.reqres.pojo.Potato;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static api.Specification.installSpecification;
import static api.Specification.requestSpec;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static utils.configurations.Configuration.getConfigurationValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tags({@Tag("@api"),@Tag("@native")})
@DisplayName("Reqres апи тест")
public class ReqresTest {

    private static String ID;
    private static Potato potato;

    @BeforeAll
    public static void prepare() {

        installSpecification(requestSpec(getConfigurationValue("reqresUrl")));

    }

    @Order(1)
    @Test
    @Epic(value = "Reqres")
    @Description(value = "Отправка POST запроса с JSON телом")
    @DisplayName("Создание персонажа")
    void creatingTest() throws IOException {

        ObjectNode json = new ObjectMapper().readValue(new File("src/test/resources/json/potato.json"), ObjectNode.class);

       step("Отправка POST запроса с JSON телом",()-> potato =
                given()
                        .body(json)
                        .basePath("/users")
                        .when().post()
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().body().as(Potato.class));

        ID = potato.getId();

    }

    @Order(2)
    @Test
    @Epic(value = "Reqres")
    @Description(value = "Отправка PUT запроса по ID созданного персонажа")
    @DisplayName("Изменение созданного персонажа")
    void changingTest() {

        potato.setName("Tomato");
        potato.setJob("Eat Maket");

        step("Отправка PUT запроса по ID созданного персонажа",()->given()
                .body(potato)
                .basePath("/users/" + ID)
                .when().put()
                .then().statusCode(200)
                .body("name", equalTo("Tomato"))
                .body("job", equalTo("Eat Maket"))
                .body("id", equalTo(ID)).log().body());

    }

}
