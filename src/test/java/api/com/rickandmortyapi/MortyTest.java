package api.com.rickandmortyapi;

import api.com.rickandmortyapi.pojo.characters.Person;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static api.Specification.installSpecification;
import static api.Specification.requestSpec;
import static api.com.rickandmortyapi.Steps.comparingCharacters;
import static utils.configurations.Configuration.getConfigurationValue;

@Tags({@Tag("@api"), @Tag("@native")})
@DisplayName("Рик и Морти апи тест")
public class MortyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MortyTest.class);

    @BeforeAll
    public static void prepare() {
        installSpecification(requestSpec(getConfigurationValue("mortyUrl")));
    }

    @Test
    @Epic(value = "Рик и Морти")
    @Severity(value = SeverityLevel.NORMAL)
    @Description(value = "Сравнение персонажей мультсерила по расе и локации")
    @DisplayName("Сравнение персонажей")
    void characterComparingTest() {

        Person morty = Steps.getCharacterByName("Morty Smith");

        LOGGER.info(morty.toString());

        String lastEpisodeMorty = morty.getEpisode().stream().reduce((first, second) -> second).get();

        LOGGER.info("Последний эпизод Морти: {}", lastEpisodeMorty);

        List<String> characters = Steps.getCharactersOfEpisode(Steps.getId(lastEpisodeMorty));

        LOGGER.info("Персонажи последнего эпизода Морти: {}", characters);

        int idLastEpisodeCharacter = Steps.getId(characters.get(characters.size() - 1));

        Person jerry = Steps.getCharacterById(idLastEpisodeCharacter);

        LOGGER.info("Последний персонаж. {}", jerry.toString());

        LOGGER.info("Раса {}: {}. Раса {}: {}.", morty.getName(), morty.getSpecies(), jerry.getName(), jerry.getSpecies());

        LOGGER.info("Местонахождение {}: {}. Местонахождение {}: {}.", morty.getName(), morty.getLocation(), jerry.getName(), jerry.getLocation());

        comparingCharacters(morty,jerry);

    }

}
