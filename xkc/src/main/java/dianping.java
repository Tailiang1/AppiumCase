import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dianping {
    private AndroidDriver<WebElement> driver;
    SimpleDateFormat timer = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String datatime = timer.format(new Date());
    @BeforeSuite()
    public void setUp() throws Exception
    {
        File app = new File("/Users/Time/Desktop/git_code/AppiumCase/xkc/lib/dianping.apk");//appium设置apk路径
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName","127.0.0.1:62001");
        cap.setCapability("app", app.getAbsolutePath());
        cap.setCapability("appPackage", "com.dianping.v1");
        cap.setCapability("noReset","True");
        cap.setCapability("appActivity", "com.dianping.main.guide.LaunchActivity");
        cap.setCapability("unicodeKeyboard","True");//支持中文输入
        cap.setCapability("resetKeyboard","True");
        driver = new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), cap);

    }
    public void hua() {
        Dimension size = driver.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        new TouchAction(driver)
                .longPress(PointOption.point(width / 2, height - 100))
                .moveTo(PointOption.point(width / 2, 100)).release().perform();

    }

    @Test(testName="sousuo")
    public void sousuo() throws Exception {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.findElement(By.id("com.oppo.market:id/tv_recommend_key")).click();

        //请求关键词接口
        String oaRestfulServiceUrl = null;
        oaRestfulServiceUrl =  "http://test-amda-api.xianjincard.com/v1/collect/data/get-main-info";
        RequestConfig requestConfig =
                RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse h_response = null;
        try {
            httpclient =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpGet postMethod = new HttpGet(oaRestfulServiceUrl);
//			postMethod.setHeader("User-Agent",
//					"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
            postMethod.setHeader("appkey", "test_key");
            postMethod.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
            postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
            postMethod.setHeader("timestamp", "1528376502");
            h_response = httpclient.execute(postMethod);
            HttpEntity repEntity = h_response.getEntity();
            String content = EntityUtils.toString(repEntity, "utf-8");
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(content);

            try {
                JsonElement keyword2=jsonObject.get("data").getAsJsonObject().get("keywords").
                        getAsJsonArray().get(0).getAsJsonObject().get("keyword");

                List<String> list = new ArrayList<String>();
                String str1=keyword2.toString();
                String str2 = str1.replace("\"", "");
                list.add(str2);
                Object[] addarr =list.toArray();
                int Compte=0;
                String value=(String) addarr[0];
                driver.findElement(By.id("et_search")).sendKeys(str2);
                Thread.sleep(500);
                Document doc1 = Jsoup.parse(driver.getPageSource());
                //获取应用列表产品名称
                Pattern p1 = Pattern.compile("textview index=\"0\" text=\"(.*?)\" class=\"android.widget.TextView\" (.*?) resource-id=\"com.oppo.market:id/tv_name\"" );

                Matcher matcher1 = p1.matcher((CharSequence) doc1.toString());
                String ww = "";
                while(matcher1.find())
                {
                    //获取直达位产品名
                    System.out.println(matcher1.group(1));
                    JSONObject obj = new JSONObject();
                    obj.put("position", "0");
                    obj.put("type", "2");
                    obj.put("app_title", matcher1.group(1).toString());
                    //JSon连接
                    ww += obj.toString()+",";
                    System.out.println(obj.toString());
                }
                //请求数据上报接口
                String oaRestfulServiceUrl2 =  "http://test-amda-api.xianjincard.com/v1/collect/data/upload-data";
                RequestConfig requestConfig2 =
                        RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
                CloseableHttpClient httpclient2 = null;
                CloseableHttpResponse h_response2 = null;
                try {
                    httpclient2 =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig2).build();
                    HttpPost postMethod2 = new HttpPost(oaRestfulServiceUrl2);
                    postMethod2.setHeader("appkey", "test_key");
                    postMethod2.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
                    //	postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
                    postMethod2.setHeader("timestamp", "1528376502");
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("platform",  "200"));
                    params.add(new BasicNameValuePair("keyword", value));
                    params.add(new BasicNameValuePair("is_install_app", "2"));
                    params.add(new BasicNameValuePair("datetime", datatime));
                    System.out.println(ww);
                    ww= ww.substring(0,ww.length()-1);
                    params.add(new BasicNameValuePair("data_items","["+ww.toString()+"]"));
                    postMethod2.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    h_response2 = httpclient2.execute(postMethod2);
                    System.out.println(h_response2);
                    HttpEntity repEntity2 = h_response2.getEntity();
                    String content2 = EntityUtils.toString(repEntity2, "utf-8");
                    //System.out.println(content2);

                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e);
                }
                driver.findElement(By.id("com.oppo.market:id/tv_search_text")).click();
                Thread.sleep(500);
                HashSet<String> set = new HashSet<String>();//存hash 防止列表数据重复
                for(int i=0;i<=11;i++) {
                    String txt=driver.getPageSource();
                    Document doc = Jsoup.parse(txt);
                    Pattern p = Pattern.compile("textview index=\"0\" text=\"(.*?)\" class=\"android.widget.TextView\" (.*?) resource-id=\"com.oppo.market:id/tv_name\"" );
                    Matcher matcher = p.matcher((CharSequence) doc.toString());
                    String kk="";
                    while(matcher.find())
                    {
                        //获取直达位产品名称
                        if(!matcher.group(1).equals("安装")) {
                            if(!matcher.group(1).equals("搜索")) {
                                if(!matcher.group(1).equals("")) {
                                    String str3 = matcher.group(1);
                                    if(set.contains(str3)) {
                                        continue;
                                    }
                                    System.out.println(matcher.group(1));
                                    Compte++;
                                    set.add(str3);
                                    String s1=""+Compte+"";
                                    JSONObject obj2 = new JSONObject();
                                    obj2.put("position", s1);
                                    obj2.put("type", "1");
                                    obj2.put("app_title", matcher.group(1).toString());
                                    kk += obj2.toString()+",";
                                    System.out.println(obj2.toString());//json拼接
                                }
                                System.out.print("搜索关键字:"+value+"\n");
                                System.out.print("排在第:"+Compte+"位"+"\n"+"\n");
                            }
                        }

                    }
                    //请求数据上报接口
                    String oaRestfulServiceUrl3 =  "http://test-amda-api.xianjincard.com/v1/collect/data/upload-data";
                    RequestConfig requestConfig3 =
                            RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
                    CloseableHttpClient httpclient3 = null;
                    CloseableHttpResponse h_response3 = null;
                    try {
                        httpclient3 =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig3).build();
                        HttpPost postMethod3 = new HttpPost(oaRestfulServiceUrl3);
                        postMethod3.setHeader("appkey", "test_key");
                        postMethod3.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
                        //	postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
                        postMethod3.setHeader("timestamp", "1528376502");
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("platform",  "2"));
                        params.add(new BasicNameValuePair("keyword", value));
                        params.add(new BasicNameValuePair("is_install_app", "2"));
                        params.add(new BasicNameValuePair("datetime", datatime));
                        System.out.println(kk);
                        kk= kk.substring(0,kk.length()-1);
                        params.add(new BasicNameValuePair("data_items","["+kk.toString()+"]"));
                        postMethod3.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                        h_response3 = httpclient3.execute(postMethod3);
                        System.out.println(h_response3);
                        HttpEntity repEntity3 = h_response3.getEntity();
                        String content3 = EntityUtils.toString(repEntity3, "utf-8");
                        //System.out.println(content3);

                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println(e);
                    }
                    if(Compte>=100)    {
                        break;
                    }   else {
                        hua();

                    }
                }

            } catch (Exception e) {
                System.out.println("end");

            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
    @Test()
    public void xh() throws Exception{
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        String oaRestfulServiceUrl = null;
        oaRestfulServiceUrl =  "http://test-amda-api.xianjincard.com/v1/collect/data/get-main-info";
        RequestConfig requestConfig =
                RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
        CloseableHttpClient httpclient = null;

        CloseableHttpResponse h_response = null;

        try {
            httpclient =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpGet postMethod = new HttpGet(oaRestfulServiceUrl);

            postMethod.setHeader("appkey", "test_key");
            postMethod.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
            //	postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
            postMethod.setHeader("timestamp", "1528376502");
            h_response = httpclient.execute(postMethod);
            HttpEntity repEntity = h_response.getEntity();

            String content = EntityUtils.toString(repEntity, "utf-8");

            JsonObject jsonObject = (JsonObject) new JsonParser().parse(content);

            for(int j=1;j<=24;j++) {
                try {
                    Thread.sleep(2000);
                    driver.findElement(By.id("com.oppo.market:id/search_clear")).click();
                    JsonElement keyword2=jsonObject.get("data").getAsJsonObject().get("keywords").
                            getAsJsonArray().get(j).getAsJsonObject().get("keyword");

                    List<String> list = new ArrayList<String>();
                    String str1=keyword2.toString();
                    String str2 = str1.replace("\"", "");//去除字符串双引号
                    list.add(str2);
                    int Compte=0;
                    driver.findElement(By.id("et_search")).sendKeys(str2);
                    Thread.sleep(500);
                    Document doc1 = Jsoup.parse(driver.getPageSource());
                    Pattern p1 = Pattern.compile("textview index=\"0\" text=\"(.*?)\" class=\"android.widget.TextView\" (.*?) resource-id=\"com.oppo.market:id/tv_name\"" );

                    Matcher matcher1 = p1.matcher((CharSequence) doc1.toString());
                    String ww="";
                    while(matcher1.find())
                    {
                        System.out.println(matcher1.group(1));
                        JSONObject obj = new JSONObject();
                        obj.put("position", "0");
                        obj.put("type", "2");
                        obj.put("app_title", matcher1.group(1).toString());
                        ww += obj.toString()+",";
                        System.out.println(obj.toString());

                    }
                    String oaRestfulServiceUrl2 =  "http://test-amda-api.xianjincard.com/v1/collect/data/upload-data";
                    RequestConfig requestConfig2 =
                            RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
                    CloseableHttpClient httpclient2 = null;
                    CloseableHttpResponse h_response2 = null;
                    try {
                        httpclient2 =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig2).build();
                        HttpPost postMethod2 = new HttpPost(oaRestfulServiceUrl2);
                        postMethod2.setHeader("appkey", "test_key");
                        postMethod2.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
                        //	postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
                        postMethod2.setHeader("timestamp", "1528376502");
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("platform",  "2"));
                        params.add(new BasicNameValuePair("keyword", str2));
                        params.add(new BasicNameValuePair("is_install_app", "2"));

                        params.add(new BasicNameValuePair("datetime", datatime));
                        System.out.println(ww);
                        ww= ww.substring(0,ww.length()-1);
                        params.add(new BasicNameValuePair("data_items","["+ww.toString()+"]"));
                        postMethod2.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                        h_response2 = httpclient2.execute(postMethod2);
                        System.out.println(h_response2);
                        //HttpEntity repEntity2 = h_response2.getEntity();
                        //String content2 = EntityUtils.toString(repEntity2, "utf-8");
                        //System.out.println(content2);

                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println(e);
                    }
                    driver.findElement(By.id("com.oppo.market:id/tv_search_text")).click();
                    Thread.sleep(500);
                    HashSet<String> set = new HashSet<String>();
                    for(int i=0;i<=11;i++) {
                        String txt=driver.getPageSource();
                        Document doc = Jsoup.parse(txt);
                        //System.out.println(doc);

                        Pattern p = Pattern.compile("textview index=\"0\" text=\"(.*?)\" class=\"android.widget.TextView\" (.*?) resource-id=\"com.oppo.market:id/tv_name\"" );

                        Matcher matcher = p.matcher((CharSequence) doc.toString());
                        String kk="";
                        while(matcher.find())
                        {

                            if(!matcher.group(1).equals("安装")) {
                                if(!matcher.group(1).equals("搜索")) {
                                    if(!matcher.group(1).equals("")) {
                                        String str3 = matcher.group(1);
                                        if(set.contains(str3)) {
                                            continue;
                                        }
                                        System.out.println(matcher.group(1));
                                        Compte++;
                                        set.add(str3);
                                        String s1=""+Compte+"";
                                        JSONObject obj2 = new JSONObject();
                                        obj2.put("position", s1);
                                        obj2.put("type", "1");
                                        obj2.put("app_title", matcher.group(1).toString());
                                        kk += obj2.toString()+",";
                                        System.out.println(obj2.toString());
                                    }
                                    System.out.print("搜索关键字:"+str2+"\n");
                                    System.out.print("排在第:"+Compte+"位"+"\n"+"\n");

                                }
                            }

                        }
                        String oaRestfulServiceUrl3 =  "http://test-amda-api.xianjincard.com/v1/collect/data/upload-data";
                        RequestConfig requestConfig3 =
                                RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000).build();
                        CloseableHttpClient httpclient3 = null;
                        CloseableHttpResponse h_response3 = null;
                        try {
                            httpclient3 =   HttpClientBuilder.create().setDefaultRequestConfig(requestConfig3).build();
                            HttpPost postMethod3 = new HttpPost(oaRestfulServiceUrl3);
                            postMethod3.setHeader("appkey", "test_key");
                            postMethod3.setHeader("debug-key", "ef575072de337dcf85941f8af0fbedc6");
                            //	postMethod.setHeader("sign", "a0908b04f004165894e2f92fa3a111b5");
                            postMethod3.setHeader("timestamp", "1528376502");
                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            params.add(new BasicNameValuePair("platform",  "2"));
                            params.add(new BasicNameValuePair("keyword", str2));
                            params.add(new BasicNameValuePair("is_install_app", "2"));

                            //	System.out.println(time);
                            params.add(new BasicNameValuePair("datetime", datatime));
                            System.out.println(kk);
                            kk= kk.substring(0,kk.length()-1);
                            params.add(new BasicNameValuePair("data_items","["+kk.toString()+"]"));
                            postMethod3.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                            h_response3 = httpclient3.execute(postMethod3);
                            System.out.println(h_response3);
                            //HttpEntity repEntity3 = h_response3.getEntity();
                            //String content3 = EntityUtils.toString(repEntity3, "utf-8");
                            //System.out.println(content3);

                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.println(e);
                        }
                        if(Compte>=100)    {
                            break;
                        }   else {
                            hua();

                        }
                    }

                } catch (Exception e) {
                    System.out.println("end");
                    break;
                }

            }

        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e);
        }

    }
    @AfterSuite
    public void quit() throws Exception {
        driver.quit();
    }

}
