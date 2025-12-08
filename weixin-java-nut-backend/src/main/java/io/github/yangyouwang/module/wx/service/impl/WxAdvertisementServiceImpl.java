package io.github.yangyouwang.module.wx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.yangyouwang.module.wx.entity.WxAdvertisement;
import io.github.yangyouwang.module.wx.mapper.WxAdvertisementMapper;
import io.github.yangyouwang.module.wx.service.WxAdvertisementService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 广告图Service实现类
 *
 * @author yangyouwang
 */
@Service
@AllArgsConstructor
@Slf4j
public class WxAdvertisementServiceImpl extends ServiceImpl<WxAdvertisementMapper, WxAdvertisement> implements WxAdvertisementService {

    private final WxAdvertisementMapper wxAdvertisementMapper;

    /**
     * 获取启用的广告图列表
     * @return 广告图列表
     */
    @Override
    public List<WxAdvertisement> getEnabledAdvertisements() {
        try {
            Date now = new Date();
            return lambdaQuery()
                    .eq(WxAdvertisement::getStatus, 1) // 1-启用状态
                    .and(wrapper -> wrapper
                            .isNull(WxAdvertisement::getStartTime)
                            .or()
                            .le(WxAdvertisement::getStartTime, now)
                    )
                    .and(wrapper -> wrapper
                            .isNull(WxAdvertisement::getEndTime)
                            .or()
                            .ge(WxAdvertisement::getEndTime, now)
                    )
                    .orderByDesc(WxAdvertisement::getUpdateTime)
                    .list();
        } catch (Exception e) {
            log.error("获取启用的广告图列表失败: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取最新的一个启用的广告图
     * @return 广告图对象
     */
    @Override
    public WxAdvertisement getLatestEnabledAdvertisement() {
        try {
            Date now = new Date();
            return lambdaQuery()
                    .eq(WxAdvertisement::getStatus, 1) // 1-启用状态
                    .and(wrapper -> wrapper
                            .isNull(WxAdvertisement::getStartTime)
                            .or()
                            .le(WxAdvertisement::getStartTime, now)
                    )
                    .and(wrapper -> wrapper
                            .isNull(WxAdvertisement::getEndTime)
                            .or()
                            .ge(WxAdvertisement::getEndTime, now)
                    )
                    .orderByDesc(WxAdvertisement::getUpdateTime)
                    .last("LIMIT 1")
                    .one();
        } catch (Exception e) {
            log.error("获取最新的启用广告图失败: {}", e.getMessage(), e);
            return null;
        }
    }
}