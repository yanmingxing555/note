package com.fomp.note;

import com.yss.fomp.common.test.FompTestNgTransactionTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

@AutoConfigureMockMvc
@EnableAutoConfiguration
@SpringBootTest(classes = NoteApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteControllerTest extends FompTestNgTransactionTest {

    @Autowired
    private MockMvc mvc;

    @Test(testName = "产品新增")
    public void testProductAdd() throws Exception {
        /*String url = "/v1/fc/product";
        Reporter.log(String.format("路径信息为:%s,请求方式为:%s", url, "POST"));
        Reporter.log(String.format("入参信息为: ProductVo"));
        ProductVo productVo = new ProductVo();
        productVo.setId("Test1000000009");
        productVo.setProductCode("300377");
        productVo.setProductName("赢时胜1号");
        productVo.setProductType("自建");
        productVo.setHolidayCode("CN");
        productVo.setProductState("存续期");
        productVo.setRaiseStartDate(DateUtils.parseDate("2021-12-22" , "YYYY-MM-DD"));
        productVo.setRaiseEndDate(DateUtils.parseDate("2021-12-22" , "YYYY-MM-DD"));
        productVo.setBuildDate(DateUtils.parseDate("2021-12-22" , "YYYY-MM-DD"));
        productVo.setCloseDate(DateUtils.parseDate("2222-12-22" , "YYYY-MM-DD"));
        productVo.setClearDate(DateUtils.parseDate("9998-12-22" , "YYYY-MM-DD"));
        String responseContent = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(productVo))).andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        Reporter.log(String.format("返回结果为：%s", responseContent));*/
    }

}
