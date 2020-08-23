package com.thomax.shadow.refresh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Shadowsocks自动刷新可用配置
 *
 * @author thomax
 */
public class ShadowRefresh4Windows<T> {

	private static final int HTTP_TIMEOUT = 12000; //单个HTTP请求的连接超时时间（毫秒）

	private static final int PING_TOTAL = 5; //校验网速的时候每个IP PING几次

	private static final int PING_TIMEOUT = 15000 * PING_TOTAL; //Ping IP的时候的响应超时时间（毫秒）

	private static final int REQUEST_TOTAL = 20; //总请求次数

	private static final String USER_DIR = System.getProperty("user.dir");

	private static final Set<String> encryptionSet = new HashSet<>();

	static {
		encryptionSet.add("aes-256-gcm");
		encryptionSet.add("aes-192-gcm");
		encryptionSet.add("aes-128-gcm");
		encryptionSet.add("chacha20-ietf-poly1305");
		encryptionSet.add("xchacha20-ietf-poly1305");
		encryptionSet.add("rc4-md5");
		encryptionSet.add("salsa20");
		encryptionSet.add("chacha20");
		encryptionSet.add("bf-cfb");
		encryptionSet.add("chacha20-ietf");
		encryptionSet.add("aes-256-cfb");
		encryptionSet.add("aes-192-cfb");
		encryptionSet.add("aes-128-cfb");
		encryptionSet.add("aes-256-ctr");
		encryptionSet.add("aes-192-ctr");
		encryptionSet.add("aes-128-ctr");
		encryptionSet.add("camellia-256-cfb");
		encryptionSet.add("camellia-192-cfb");
		encryptionSet.add("camellia-128-cfb");
	}

	public static void main(String[] args) {
		try {
			handler();
		} catch (Exception e) {
			notice("操作过程发生异常:" + e.getMessage());
		}
	}

	private static void handler() throws Exception {
		String guiConfig = readFileContent(USER_DIR, "gui-config.json");
		JSONObject guiObject = JSON.parseObject(guiConfig);
		int localPort = (int) guiObject.get("localPort");

		//异步获得http结果
		ShadowRefresh4Windows<String> main = new ShadowRefresh4Windows<>();
		List<Callable<String>> taskList = new ArrayList<>();
		for (int i = 0; i < REQUEST_TOTAL; i++) {
			taskList.add(new Callable<String>() {
				@Override
				public String call() {
					return sendGet("http://ss.pythonic.life/full/json", localPort);
				}
			});
		}
		List<String> responseList = main.asyncExec(taskList, HTTP_TIMEOUT);
		if (responseList.size() == 0) {
			notice("没有爬到可用账号，请先保证电脑上的代理处于成功状态");
		}

		//剔除加密方式不支持的配置
		List<JSONObject> availableList = new ArrayList<>();
		for (String responseValue : responseList) {
			try {
				JSONObject responseObject = JSON.parseObject(responseValue);
				if (encryptionSet.contains(responseObject.get("method").toString())) { //校验加密方式是否支持
					availableList.add(responseObject);
				}
			} catch (JSONException e) {
				//
			}
		}

		//插入新的配置到原始配置中
		Set<String> ipSet = new HashSet<>();
		JSONArray configs = new JSONArray();
		for (JSONObject available : availableList) {
			if (!ipSet.contains(available.get("server").toString())) { //去重
				JSONObject newJsonObject = new JSONObject();
				newJsonObject.put("server", available.get("server"));
				newJsonObject.put("server_port", available.get("server_port"));
				newJsonObject.put("password", available.get("password"));
				newJsonObject.put("method", available.get("method"));
				newJsonObject.put("plugin", "");
				newJsonObject.put("plugin_opts", "");
				newJsonObject.put("plugin_args", "");
				newJsonObject.put("remarks", "");
				newJsonObject.put("timeout", 5);
				configs.add(newJsonObject);

				ipSet.add(available.get("server").toString());
			}
		}

		//configs = testPing(configs);

		//更新ip配置
		guiObject.put("configs", configs);
		//重置配置为高可用模式
		guiObject.put("strategy", "com.shadowsocks.strategy.ha");
		guiObject.put("index", -1);

		replaceFileContent(USER_DIR, "gui-config.json", guiObject.toJSONString());


		//重启Shadowsocks.exe
		execCMD("taskkill /f /t /im Shadowsocks.exe");
		startProgram(USER_DIR + File.separator + "Shadowsocks.exe");

		notice("操作结束，重新刷入" + configs.size() + "条配置！！");
	}

