package com.fomp.note.springcloud;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//相当于拥有了getter、setter、toString方法
@AllArgsConstructor//全参构造
@NoArgsConstructor//空参构造
public class Goods {
    public Integer id;
    private String name;
    private String image;
    private Double price;
} 
