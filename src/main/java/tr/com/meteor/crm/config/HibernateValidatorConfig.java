package tr.com.meteor.crm.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import tr.com.meteor.crm.aop.logging.ValidatorAspect;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class HibernateValidatorConfig {

    @Bean
    public Validator validator (final AutowireCapableBeanFactory autowireCapableBeanFactory) {
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
            .configure()
            .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
            .buildValidatorFactory();

        return validatorFactory.getValidator();
    }

    @Bean
    public ValidatorAspect validatorAspect(final Validator validator) {
        return new ValidatorAspect(validator);
    }
}
