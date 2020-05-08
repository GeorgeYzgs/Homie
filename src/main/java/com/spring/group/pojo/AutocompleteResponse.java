package com.spring.group.pojo;

public class AutocompleteResponse {
    /*
    jQuery Autocomplete
    The label property is displayed in the suggestion menu.
    The value will be inserted into the input element when a user selects an item.
    If just one property is specified, it will be used for both, e.g., if you provide only value properties,
    the value will also be used as the label.
     */

    private String label;
    private String value;

    public AutocompleteResponse(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public AutocompleteResponse(String labelAndValue) {
        this.label = labelAndValue;
        this.value = labelAndValue;
    }

    public AutocompleteResponse() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLabelAndValue(String labelAndValue) {
        this.label = labelAndValue;
        this.value = labelAndValue;
    }
}
