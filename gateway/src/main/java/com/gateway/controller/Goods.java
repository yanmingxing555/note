package com.gateway.controller;

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

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                '}';
    }
}
