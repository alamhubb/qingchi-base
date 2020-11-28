package com.qingchi.base.entity;

import com.qingchi.base.model.talk.PositionDO;
import com.qingchi.base.repository.position.PositionRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PositionUtils {
    private static PositionRepository positionRepository;

    @Resource
    public void setPositionRepository(PositionRepository positionRepository) {
        PositionUtils.positionRepository = positionRepository;
    }

    public static PositionDO get(Integer positionId) {
        return positionRepository.findById(positionId).get();
    }


    public static PositionDO save(PositionDO positionDO) {
        return positionRepository.save(positionDO);
    }
}
