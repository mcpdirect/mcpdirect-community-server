package ai.mcpdirect.community;

import appnet.hstp.ServiceEngine;
import appnet.hstp.ServiceEngineFactory;
import appnet.hstp.annotation.ServiceScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ServletComponentScan({"ai.mcpdirect"})
@ComponentScan("ai.mcpdirect.backend")
@ServiceScan({"ai.mcpdirect.gateway","ai.mcpdirect.backend"})
public class MCPdirectCommunityApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MCPdirectCommunityApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MCPdirectCommunityApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        ServiceEngineFactory.setServiceEngineIdSeed("ai.mcpdirect.community");
        ServiceEngine serviceEngine = ServiceEngineFactory.getServiceEngine();
        LOG.info("ServiceEngine "+serviceEngine+" started");
    }
}
