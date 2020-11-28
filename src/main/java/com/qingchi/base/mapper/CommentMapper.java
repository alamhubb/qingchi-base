package com.qingchi.base.mapper;

import com.qingchi.base.model.talk.CommentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("SELECT * FROM comment t WHERE t.id>=(SELECT tc.id FROM comment tc ORDER BY tc.id LIMIT #{startIndex},1) ORDER BY t.id LIMIT 0,#{num}")
    List<CommentDO> queryCommentsByPageOrderById(@Param("startIndex") Integer startIndex, @Param("num") Integer num);

}
