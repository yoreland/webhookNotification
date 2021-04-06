package io.agora.github.notification.service;

import io.agora.github.notification.model.Owner;
import io.agora.github.notification.model.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class PropertyService {

    private Properties properties;

    @PostConstruct
    public void init() throws IOException {
        properties = new Properties();
        InputStream in = PropertyService.class.getClassLoader().getResourceAsStream("config.properties");
        properties.load(in);
    }

    public List<String> getIgnoredRepoList(){
        return Arrays.asList(properties.getProperty("ignored").split(","));
    }

    public List<String> getIngoredReplierList(){
        return Arrays.asList(properties.getProperty("staff").split(","));
    }

    public String getWebHookKey(){
        return properties.getProperty("webhook");
    }

    public List<Owner> getNotifier(String repository){
        List<Owner> rel = new ArrayList<>();
        Arrays.stream(properties.getProperty(repository).split(","))
                .forEach(name -> rel.add(new Owner(name)));
        return rel;
    }

}
