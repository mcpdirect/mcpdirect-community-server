package ai.mcpdirect.community;

import appnet.communicator.ssl.SSLContextGenerator;
import appnet.hstp.SSLContextFactory;
import appnet.hstp.ServiceEngine;
import appnet.hstp.ServiceEngineFactory;
import appnet.hstp.annotation.ServiceScan;
import appnet.hstp.exception.ServiceEngineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

import javax.net.ssl.SSLContext;
import java.util.Map;

@SpringBootApplication
@ServletComponentScan({"ai.mcpdirect.gateway","appnet.hstp.labs.http.servlet"})
@ComponentScan("ai.mcpdirect.backend")
@ServiceScan({"ai.mcpdirect.gateway","ai.mcpdirect.backend"})
public class MCPdirectCommunityApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(MCPdirectCommunityApplication.class);
    public static void main(String[] args) throws ServiceEngineException {
        SpringApplication app = new SpringApplication(MCPdirectCommunityApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);

        ServiceEngineFactory.setServiceEngineIdSeed("ai.mcpdirect.community");
        ServiceEngineFactory.setSSLContextFactory((config, isClient) -> {
            try {
                return SSLContextGenerator.createSSLContext();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        });
        ServiceEngine serviceEngine = ServiceEngineFactory.getServiceEngine();
        LOG.info("ServiceEngine "+serviceEngine+" started");
    }

    @Override
    public void run(String... args) throws Exception {
//        ServiceEngineFactory.setServiceEngineIdSeed("ai.mcpdirect.community");
//        ServiceEngine serviceEngine = ServiceEngineFactory.getServiceEngine();
//        LOG.info("ServiceEngine "+serviceEngine+" started");
    }
}
