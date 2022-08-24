package com.sparta.week01.prac;

public class Tutor {
    private String name;
    private String bio;

    // 기본 생성자
    public Tutor() {

    }

    // 생성자
    public Tutor(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    // Getter
    public String getName(String name) {
        return this.name;
    }

    public String getBio(String bio) {
        return this.bio;
    }
}
