package io.github.yangyouwang.module.wx.service;

import cn.hutool.core.collection.CollectionUtil;
import io.github.yangyouwang.module.wx.entity.RedPacketBatch;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import io.github.yangyouwang.module.wx.mapper.RedPacketBatchMapper;
import io.github.yangyouwang.module.wx.service.RedPacketQrcodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 红包批次信息表 服务实现类
 * </p>
*
* @author paul
* @since 2025-08-05
*/
@Service
public class RedPacketBatchService extends ServiceImpl<RedPacketBatchMapper, RedPacketBatch> {

  /**
  * 红包批次信息表分页列表
  * @param param 参数
  * @return 结果
  */
  public List<RedPacketBatch> page(RedPacketBatch param) {
    QueryWrapper<RedPacketBatch> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
      // 批次ID
          .eq(!StringUtils.isEmpty(param.getBatchId()), RedPacketBatch::getBatchId, param.getBatchId())
          // 总金额
          .eq(param.getTotalAmount() != null, RedPacketBatch::getTotalAmount, param.getTotalAmount())
          // 总数量
          .eq(param.getTotalCount() != null, RedPacketBatch::getTotalCount, param.getTotalCount())
          // 已使用数量
          .eq(param.getUsedCount() != null, RedPacketBatch::getUsedCount, param.getUsedCount())
          // 已提现数量
          .eq(param.getWithdrawnCount() != null, RedPacketBatch::getWithdrawnCount, param.getWithdrawnCount())
          // 批次状态：0-未生成，1-已生成，2-已过期
          .eq(param.getStatus() != null, RedPacketBatch::getStatus, param.getStatus())
    .orderByDesc(RedPacketBatch::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * 红包批次信息表详情
  * @param id 主键
  * @return 结果
  */
  public RedPacketBatch info(Long id) {
    RedPacketBatch batch = getById(id);
    if (batch != null) {
      loadQrcodes(batch);
    }
    return batch;
  }

  /**
  * 通过批次ID获取红包批次详情
  * @param batchId 批次ID
  * @return 结果
  */
  public RedPacketBatch infoByBatchId(String batchId) {
    RedPacketBatch batch = lambdaQuery()
        .eq(RedPacketBatch::getBatchId, batchId)
        .one();
    if (batch != null) {
      loadQrcodes(batch);
    }
    return batch;
  }

  private void loadQrcodes(RedPacketBatch batch) {
    // 获取关联的二维码数据
    List<RedPacketQrcode> qrcodes = redPacketQrcodeService.lambdaQuery()
        .eq(RedPacketQrcode::getBatchId, batch.getBatchId())
        .orderByAsc(RedPacketQrcode::getCreateTime)
        .list();
    batch.setQrcodes(qrcodes);
  }

  @Autowired
  private RedPacketQrcodeService redPacketQrcodeService;

  /**
  * 红包批次信息表新增
  * @param param 参数
  * @return 结果
  */
  public boolean add(RedPacketBatch param) {
//    boolean saved = save(param);
//    if (saved) {
      // 生成红包和二维码
    List<String> strings = redPacketQrcodeService.generateQrcodes(param.getBatchId(), param.getTotalAmount().doubleValue(), param.getTotalCount());
//    }
    return CollectionUtil.isNotEmpty(strings);
  }

  /**
  * 红包批次信息表修改
  * @param param 根据需要进行传值
  */
  public void modify(RedPacketBatch param) {
    updateById(param);
  }

  /**
  * 红包批次信息表删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * 红包批次信息表删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
