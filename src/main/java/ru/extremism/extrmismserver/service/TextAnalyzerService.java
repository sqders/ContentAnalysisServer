package ru.extremism.extrmismserver.service;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.extremism.extrmismserver.model.AnalyzingText;
import ru.extremism.extrmismserver.parser.JsonTextParser;

@Service
public class TextAnalyzerService {

    @Autowired
    private JsonTextParser parser;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled
    public void send(AnalyzingText analyzingText) {
        // Создаем JSON объект с текстом для анализа
        String payload = parser.parseToJson(analyzingText);
        rabbitTemplate.convertAndSend("texts_analysis", payload);
    }
}
