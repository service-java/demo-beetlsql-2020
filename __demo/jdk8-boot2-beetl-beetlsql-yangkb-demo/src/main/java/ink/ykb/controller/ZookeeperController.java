package ink.ykb.controller;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ink.ykb.service.UserService;
import ink.ykb.util.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags= {"zk操作相关接口"})
@Controller
@RequestMapping("/zk")
public class ZookeeperController {

	@Autowired
	private CuratorFramework zkClient;
	@Autowired
	private UserService userService;
	
	
	@ApiOperation(value="zk测试")
	@GetMapping("/test")
	@ResponseBody
	public CommonResult test(
			@ApiParam(name = "type", value = "操作类型 0：读，1：写") @RequestParam(required = false) Integer type,
			@ApiParam(name = "dir", value = "节点名称") @RequestParam(required = false) String dir,
			@ApiParam(name = "data", value = "节点数据") @RequestParam(required = false) String data) throws Exception{
		
			String result = null;
			if(type == 0){
				byte[] bs = zkClient.getData().forPath("/head");
				result = new String(bs, "UTF-8");
			}else if(type == 1){
//				result = curator.create().forPath("/head",new byte[0]);
//				result = curator.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/head/child",new byte[0]);
				zkClient.setData().forPath("/head", new byte[0]);
			}
			
			return CommonResult.resultSuccess(result, 1);
		
	}
	@ApiOperation(value="zk分布式锁")
	@GetMapping("/lock")
	@ResponseBody
	public CommonResult lock(@ApiParam(name = "type") @RequestParam(required = true) String type){
		
			userService.makeOrderType(type);
			return CommonResult.resultSuccess("操作成功", 1);
	}
	@ApiOperation(value="zk分布式锁")
	@GetMapping("/lock2")
	@ResponseBody
	public CommonResult lock2(@ApiParam(name = "type") @RequestParam(required = true) String type) throws Exception{
		
			userService.makeOrderType2(type);
			return CommonResult.resultSuccess("操作成功hhhhh", 1);
	}
	
}
