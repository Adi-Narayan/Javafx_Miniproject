package oop;

import java.util.ArrayList;
import java.util.List;

public class Cattle extends User {
    protected String breed;
    protected String age;
    protected String weight;
    protected String UID;
    protected boolean health;
    private static List<Cattle> cattleList = new ArrayList<>();

    public Cattle(String username, String password, String email,
                  String breed, String age, String weight, String UID, boolean health) {
        super(username, password, email);
        this.breed = breed;
        this.age = age;
        this.weight = weight;
        this.UID = UID;
        this.health = health;
    }

    public Cattle(String username, String password, String email) {
        super(username, password, email);
    }

    public String getBreed() {
        return breed;
    }

    public String getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }

    public String getUID() {
        return UID;
    }

    public boolean getHealth() {
        return health;
    }

    public void setUID(String uid) {
        this.UID = uid;
    }

    public static void addCattle(Cattle cattle) {
        cattleList.add(cattle);
    }

    public static List<Cattle> getCattleForUser(String username) {
        List<Cattle> userCattle = new ArrayList<>();
        for (Cattle cattle : cattleList) {
            if (cattle.getUsername().equals(username)) {
                userCattle.add(cattle);
            }
        }
        return userCattle;
    }
}
