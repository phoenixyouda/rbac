package com.weitian.dto;

import com.weitian.entity.SysDept;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/11/25 0025.
 */
@Data
public class SysDeptDto extends SysDept implements Serializable {

    private static final long serialVersionUID = -4394602492465607058L;
    //children固定命名，配合前台ztree“children”属性
    private List<SysDeptDto> children;
    //层级是否默认打开
    private boolean open;
}
