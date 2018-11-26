package com.weitian.dto;

import com.weitian.entity.SysDept;
import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2018/11/25 0025.
 */
@Data
public class SysDeptDto extends SysDept {
    private List<SysDeptDto> childDeptList;
}
