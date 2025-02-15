package com.jnu.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jnu.commmon.vo.Result;
import com.jnu.sys.entity.Book;
import com.jnu.sys.mapper.BookMapper;
import com.jnu.sys.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnu.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
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
    /**
     * 文字识别图书
     * @return
     */
    @Override
    public String ocr(String file_base64) {
        // 请求url
        String url = OCR_URL;

        try {
            // 将base64编码进行url encode
            String imgParam = URLEncoder.encode(file_base64, "UTF-8");

            String param = "image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = OCR_TOKEN;

            String result = HttpUtil.post(url, accessToken, param);
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
        Book select_book = baseMapper.selectOne(bookWrapper);

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
}
