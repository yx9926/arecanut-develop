package io.github.yangyouwang.module.wx.service;

import io.github.yangyouwang.module.wx.entity.WithdrawRecord;
import io.github.yangyouwang.module.wx.mapper.WithdrawRecordMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 微信用户提现记录表 服务实现类
 * </p>
*
* @author paul
* @since 2025-08-05
*/
@Service
public class WithdrawRecordService extends ServiceImpl<WithdrawRecordMapper, WithdrawRecord> {

  /**
  * 微信用户提现记录表分页列表
  * @param param 参数
  * @return 结果
  */
  public List<WithdrawRecord> page(WithdrawRecord param) {
    QueryWrapper<WithdrawRecord> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
      // 用户ID
          .eq(param.getUserId() != null, WithdrawRecord::getUserId, param.getUserId())
          // 小程序appid
          .eq(!StringUtils.isEmpty(param.getAppid()), WithdrawRecord::getAppid, param.getAppid())
          // 微信用户openid
          .eq(!StringUtils.isEmpty(param.getOpenid()), WithdrawRecord::getOpenid, param.getOpenid())
          // 提现金额
          .eq(param.getAmount() != null, WithdrawRecord::getAmount, param.getAmount())
          // 提现状态：0-申请中，1-已处理，2-已拒绝
          .eq(param.getStatus() != null, WithdrawRecord::getStatus, param.getStatus())
          // 提现方式：0-微信支付
          .eq(param.getMethod() != null, WithdrawRecord::getMethod, param.getMethod())
          // 申请时间
          .eq(param.getApplyTime() != null, WithdrawRecord::getApplyTime, param.getApplyTime())
          // 处理时间
          .eq(param.getProcessTime() != null, WithdrawRecord::getProcessTime, param.getProcessTime())
          // 拒绝原因
          .eq(!StringUtils.isEmpty(param.getRejectReason()), WithdrawRecord::getRejectReason, param.getRejectReason())
          // 交易单号
          .eq(!StringUtils.isEmpty(param.getTradeNo()), WithdrawRecord::getTradeNo, param.getTradeNo())
          // 删除标志 0-未删除、1-已删除
          .eq(param.getDelFlag() != null, WithdrawRecord::getDelFlag, param.getDelFlag())
    .orderByDesc(WithdrawRecord::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * 微信用户提现记录表详情
  * @param id 主键
  * @return 结果
  */
  public WithdrawRecord info(Long id) {
    return getById(id);
  }

  /**
  * 微信用户提现记录表新增
  * @param param 根据需要进行传值
  */
  public void add(WithdrawRecord param) {
    save(param);
  }

  /**
  * 微信用户提现记录表修改
  * @param param 根据需要进行传值
  */
  public void modify(WithdrawRecord param) {
    updateById(param);
  }

  /**
  * 微信用户提现记录表删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * 微信用户提现记录表删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
