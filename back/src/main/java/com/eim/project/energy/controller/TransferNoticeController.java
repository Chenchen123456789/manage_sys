package com.eim.project.energy.controller;

import com.eim.common.utils.SecurityUtils;
import com.eim.common.utils.poi.ExcelUtil;
import com.eim.framework.aspectj.lang.annotation.Log;
import com.eim.framework.aspectj.lang.enums.BusinessType;
import com.eim.framework.web.controller.BaseController;
import com.eim.framework.web.domain.AjaxResult;
import com.eim.framework.web.page.TableDataInfo;
import com.eim.project.energy.entity.PlantTransferNotice;
import com.eim.project.energy.entity.TransferNotice;
import com.eim.project.energy.service.ReportService;
import com.eim.project.energy.service.TransferNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: chenchen
 * @Date: 2020/7/17
 */
@RestController
@RequestMapping("/energy/transferNotice")
public class TransferNoticeController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(TransferNoticeController.class);
    @Autowired
    private TransferNoticeService transferNoticeService;

    @Autowired
    private ReportService reportService;

    @PreAuthorize("@ss.hasPermi('energy:transferNotice:list')")
    @PostMapping("/list")
    public TableDataInfo selectTransferNoticeList(@RequestBody Map<String, Object> map) {
//        List<TransferNotice> transferNoticeList = transferNoticeService.selectTransferNoticeList(map);
//        if (transferNoticeList == null || transferNoticeList.size() == 0) {
        List<Map<String, Object>> settlementList = reportService.selectMonthSettlementByRangeTime(map);
        List<Map<String, Object>> FPGList = reportService.selectElectricityOfFPGValueByRangeTime(map);
        List resList = new ArrayList();
        Map itemMap = new HashMap<String, Object>();
        itemMap.put("settlementList", settlementList);
        itemMap.put("FPGList", FPGList);
        resList.add(itemMap);
        return getDataTable(resList);
//        } else {
//            return getDataTable(transferNoticeList);
//        }
    }

    @Log(title = "费用转账通知单管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:transferNotice:export')")
    @PostMapping("/export")
    public AjaxResult export(@RequestBody Map<String, Object> map) {
        List<TransferNotice> list = transferNoticeService.selectTransferNoticeList(map);
        ExcelUtil<TransferNotice> util = new ExcelUtil<TransferNotice>(TransferNotice.class);
        return util.exportExcel(list, "费用转账通知单数据");
    }

    /**
     * 新增费用转账通知单
     */
    @PreAuthorize("@ss.hasPermi('energy:transferNotice:add')")
    @Log(title = "费用转账通知单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TransferNotice[] transferNoticeArray) {
        if (transferNoticeArray != null && transferNoticeArray.length > 0) {
            TransferNotice transferNotice = transferNoticeArray[0];
            transferNoticeService.deleteByBuildingIdAndDataTime(transferNotice.getBuildingId(), transferNotice.getDataTime());
            return toAjax(transferNoticeService.insertTransferNotice(transferNoticeArray));
        } else {
            return toAjax(0);
        }
    }

    /**
     * 修改费用转账通知单
     */
    @PreAuthorize("@ss.hasPermi('energy:transferNotice:edit')")
    @Log(title = "费用转账通知单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody TransferNotice transferNotice) {
        return toAjax(transferNoticeService.updateTransferNotice(transferNotice));
    }

    @PreAuthorize("@ss.hasPermi('energy:plantTransferNotice:list')")
    @PostMapping("/plant/list")
    public TableDataInfo selectPlantTransferNoticeList(@RequestBody Map<String, Object> map) {
//        List<PlantTransferNotice> plantTransferNoticeList = transferNoticeService.selectPlantTransferNoticeList(map);
//        if (plantTransferNoticeList == null || plantTransferNoticeList.size() == 0) {
        List settlementList = reportService.selectMonthSettlement(map);
        List FPGList = reportService.selectElectricityOfFPGValue(map);
        List resList = new ArrayList();
        Map itemMap = new HashMap<String, Object>();
        itemMap.put("settlementList", settlementList);
        itemMap.put("FPGList", FPGList);
        resList.add(itemMap);
        return getDataTable(resList);
//        } else {
//            return getDataTable(plantTransferNoticeList);
//        }
    }

    @PreAuthorize("@ss.hasPermi('energy:plantTransferNotice:list')")
    @PostMapping("/plant/list/history")
    public TableDataInfo selectPlantTransferNoticeListHistory(@RequestBody Map<String, Object> map) {
        List<PlantTransferNotice> plantTransferNoticeList = transferNoticeService.selectPlantTransferNoticeList(map);
        return getDataTable(plantTransferNoticeList);
    }

    /**
     * 新增费用转账通知单
     */
    @PreAuthorize("@ss.hasPermi('energy:plantTransferNotice:add')")
    @Log(title = "费用转账通知单管理", businessType = BusinessType.INSERT)
    @PostMapping("/plant")
    public AjaxResult add(@RequestBody PlantTransferNotice[] plantTransferNotices) {
        if (plantTransferNotices != null && plantTransferNotices.length > 0) {
            PlantTransferNotice plantTransferNotice = plantTransferNotices[0];
            transferNoticeService.deleteByPlantIdAndDataTime(plantTransferNotice.getPlantId(), plantTransferNotice.getDataTime());
            return toAjax(transferNoticeService.insertPlantTransferNotice(plantTransferNotices));
        } else {
            return toAjax(0);
        }
    }

    @Log(title = "费用转账通知单管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('energy:plantTransferNotice:export')")
    @PostMapping("/plant/export")
    public AjaxResult exportPlantTN(@RequestBody Map<String, Object> map) {
        List<PlantTransferNotice> list = transferNoticeService.selectPlantTransferNoticeList(map);
        ExcelUtil<PlantTransferNotice> util = new ExcelUtil<>(PlantTransferNotice.class);
        return util.exportExcel(list, "费用转账通知单数据");
    }

}
