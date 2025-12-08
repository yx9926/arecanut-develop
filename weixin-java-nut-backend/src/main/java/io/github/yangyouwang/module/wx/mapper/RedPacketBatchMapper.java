package io.github.yangyouwang.module.wx.mapper;

import io.github.yangyouwang.module.wx.entity.RedPacketBatch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 红包批次信息表 Mapper 接口
 * </p>
 *
 * @author paul
 * @since 2025-08-05
 */
@Mapper
public interface RedPacketBatchMapper extends BaseMapper<RedPacketBatch> {

    /**
     * 根据批次ID查询批次信息
     * @param batchId 批次ID
     * @return 批次信息
     */
    RedPacketBatch selectByBatchId(String batchId);

    /**
     * 增加已提现数量
     * @param batchId 批次ID
     * @return 影响行数
     */
    int incrementWithdrawnCount(String batchId);

    /**
     * 增加已使用数量
     * @param batchId 批次ID
     * @return 影响行数
     */
    int incrementUsedCount(String batchId);

}
