package com.jnu.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnu.commmon.vo.Result;
import com.jnu.sys.entity.Book;
import com.jnu.sys.service.IBookService;
import com.jnu.utils.BigModelUtil;
import com.jnu.utils.ImageToBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jnu.constants.ConfigConstants.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
@RestController
@RequestMapping(value = {"/book", "/statistic"})
public class BookController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private BigModelUtil bigModelUtil;

    @GetMapping("/all")
    public Result<List<Book>> getAllBooks() {
        List<Book> list = bookService.list();
        return Result.success(list, "查询成功");
    }

    // 扫描图书
    @PostMapping("/ocr")
    public Result<Map<String,Object>> ocr(@RequestBody MultipartFile file) {
        // 将MultipartFile转成Base64
        String file_base64 = ImageToBase64.generateBase64(file);

        // 调用百度OCR服务 ocr_result:识别到的文字
        String ocr_result = bookService.ocr(file_base64);

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

    // 录入图书
    @PostMapping("/add")
    public Result<Map<String,Object>> addBook(@RequestBody Book book) {
        // 打印book，方便检查问题
        System.out.println(book);

        // 获取图书封面
        String image = bookService.getImage(book.getIsbn());
        book.setImage(image);

        // 获取图书评论报告
        String book_data = "{title:" + book.getTitle() + ",author:" +book.getAuthor() + ",isbn:" +book.getIsbn() + ",introduction:" + book.getIntroduction() + "}";
        String question = GET_COMMENTS_JSON_QUESTION + book_data;
        String comments = bigModelUtil.sendQuestion(question);
        // 由于大语言模型返回的json串为Markdown格式，需要对answer进行去除多余的内容得到json串(string)
        comments = bigModelUtil.cleanAiResponse(comments);
        book.setComments(comments);

        // 方便检查
        System.out.println("comments:" + comments);

        // 录入图书
        boolean isSave = bookService.addBook(book);

        if (isSave) {
            return Result.success("录入成功!!!");
        } else {
            return Result.fail("该图书已存在");
        }
    }

    // 按(条件)查询图书列表
    @GetMapping("/list")
    public Result<Map<String, Object>> getBookList(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "isbn", required = false) String isbn,
                                                  @RequestParam(value = "author", required = false) String author,
                                                  @RequestParam(value = "pageNo") Long pageNo,
                                                  @RequestParam(value = "pageSize") Long pageSize) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(title), Book::getTitle, title);
        wrapper.eq(StringUtils.hasLength(isbn), Book::getIsbn, isbn);
        wrapper.eq(StringUtils.hasLength(author), Book::getAuthor, author);
        wrapper.orderByDesc(Book::getIsbn);

        Page<Book> page = new Page<>(pageNo, pageSize);
        bookService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        return Result.success(data);
    }

    // 修改图书
    @PutMapping
    public Result<?> updateBook(@RequestBody Book book) {
        bookService.updateById(book);
        return Result.success("修改图书信息成功!!");
    }

    // 根据ISBN查询图书
    @GetMapping("/{isbn}")
    public Result<Book> getBookByIsbn(@PathVariable("isbn") String isbn) {
        Book book = bookService.getById(isbn);
        return Result.success(book);
    }

    // 获得统计数据
    @GetMapping("/data")
    public Result<?> getStatisticData() {
        // 总销量
        long saleTotal = bookService.getSaleTotal();
        // 图书数
        long bookTotal = bookService.count();
        // 热销前6的图书
        List<Book> top6Books = bookService.findTop6BySale();
        List<Map<String, Object>> hotBooks = new ArrayList<>();
        // 词云图数据
        List<Map<String, Object>> keywordData = new ArrayList<>();

        // 填充hotBooks及keywordData
        int rank = 1;
        for (Book book : top6Books) {
            // 获得相应图书信息
            String response = bookService.getBookMessage(book.getIsbn());
            JSONObject jsonObject = JSON.parseObject(response);
            // 图书评分
            String rating = jsonObject.getJSONObject("rating").getString("average");
            // 图书tags
            JSONArray tags = jsonObject.getJSONArray("tags");

            // 填充hotBooks
            Map<String, Object> bookMap = new HashMap<>();
            bookMap.put("rank", rank);
            bookMap.put("title", book.getTitle());
            bookMap.put("author", book.getAuthor());
            bookMap.put("sales", book.getSale());
            bookMap.put("rating", rating);
            hotBooks.add(bookMap);
            rank++;

            // 填充keywordData
            for(Object tagObject : tags) {
                JSONObject tag = (JSONObject) tagObject;
                Map<String, Object> keywordMap = new HashMap<>();
                keywordMap.put("name", tag.getString("name"));
                keywordMap.put("value", tag.getString("count"));
                keywordData.add(keywordMap);
            }
        }

        // 将数据都放入data中，然后传给前端
        Map<String, Object> data = new HashMap<>();
        data.put("saleTotal", saleTotal);
        data.put("bookTotal", bookTotal);
        data.put("hotBooks", hotBooks);
        data.put("keywordData", keywordData);

        // 便于检查
        System.out.println(data);
        return Result.success(data);
    }
}
