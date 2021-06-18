package io.reflectoring.coderadar.app;

import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties
@EntityScan(basePackages = "io.reflectoring.coderadar")
@SpringBootApplication(scanBasePackages = "io.reflectoring.coderadar")
@Controller
@Slf4j
public class CoderadarApplication implements ErrorController {

  public static void main(String[] args) {
    Locale.setDefault(Locale.US);
    SpringApplication.run(CoderadarApplication.class, args);
  }

  @EventListener
  public void handleContextRefresh(ContextRefreshedEvent event) {
    ApplicationContext applicationContext = event.getApplicationContext();
    RequestMappingHandlerMapping requestMappingHandlerMapping =
        applicationContext.getBean(
            "requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
    Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
    map.forEach((key, value) -> log.info("{} {}", key, value));
  }

  private static final String ERROR = "/error";

  /** @return forwards to index.html (angular app) */
  @GetMapping(value = ERROR)
  public String error() {
    return "forward:/index.html";
  }

  @Override
  public String getErrorPath() {
    return ERROR;
  }
}
