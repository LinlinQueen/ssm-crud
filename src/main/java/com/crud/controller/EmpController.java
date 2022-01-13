package com.crud.controller;

import com.crud.bean.Employee;
import com.crud.bean.Msg;
import com.crud.service.EmployeeService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Yanx
 * @Create 2022-01-07-0:15
 */
@Controller
public class EmpController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 单个批量二合一
     * 批量删除：1-2-3
     * 单个删除：1
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public Msg deleteEmp(@PathVariable("id") String ids){
        //包含 - 就是批量删除
        if(ids.contains("-")){
            List<Integer> del_ids = new ArrayList<>();

            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String str_id : str_ids) {
                del_ids.add(Integer.parseInt(str_id));
            }
            employeeService.deleteBatch(del_ids);
        }else {//单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.delEmpById(id);
        }
        return Msg.success();
    }

    /**
     * 如果Ajax发送Ajax=put形式的请求
     * 封装的数据：Employee{empId=1, empName='null', gender='null', email='null', dId=null, dept=null}
     *
     * 问题：
     * 请求体重有数据：
     * Employee对象封装不上；
     *
     * 原因：
     * Tomcat
     *      1 将请求体中的数据，封装一个map
     *      2 request.getParameter("empName")就会重这个map中取值
     *      3 springMVC封装POJO的时候
     *              会把POJO中每个属性的值，request.getParameter("email");
     *
     *  ajax发送put请求引发的问题
     *          put请求，request.getParameter()拿不到数据
     *          Tomcat不会封装put请求数据为map
     *  web.xml中配置上FormContentFilter
     *  将请求体中的数据封装成一个map
     * 员工更新方法
     *
     * @param employee
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    public Msg updateEmp(Employee employee){
        System.out.println("更新的数据："+employee);
        int rows = employeeService.updateEmp(employee);
        System.out.println(rows);
        if(rows>0){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","更新失败");
        }

    }


    /**
     * 查询员工数据回显
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id){
        Employee emp = employeeService.getEmp(id);
        return Msg.success().add("emp",emp);
    }

    /**
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkuser")
    public Msg checkUser(@RequestParam("empName") String empName){
        // 判断用户名是否合法的表达式
        String regx = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5})";
        if(!empName.matches(regx)){
            return Msg.fail().add("va_msg","用户名可以是2-5位中文或者6-16位英文和数字的组合");
        }
        boolean b = employeeService.checkUser( empName);
        if(b){
            return Msg.success();
        }else {
            return Msg.fail().add("va_msg","用户名不可用");
        }
    }

    /**
     * 保存员工数据
     * 1 支持JSR303校验
     * 2 导入hibernate-validator
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    public Msg savaEmp(@Valid Employee employee , BindingResult result){
        if(result.hasErrors()){
            //校验失败，返回失败，在模态框中显示校验失败的错误信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError error : errors) {
                System.out.println("错误的字段名："+error.getField());
                System.out.println("错误信息："+error.getDefaultMessage());
                map.put(error.getField(),error.getDefaultMessage());
            }
            return Msg.fail().add("errorFields",map);
        }else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn){
        PageHelper.startPage(pn,5);
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果
        //封装了详细的分页信息，包括查询出来的数据
        PageInfo page = new PageInfo(emps,5);
        return Msg.success().add("pageInfo",page);
    }

    /**
     * 查询员工数据
     * @return
     */
    //@RequestMapping("/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model){
        PageHelper.startPage(pn,5);
        List<Employee> emps = employeeService.getAll();
        //使用pageInfo包装查询后的结果
        //封装了详细的分页信息，包括查询出来的数据
        PageInfo page = new PageInfo(emps,5);
        model.addAttribute("pageInfo",page);
        return "list";
    }

}
