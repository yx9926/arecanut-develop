package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.module.wx.entity.WithdrawRecord;
import io.github.yangyouwang.module.wx.service.WithdrawRecordService;
import io.github.yangyouwang.common.base.domain.Result;
import io.github.yangyouwang.common.base.domain.TableDataInfo;
import io.github.yangyouwang.common.annotation.CrudLog;
import io.github.yangyouwang.common.annotation.PassToken;
import io.github.yangyouwang.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;

import org.springframework.stereotype.Controller;
import io.github.yangyouwang.common.base.controller.BaseController;

import javax.validation.Valid;
import java.util.Objects;
import java.util.List;
/**
* <p>
* 微信用户提现记录表 前端控制器
* </p>
* @author paul
* @since 2025-08-05
*/
@Api(tags = "微信用户提现记录表")
@Controller
@RequestMapping("/wx/withdrawRecord")
@PassToken
public class WithdrawRecordController extends BaseController {

  private static final String SUFFIX = "wx/withdrawRecord";

  @Autowired
  private WithdrawRecordService withdrawRecordService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "微信用户提现记录表分页列表", response = WithdrawRecord.class)
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
    @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('withdrawRecord:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public Result page(WithdrawRecord param) {
    startPage();
    List<WithdrawRecord> data = withdrawRecordService.page(param);
    return Result.success(getDataTable(data));
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = withdrawRecordService.info(id);
    map.put("withdrawRecord",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }

  @CrudLog(title = "微信用户提现记录表新增",businessType = BusinessType.INSERT)
  @ApiOperation(value = "微信用户提现记录表新增")
  @PreAuthorize("hasAuthority('withdrawRecord:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated WithdrawRecord param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    withdrawRecordService.add(param);
    return Result.success();
  }

  @CrudLog(title = "微信用户提现记录表修改",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "微信用户提现记录表修改")
  @PreAuthorize("hasAuthority('withdrawRecord:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated WithdrawRecord param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    withdrawRecordService.modify(param);
    return Result.success();
  }

  @CrudLog(title = "微信用户提现记录表删除(单个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "微信用户提现记录表删除(单个条目)")
  @PreAuthorize("hasAuthority('withdrawRecord:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    withdrawRecordService.remove(id);
    return Result.success();
  }

  @CrudLog(title = "微信用户提现记录表删除(多个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "微信用户提现记录表删除(多个条目)")
  @PreAuthorize("hasAuthority('withdrawRecord:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     withdrawRecordService.removes(ids);
     return Result.success();
   }
}
