package com.liangzhicheng.config.init;

import com.liangzhicheng.common.utils.PrintUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动初始化加载数据配置，存在多个初始化加载数据，创建多个类，使用@Order(value = ?)注解来区分先后顺序
 * @author liangzhicheng
 */
@Component
public class StartupRunner implements CommandLineRunner {

	@Override
	public void run(String ... args) throws Exception {
		PrintUtil.info("[CommandLineRunner的初始化] StartupRunner 执行开始 >>>>>>");
		//缓存初始化 init()

	}

}
