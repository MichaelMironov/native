package api;

import api.loggers.SimpleFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specification {
    public static RequestSpecification requestSpec(String url){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build().filters(new SimpleFilter(),new AllureRestAssured());
    }

    public static RequestSpecification requestSpecWithSession(String url, String sessionId){

        return new RequestSpecBuilder()
                .setBaseUri(url)
                .setSessionId(sessionId)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .build().filter(new SimpleFilter());

    }

    public static void installSpecification(RequestSpecification request){
        RestAssured.requestSpecification = request;
    }

}
