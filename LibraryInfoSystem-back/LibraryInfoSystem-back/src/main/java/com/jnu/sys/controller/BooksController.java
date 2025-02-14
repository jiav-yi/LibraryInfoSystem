package com.jnu.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jnu.commmon.vo.Result;
import com.jnu.sys.entity.Books;
import com.jnu.sys.service.IBooksService;
import com.jnu.utils.BigModelUtil;
import com.jnu.utils.ImageToBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.jnu.constants.ConfigConstants.SCAN_BOOK_QUESTION;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private IBooksService booksService;

    @Autowired
    private BigModelUtil bigModelUtil;

    @GetMapping("/all")
    public Result<List<Books>> getAllBooks() {
        List<Books> list = booksService.list();
        return Result.success(list, "查询成功");
    }

    @PostMapping("/ocr")
    public Result<Map<String,Object>> ocr(@RequestBody MultipartFile file) {
        // 将MultipartFile转成Base64
        String file_base64 = ImageToBase64.generateBase64(file);

        // 调用百度OCR服务 ocr_result:识别到的文字
        String ocr_result = booksService.ocr(file_base64);

        // question:将ocr_result拼接上询问大语言模型的问题
        String question = ocr_result + SCAN_BOOK_QUESTION;

        /* 调用大语言模型得到answer
            {
                "title": "活着",
                "author": "余华",
                "isbn": "978-7-5063-6543-7",
                "introduction": "《活着》是余华的作品，首次出版于2012年。这本书是一部长篇小说，讲述了主人公福贵一生的坎坷经历和对生活的深刻感悟。"
            }
         */
        String answer = bigModelUtil.sendQuestion(question);
        // 打印answer，方便检查问题
        System.out.println("answer:" + answer);

        // 由于大语言模型返回的json串为Markdown格式，需要对answer进行去除多余的内容得到json串(string)
        answer = bigModelUtil.cleanAiResponse(answer);

        // 将answer转为Map得到data
        JSONObject jsonObject = JSON.parseObject(answer);
        Map<String, Object> data = (Map<String, Object>) jsonObject;
        // 打印data，方便检查问题
        System.out.println(data);

        if (data != null) {
            return Result.success(data, "识别成功!!!");
        }
        return Result.fail("识别错误!!!");
    }
}
