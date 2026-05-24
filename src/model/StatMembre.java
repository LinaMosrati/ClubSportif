package model;

public class StatMembre {

    private String nomComplet;
    private int age;
    private int totalActivites;

    public StatMembre(
            String nomComplet,
            int age,
            int totalActivites
    ) {

        this.nomComplet = nomComplet;
        this.age = age;
        this.totalActivites = totalActivites;

    }

    public String getNomComplet() {
        return nomComplet;
    }

    public int getAge() {
        return age;
    }

    public int getTotalActivites() {
        return totalActivites;
    }

}