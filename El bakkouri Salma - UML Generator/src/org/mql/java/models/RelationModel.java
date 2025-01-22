package org.mql.java.models;

import org.mql.java.enums.RelationType;
import java.util.Objects;

public class RelationModel {
	private String sourceClass;
	private String targetClass;
	private RelationType relationType;

	public RelationModel(String sourceClass, String targetClass, RelationType relationType) {
		this.sourceClass = sourceClass;
		this.targetClass = targetClass;
		this.relationType = relationType;
	}

	public RelationModel() {
	}

	public String getSourceClass() {
		return sourceClass;
	}

	public void setSourceClass(String sourceClass) {
		this.sourceClass = sourceClass;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public RelationType getRelationType() {
		return relationType;
	}

	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	@Override
	public String toString() {
		return sourceClass + " " + relationType + " " + targetClass;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		RelationModel otherObject = (RelationModel) obj;

		boolean res = Objects.equals(sourceClass, otherObject.sourceClass) 
				&& Objects.equals(targetClass, otherObject.targetClass)
				&& relationType == otherObject.relationType; 
		
		return res;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sourceClass, targetClass, relationType);
	}
}