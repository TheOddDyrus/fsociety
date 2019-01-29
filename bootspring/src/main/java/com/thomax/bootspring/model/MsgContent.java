package com.thomax.bootspring.model;

import java.util.List;

public class MsgContent {

    private Integer type;

    private List<Object> content;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Object> getContent() {
        return content;
    }

    public void setContent(List<Object> content) {
        this.content = content;
    }

}
