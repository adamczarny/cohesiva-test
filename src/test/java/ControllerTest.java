import org.cohesiva.test.config.MVCConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * Created by adam on 3/5/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring-servlet.xml")
public class ControllerTest {
    MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @Before
    public void setup(){
        mockMvc = webAppContextSetup(wac).build();
        for(String s: wac.getBeanDefinitionNames()){
            System.out.printf(s+"\n");
        }
    }

    @Test
    public void shouldUpload() throws Exception {
        MvcResult result = mockMvc.perform(get("/testUser/testFile?type=UPLOAD")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String url = result.getResponse().getContentAsString();
        Assert.assertEquals(200,uploadObject(new URL(url)));
    }

    @Test
    public void shouldDownload() throws Exception {
        MvcResult result = mockMvc.perform(get("/testUser/testFile?type=DOWNLOAD")).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String url = result.getResponse().getContentAsString();
        Assert.assertEquals("test!@#",downloadObject(new URL(url)));
    }

    public int uploadObject(URL url) throws IOException
    {
        HttpURLConnection connection=(HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        OutputStreamWriter out = new OutputStreamWriter(
                connection.getOutputStream());
        out.write("test!@#");
        out.close();
        return connection.getResponseCode();
    }

    public String downloadObject(URL url) throws IOException {
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        File file = new File("/tmp/test");
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        String content = new String(Files.readAllBytes(file.toPath()));
        return content;

    }
}
