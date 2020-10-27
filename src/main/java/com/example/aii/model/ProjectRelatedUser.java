package com.example.aii.model;

import com.example.aii.entity.Project;

import java.util.List;

public class ProjectRelatedUser extends Project {
    private List<Number> relatedUserIdArray;

    public List<Number> getRelatedUserIdArray() {
        return relatedUserIdArray;
    }

    public void setRelatedUserIdArray(List<Number> relatedUserIdArray) {
        this.relatedUserIdArray = relatedUserIdArray;
    }
}
