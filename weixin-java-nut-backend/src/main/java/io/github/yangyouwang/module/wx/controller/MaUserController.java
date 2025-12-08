package io.github.yangyouwang.module.wx.controller;

import io.github.yangyouwang.module.wx.entity.MaUser;
import io.github.yangyouwang.module.wx.service.MaUserService;
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
* 微信小程序用户表 前端控制器
* </p>
* @author paul
* @since 2025-08-05
*/
@Api(tags = "微信小程序用户表")
@Controller
@RequestMapping("/wx/maUser")
@PassToken
public class MaUserController extends BaseController {

  private static final String SUFFIX = "wx/maUser";

  @Autowired
  private MaUserService maUserService;

  @GetMapping("/listPage")
  public String listPage(){
    return SUFFIX + "/list";
  }

  @ApiOperation(value = "微信小程序用户表分页列表", response = MaUser.class)
  @ApiImplicitParams({
    @ApiImplicitParam(name = "page", value = "第几页", dataType = "Integer", paramType="query"),
    @ApiImplicitParam(name = "limit", value = "每页记录数", dataType = "Integer", paramType="query")
  })
  @PreAuthorize("hasAuthority('maUser:list')")
  @GetMapping(value = "/page")
  @ResponseBody
  public Result page(MaUser param) {
    startPage();
    List<MaUser> data = maUserService.page(param);
    return Result.success(getDataTable(data));
  }

   @GetMapping("/editPage/{id}")
   public String editPage(@Valid @NotNull(message = "id不能为空") @PathVariable Long id, ModelMap map){
    Object data = maUserService.info(id);
    map.put("maUser",data);
    return SUFFIX + "/edit";
  }

  @GetMapping("/addPage")
  public String addPage(){
    return SUFFIX + "/add";
  }

  @CrudLog(title = "微信小程序用户表新增",businessType = BusinessType.INSERT)
  @ApiOperation(value = "微信小程序用户表新增")
  @PreAuthorize("hasAuthority('maUser:add')")
  @PostMapping(value = "/add")
  @ResponseBody
  public Result add(@RequestBody @Validated MaUser param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    maUserService.add(param);
    return Result.success();
  }

  @CrudLog(title = "微信小程序用户表修改",businessType = BusinessType.UPDATE)
  @ApiOperation(value = "微信小程序用户表修改")
  @PreAuthorize("hasAuthority('maUser:edit')")
  @PostMapping(value = "/modify")
  @ResponseBody
  public Result modify(@RequestBody @Validated MaUser param,BindingResult bindingResult) {
    if (bindingResult.hasErrors()){
        return Result.failure(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
    }
    maUserService.modify(param);
    return Result.success();
  }

  @CrudLog(title = "微信小程序用户表删除(单个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "微信小程序用户表删除(单个条目)")
  @PreAuthorize("hasAuthority('maUser:del')")
  @DeleteMapping(value = "/remove/{id}")
  @ResponseBody
  public Result remove(@Valid @NotNull(message = "id不能为空") @PathVariable Long id) {
    maUserService.remove(id);
    return Result.success();
  }

  @CrudLog(title = "微信小程序用户表删除(多个条目)",businessType = BusinessType.DELETE)
  @ApiOperation(value = "微信小程序用户表删除(多个条目)")
  @PreAuthorize("hasAuthority('maUser:del')")
  @PostMapping(value = "/removes")
  @ResponseBody
  public Result removes(@RequestBody @Valid List<Long> ids) {
     maUserService.removes(ids);
     return Result.success();
   }
}
