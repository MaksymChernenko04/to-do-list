package com.maksymchernenko.todolist;

/**
 * Represents different categories (tags) that can be assigned to a {@link PersonalTask}.
 * <p>
 * Tags help classify personal tasks into specific groups.
 * </p>
 */
public enum Tag {
    HOUSEWORK,
    STUDY,
    TRAVEL,
    EVENT,
    HOBBY,
    CARE,
    FINANCE,
    OTHER;

    /**
     * Converts a string representation into a corresponding {@link Tag} enum.
     * <p>
     * This method is case-insensitive and will return {@link #OTHER} if the provided string
     * does not match any predefined tag.
     * </p>
     *
     * @param tag the tag as a string value (case-insensitive)
     * @return the corresponding {@link Tag} or {@link #OTHER} if the input does not match any tag
     */
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
