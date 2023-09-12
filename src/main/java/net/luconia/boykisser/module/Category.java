package net.luconia.boykisser.module;

public enum Category {
    VISUAL("Visual"),
    RENDER("Render"),
    OTHER("Other");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
