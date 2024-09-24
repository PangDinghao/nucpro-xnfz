package com.nuc.xnfz.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddToPlanRequest {//用于更新用户班级id
    @JsonProperty("AddClassId")
    private Integer[] AddClassId;
    @JsonProperty("planid")
    private Integer planid;

}
