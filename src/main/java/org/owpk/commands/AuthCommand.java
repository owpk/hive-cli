package org.owpk.commands;

import io.micronaut.http.HttpResponse;
import org.owpk.api.AuthApi;
import org.owpk.auth.Credentials;
import org.owpk.auth.Token;
import org.owpk.auth.TokenManagerImpl;
import picocli.CommandLine;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Scanner;

@CommandLine.Command(name = "auth",
        description = "Authenticate to hive.farm\n")
public class AuthCommand implements Runnable{

    @Inject
    private AuthApi api;

    @Inject
    private TokenManagerImpl tokenManager;

    @CommandLine.Command(name = "login", description = "Create auth token (sign in),\n\t" +
            "Endpoint: /auth/login")
    public void getWalletInfo() {
        tokenManager.init();
        Credentials credentials = new Credentials();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Login: ");
        credentials.setLogin(scanner.nextLine());
        System.out.print("Password: ");
        credentials.setPassword(scanner.nextLine());
        System.out.print("Two FA code (if use): ");
        credentials.setTwoFa(scanner.nextLine());
        System.out.print("Remember? (default true): ");
        String remember = scanner.nextLine();
        if (remember == null || remember.isBlank()) {
            credentials.setRemember(true);
        } else if (!remember.isBlank() && remember.toLowerCase()
                .startsWith("no")) {
            credentials.setRemember(false);
        }
        HttpResponse<Token> token = api.doAuth(credentials);
        if (token.code() == 200) {
            tokenManager.saveToken(Objects.requireNonNull(token.body()));
            System.out.println("Login success!");
        } else throw new RuntimeException("No auth token returned");
    }

    @Override
    public void run() {
        getWalletInfo();
    }
}
