package test_models;

import ru.clevertec.annotation.JsonField;

public class Owner {
    @JsonField("full_name")
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
