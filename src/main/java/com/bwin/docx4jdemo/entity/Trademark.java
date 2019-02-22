package com.bwin.docx4jdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("t_brand_category_big")
public class Trademark implements Serializable {

    String id;
    String parentId;
    String code;
    String name;
    String isParent;

}
