package ru.extremism.extrmismserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.extremism.extrmismserver.controler.PageController;
import ru.extremism.extrmismserver.model.AnalyzingText;
import ru.extremism.extrmismserver.repository.text.TextsAnalysisRepository;
import ru.extremism.extrmismserver.service.TextAnalyzerService;

@SpringBootTest
public class RabbitMQMessageTest {


    @Autowired
    private TextAnalyzerService service;
    @Autowired
    private PageController controller;
    @Test
    public void sendMsg() throws InterruptedException {
        AnalyzingText analyzingText = new AnalyzingText();
        analyzingText.setText("Серега гей аниме на аве, годжу пидр, систему накатывать впадлу, силвер медленный");
        analyzingText.setLabel("?");
        controller.analyzeText(analyzingText);
    }
}
