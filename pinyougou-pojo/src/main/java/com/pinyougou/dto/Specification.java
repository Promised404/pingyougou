package com.pinyougou.dto;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 规格组合实体类.
 *
 * @author 邓鹏涛
 * @date 2019/2/11 15:57
 */
@Getter
@Setter
@ToString
public class Specification implements Serializable {

    private TbSpecification specification;

    private List<TbSpecificationOption> specificationOptionList;

}
