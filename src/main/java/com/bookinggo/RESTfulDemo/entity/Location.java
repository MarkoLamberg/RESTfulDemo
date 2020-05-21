package com.bookinggo.RESTfulDemo.entity;

public enum Location {

    London("London"), Paris("Paris"), Rome("Rome"), Barcelona("Barcelona");

    private String label;

    private Location(String label) {
        this.label = label;
    }

    public static Location findByLabel(String byLabel) {
        for(Location r: Location.values()) {
            if (r.label.equalsIgnoreCase(byLabel)) {
                return r;
            }
        }

        return null;
    }

    public String getLabel() {
        return label;
    }
}
