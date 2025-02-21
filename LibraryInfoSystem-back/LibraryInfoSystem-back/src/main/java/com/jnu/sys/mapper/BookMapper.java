package com.jnu.sys.mapper;

import com.jnu.sys.entity.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jiav
 * @since 2025-02-16
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}
