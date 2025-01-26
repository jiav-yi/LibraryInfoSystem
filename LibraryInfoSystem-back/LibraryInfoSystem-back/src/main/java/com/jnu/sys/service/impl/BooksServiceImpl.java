package com.jnu.sys.service.impl;

import com.jnu.sys.entity.Books;
import com.jnu.sys.mapper.BooksMapper;
import com.jnu.sys.service.IBooksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
