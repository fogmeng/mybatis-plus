/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.baomidou.mybatisplus.test;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.baomidou.mybatisplus.MybatisSessionFactoryBuilder;

/**
 * <p>
 * MybatisPlus 测试类
 * </p>
 * http://git.oschina.net/jrl/mybatis-mapper
 * 
 * @author hubin
 * @Date 2016-01-23
 */
public class UserMapperTest {
	private static final String RESOURCE = "mybatis.xml";

	/**
	 * RUN 测试
	 */
	public static void main(String[] args) {
		InputStream in = UserMapperTest.class.getClassLoader().getResourceAsStream(RESOURCE);

		/*
		 * 此处采用 MybatisSessionFactoryBuilder 构建
		 * SqlSessionFactory，目的是引入AutoMapper功能
		 */
		SqlSessionFactory sessionFactory = new MybatisSessionFactoryBuilder().build(in);
		SqlSession session = sessionFactory.openSession();
		UserMapper userMapper = session.getMapper(UserMapper.class);
		
		int result = userMapper.deleteByName("test");
		System.out.println("\n------------------deleteByName----------------------\n result=" + result);
		
		userMapper.insert(new User("test", 18));
		System.out.println("\n------------------insert----------------------\n name=test, age=18");

		/*
		 * 此处的 selectById 被UserMapper.xml中的 selectById 覆盖了
		 */
		System.err.println("\n------------------selectById----------------------");
		User user = userMapper.selectById(2L);
		print(user);


		/*
		 * updateById 是从 AutoMapper 中继承而来的，UserMapper.xml中并没有申明改sql
		 */
		System.err.println("\n------------------updateById----------------------");
		user.setName("MybatisPlus_" + System.currentTimeMillis());
		userMapper.updateById(user);
		/*
		 * 此处的 selectById 被UserMapper.xml中的 selectById 覆盖了
		 */
		user = userMapper.selectById(user.getId());
		print(user);

		System.err.println("\n------------------selectAll----------------------");
		List<User> userList = userMapper.selectAll();
		for (int i = 0; i < userList.size(); i++) {
			print(userList.get(i));
		}
		// 提交
		session.commit();
	}

	/*
	 * 打印测试信息
	 */
	private static void print(User user) {
		if (user != null) {
			System.out.println("\n user: id=" + user.getId() + ", name=" + user.getName() + ", age=" + user.getAge());
		} else {
			System.out.println("\n user is null.");
		}
	}
}
