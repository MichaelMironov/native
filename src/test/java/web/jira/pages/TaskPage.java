package web.jira.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Keys;
import web.jira.models.Task;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.switchTo;
import static io.qameta.allure.Allure.step;

public class TaskPage {

    private static final Logger LOGGER = LogManager.getLogger(TaskPage.class);

    private static final SelenideElement issueTypeField = $x("//input[@id='issuetype-field']");

    private static final SelenideElement descriptionField = $x("//body[@id='tinymce']//p");

    private static final SelenideElement taskTitle = $x("//input[@name='summary']");

    private static final SelenideElement messageSuccessCreation = $x("//a[@class=\"issue-created-key issue-link\"]");

    public static final SelenideElement sectionTaskCreation = $x("//section[@id=\"create-issue-dialog\"]");

    private static final SelenideElement submitButton = $x("//input[@id='create-issue-submit']");

    public static int taskId;

    public static void selectType(String type) {

        step("Выбрать тип: " + type, ()->{
            issueTypeField.click();
            issueTypeField.sendKeys(Keys.BACK_SPACE);
            issueTypeField.setValue(type);
            issueTypeField.pressEnter().shouldBe(Condition.focused);
        });

    }

    public static void setTitle(String title) {
        step("Ввести название: " + title,()->taskTitle.setValue(title));
    }

    public static void setDescription(String description) {
        step("Ввести описание: " + description , ()->{
            switchTo().frame(0);
            descriptionField.setValue(description);
            switchTo().defaultContent();});
    }

    public static void submitTask() {
        step("Создать задачу", ()->{
            submitButton.click();
            String[] nameOfCreatedTask = messageSuccessCreation.shouldBe(Condition.visible).innerText().split("-");
            taskId = Integer.parseInt(nameOfCreatedTask[1].trim());});

        LOGGER.info("Id созданной задачи: " + taskId);
    }

    public static void idCreatedTaskShouldBe(Integer id) {
        Assertions.assertEquals(id,taskId);
    }

    public static Task createNewTask(String title, String type, String description) {
        setTitle(title);
        selectType(type);
        setDescription(description);
        submitTask();
        return new Task(title, type, description, taskId);
    }

}
