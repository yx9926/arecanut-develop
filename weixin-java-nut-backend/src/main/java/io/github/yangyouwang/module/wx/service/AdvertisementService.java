package io.github.yangyouwang.module.wx.service;

import io.github.yangyouwang.module.wx.entity.Advertisement;
import io.github.yangyouwang.module.wx.mapper.AdvertisementMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 广告图表 服务实现类
 * </p>
*
* @author paul
* @since 2025-08-28
*/
@Service
public class AdvertisementService extends ServiceImpl<AdvertisementMapper, Advertisement> {

  /**
  * 广告图表分页列表
  * @param param 参数
  * @return 结果
  */
  public List<Advertisement> page(Advertisement param) {
    QueryWrapper<Advertisement> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
      // 广告标题
          .eq(!StringUtils.isEmpty(param.getTitle()), Advertisement::getTitle, param.getTitle())
          // 广告图片URL
          .eq(!StringUtils.isEmpty(param.getImageUrl()), Advertisement::getImageUrl, param.getImageUrl())
          // 广告展示时长（秒），默认5秒
          .eq(param.getDuration() != null, Advertisement::getDuration, param.getDuration())
          // 广告点击跳转链接
          .eq(!StringUtils.isEmpty(param.getLinkUrl()), Advertisement::getLinkUrl, param.getLinkUrl())
          // 开始展示时间
          .eq(param.getStartTime() != null, Advertisement::getStartTime, param.getStartTime())
          // 结束展示时间
          .eq(param.getEndTime() != null, Advertisement::getEndTime, param.getEndTime())
          // 广告状态：0-禁用，1-启用
          .eq(param.getStatus() != null, Advertisement::getStatus, param.getStatus())
          // 删除标志 0-未删除、1-已删除
          .eq(param.getDeleted() != null, Advertisement::getDeleted, param.getDeleted())
    .orderByDesc(Advertisement::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * 广告图表详情
  * @param id 主键
  * @return 结果
  */
  public Advertisement info(Long id) {
    return getById(id);
  }

  /**
  * 广告图表新增
  * @param param 根据需要进行传值
  */
  public void add(Advertisement param) {
    save(param);
  }

  /**
  * 广告图表修改
  * @param param 根据需要进行传值
  */
  public void modify(Advertisement param) {
    updateById(param);
  }

  /**
  * 广告图表删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * 广告图表删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
