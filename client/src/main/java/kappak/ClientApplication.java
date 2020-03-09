package kappak;

import kappak.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;

@SpringBootApplication(scanBasePackages = "kappak")
@Slf4j
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        DispatcherServlet dispatcherServlet = context.getBean(DispatcherServlet.class);
        MockServletConfig myServletConfig = new MockServletConfig();
        ReflectionUtils.setFieldValue(dispatcherServlet, "config", myServletConfig);
        log.info("config complete");
        try {
            dispatcherServlet.init();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        log.info("dispatcherServlet init complete");
    }
}
