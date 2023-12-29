package ru.extremism.extrmismserver.listener;

public interface TableObserver {

    void subscribe(Subscriber subscriber);

    void unsubscribe(Subscriber subscriber);

    void notifySubscribers(String message);
}
