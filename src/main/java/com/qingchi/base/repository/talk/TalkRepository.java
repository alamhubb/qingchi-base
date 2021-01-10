package com.qingchi.base.repository.talk;

import com.qingchi.base.model.BaseModelDO;
import com.qingchi.base.model.report.ReportDO;
import com.qingchi.base.model.talk.TalkDO;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TalkRepository extends JpaRepository<TalkDO, Integer> {
    Optional<TalkDO> findOneByIdAndStatusIn(Integer id, List<String> status);

    @Cacheable(cacheNames = "talkById", key = "#id")
    Optional<TalkDO> findById(Integer id);

    //需要修改使用上面
    @Cacheable(cacheNames = "talkById", key = "#id")
    Optional<BaseModelDO> findTop1ById(Integer id);

    @CachePut(cacheNames = "talkById", key = "#talk.id")
    TalkDO save(TalkDO talk);


    /**
     * 查询可用的全局置顶的动态，为官方置顶的动态
     *
     * @param status
     * @param globalTop
     * @return
     */
    //缓存
    @Cacheable(cacheNames = "stickTalks", key = "#globalTop")
    List<TalkDO> findTop2ByStatusInAndIdNotInAndGlobalTopGreaterThanOrderByGlobalTopDesc(List<String> status, List<Integer> talkIds, Integer globalTop);

    @Query(value = "SELECT t.id FROM TalkDO t where t.status in (:status) and t.userId=:userId and t.id not in (:talkIds) order by t.createTime desc")
    List<Integer> queryTalkIdsTop10ByUser(
            @Param("talkIds") List<Integer> talkIds,
            @Param("userId") Integer userId,
            @Param("status") List<String> status,
            Pageable pageable);

    @Query(value = "select t.id from TalkDO t where (t.status in (:status) or (t.userId =:userId and t.status =:onlyUserSeeStatus)) and (t.userId = :userId or t.userId in (select f.userId from FollowDO f where f.userId = :userId and f.status=:followStatus)) and t.id not in (:talkIds) order by t.createTime desc ")
    List<Integer> queryTalkIdsTop10ByUserFollow(
            @Param("talkIds") List<Integer> talkIds,
            @Param("userId") Integer userId,
            @Param("onlyUserSeeStatus") String onlyUserSeeStatus,
            @Param("status") List<String> status,
            @Param("followStatus") String followStatus,
            Pageable pageable);

    //只根据性别查询
    @Query(value = "SELECT t.id FROM TalkDO t,UserDO u where" +
            " t.userId=u.id and (t.status in (:status) or (t.userId =:userId and t.status =:onlyUserSeeStatus)) and u.gender in (:genders) and t.id not in (:talkIds) order by t.updateTime desc")
    List<Integer> queryTalkIdsTop10ByGender(
            @Param("talkIds") List<Integer> talkIds,
            @Param("userId") Integer userId,
            @Param("onlyUserSeeStatus") String onlyUserSeeStatus,
            @Param("status") List<String> status,
            @Param("genders") List<String> genders,
            Pageable pageable);


    //根据性别年龄和同城
    @Query(value = "SELECT t.id FROM TalkDO t,UserDO u where t.userId=u.id and (t.status in (:status) or (t.userId =:userId and t.status =:onlyUserSeeStatus)) and u.gender in (:genders) and u.age between :minAge and :maxAge and t.adCode like :adCode and t.id not in (:talkIds) order by t.updateTime desc")
    List<Integer> queryTalkIdsTop10ByGenderAgeAndLikeAdCode(
            @Param("talkIds") List<Integer> talkIds,
            @Param("userId") Integer userId,
            @Param("onlyUserSeeStatus") String onlyUserSeeStatus,
            @Param("status") List<String> status,
            @Param("genders") List<String> genders,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("adCode") String adCode,
            Pageable pageable);


    //根据性别年龄和同城和标签
    @Query(value = "SELECT t.id FROM TalkDO t,UserDO u, TalkTagDO ttg,TagDO tg where " +
            "t.userId=u.id and ttg.talkId=t.id and ttg.tagId = tg.id  " +
            "and (t.status in (:status) or (t.userId =:userId and t.status =:onlyUserSeeStatus)) and u.gender in (:genders) and u.age between :minAge and :maxAge and t.adCode like :adCode and tg.id in (:tagIds) and t.id not in (:talkIds) order by t.updateTime desc")
    List<Integer> queryTalkIdsTop10ByGenderAgeAndLikeAdCodeAndTagIds(
            @Param("talkIds") List<Integer> talkIds,
            @Param("userId") Integer userId,
            @Param("onlyUserSeeStatus") String onlyUserSeeStatus,
            @Param("status") List<String> status,
            @Param("genders") List<String> genders,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("adCode") String adCode,
            @Param("tagIds") List<Integer> tagIds,
            Pageable pageable);

    Integer countByUserIdAndCreateTimeBetween(Integer userId, Date startDate, Date endDate);


    //供后台统计使用**************************************************************************************
    @Query(nativeQuery = true, value = "SELECT COUNT(0),DATE_FORMAT(t.create_time,'%Y-%m-%d')AS DAY " +
            "FROM talk t" +
            " WHERE " +
            "t.create_time BETWEEN '2020-01-01' AND now() " +
            "GROUP BY DATE_FORMAT(t.create_time,'%Y-%m-%d')")
    List<Object> countAndCreateTimeByCreateTimeBetween();

    List<TalkDO> findTop2000ByStatusAndViolateTypeOrderByIdDesc(String status, String violateType);

    //查询关键词触发次数时使用
    Page<TalkDO> findByStatusNotInOrderByIdDesc(Pageable pageable, List<String> status);

    Page<TalkDO> findByStatusNotInAndContentLikeOrderByIdDesc(Pageable pageable, List<String> status, String content);

    List<TalkDO> findTop20ByUserIdOrderByIdDesc(Integer userId);

    //弃用的*************************************************************************************

    @Query(nativeQuery = true, value = "SELECT t.* FROM talk t,position p,user u WHERE t.user_id = u.id and u.age between :minAge and :maxAge and u.gender in (:genders) and t.id not in (:talkIds) and t.status in (:status) and t.position_id = p.`id` and p.`lon` BETWEEN :lon-:distance AND :lon+:distance AND p.`lat` BETWEEN :lat-:distance AND :lat+:distance ORDER BY FLOOR(UNIX_TIMESTAMP(t.`update_time`)/3600) DESC,t.id desc LIMIT :limitNum")
    List<TalkDO> findTalksByPosition(@Param("status") List<String> status, @Param("genders") List<String> genders, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, @Param("talkIds") List<Integer> talkIds, @Param("lon") Double lon, @Param("lat") Double lat, @Param("distance") Double distance, @Param("limitNum") Integer limitNum);

    @Query(nativeQuery = true, value = "SELECT distinct t.* FROM talk t,position p,talk_tag g,user u WHERE t.user_id = u.id and u.age between :minAge and :maxAge and u.gender in (:genders) and t.position_id = p.`id` and t.id = g.talk_id and t.id not in (:talkIds) and t.status in (:status) and g.tag_id in (:tagIds) and p.`lon` BETWEEN :lon-:distance AND :lon+:distance AND p.`lat` BETWEEN :lat-:distance AND :lat+:distance ORDER BY FLOOR(UNIX_TIMESTAMP(t.`update_time`)/3600) DESC,t.id desc LIMIT :limitNum")
    List<TalkDO> findTalksByPositionAndTags(@Param("status") List<String> status, @Param("genders") List<String> genders, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge, @Param("talkIds") List<Integer> talkIds, @Param("lon") Double lon, @Param("lat") Double lat, @Param("distance") Double distance, @Param("limitNum") Integer limitNum, @Param("tagIds") List<Integer> tagIds);


    //新增自己可见预审核状态动态，弃用这个
    //缓存
//    List<TalkDO> findTop10ByStatusInAndUserGenderInAndUserAgeBetweenAndIdNotInAndDistrictAdCodeLikeAndTagsIdInOrderByUpdateTimeDesc(List<String> status, List<String> genders, Integer minAge, Integer maxAge, List<Integer> talkIds, String adCode, List<Integer> tagIds);

}