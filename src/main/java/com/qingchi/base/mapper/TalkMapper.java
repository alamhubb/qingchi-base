package com.qingchi.base.mapper;

import com.qingchi.base.model.talk.TalkDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TalkMapper {
    @Select("SELECT * FROM talk t WHERE t.id>=(SELECT tc.id FROM talk tc ORDER BY tc.id LIMIT #{startIndex},1) ORDER BY t.id LIMIT 0,#{num}")
    List<TalkDO> queryTalksByPageOrderById(@Param("startIndex") Integer startIndex, @Param("num") Integer num);


    @Select("SELECT t.id FROM talk t,user u where t.user_id=u.id and t.status in (#{status}) order by t.update_time desc")
    List<Integer> queryTalkIdsTop10ByStatusInAndUserGenderInAndAndIdNotInOrderByUpdateTimeDesc(@Param("status")List<String> status, @Param("genders")List<String> genders, @Param("talkIds")List<Integer> talkIds);


    @Select("select * from talk t,user_contact c where c.user_id = #{userId} and c.be_user_id = t.user_id and c.contact_type = #{contactType}")
    List<TalkDO> queryTalkList(@Param("userId") Integer userId, @Param("contactType") Integer contactType);


    @Select("select * from position where user_id = #{userId} order by date desc limit 1\n" +
            "\n" +
            "select t.*\n" +
            "from\n" +
            "  talk t,\n" +
            "  position p,\n" +
            "  (select\n" +
            "     lat,\n" +
            "     lng\n" +
            "   from position\n" +
            "   where user_id = 1\n" +
            "   order by date desc\n" +
            "   limit 1) up\n" +
            "where\n" +
            "  t.position_id = p.id and\n" +
            "  p.lat between (up.lat - 0.02) and (up.lat + 0.02) and\n" +
            "  p.lng between (up.lng - 0.02) and (up.lng + 0.02)")
    List<TalkDO> queryTalkListByPosition(Integer userId);
}
