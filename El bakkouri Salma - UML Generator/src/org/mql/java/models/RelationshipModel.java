package org.mql.java.models;

public class RelationshipModel {
    private String fromClass;
    private String toClass;
    private String relationshipType;

    public RelationshipModel(String fromClass, String toClass, String relationshipType) {
        this.fromClass = fromClass;
        this.toClass = toClass;
        this.relationshipType = relationshipType;
    }

    @Override
    public String toString() {
        return fromClass + " " + relationshipType + " " + toClass;
    }
}
