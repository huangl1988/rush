package com.newtouch.fbb.mode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by steven on 2018/1/26.
 */

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderEvent {

    private List<CommodyInfo> commodyInfoList;

    private String userId;

    public String code;

}
