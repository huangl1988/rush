package com.newtouch.fbb.mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by steven on 2018/1/18.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommodyInfo {

    private String comodyCode;

    private Long number;

    private Map<String,String> otherInfo;

    public String userId;
}
