package no.entur.nanna.nanna;

import no.entur.nanna.nanna.provider.domain.Provider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { Provider.class, Jsr310JpaConverters.class })
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
