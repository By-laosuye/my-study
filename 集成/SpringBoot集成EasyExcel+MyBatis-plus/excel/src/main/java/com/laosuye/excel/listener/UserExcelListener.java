package com.laosuye.excel.service.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.laosuye.excel.entity.Student;
import com.laosuye.excel.entity.UserDTO;
import com.laosuye.excel.service.StudentService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 用户Excel监听器，负责处理Excel解析事件
 * @version 1.0
 * @date 2024/4/12 16:22
 */
@Component
@Slf4j
public class UserExcelListener extends AnalysisEventListener<UserDTO> {

    private final StudentService studentService;

    public UserExcelListener(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 每隔10000条处理下，然后清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;

    private static final int THREAD_COUNT = 10;

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);

    private final ThreadLocal<ArrayList<Student>> studentList = ThreadLocal.withInitial(ArrayList::new);

    private static final AtomicInteger count = new AtomicInteger(1);

    /**
     * 记录开始时间
     */
    private long startTime;

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        startTime = System.currentTimeMillis();
    }


    /**
     * 当读取到一行数据时调用此方法
     * @param userDTO
     * @param context
     */
    @SneakyThrows
    @Override
    public void invoke(UserDTO userDTO, AnalysisContext context) {
        Student student = new Student();
        student.setUserId(userDTO.getUserId());
        student.setUserName(userDTO.getUserName());
        student.setRealName(userDTO.getRealName());
        student.setSex(userDTO.getSex());
        student.setPhone(userDTO.getMobile());
        studentList.get().add(student);

        if (studentList.get().size() >= BATCH_COUNT) {
            saveData();
        }
    }

    /**
     * 保存数据到数据库
     */
    private void saveData() {
        if (!studentList.get().isEmpty()) {
            ArrayList<Student> students = (ArrayList<Student>)studentList.get().clone();
            executorService.execute(new saveTask(studentService,students));
            studentList.get().clear();
        }
    }

    /**
     * 数据解析完成后调用此方法
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        ArrayList<Student> students = (ArrayList<Student>)studentList.get().clone();
        // 收尾工作，处理剩下的缓存数据
        if (!studentList.get().isEmpty()){
            studentService.saveBatch(students,students.size());
            log.info("第{}次导出剩下数据，共插入{}条数据", count.getAndIncrement(), students.size());
        }
        executorService.shutdown();
    }

    /**
     * 解析过程中发生异常时调用此方法
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        log.error("解析异常：", exception);
        throw exception;
    }


    public class saveTask implements Runnable{

        private StudentService studentService;

        private List<Student> studentList;

        public saveTask(StudentService studentService, List<Student> studentList) {
            this.studentService = studentService;
            this.studentList = studentList;
        }

        @Override
        public void run() {
            studentService.saveBatch(studentList);
            log.info("第{}批数据插入完成，共插入{}条数据", count.getAndIncrement(), studentList.size());
        }
    }
}
