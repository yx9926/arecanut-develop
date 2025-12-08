package io.github.yangyouwang.module.wx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.yangyouwang.module.wx.entity.WxMaUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 微信小程序用户Mapper接口
 *
 * @author yangyouwang
 */
@Mapper
public interface WxMaUserMapper extends BaseMapper<WxMaUser> {

}