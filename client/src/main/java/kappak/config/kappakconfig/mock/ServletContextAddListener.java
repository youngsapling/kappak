package kappak.config.kappakconfig.mock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.servlet.ServletContext;

/**
 * @author ：youngsapling
 * @date ：Created in 2020/3/9 16:26
 * @modifyTime :
 * @description :
 */
@Data
@Slf4j
public class ServletContextAddListener implements SpringApplicationRunListener {


    public ServletContextAddListener(SpringApplication application, String[] args) {
        super();
    }


    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("contextPrepared");
        ServletContext servletContext = new KappkaServletContext();
        ServletWebServerApplicationContext applicationContext = (ServletWebServerApplicationContext) context;
        applicationContext.setServletContext(servletContext);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        //Not used.
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        System.out.println("started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
