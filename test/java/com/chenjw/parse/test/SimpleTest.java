package com.chenjw.parse.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.chenjw.parse.ParseEngine;
import com.chenjw.parse.core.Context;
import com.chenjw.parse.spi.Filter;
import com.chenjw.parse.spi.Tool;

public class SimpleTest {

	private ParseEngine parseEngine = new ParseEngine(
	// 脚本配置
			new HashMap<String, Object>() {
				{
					this.put("field1", new HashMap<String, Object>() {
						{

							this.put("selector", "/html/body/header/div[4]/div/div/nav/ul/li[2]/a/span");
							this.put("filter", "add1");

						}

					});
					this.put("field2", new HashMap<String, Object>() {
						{

							this.put("selector", "/html/body/header/div[4]/div/div/nav/ul/li[3]/a/span");
							this.put("filter", "add1");

						}

					});

				}

			},
			// 函数配置（可以全局共用）
			new HashMap<String, Tool>() {
				{
					this.put("add1", new Filter() {

						@Override
						public String doFilter(String input, Context context) {
							return input + "1";
						}

					});
				}

			});

	private String loadPage() throws Exception {
		String url = "http://hz.meituan.com/";

		return IOUtils.toString(new URL(url).openStream(),"UTF-8");
	}

	public void doParse() throws Exception {
		String html = loadPage();
		System.out.println("html:" + html);
		long start=System.currentTimeMillis();
		for(int i=0;i<100;i++){
			Map<String, Object> result = parseEngine.parse(html);
			System.out.println("result:" + result);
		}
		System.out.println("cost:"+(System.currentTimeMillis()-start)/100+"ms");
		
	}

	public static void main(String[] args) throws Exception {
		SimpleTest t = new SimpleTest();
		t.doParse();
	}

}
