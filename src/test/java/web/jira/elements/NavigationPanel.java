package web.jira.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import web.jira.pages.TaskPage;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static io.qameta.allure.Allure.step;
import static utils.configurations.Configuration.getConfigurationValue;

public class NavigationPanel {
    public static final SelenideElement PROJECTS = $x("//a[@id='browse_link']");

    public static final SelenideElement TASKS = $x("//*[@href='/issues/']");

    public static final SelenideElement BOARD = $x("//a[@id='greenhopper_menu']");

    private static final SelenideElement createTaskButton = $x("//a[@id=\"create_link\"]");

    private static final Logger LOGGER = LogManager.getLogger(NavigationPanel.class);

    public static void selectMenuSubsectionByText(SelenideElement menu, String text){
        step("Открыть подраздел: " + text, () -> {
            menu.click();
            menu.shouldHave(Condition.attribute("aria-expanded", "true"));
            $(By.linkText(text)).shouldBe(Condition.visible).click();
        });
        LOGGER.info("Переход в подраздел - " + text);
    }

    public static void clickToCreateTask(){
        step("Нажать кнопку [создать задачу]",()->{
            createTaskButton.click();
            TaskPage.sectionTaskCreation.shouldBe(Condition.appear);
        });
    }
}
