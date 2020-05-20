package com.spring.group.dto.property;

public enum SortTypes {
    AUTOMATIC,
    PRICE_ASC,
    PRICE_DESC,
    AREA_ASC,
    AREA_DESC;

    public static boolean contains(String s) {
        for (SortTypes sort : values())
            if (sort.name().equals(s))
                return true;
        return false;
    }
}
