package org.owpk.auth;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.owpk.conf.HiveConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Properties;

@Singleton
@Getter
public class TokenManagerImpl {
    private final String LOCATION;
    private HiveConfiguration hiveConfiguration;

    @Inject
    public TokenManagerImpl(HiveConfiguration hiveConfiguration) {
        String system = System.getProperty("os.name");
        String home;
        if (system.toLowerCase().startsWith("windows"))
            home = System.getProperty("user.home");
        else if (system.toLowerCase().startsWith("linux"))
            home = System.getenv("HOME");
        else throw new UnsupportedOperationException("Unsupported operation system. Exiting...");
        this.LOCATION = String.format("%s/%s/credentials.properties",
                home, hiveConfiguration.getConfig());
        this.hiveConfiguration = hiveConfiguration;
    }

    public void init() {
        File tokenLocation = new File(LOCATION);
        File parent = tokenLocation.getParentFile();
        if (!parent.exists() && !parent.mkdir())
            throw new IllegalStateException("Cannot create configuration directory: " + parent);
        try {
            if (!tokenLocation.exists() && !tokenLocation.createNewFile())
                throw new IllegalStateException("Cannot create configuration file: " + tokenLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("micronaut.config.files", LOCATION);
    }

    public void saveToken(Token token) {
        try (var os = new FileOutputStream(LOCATION)) {
           var content = "hive.auth.token = " + token.getAccessToken() + "\n";
           var expiration = "hive.auth.expiration = " + token.getExpiresIn() + "\n";
           var lastUpdate = "hive.auth.lastUpdated = " + new Date().getTime();
           os.write(content.getBytes(StandardCharsets.UTF_8));
           os.write(expiration.getBytes());
           os.write(lastUpdate.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public Token getToken() {
        Properties properties = new Properties();
        properties.load(new FileInputStream(LOCATION));
        String accToken = properties.getProperty("hive.auth.token");
        Long expiration = Long.parseLong(properties.getProperty("hive.auth.expiration"));
        Long lastUpdated = Long.parseLong(properties.getProperty("hive.auth.lastUpdated"));
        Token token = new Token();
        token.setAccessToken(accToken);
        token.setExpiresIn(expiration);
        token.setLastUpdated(lastUpdated);
        return token;
    }

    public void checkIfTokenExpired() {
        Token token = getToken();
        val currentTime = new Date().getTime() / 1000;
        val creationTime = token.getLastUpdated() / 1000;
        if (creationTime + token.getExpiresIn() < currentTime)
            throw new RuntimeException("Token has expired");
    }
}

