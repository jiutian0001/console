/*
 * package com.bxsbot.console.service;
 * 
 * import java.io.File; import java.io.IOException; import
 * java.net.ServerSocket; import java.time.Duration; import java.util.ArrayList;
 * import java.util.HashMap; import java.util.List; import java.util.Map;
 * 
 * import org.openqa.selenium.By; import org.openqa.selenium.Dimension; import
 * org.openqa.selenium.JavascriptExecutor; import org.openqa.selenium.WebDriver;
 * import org.openqa.selenium.WebElement; import
 * org.openqa.selenium.chrome.ChromeDriver; import
 * org.openqa.selenium.chrome.ChromeOptions; import
 * org.openqa.selenium.support.ui.ExpectedConditions; import
 * org.openqa.selenium.support.ui.WebDriverWait; import
 * org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service;
 * 
 * import com.bxsbot.console.mapper.IndexDataMapper; import
 * com.bxsbot.console.utils.ReflectionUtil;
 * 
 * 
 * //@Service public class AutoLmService {
 * 
 * @Autowired private IndexDataMapper mapper;
 * 
 * private static final String CHROME_PATH =
 * "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"; private static
 * final String CHROMEDRIVER_PATH =
 * "C:\\Users\\kk\\Downloads\\Win_1388899_chromedriver_win32\\chromedriver_win32\\chromedriver.exe";
 * 
 * static { System.setProperty("webdriver.chrome.driver", CHROMEDRIVER_PATH); }
 * 
 * public void autoFunc(Integer id) { //查询数据库 if(null!=id) { AutoLm autoConfig
 * =null;// mapper.getAutoConfig(id); if(null!=autoConfig) { File[]
 * fs=getSubFile(autoConfig.getFiles()); if(null!=fs) { //便利启动 for (File file :
 * fs) { //启动浏览器，获取步骤 try { //查询步骤 List<LmItem> lmItem =null;//
 * mapper.getLmItem(id); if(null!=lmItem) {
 * startBrowser(file.toString(),lmItem); }
 * 
 * } catch (Exception e) { e.printStackTrace(); } } } } } }
 * 
 * 
 *//***
	 * 启动浏览器
	 * 
	 * @param file
	 * @param id
	 * @throws IOException
	 * @throws InterruptedException
	 */
/*
 * private void startBrowser(String file, List<LmItem> lmItem) throws
 * IOException, InterruptedException { //configBrowser(); WebDriver driver =
 * null; Map<String,String> tmp=null; int debugPort = findAvailablePort(); // 启动
 * Chrome Process process = startChrome(file, debugPort); if (process == null ||
 * !waitForPort(debugPort, 6000)) { System.err.println("无法启动 Chrome 或连接到调试端口 " +
 * debugPort); if (process != null) process.destroyForcibly(); return; }
 * ChromeOptions options = new ChromeOptions();
 * options.setExperimentalOption("debuggerAddress", "127.0.0.1:" + debugPort);
 * options.addArguments("--disable-web-security");
 * options.addArguments("--allow-running-insecure-content");
 * options.addArguments("--disable-cache"); // 清理缓存，避免白板 try { driver = new
 * ChromeDriver(options); WebDriverWait wait = new WebDriverWait(driver,
 * Duration.ofSeconds(15)); JavascriptExecutor jsExecutor = (JavascriptExecutor)
 * driver; tmp=new HashMap<String, String>(); // 强制设置窗口大小
 * driver.manage().window().setSize(new Dimension(960, 1080));
 * System.out.println("已设置窗口大小为 960x1080"); //循环步骤，开始操作
 * dispatch(driver,wait,lmItem,file,tmp);
 * 
 * 
 * } catch (Exception e) { e.printStackTrace(); }finally { if (driver != null) {
 * driver.quit(); } if (process != null) { process.destroyForcibly();
 * process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS);
 * System.out.println("Chrome 进程已强制关闭"); } }
 * 
 * 
 * 
 * }
 * 
 * 
 *//***
	 * 调度逻辑
	 * 
	 * @param driver
	 * @param wait
	 * @param lmItem
	 * @param file
	 * @throws Exception
	 */
/*
 * private void dispatch(WebDriver driver, WebDriverWait wait, List<LmItem>
 * lmItem, String file,Map<String,String> tmp) throws Exception { String
 * text=null; Map<String,Object> map=null; for (LmItem it : lmItem) { switch
 * (it.getItemType()) { case 1: { //打开 driver.get(it.getItemVal()); break; }
 * case 2: { //暂停(S) Thread.sleep(Integer.parseInt(it.getItemVal())*1000);
 * break; } case 3: {
 * 
 * //按键 break; } case 4: { //Xpath WebElement el =
 * wait.until(ExpectedConditions.elementToBeClickable(By.xpath(it.getItemVal()))
 * ); if(it.getOperation()==1) { //点击 el.click(); }else if
 * (it.getOperation()==2) { //获取文本 text = el.getText(); tmp.put("data", text);
 * }else if (it.getOperation()==3) { //输入 if(1==it.getOperationType()) { //静态文本
 * text=it.getOpTypeFun(); }else { //需要动态获取 map=new HashMap<String, Object>();
 * map.put("p1", it); map.put("path", file); String[] split =
 * it.getOpTypeFun().split("-"); map.put("fileName", split[2]);
 * text=ReflectionUtil.invokeMethod(split[0]+"-"+split[1],map); }
 * el.sendKeys(text); } break; } case 5: { //获取数据保存本地 map=new HashMap<String,
 * Object>(); if(tmp.containsKey("data")) { //key 名称 map.put("data",
 * tmp.get("data")); map.put("fileName", it.getItemVal()); map.put("path",
 * file); ReflectionUtil.invokeMethod(it.getOpTypeFun(), map); } break; }
 * 
 * default: System.out.println("没有匹配！！"); }
 * 
 * } }
 * 
 * 
 * private File[] getSubFile(String files) { if(null==files ||
 * files.length()==0) { return null; } File parentFolder = new File(files);
 * File[] subFolders = parentFolder.listFiles(File::isDirectory);
 * 
 * if (subFolders == null) { System.out.println("父目录为空或不存在！"); return null; }
 * return subFolders; }
 *//***
	 * 动态分配调试端口
	 * 
	 * @return
	 * @throws IOException
	 */
/*
 * private static int findAvailablePort() throws IOException { try (ServerSocket
 * socket = new ServerSocket(0)) { return socket.getLocalPort(); } }
 *//***
	 * 等待端口可用
	 * 
	 * @param port
	 * @param timeoutMs
	 * @return
	 * @throws InterruptedException
	 */
/*
 * private static boolean waitForPort(int port, long timeoutMs) throws
 * InterruptedException { long startTime = System.currentTimeMillis(); while
 * (System.currentTimeMillis() - startTime < timeoutMs) { try (java.net.Socket
 * socket = new java.net.Socket("127.0.0.1", port)) { return true; } catch
 * (IOException e) { Thread.sleep(500); } } return false; }
 *//***
	 * 启动 Chrome
	 * 
	 * @param userDataDir
	 * @param debugPort
	 * @return
	 * @throws IOException
	 *//*
		 * private static Process startChrome(String userDataDir, int debugPort) throws
		 * IOException { List<String> command = new ArrayList<>();
		 * command.add(CHROME_PATH); command.add("--user-data-dir=" + userDataDir);
		 * command.add("--remote-debugging-port=" + debugPort);
		 * command.add("--no-first-run"); command.add("--disable-cache");
		 * 
		 * ProcessBuilder pb = new ProcessBuilder(command); return pb.start(); } }
		 */