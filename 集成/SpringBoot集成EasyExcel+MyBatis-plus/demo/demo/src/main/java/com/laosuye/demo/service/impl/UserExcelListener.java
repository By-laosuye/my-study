package com.laosuye.demo.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;

import com.laosuye.demo.entity.User;
import com.laosuye.demo.entity.UserDTO;
import com.laosuye.demo.service.IUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author fjzheng
 * @version 1.0
 * @date 2024/4/12 16:22
 */
@Component
@Slf4j
public class UserExcelListener extends AnalysisEventListener<UserDTO> {

    private final IUserService userService;

    public UserExcelListener(IUserService userService){
        this.userService = userService;
    }

    /**
     * 每隔100条处理下，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;


    /**
     * 缓存的数据
     */
    private List<User> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);


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
     * @param userDTO
     * @param context
     */
    @SneakyThrows
    @Override
    public void invoke(UserDTO userDTO, AnalysisContext context) {
        User user = new User();
        user.setId(userDTO.getUserId());
        user.setAppId("paas-demo");
        user.setUserName(userDTO.getRealName());
        user.setAccount(userDTO.getUserName());
        user.setGender(userDTO.getSex());
        user.setPhone(userDTO.getMobile());
        user.setPassword("201bc031599f1630f465adb79b5097aad4f2748cf4d437a7b386b0208f537f5f");
        user.setCreateDate(new Date());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setPasswordExpiryTime(formatter.parse("2024-12-31 19:21:13"));
        user.setStatus("1");
        user.setAuthenticationType("2");
        cachedDataList.add(user);
        if (cachedDataList.size() >= BATCH_COUNT) {
            log.info("开始保存数据");
            long start = System.currentTimeMillis();
            // 处理缓存的数据，比如说入库。。。
            userService.saveBatch(cachedDataList);
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
            userService.saveBatch(cachedDataList);
        }
        log.info("sheet={} 所有数据解析完成！", context.readSheetHolder().getSheetName());
    }

}

