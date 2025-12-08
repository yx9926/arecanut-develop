package io.github.yangyouwang.module.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.yangyouwang.module.wx.entity.WxWithdrawRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信用户提现记录Mapper接口
 *
 * @author yangyouwang
 */
@Mapper
public interface WxWithdrawRecordMapper extends BaseMapper<WxWithdrawRecord> {

}