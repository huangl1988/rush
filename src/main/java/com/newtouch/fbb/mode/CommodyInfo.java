package com.newtouch.fbb.mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import redis.clients.jedis.Response;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Response<Set<String>> commodyNos;

    private Set<String> commodys;

}
