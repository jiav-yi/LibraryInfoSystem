package com.jnu;

import com.jnu.sys.entity.Book;
import com.jnu.sys.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class LibraryInfoSystemBackApplicationTests {

    @Resource
    private BookMapper bookMapper;

    @Test
    void testMapper() {
        List<Book> books = bookMapper.selectList(null);
        books.forEach(System.out::println);
    }
}
