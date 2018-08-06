package org.ethereum.facade;

import org.ethereum.config.DefaultConfig;
import org.ethereum.config.SystemProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author: HuShili
 * @date: 2018/8/6
 * @description: none
 */
@Component
public class P2PFactory {

    private static final Logger logger = LoggerFactory.getLogger("general");

    /**
     * @deprecated The config parameter is not used anymore. The configuration is passed
     * via 'systemProperties' bean either from the DefaultConfig or from supplied userSpringConfig
     * @param config  Not used
     * @param userSpringConfig   User Spring configuration class
     * @return  Fully initialized P2P instance
     */
    public static P2P createP2P(SystemProperties config, Class userSpringConfig) {

        return userSpringConfig == null ? createP2P(new Class[] {DefaultConfig.class}) :
                createP2P(DefaultConfig.class, userSpringConfig);
    }

    public static P2P createP2P(Class ... springConfigs) {
        logger.info("Starting P2P...");
        ApplicationContext context = new AnnotationConfigApplicationContext(springConfigs);

        return context.getBean(P2P.class);
    }
}
