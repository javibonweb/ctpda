package es.juntadeandalucia.ctpda.gestionpdt.web.conf;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// visto en https://blog.swdev.ed.ac.uk/2015/06/24/adding-embedded-tomcat-ajp-support-to-a-spring-boot-application/
public class AJPConfig {

	@Value("${tomcat.ajp.port:8009}")
	int ajpPort;

	@Value("${tomcat.ajp.remoteauthentication:false}")
	String remoteAuthentication;

	@Value("${tomcat.ajp.enabled:false}")
	boolean tomcatAjpEnabled;
	
	@Value("${tomcat.ajp.scheme:https}")
	String tomcatScheme;

	@Value("${tomcat.ajp.allow-trace}")
	private boolean allowTrace;

	@Value("${tomcat.ajp.allow-secured}")
	private boolean secured;

	
	@Value("${tomcat.ajp.secret-required:false}")
	private boolean secretRequired;

	@Bean
	public TomcatServletWebServerFactory servletContainer() {

	    TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
	    if (tomcatAjpEnabled)
	    {
	        Connector ajpConnector = new Connector("AJP/1.3");
	        ajpConnector.setPort(ajpPort);
	        ajpConnector.setSecure(secured);
	        ajpConnector.setAllowTrace(allowTrace);
	        ajpConnector.setScheme(tomcatScheme);
	        
	        //from https://stackoverflow.com/questions/60501470/springboot-the-ajp-connector-is-configured-with-secretrequired-true-but-the-s
            ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecretRequired(secretRequired);

            tomcat.addAdditionalTomcatConnectors(ajpConnector);
	    }

	    return tomcat;
	  }
	
}
