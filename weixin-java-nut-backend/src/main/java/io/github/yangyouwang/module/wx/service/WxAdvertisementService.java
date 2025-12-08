package io.github.yangyouwang.module.wx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.yangyouwang.module.wx.entity.WxAdvertisement;

import java.util.List;

/**
 * 广告图Service接口
 *
 * @author yangyouwang
 */
public interface WxAdvertisementService extends IService<WxAdvertisement> {

    /**
     * 获取启用的广告图列表
     * @return 广告图列表
     */
    List<WxAdvertisement> getEnabledAdvertisements();

    /**
     * 获取最新的一个启用的广告图
     * @return 广告图对象
     */
    WxAdvertisement getLatestEnabledAdvertisement();
}