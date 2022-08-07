package org.retriever.server.dailypet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DailypetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailypetApplication.class, args);
	}

}
