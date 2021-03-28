package org.owpk

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class HiveCliCommandSpec extends Specification {

    @Shared @AutoCleanup ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

//    void "test hive-cli with command line option"() {
//        given:
//        ByteArrayOutputStream baos = new ByteArrayOutputStream()
//        PrintStream out = System.out;
//
//        System.setOut(new PrintStream(baos))
//
//        String[] args = ['wal', "--raw"] as String[]
//
//        PicocliRunner.run(HiveCliCommand, ctx, args)
//
//        out.println baos.toString()
//
//        expect:
//        baos.toString() =~ $/\d+/$
//        System.setOut(out)
//    }
//
//    void "properties test"() {
//        given:
//        TokenManagerImpl tokenManager = ctx.getBean(TokenManagerImpl.class)
//        String location = tokenManager.LOCATION
//        println location
//
//        expect:
//        location =~ $/\w+/$
//    }
//
//    void "auth test"() {
//        given:
//        ByteArrayOutputStream baos = new ByteArrayOutputStream()
//        PrintStream out = System.out;
//
//        System.setOut(new PrintStream(baos))
//
//        String[] args = ['auth', 'login'] as String[]
//
//        PicocliRunner.run(HiveCliCommand, ctx, args)
//
//        out.println baos.toString()
//
//        expect:
//        baos.toString() =~ $/\d+/$
//        System.setOut(out)
//    }
}

