package com.laosuye.excel.listener;

import cn.hutool.core.util.IdUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.laosuye.excel.entity.UserAppRelation;
import com.laosuye.excel.entity.appRelationDTO;
import com.laosuye.excel.service.IUserAppRelationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2024/4/12 16:22
 */
@Component
@Slf4j
public class AppRelationExcelListener extends AnalysisEventListener<appRelationDTO> {

    private final IUserAppRelationService iUserAppRelationService;

    public AppRelationExcelListener(IUserAppRelationService userService){
        this.iUserAppRelationService = userService;
    }

    /**
     * 每隔100条处理下，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;


    /**
     * 缓存的数据
     */
    private List<UserAppRelation> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


    /**
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("======>>>解析异常：", exception);
        throw exception;
    }

    /**
     * 当读取到一行数据时，会调用这个方法，并将读取到的数据以及上下文信息作为参数传入
     * 可以在这个方法中对读取到的数据进行处理和操作，处理数据时要注意异常错误，保证读取数据的稳定性
     * @param relationDTO
     * @param context
     */
    @SneakyThrows
    @Override
    public void invoke(appRelationDTO relationDTO, AnalysisContext context) {
        UserAppRelation appRelation = new UserAppRelation();
        appRelation.setId(IdUtil.simpleUUID());
        appRelation.setUserId(relationDTO.getUserId());
        appRelation.setAppId("paas-demo");
        appRelation.setStatus("1");
        appRelation.setCompanyId(relationDTO.getComNo());
        appRelation.setOrgId(relationDTO.getOrgNo());
        appRelation.setCreateDate(new Date());

        cachedDataList.add(appRelation);
        if (cachedDataList.size() >= BATCH_COUNT) {
            log.info("开始保存数据");
            long start = System.currentTimeMillis();
            // 处理缓存的数据，比如说入库。。。
            iUserAppRelationService.saveBatch(cachedDataList);
            long end = System.currentTimeMillis();
            log.info("保存数据成功耗时：{}",(end-start)/1000);
            // 然后清空
            cachedDataList.clear();
        }
    }

    /**
     * 当每个sheet所有数据读取完毕后，会调用这个方法，可以在这个方法中进行一些收尾工作，如资源释放、数据汇总等。
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 收尾工作，处理剩下的缓存数据。。。
        log.info("开始处理剩下的数据");
        if (!cachedDataList.isEmpty()) {
            iUserAppRelationService.saveBatch(cachedDataList);
        }
        log.info("sheet={} 所有数据解析完成！", context.readSheetHolder().getSheetName());
    }

}

