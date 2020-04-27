package org.geotools.ogcapi;

import java.util.ArrayList;

public class FeaturesType {
    String title;
    String description;
    ArrayList<Link> links = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OGCAPI Features:\n")
                .append("\tTitle: " + title + "\n")
                .append("\tDesc: " + description + "\n");
        sb.append("\tLinks:\n");
        for (Link l : links) {
            sb.append("\t\t" + l.toString()).append("\n");
        }
        return sb.toString();
    }
}
