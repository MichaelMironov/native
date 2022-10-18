package web.jira;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;
import utils.configurations.WebConfiguration;
import web.jira.models.Task;

import java.util.concurrent.TimeUnit;

import static utils.configurations.Configuration.setEnvironmentProperties;
import static web.jira.elements.NavigationPanel.*;
import static web.jira.pages.AuthorizationPage.*;
import static web.jira.pages.BoardsPage.*;
import static web.jira.pages.TaskPage.createNewTask;
import static web.jira.pages.TaskPage.idCreatedTaskShouldBe;
import static web.jira.pages.TasksPage.*;

@DisplayName("Jira UI тест")
@Tags({@Tag("@web"),@Tag("@native")})
public class JiraUiTest {

    @BeforeAll
    public static void setup(){

        WebConfiguration cfg = ConfigFactory.create(WebConfiguration.class, System.getProperties());

        switch (cfg.webDriverBrowserName()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                break;
            default: {
                throw new IllegalArgumentException(String.format("Браузер: %s не поддерживается", cfg.webDriverBrowserName()));
            }
        }

        Configuration.browser = cfg.webDriverBrowserName();
        Configuration.browserSize = cfg.webDriverBrowserSize();
        Configuration.savePageSource = true;
        Configuration.screenshots = true;
        Configuration.webdriverLogsEnabled = false;
        Configuration.timeout = TimeUnit.SECONDS.toMillis(cfg.webDriverTimeoutSeconds());

    }

    @BeforeEach
    public void login(){
        openAuthorizationPage();
        logInAs(USER);
    }

    @AfterEach
    public void logout(){
        logOut();
    }

    @Test
    @Epic(value = "Jira")
    @DisplayName("Расчет общего количества задач в проекте")
    void TestCountTasks(){
        selectMenuSubsectionByText(TASKS,"Поиск задач");
        totalCountTasks();
    }

    @Test
    @Epic(value = "Jira")
    @DisplayName("Проверка статуса задачи")
    void TestStatusTask(){
        selectMenuSubsectionByText(TASKS,"Поиск задач");
        filterTasksByText("TestSelenium_bug");
        openTaskWithId(21967);
        statusShouldBe("Сделать");
        checkFixIn("2.0");
    }

    @Test
    @Epic(value = "Jira")
    @DisplayName("Проверка создания новой задачи")
    void TestCreationOfNewTask(){
        selectMenuSubsectionByText(TASKS,"Поиск задач");
        clickToCreateTask();
        Task newTask = createNewTask("test selenide", "Ошибка", "12345678asdasdasdasdasdasdasd");
        selectMenuSubsectionByText(TASKS, "Поиск задач");
        filterTasksByText(newTask.getTitle());
        openTaskWithId(newTask.getId());
        idCreatedTaskShouldBe(newTask.getId());
    }

    @Test
    @Epic(value = "Jira")
    @Description("Перевод созданной задачи по колонкам на скрам доске")
    @DisplayName("Проверка изменения статуса задачи")
    void TestClosingTask(){
        selectMenuSubsectionByText(TASKS,"Поиск задач");
        clickToCreateTask();
        Task newTask = createNewTask("test selenide", "Ошибка", "12345678asdasdasdasdasdasdasd");
        selectMenuSubsectionByText(BOARD, "Доска TEST");
        addTaskToSprint(newTask.getId(), "test selenide");
        toSprintBoard();
        moveTaskByIdTo(newTask.getId(), IN_WORK);
        moveTaskByIdTo(newTask.getId(), DONE);
        statusTaskByIdShouldBe("Готово", newTask.getId());
    }

}