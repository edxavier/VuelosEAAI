package com.edxavier.vueloseaai.lib;

/**
 * Created by Eder Xavier Rojas on 26/06/2016.
 */
public interface EventBusIface {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);
}
