package com.jnu.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.sys.entity.Book;
import com.jnu.sys.mapper.BookMapper;
import com.jnu.sys.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnu.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static com.jnu.constants.ConfigConstants.*;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {
    @Autowired
    private BookMapper bookMapper;

    /**
     * 文字识别图书
     */
    @Override
    public String ocr(String file_base64) {
        try {
            // 将base64编码进行url encode
            String imgParam = URLEncoder.encode(file_base64, "UTF-8");

            String param = "image=" + imgParam;

            String result = HttpUtil.post(OCR_URL, OCR_TOKEN, param);
            System.out.println("result" + result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图书录入
     */
    public boolean addBook(Book book) {
        QueryWrapper<Book> bookWrapper = new QueryWrapper<>();
        // 检查图书是否存在(ISBN为主键)
        bookWrapper.eq("isbn", book.getIsbn());
        Book select_book = bookMapper.selectOne(bookWrapper);

        // 便于检查
        System.out.println(select_book);

        // 判断图书是否存在
        if (select_book == null) {
            boolean isSave = save(book);
            System.out.println(isSave);
            return isSave;
        }
        // 图书已经存在
        return false;
    }

    /**
     * 获取图书封面
     */
    public String getImage(String isbn) {
        RestTemplate restTemplate = new RestTemplate();

        // 请求url
        String url = DB_URL + "/" + isbn + "?apikey=" + DB_API_KEY;
        String response = restTemplate.getForObject(url, String.class);
        String image = JSON.parseObject(response).getString("image");

        System.out.println("image:" + image);

        return image;
    }

    /**
     * 获得总销量
     */
    public long getSaleTotal() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("SUM(sale) as saleTotal");
        Map<String, Object> result = bookMapper.selectMaps(queryWrapper).get(0);
        BigDecimal saleTotal = (BigDecimal) result.get("saleTotal");
        return saleTotal.longValue();
    }

    /**
     * 获得热销前6的图书
     */
    public List<Book> findTop6BySale() {
        QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sale"); // 按销量降序排序
        queryWrapper.last("LIMIT 6"); // 限制结果为前6条记录
        return bookMapper.selectList(queryWrapper);
    }

    /**
     * 获得图书相关信息
     */
    public String getBookMessage(String isbn) {
        RestTemplate restTemplate = new RestTemplate();

        // 请求url
        String url = DB_URL + "/" + isbn + "?apikey=" + DB_API_KEY;

        return restTemplate.getForObject(url, String.class);
    }
}
