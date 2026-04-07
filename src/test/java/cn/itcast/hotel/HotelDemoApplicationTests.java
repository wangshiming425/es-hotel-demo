package cn.itcast.hotel;

import cn.itcast.hotel.constants.HotelConstants;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class HotelDemoApplicationTests {
    @Autowired
    private RestHighLevelClient client;
   @Test
    void createHotelIndex() throws IOException {
       //1.创建Request对象
       CreateIndexRequest request = new CreateIndexRequest(HotelConstants.INDEX_NAME);
       //2.准备请求参数(DSL语句)
       request.source(HotelConstants.MAPPING_TEMPLATE, XContentType.JSON);
       //3.发送请求
       client.indices().create(request, RequestOptions.DEFAULT);
   }

   @Test void deleteHotelIndex() throws IOException {
       //1.创建Request对象
       DeleteIndexRequest request = new DeleteIndexRequest(HotelConstants.INDEX_NAME);
       //2.发送请求
       client.indices().delete(request, RequestOptions.DEFAULT);
   }
   @Test
    void existHotelIndex() throws IOException {
       //1.创建Request对象
       GetIndexRequest request = new GetIndexRequest(HotelConstants.INDEX_NAME);
       //2.发送请求
       boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
       //3.处理响应
       System.out.println(exists);
   }

}
