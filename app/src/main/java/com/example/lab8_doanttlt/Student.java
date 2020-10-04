package com.example.lab8_doanttlt;

public class Student {
    private String ID;
    private String Name;
    private String Major;

    public Student(String ID, String Name, String Major) {
        this.ID = ID;
        this.Name = Name;
        this.Major = Major;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    @Override
    public String toString() {
        return this.ID + " - " + this.Name;
    }
}
