package cn.itcast.hotel;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.HotelDoc;
import cn.itcast.hotel.service.impl.HotelService;
import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Update;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
public class HotelDemoDocumentTest {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private HotelService hotelService;
    @Test
    void contextLoads() throws IOException {
        Hotel hotel = hotelService.getById(61083L);
        //1.创建请求对象
        IndexRequest request = new IndexRequest("hotel").id(hotel.getId().toString());
        //2.将JSON格式传入到Request对象中
        HotelDoc hotelDoc=new HotelDoc( hotel);
        request.source(JSON.toJSONString(hotelDoc), XContentType.JSON);
        //3.发送请求
        client.index(request, RequestOptions.DEFAULT);
        System.out.println("完成");
    }
    @Test
    void testget() throws IOException {
        GetRequest request = new GetRequest("hotel","61083");

        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSource());
    }
    @Test
    void testdelete() throws IOException {
        DeleteRequest request = new DeleteRequest("hotel","61083");
        client.delete(request, RequestOptions.DEFAULT);
    }
    @Test
    void testupdate() throws IOException {
        UpdateRequest request = new UpdateRequest("hotel","61083");
        request.doc(
                "price",500,
                "starName","5钻"
        );
        client.update(request, RequestOptions.DEFAULT);
    }
    @Test
    void testBulk() throws IOException {
        //1.创建Request对象
        BulkRequest request = new BulkRequest();
        //2.从数据库中获取数据
        List<Hotel> hotels = hotelService.list();
        for (Hotel hotel : hotels) {
            HotelDoc hotelDoc = new HotelDoc(hotel);
            request.add(new IndexRequest("hotel")
                    .id(hotelDoc.getId().toString())
                    .source(JSON.toJSONString(hotelDoc), XContentType.JSON));
        }
        client.bulk(request,RequestOptions.DEFAULT);
    }

}
