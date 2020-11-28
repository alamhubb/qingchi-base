package com.qingchi.base.store;

import com.qingchi.base.config.redis.RedisKeysConst;
import com.qingchi.base.config.redis.RedisUtil;
import com.qingchi.base.model.user.ShellOrderDO;
import com.qingchi.base.repository.shell.ShellOrderRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShellOrderStore {
    @Resource
    private ShellOrderRepository shellOrderRepository;
    @Resource
    private RedisUtil redisUtil;

    public List<ShellOrderDO> saveAll(List<ShellOrderDO> shellOrderDOS) {
        shellOrderDOS = shellOrderRepository.saveAll(shellOrderDOS);

        Set<String> redisKeys = shellOrderDOS.stream()
                .map((item -> RedisKeysConst.userById + RedisKeysConst.springKey + item.getUserId())).collect(Collectors.toSet());
        //清除缓存
        redisUtil.del(redisKeys);
        return shellOrderDOS;
    }
}