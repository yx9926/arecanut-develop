package io.github.yangyouwang.module.wx.service;

import io.github.yangyouwang.module.wx.entity.MaUser;
import io.github.yangyouwang.module.wx.mapper.MaUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
* <p>
 * 微信小程序用户表 服务实现类
 * </p>
*
* @author paul
* @since 2025-08-05
*/
@Service
public class MaUserService extends ServiceImpl<MaUserMapper, MaUser> {

  /**
  * 微信小程序用户表分页列表
  * @param param 参数
  * @return 结果
  */
  public List<MaUser> page(MaUser param) {
    QueryWrapper<MaUser> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda()
      // 小程序appid
          .eq(!StringUtils.isEmpty(param.getAppid()), MaUser::getAppid, param.getAppid())
          // 用户唯一标识
          .eq(!StringUtils.isEmpty(param.getOpenid()), MaUser::getOpenid, param.getOpenid())
          // 会话密钥
          .eq(!StringUtils.isEmpty(param.getSessionKey()), MaUser::getSessionKey, param.getSessionKey())
          // 用户昵称
          .eq(!StringUtils.isEmpty(param.getNickName()), MaUser::getNickName, param.getNickName())
          // 用户头像URL
          .eq(!StringUtils.isEmpty(param.getAvatarUrl()), MaUser::getAvatarUrl, param.getAvatarUrl())
          // 性别 0-未知、1-男、2-女
          .eq(param.getGender() != null, MaUser::getGender, param.getGender())
          // 手机号码
          .eq(!StringUtils.isEmpty(param.getPhoneNumber()), MaUser::getPhoneNumber, param.getPhoneNumber())
          // 国家/地区
          .eq(!StringUtils.isEmpty(param.getCountry()), MaUser::getCountry, param.getCountry())
          // 省份
          .eq(!StringUtils.isEmpty(param.getProvince()), MaUser::getProvince, param.getProvince())
          // 城市
          .eq(!StringUtils.isEmpty(param.getCity()), MaUser::getCity, param.getCity())
          // 语言
          .eq(!StringUtils.isEmpty(param.getLanguage()), MaUser::getLanguage, param.getLanguage())
          // 用户状态 0-正常、1-禁用
          .eq(param.getStatus() != null, MaUser::getStatus, param.getStatus())
          // 余额
          .eq(param.getBalance() != null, MaUser::getBalance, param.getBalance())
          // 删除标志 0-未删除、1-已删除
          .eq(param.getDelFlag() != null, MaUser::getDelFlag, param.getDelFlag())
    .orderByDesc(MaUser::getCreateTime);
    return list(queryWrapper);
  }

  /**
  * 微信小程序用户表详情
  * @param id 主键
  * @return 结果
  */
  public MaUser info(Long id) {
    return getById(id);
  }

  /**
  * 微信小程序用户表新增
  * @param param 根据需要进行传值
  */
  public void add(MaUser param) {
    save(param);
  }

  /**
  * 微信小程序用户表修改
  * @param param 根据需要进行传值
  */
  public void modify(MaUser param) {
    updateById(param);
  }

  /**
  * 微信小程序用户表删除(单个条目)
  * @param id 主键
  */
  public void remove(Long id) {
    removeById(id);
  }

  /**
  * 微信小程序用户表删除(多个条目)
  * @param ids 主键数组
  */
  public void removes(List<Long> ids) {
     removeByIds(ids);
   }
}
