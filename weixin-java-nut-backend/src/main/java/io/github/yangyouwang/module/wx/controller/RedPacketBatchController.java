package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.module.wx.entity.RedPacketBatch;
import io.github.yangyouwang.module.wx.service.RedPacketBatchService;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;

import org.springframework.stereotype.Controller;
import io.github.yangyouwang.common.base.controller.BaseController;
import io.github.yangyouwang.module.wx.entity.RedPacketQrcode;
import io.github.yangyouwang.module.wx.service.RedPacketQrcodeService;

import javax.validation.Valid;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.io.ByteArrayInputStream;
import lombok.extern.slf4j.Slf4j;
import cn.binarywang.wx.miniapp.api.WxMaService;
/**
* <p>
* 红包批次信息表 前端控制器
* </p>
* @author paul
* @since 2025-08-05
*/
@Api(tags = "红包批次信息表")
@Controller
@RequestMapping("/wx/redPacketBatch")
@RequiredArgsConstructor
@Slf4j
public class RedPacketBatchController extends BaseController {

  private static final String SUFFIX = "wx/redPacketBatch";

  @Autowired
  private RedPacketBatchService redPacketBatchService;

  @Autowired
  private RedPacketQrcodeService redPacketQrcodeService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "红包批次信息表分页列表", response = RedPacketBatch.class)
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
    @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('redPacketBatch:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public TableDataInfo page(RedPacketBatch param) {
    startPage();
    List<RedPacketBatch> data = redPacketBatchService.page(param);
    return getDataTable(data);
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = redPacketBatchService.info(id);
    map.put("redPacketBatch",data);
    return SUFFIX + "/edit";
  }

   @GetMapping("/detailPage/{id}")
   public String detailPage(@PathVariable String id, ModelMap map){
    Object data = redPacketBatchService.infoByBatchId(id);
    map.put("redPacketBatch",data);
    return SUFFIX + "/detail";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }

  @GetMapping("/batchAddPage")
  public String batchAddPage(){
    return SUFFIX + "/batchAdd";
  }

  @CrudLog(title = "红包批次信息表新增",businessType = BusinessType.INSERT)
  @ApiOperation(value = "红包批次信息表新增")
  @PreAuthorize("hasAuthority('redPacketBatch:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated RedPacketBatch param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    redPacketBatchService.add(param);
    return Result.success();
  }

  @CrudLog(title = "红包批次信息表修改",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "红包批次信息表修改")
  @PreAuthorize("hasAuthority('redPacketBatch:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated RedPacketBatch param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    redPacketBatchService.modify(param);
    return Result.success();
  }

  @CrudLog(title = "红包批次信息表删除(单个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "红包批次信息表删除(单个条目)")
  @PreAuthorize("hasAuthority('redPacketBatch:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    redPacketBatchService.remove(id);
    return Result.success();
  }

  @CrudLog(title = "红包批次信息表批量新增",businessType = BusinessType.INSERT)
  @ApiOperation(value = "红包批次信息表批量新增")
  @PreAuthorize("hasAuthority('redPacketBatch:add')")
  @PostMapping(value = "/batchAdd")
  @ResponseBody
  public Result batchAdd(@RequestBody @Valid List<RedPacketBatch> batches) {
    if (batches == null || batches.isEmpty()) {
        return Result.failure("批次数据不能为空");
    }
    
    for (RedPacketBatch batch : batches) {
        if (batch.getTotalAmount() == null || batch.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return Result.failure("批次 " + batch.getBatchId() + " 的总金额必须大于0");
        }
        if (batch.getTotalCount() == null || batch.getTotalCount() <= 0) {
            return Result.failure("批次 " + batch.getBatchId() + " 的红包数量必须大于0");
        }
        redPacketBatchService.add(batch);
    }
    
    return Result.success("成功批量创建 " + batches.size() + " 个红包批次");
  }

  @CrudLog(title = "红包批次信息表删除(多个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "红包批次信息表删除(多个条目)")
  @PreAuthorize("hasAuthority('redPacketBatch:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     redPacketBatchService.removes(ids);
     return Result.success();
   }

  /**
   * 按批次ID下载二维码文件夹
   * 零存储实时生成，自动创建批次文件夹
   */
  @ApiOperation(value = "按批次下载二维码文件夹")
  @ApiImplicitParam(name = "batchId", value = "批次ID", required = true, dataType = "String", paramType = "path")
  @GetMapping("/{batchId}/download-qrcodes")
  public void downloadBatchQrcodes(@PathVariable String batchId,
                                   @RequestParam(defaultValue = "false") boolean miniProgramCode,
                                   HttpServletResponse response) throws IOException {
    
    // 获取批次信息
    RedPacketBatch batch = redPacketBatchService.lambdaQuery()
        .eq(RedPacketBatch::getBatchId, batchId)
        .one();
    if (batch == null) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "批次不存在");
      return;
    }
    
    // 获取批次下所有二维码
    List<RedPacketQrcode> qrcodes = redPacketQrcodeService.lambdaQuery()
        .eq(RedPacketQrcode::getBatchId, batchId)
        .list();
    
    if (qrcodes.isEmpty()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "该批次暂无二维码");
      return;
    }
    
    // 自动命名文件夹：批次ID_批次名称_日期
    String folderName = String.format("红包批次%s_%s",
        batchId,
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")));
    
    // 设置响应头
    response.setContentType("application/zip");
    response.setHeader("Content-Disposition", 
        "attachment; filename*=UTF-8''" + 
        java.net.URLEncoder.encode(folderName + ".zip", "UTF-8"));
    
    try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
      String basePath = folderName + "/";
      
      // 生成每个二维码并添加到zip
      for (int i = 0; i < qrcodes.size(); i++) {
        RedPacketQrcode qrcode = qrcodes.get(i);
        
        // 构建二维码内容URL
        String url = "/pages/redPacket/redPacket?hid=" + qrcode.getQrcodeId();
        
        // 文件名：序号_金额_二维码ID.png
        String fileName = String.format("%03d_%.2f元_%s.png", 
            i + 1,
            qrcode.getAmount(),
            qrcode.getQrcodeId());
        
        ZipEntry zipEntry = new ZipEntry(basePath + fileName);
        zos.putNextEntry(zipEntry);
        
        // 实时生成二维码图片
        int size = miniProgramCode ? 430 : 300; // 小程序码建议430px
        BufferedImage qrcodeImage;
        if (miniProgramCode) {
          qrcodeImage = generateMiniProgramCodeImage(url, size, size);
        } else {
          qrcodeImage = generateQrcodeImage(url, size, size);
        }
        ImageIO.write(qrcodeImage, "PNG", zos);
        zos.closeEntry();
      }
      
      // 添加批次信息文件
      addBatchInfoFile(batch, qrcodes, zos, basePath);
    }
  }
  
  /**
   * 生成二维码图片
   */
  private BufferedImage generateQrcodeImage(String content, int width, int height) {
    // 使用简单的二维码生成，实际项目中可替换为更复杂的生成逻辑
    try {
      return cn.hutool.extra.qrcode.QrCodeUtil.generate(content, width, height);
    } catch (Exception e) {
      // 如果hutool不可用，使用备用方案
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
  }

  /**
   * 生成小程序码图片
   * 使用微信官方接口生成小程序码，可直接扫码跳转到小程序指定页面
   */
  @Autowired
  private WxMaService wxMaService;

  private BufferedImage generateMiniProgramCodeImage(String content, int width, int height) {
    try {
      // 切换到默认的小程序配置（实际应用中可根据需要指定appid）
      // 由于这是多应用配置，需要确保有默认配置或指定具体的appid
      // wxMaService.switchover("your_appid_here");
      
      // 调用微信官方接口生成小程序码
      // content参数应该是小程序页面路径，如"/pages/redPacket/redPacket?hid=xxx"
      String pagePath = content; // 页面路径
      String scene = "hid=" + content.split("\\?hid=")[1]; // 提取hid参数作为场景值
      File qrcodeFile = wxMaService.getQrcodeService().createQrcode(scene, width);
      // 读取文件内容为字节数组
      byte[] codeBytes = java.nio.file.Files.readAllBytes(qrcodeFile.toPath());
      
      // 将字节数组转换为BufferedImage
      ByteArrayInputStream bis = new ByteArrayInputStream(codeBytes);
      BufferedImage image = ImageIO.read(bis);
      bis.close();
      
      return image;
    } catch (Exception e) {
      // 记录错误信息
      log.error("生成小程序码失败: {}", e.getMessage(), e);
      // 如果生成失败，返回一个空白图像
      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }
  }
  
  /**
   * 添加批次信息文件到zip
   */
  private void addBatchInfoFile(RedPacketBatch batch, 
                               List<RedPacketQrcode> qrcodes, 
                               ZipOutputStream zos, 
                               String basePath) throws IOException {
    
    BigDecimal totalAmount = qrcodes.stream()
        .map(RedPacketQrcode::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
    String infoContent = String.format(
        "红包批次信息\n" +
        "============\n" +
        "批次ID: %s\n" +
        "批次名称: %s\n" +
        "批次总金额: %.2f元\n" +
        "红包数量: %d个\n" +
        "创建时间: %s\n" +
        "下载时间: %s\n\n" +
        "文件说明:\n" +
        "文件名格式: 序号_金额_二维码ID.png\n" +
        "例如: 001_5.20元_abc123.png\n\n" +
        "二维码内容: /pages/redPacket/redPacket?hid=二维码ID\n\n" +
        "温馨提示: 扫码后可直接领取红包",
        batch.getBatchId(),
        batch.getBatchId(),
        totalAmount.doubleValue(),
        qrcodes.size(),
        batch.getCreateTime() != null ? 
            batch.getCreateTime().toString() : "未知",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    );
    
    ZipEntry infoEntry = new ZipEntry(basePath + "批次说明.txt");
    zos.putNextEntry(infoEntry);
    zos.write(infoContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    zos.closeEntry();
  }
  
  /**
   * 批量下载多个批次的二维码文件夹
   * 将所有选中的批次打包到一个zip文件中
   */
  @ApiOperation(value = "批量下载多个批次的二维码文件夹")
  @ApiImplicitParam(name = "batchIds", value = "批次ID列表，用逗号分隔", required = true, dataType = "String", paramType = "query")
  @GetMapping("/batch-download-qrcodes")
  public void batchDownloadQrcodes(@RequestParam String batchIds,
                                   @RequestParam(defaultValue = "false") boolean miniProgramCode,
                                   HttpServletResponse response) throws IOException {
    
    if (batchIds == null || batchIds.trim().isEmpty()) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "批次ID不能为空");
      return;
    }
    
    String[] batchIdArray = batchIds.split(",");
    if (batchIdArray.length == 0) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "批次ID格式不正确");
      return;
    }
    
    // 获取所有批次信息
    List<RedPacketBatch> batches = redPacketBatchService.lambdaQuery()
        .in(RedPacketBatch::getBatchId, (Object[]) batchIdArray)
        .list();
    
    if (batches.isEmpty()) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到指定的批次");
      return;
    }
    
    // 自动命名文件夹：批量下载_日期
    String folderName = String.format("批量红包批次_%s",
        LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd")));
    
    // 设置响应头
    response.setContentType("application/zip");
    response.setHeader("Content-Disposition", 
        "attachment; filename*=UTF-8''" + 
        java.net.URLEncoder.encode(folderName + ".zip", "UTF-8"));
    
    try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
      
      // 为每个批次生成二维码
      for (RedPacketBatch batch : batches) {
        String batchId = batch.getBatchId();
        
        // 获取该批次下所有二维码
        List<RedPacketQrcode> qrcodes = redPacketQrcodeService.lambdaQuery()
            .eq(RedPacketQrcode::getBatchId, batchId)
            .list();
        
        if (qrcodes.isEmpty()) {
          continue; // 跳过没有二维码的批次
        }
        
        // 批次文件夹名称
        String batchFolderName = String.format("批次%s", batchId);
        String basePath = folderName + "/" + batchFolderName + "/";
        
        // 生成每个二维码并添加到zip
        for (int i = 0; i < qrcodes.size(); i++) {
          RedPacketQrcode qrcode = qrcodes.get(i);
          
          // 构建二维码内容URL
          String url = "/pages/redPacket/redPacket?hid=" + qrcode.getQrcodeId();
          
          // 文件名：序号_金额_二维码ID.png
          String fileName = String.format("%03d_%.2f元_%s.png", 
              i + 1,
              qrcode.getAmount(),
              qrcode.getQrcodeId());
          
          ZipEntry zipEntry = new ZipEntry(basePath + fileName);
          zos.putNextEntry(zipEntry);
          
          // 实时生成二维码图片
          int size = miniProgramCode ? 430 : 300; // 小程序码建议430px
          BufferedImage qrcodeImage;
          if (miniProgramCode) {
            qrcodeImage = generateMiniProgramCodeImage(url, size, size);
          } else {
            qrcodeImage = generateQrcodeImage(url, size, size);
          }
          ImageIO.write(qrcodeImage, "PNG", zos);
          zos.closeEntry();
        }
        
        // 为每个批次添加批次信息文件
        addBatchInfoFile(batch, qrcodes, zos, basePath);
      }
      
      // 添加总的批次信息文件
      addBatchSummaryFile(batches, zos, folderName + "/");
    }
  }
  
  /**
   * 添加批次汇总信息文件到zip
   */
  private void addBatchSummaryFile(List<RedPacketBatch> batches, 
                                 ZipOutputStream zos, 
                                 String basePath) throws IOException {
    
    int totalBatches = batches.size();
    int totalQrcodes = 0;
    BigDecimal totalAmount = BigDecimal.ZERO;
    
    // 统计所有批次的信息
    for (RedPacketBatch batch : batches) {
      List<RedPacketQrcode> qrcodes = redPacketQrcodeService.lambdaQuery()
          .eq(RedPacketQrcode::getBatchId, batch.getBatchId())
          .list();
      
      totalQrcodes += qrcodes.size();
      BigDecimal batchAmount = qrcodes.stream()
          .map(RedPacketQrcode::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      totalAmount = totalAmount.add(batchAmount);
    }
    
    String summaryContent = String.format(
        "批量红包批次汇总信息\n" +
        "====================\n" +
        "下载时间: %s\n" +
        "总批次数量: %d个\n" +
        "总红包数量: %d个\n" +
        "总红包金额: %.2f元\n\n" +
        "批次详情:\n",
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
        totalBatches,
        totalQrcodes,
        totalAmount.doubleValue()
    );
    
    // 添加每个批次的详细信息
    for (RedPacketBatch batch : batches) {
      List<RedPacketQrcode> qrcodes = redPacketQrcodeService.lambdaQuery()
          .eq(RedPacketQrcode::getBatchId, batch.getBatchId())
          .list();
      
      BigDecimal batchAmount = qrcodes.stream()
          .map(RedPacketQrcode::getAmount)
          .reduce(BigDecimal.ZERO, BigDecimal::add);
      
      summaryContent += String.format(
          "\n批次 %s:\n" +
          "  红包数量: %d个\n" +
          "  总金额: %.2f元\n",
          batch.getBatchId(),
          qrcodes.size(),
          batchAmount.doubleValue()
      );
    }
    
    summaryContent += "\n文件结构:\n" +
        "  根目录/批次文件夹/二维码图片.png\n" +
        "  根目录/批次文件夹/批次说明.txt\n" +
        "  根目录/批次汇总.txt";
    
    ZipEntry summaryEntry = new ZipEntry(basePath + "批次汇总.txt");
    zos.putNextEntry(summaryEntry);
    zos.write(summaryContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    zos.closeEntry();
  }
}
