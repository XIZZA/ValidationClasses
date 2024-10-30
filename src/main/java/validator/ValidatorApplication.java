package validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class ValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidatorApplication.class, args);
	}


	@Configuration
	public static class ValidationConfig {
		@Bean
		public LocalValidatorFactoryBean getValidator() {
			LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
			bean.afterPropertiesSet();
			return bean;
		}
	}

}

