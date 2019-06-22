package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 邓鹏涛
 * @date 2019/2/9 15:01
 */
@RestController
@RequestMapping("/brand")
public class BrandController  {

    // @Reference 引用 dubbo 所扫描的服务.
    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌.
     * @return
     */
    @RequestMapping("/all")
    public List<TbBrand> findAll() {
        return  brandService.findAll();
    }

    /**
     * 分页查询品牌.
     * @param page 当前页
     * @param size 每页显示数量
     * @return
     */
    @RequestMapping("/page")
    public PageResult findPage(int page, int size) {
        return brandService.findPage(page, size);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        // 捕获可能出现的异常
        try {
            brandService.add(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            return new Result(false,"添加失败，提示信息：" + e.getMessage());
        }
    }

    @RequestMapping("/findOne")
    public TbBrand findOne(long id) {
       return brandService.findOne(id);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        // 捕获可能出现的异常
        try {
            brandService.update(brand);
            return new Result(true,"更新成功");
        } catch (Exception e) {
            return new Result(false,"更新失败，提示信息：" + e.getMessage());
        }
    }

    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            if (ids.length > 1) {
                brandService.deleteBatch(ids);
            } else {
                brandService.delete(ids[0]);
            }
            return new Result(true, "删除成功");
        } catch (Exception e) {
            return new Result(false, "删除失败");
        }
    }

    /**
     * 分页条件查询品牌.
     * @param page 当前页
     * @param size 每页显示数量
     * @return
     */
    @RequestMapping("/search")
    public PageResult findPage(@RequestBody TbBrand brand, int page, int size) {
        return brandService.findPage(brand, page, size);
    }

    /**
     * 前端品牌下拉款需要数据.
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList() {
        return brandService.selectOptionList();
    }
}
