package com.qingchi.base.mapper;
import com.qingchi.base.model.user.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /*@Select("SELECT\n" +
            "\tt.*\n" +
            "\tFROM\n" +
            "\ttag t\n" +
            "\t,(SELECT COUNT(0) AS num ,ts.tag_id FROM Talk_tag ts GROUP BY ts.`tag_id` ORDER BY num DESC LIMIT 30) AS b \n" +
            "\tWHERE \n" +
            "\t\n" +
            "\tt.`id`=b.tag_id\n" +
            "\tORDER BY num desc")
    List<Tag> findByName();*/
    
    @Select("SELECT user.`id`,user.`name`,tag.`id` AS tagid,tag.`name` AS tagname,state FROM user,tag,user_tag WHERE user.`id`=1 AND user_tag.`user_id`=user.`id` AND user_tag.`tag_id`=tag.`id`")
    UserDO getUserById();
    
    
    @Select("SELECT 1")
    UserDO test();
}