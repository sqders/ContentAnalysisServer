package ru.extremism.extrmismserver.listener;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.concurrent.CountDownLatch;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS )
public class TextsAnalysisSubscriber implements Subscriber {
    @Getter
    @Setter
    private CountDownLatch latch;

    @Getter
    @Setter
    private String result;

    private TextsAnalysisObserver tableObserver;

    private static final Logger log = LoggerFactory.getLogger(TextsAnalysisSubscriber.class);

    public TextsAnalysisSubscriber(TextsAnalysisObserver tableObserver){
        this.tableObserver = tableObserver;
        tableObserver.subscribe(this);
    }

    @Override
    public void update(String message) {
        latch.countDown();
        result = message;
    }
}
