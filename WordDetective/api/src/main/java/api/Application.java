package api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import core.Game;
import core.UserAccess;

/**
 *
 * Spring Boot application starter class.
 */
@SpringBootApplication
public class Application {

    /**
     * Main method for starting the application.
     *
     * @param args Argument used to start the program.
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            System.out.println("\n\n\n" + "IP: " + InetAddress.getLocalHost().getHostAddress() + "\n\n\n");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Session scoped bean used as reference to the instance of game object used in
     * the gamepage controller.
     *
     * @return Bean for injection.
     */
    @Primary
    @Bean
    @Scope("session")
    public Game game() {
        return null;
    }

    /**
     * Session scoped bean used as reference to the instance of game object used in
     * the gamepage controller.
     *
     * @return Bean for injection.
     */
    @Primary
    @Bean
    @Scope("session")
    public UserAccess userAccess() {
        return null;
    }

}
