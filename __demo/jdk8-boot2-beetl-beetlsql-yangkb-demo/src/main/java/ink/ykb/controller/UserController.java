package ink.ykb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.beetl.sql.core.SQLManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;

import ink.ykb.entity.User;
import ink.ykb.service.UserService;
import ink.ykb.util.CommonResult;
import ink.ykb.util.filterDto.FilterView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags= {"用户操作相关接口"})
@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SQLManager sqlManager;
	
	
	@ApiOperation(value="访问管理端主页", notes="注意需要登录")
	@GetMapping("/admin/home")
	public String home(){
		return "home";
	}
	
	@ApiOperation(value="获取用户信息", notes="")
	@PostMapping("/user/info")
	@ResponseBody
	public CommonResult userInfo(
			@ApiParam(value = "用户ID",required=true) @RequestParam Integer id){
			
			User user = userService.getUser(id);
			return CommonResult.resultSuccess(user, 1);
	}
	
	@ApiOperation(value="获取用户列表", notes="根据用户条件查询")
	@PostMapping("/user/list")
	@ResponseBody
	public CommonResult userList(
			@ApiParam(value = "姓名模糊查询") @RequestParam(required = false)String keyword){
			List<User> list = userService.getUserList(keyword);
			return CommonResult.resultSuccess(list, list.size());
	}
	
	@ApiOperation(value="新增用户", notes="")
	@PostMapping("/user/add")
	@ResponseBody
	public CommonResult addUser(
			@ApiParam(name = "user", value = "新增用户") @RequestParam User user){
			userService.addUser(user);
			return CommonResult.resultSuccess("新增成功");
	}
	
	@ApiOperation(value="更新用户", notes="ID不能为空")
	@PostMapping("/user/update")
	@ResponseBody
	public CommonResult updateUser(
			@ApiParam(name = "user", value = "更新用户") User user){
			userService.updateUser(user);
			return CommonResult.resultSuccess("修改成功");
	}
	
	@ApiOperation(value="删除用户", notes="ID不能为空")
	@PostMapping("/user/delete")
	@ResponseBody
	public CommonResult deleteUser(
			@ApiParam(name = "id", value = "用户ID") @RequestParam Integer id){
			userService.deleteUser(id);
			return CommonResult.resultSuccess("删除成功");
	}

	@PostMapping("/putsession")
	@ResponseBody
	public CommonResult putsession(HttpServletRequest request){
			HttpSession session = request.getSession();
			Map<String,Object> map = new HashMap<>();
			map.put("session-class", session.getClass());
			map.put("session-id", session.getId());
			map.put("session-CreationTime", session.getCreationTime());
			map.put("session-LastAccessedTime", session.getLastAccessedTime());
			map.put("session-MaxInactiveInterval", session.getMaxInactiveInterval());
			session.setAttribute("user", "闲大赋 xiandafu");
			return CommonResult.resultSuccess(map,map.size());
	}
	
	/**
	 * 添加ObjectMapper过滤测试
	 * @param id
	 * @return
	 */
	@PostMapping("/test")
	@ResponseBody
//	@JsonView(FilterView.OutputAutoMark.class)
	@JsonView(FilterView.OutputB.class)
//	@JsonView(FilterView.OutputA.class)
	public CommonResult test(Integer id){
		User user = sqlManager.single(User.class, id);
		return CommonResult.resultSuccess(user,1);
	}
}
