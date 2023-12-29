package ru.extremism.extrmismserver.listener;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class TextsAnalysisObserver implements TableObserver{

    private final ArrayList<Subscriber> mySubscribers = new ArrayList<>();

    @Override
    public void subscribe(Subscriber subscriber) {
        mySubscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        mySubscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers(String message) {
        mySubscribers.forEach(subscriber -> subscriber.update(message));
    }
}