	private static JSONArray testPing(JSONArray configs) {
		//异步校验Ping的信息
		ShadowRefresh4Windows<JSONObject> main = new ShadowRefresh4Windows<>();
		List<Callable<JSONObject>> taskList = new ArrayList<>();
		Pattern pa = Pattern.compile("丢失 = [0-9]+ \\(");
		Pattern pa2 = Pattern.compile("平均 = [0-9]+ms");
		for (Object config : configs) {
			taskList.add(new Callable<JSONObject>() {
				@Override
				public JSONObject call() {
					JSONObject obj = (JSONObject) config;
					String info;
					Integer bestLost = null;
					Integer bestAvg = null;
					for (int i = 0; i < PING_TOTAL; i++) {
						try {
							info = execCMD(("ping " + obj.get("server").toString()));
						} catch (Exception e) {
							continue;
						}

						Integer lost = null;
						Matcher ma = pa.matcher(info);
						if (ma.find()) {
							String group = ma.group(0);
							lost = Integer.parseInt(group.substring(5, group.length() - 2));
						}

						Integer avg = null;
						Matcher ma2 = pa2.matcher(info);
						if (ma2.find()) {
							String group = ma2.group(0);
							avg = Integer.valueOf(group.substring(5, group.length() - 2));
							if (avg == 0) {
								continue; //操作系统原因，这种值不进行逻辑判断了
							}
						}

						if (lost == null || avg == null) {
							continue;
						}
						if (bestLost == null || bestLost > lost) {
							bestLost = lost;
						}
						if (bestAvg == null || bestAvg > avg) {
							bestAvg = avg;
						}
					}

					if (bestLost != null && bestAvg != null) {
						obj.put("lost", bestLost);
						obj.put("avg", bestAvg);
						return obj;
					}

					return null;
				}
			});
		}
		List<JSONObject> responseList = main.asyncExec(taskList, PING_TIMEOUT);
		if (responseList.size() == 0) {
			notice("经过爬数据的配置，以及校验本地配置，发现所有的配置都不可用，直接结束");
		}

		//网速排序
		Object[] arr = responseList.toArray();
		for (int x = 0; x < arr.length - 1; x++) {
			for (int y = 0; y < arr.length - x - 1; y++) {
				int lost = (int) ((JSONObject) arr[y]).get("lost");
				int nextLost = (int) ((JSONObject) arr[y + 1]).get("lost");
				int avg = (int) ((JSONObject) arr[y]).get("avg");
				int nextAvg = (int) ((JSONObject) arr[y + 1]).get("avg");

				if (lost > nextLost && avg > nextAvg) {
					Object tempArr = arr[y];
					arr[y] = arr[y + 1];
					arr[y + 1] = tempArr;
				}
			}
		}
		configs = new JSONArray();
		//移除冗余数据
		for (Object o : arr) {
			((JSONObject) o).remove("lost");
			((JSONObject) o).remove("avg");
			configs.add(o);
		}

		return configs;
	}

	private List<T> asyncExec(List<Callable<T>> taskList, int timeout) {
		//异步执行任务
		ExecutorService executorService = Executors.newFixedThreadPool(taskList.size());
		List<Future<T>> list = new CopyOnWriteArrayList<>();
		for (Callable<T> callable : taskList) {
			Future<T> future = executorService.submit(callable);
			list.add(future);
		}

		int threadSleep = 500; //ms

		//获得异步任务的结果
		int total = list.size();
		List<T> responseList = new ArrayList<>();
		for (int i = 0; true; i++) {
			for (Future<T> future : list) {
				if (future.isDone()) {
					try {
						T value = future.get();
						if (value != null) { //封装的异步任务结果通过是否为null来判断是否要汇总返回
							responseList.add(value);
						}

						list.remove(future);
						break;
					} catch (InterruptedException | ExecutionException e) {
						//
					}
				}
			}

			if (responseList.size() == total || i > (timeout / threadSleep)) {
				break;
			}

			try {
				Thread.sleep(threadSleep);
			} catch (InterruptedException e) {
				//
			}
		}
		executorService.shutdown();

		return responseList;
	}

	private static void notice(String content) {
		JOptionPane.showMessageDialog(null, content, "火の意志", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	private static String readFileContent(String filePath, String fileName) {
		File file = new File(filePath + File.separator + fileName);
		BufferedReader reader = null;
		StringBuilder sbf = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempStr;
			while ((tempStr = reader.readLine()) != null) {
				sbf.append(tempStr).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			notice("读Shadowsocks目录内的文件" + fileName + "时发生IO异常");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					notice("关闭Shadowsocks目录内文件" + fileName + "的IO时发生异常");
				}
			}
		}

		return sbf.toString();
	}

	private static void replaceFileContent(String filePath, String fileName, String content) {
		try {
			FileWriter fileWriter = new FileWriter(new File(filePath + File.separator + fileName));
			fileWriter.write(content);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			notice("在Shadowsocks目录操作文件" + fileName + "写入更新后的配置时发生IO异常");
		}
	}

	private static String sendGet(String url, int port) {
		StringBuilder result = new StringBuilder();
		BufferedReader in = null;
		try {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", port));
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection(proxy);

			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;ST1)");
			connection.setConnectTimeout(HTTP_TIMEOUT);
			connection.setReadTimeout(5000);

			connection.connect();
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				//
			}
		}

		return result.toString();
	}

	private static String execCMD(String command) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(command);
		InputStream is = p.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("GBK")));
		String line;
		StringBuilder response = new StringBuilder();
		while((line = reader.readLine())!= null){
			response.append(line).append("\n");
		}
		p.waitFor();
		is.close();
		reader.close();
		p.destroy();

		return response.toString();
	}

	public static void startProgram(String programPath) {
		try {
			Desktop.getDesktop().open(new File(programPath));
		} catch (Exception e) {
			notice("最新配置已更新，Shadowsocks.exe无法重启，请手工重启");
		}
	}

}



