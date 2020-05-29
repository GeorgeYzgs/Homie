package com.spring.group.dto.property;

public enum SortTypes {
    AUTOMATIC,
    PRICE_ASC,
    PRICE_DESC,
    AREA_ASC,
    AREA_DESC;

    /**
     * Method to check if the enum contains a specific value
     *
     * @param s the string to search within the values of the enum
     * @return whether the string was found or not as a boolean
     */
    public static boolean contains(String s) {
        for (SortTypes sort : values())
            if (sort.name().equals(s))
                return true;
        return false;
    }
}
