package ru.extremism.extrmismserver.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.extremism.extrmismserver.listener.TextAnalysisListener;
import ru.extremism.extrmismserver.listener.TextsAnalysisObserver;
import ru.extremism.extrmismserver.listener.TextsAnalysisSubscriber;
import ru.extremism.extrmismserver.model.AnalyzedText;
import ru.extremism.extrmismserver.model.AnalyzingText;
import ru.extremism.extrmismserver.parser.JsonTextParser;
import ru.extremism.extrmismserver.service.TextAnalyzerService;

import java.util.concurrent.CountDownLatch;

@Controller
public class PageController {
    @Autowired
    private TextsAnalysisObserver observer;
    @Autowired
    private TextAnalyzerService service;
    @Autowired
    private TextsAnalysisSubscriber subscriber;

    @Autowired
    private JsonTextParser parser;
    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

    @PostMapping("/analyze")
    @ResponseBody   
    public AnalyzedText analyzeText(@RequestBody AnalyzingText analyzingText) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        subscriber.setLatch(latch);
        service.send(analyzingText);
        latch.await();
        observer.unsubscribe(subscriber);
        return parser.parseFromJson(subscriber.getResult());
    }
}