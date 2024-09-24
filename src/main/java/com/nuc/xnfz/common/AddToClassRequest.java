package com.nuc.xnfz.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddToClassRequest {//用于更新用户班级id
    @JsonProperty("AddStudentId")
    private Integer[] AddStudentId;
    @JsonProperty("classid")
    private Integer classid;

}
