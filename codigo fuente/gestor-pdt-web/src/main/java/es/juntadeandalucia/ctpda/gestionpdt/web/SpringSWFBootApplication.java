/*
 * Copyright 2016-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.juntadeandalucia.ctpda.gestionpdt.web;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


import es.juntadeandalucia.ctpda.gestionpdt.model.core.AuditorAwareImpl;

@SpringBootApplication
@EnableCaching //activamos cache
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
@ComponentScan (basePackages = {"es.juntadeandalucia.ctpda.gestionpdt", "com.onlyoffice.integration"})
@EntityScan(basePackages = {"es.juntadeandalucia.ctpda.gestionpdt.model"})
@EnableJpaRepositories(
		  basePackages = {"es.juntadeandalucia.ctpda.gestionpdt"}, repositoryImplementationPostfix = "Impl")
@EnableScheduling
public class SpringSWFBootApplication {
	
    @Bean
    AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
	/**
	* Main method.
	*/
	public static void main(String[] args) {
		SpringApplication.run(SpringSWFBootApplication.class, args);
	}
	
	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
	}
}
