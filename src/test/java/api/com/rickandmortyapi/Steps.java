package api.com.rickandmortyapi;

import api.com.rickandmortyapi.pojo.characters.Person;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.qameta.allure.Step;
import static io.qameta.allure.Allure.step;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class Steps {

    @Step("Найти персонажа по имени {name}")
    public static Person getCharacterByName(String name){
        return  given()
                .basePath("/character")
                .queryParam("name",name)
                .when().get()
                .then().statusCode(200)

                .extract().body().jsonPath().getObject("results[0]", Person.class);
    }

    public static List<String> getCharacterEpisodes(ObjectNode character) {

        return  given()
                .basePath("/character")
                .body(character)
                .when().get()
                .then().statusCode(200)
                .extract()
                .jsonPath().getList("results[0].episode");

    }

    @Step("Получить персонажей эпизода с id: {episodeId}")
    public static List<String> getCharactersOfEpisode(Integer episodeId){

        return  given()
                .basePath("/episode/"+episodeId)
                .when().get()
                .then().statusCode(200)
                .log().body()
                .extract()
                .jsonPath().getList("characters");
    }

    public static int getId(String title){
        return Integer.parseInt(Arrays.stream(title.split("/"))
                .reduce((first, second) -> second).get());
    }

    @Step("Найти персонажа по id: {id}")
    public static Person getCharacterById(int id){

        return  given()
                .basePath("/character/"+id)
                .when().get()
                .then().statusCode(200)
                .extract()
                .body().as(Person.class);
    }

    @Step("Сравнить персонажей")
    public static void comparingCharacters(Person first, Person second){
        step("Сравнение персонажей " + first.getName() + " и " + second .getName()+ ".\nПо расе: " +
                        first.getSpecies() + " и " + second.getSpecies(),
                () -> assertEquals(first.getSpecies(), second.getSpecies()));
        step("Сравнение персонажей " + first.getName() + " и " + second.getName() + ".\nПо местонахождению: " +
                        first.getLocation() + " и " + second.getLocation(),
                () -> assertNotEquals(first.getLocation(), second.getLocation()));
    }
}
