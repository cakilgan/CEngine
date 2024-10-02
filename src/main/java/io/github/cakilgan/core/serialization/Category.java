package io.github.cakilgan.core.serialization;

public enum Category {
    CORE("core"),MANAGMENT("managment"), DATA("data");
    String category_name;
    Category(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "category."+category_name;
    }
}
