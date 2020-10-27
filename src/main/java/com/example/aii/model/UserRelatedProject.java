package com.example.aii.model;

import com.example.aii.entity.User;

import java.util.List;

public class UserRelatedProject extends User {
    private List<Number> relatedProjectIdArray;

    public List<Number> getRelatedProjectIdArray() {
        return relatedProjectIdArray;
    }

    public void setRelatedProjectIdArray(List<Number> relatedProjectIdArray) {
        this.relatedProjectIdArray = relatedProjectIdArray;
    }
}
