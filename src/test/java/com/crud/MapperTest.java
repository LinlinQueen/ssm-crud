package com.crud;

import com.crud.bean.Employee;
import com.crud.dao.DepartmentMapper;
import com.crud.dao.EmployeeMapper;
import com.crud.service.EmployeeService;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.UUID;

/**
 * 测试dao层的工作
 * 推荐spring的项目使用spring的单元测试，可以自动注入我们需要的组件
 * 1 导入springTest模块
 * 2 @ContextConfiguration指定spring配置文件的位置
 * @Yanx
 * @Create 2022-01-06-23:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MapperTest {
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SqlSession sqlSession;
    /**
     * 测试DepartmentMapper
     */
    @Test
    public void testCRUD(){
//        ApplicationContext ioc =new ClassPathXmlApplicationContext("applicationContext.xml");
//        EmployeeMapper bean = ioc.getBean(EmployeeMapper.class);
//        System.out.println(bean);
//        List<Employee> employees = bean.selectByExampleWithDept(null);
//        for (Employee employee : employees) {
//            System.out.println(employee);
//        }

        //插入部门
//        Department department = new Department(null, "测试部");
//        departmentMapper.insertSelective(department);

        //生成员工数据，测试员工插入
        //employeeMapper.insertSelective(new Employee(null,"李四","M","lisi@qq.com",2));

        //批量插入多个员工
        //EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
        //List<Employee> employees = mapper.selectByExampleWithDept(null);
        EmployeeService employeeService = new EmployeeService();
        List<Employee> employees = employeeService.getAll();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
//        for (int i = 0; i < 1000; i++) {
//            String uuid = UUID.randomUUID().toString().substring(0, 5)+i;
//            mapper.insertSelective(new Employee(null,uuid,"M",uuid+"@qq.com",3));
//
//        }

    }
}
