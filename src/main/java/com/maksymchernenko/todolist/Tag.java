package com.maksymchernenko.todolist;

public enum Tag {
    HOUSEWORK,
    STUDY,
    TRAVEL,
    EVENT,
    HOBBY,
    CARE,
    FINANCE,
    OTHER;

    public static Tag fromString(String tag) {
        return switch (tag.toLowerCase()) {
            case "housework" -> HOUSEWORK;
            case "study" -> STUDY;
            case "travel" -> TRAVEL;
            case "event" -> EVENT;
            case "hobby" -> HOBBY;
            case "care" -> CARE;
            case "finance" -> FINANCE;
            default -> OTHER;
        };
    }
}
