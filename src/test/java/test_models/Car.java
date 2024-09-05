package test_models;

import java.util.List;

public class Car {
    private String model;
    private int year;
    private Engine engine;
    private List<Owner> owners;

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public Engine getEngine() {
        return engine;
    }

    public List<Owner> getOwners() {
        return owners;
    }
}
