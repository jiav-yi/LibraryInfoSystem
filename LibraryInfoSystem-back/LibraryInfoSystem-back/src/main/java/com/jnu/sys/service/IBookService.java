package com.jnu.sys.service;

import com.jnu.sys.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiav
 * @since 2025-01-26
 */
public interface IBookService extends IService<Book> {
    /**
     * 识别图书
     * @param file_base64
     * @return 文字识别获取到的信息
     *         格式如下:
     *          {"words_result":[
     *               {"words":"活着/余华著.-3版.-北京：作家出版社，2012.8"},
     *               {"words":"(2018.8重印)"},{"words":"(余华作品)"},
     *               {"words":"ISBN978-7-5063-6543-7"}],
     *           "words_result_num":4,
     *           "log_id":1889514639947915854
     *           }
     */
    String ocr(String file_base64);

    /**
     * 录入图书
     * @param book
     * @return 是否成功录入
     */
    boolean addBook(Book book);

    /**
     * 获取图书封面
     * @param isbn
     * @return 图书封面
     */
    String getImage(String isbn);

    /**
     * 获取总销量
     * @return 总销量
     */
    public long getSaleTotal();

    /**
     * 获得热销前6的图书
     * @return 热销前6的图书
     */
    public List<Book> findTop6BySale();

    /**
     * 根据isbn获取图书相关信息
     * @param isbn
     * @return 图书相关信息Json String形式
     */
    public String getBookMessage(String isbn);
}
