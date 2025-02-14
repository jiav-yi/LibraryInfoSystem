package com.jnu.sys.service.impl;

import com.jnu.sys.entity.Books;
import com.jnu.sys.mapper.BooksMapper;
import com.jnu.sys.service.IBooksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnu.utils.HttpUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;

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
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements IBooksService {

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
}
