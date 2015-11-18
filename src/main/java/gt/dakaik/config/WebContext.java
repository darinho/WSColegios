/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author Dario Calderon
 */
@Configuration
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan(basePackages = {"gt.dakaik.rest"})
public class WebContext extends WebMvcConfigurerAdapter {

    public static Logger log = LoggerFactory.getLogger(WebContext.class);

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).
                favorParameter(false).
                ignoreAcceptHeader(false).
                useJaf(false).
                defaultContentType(MediaType.APPLICATION_JSON).
                mediaType("xml", MediaType.APPLICATION_XML).
                mediaType("json", MediaType.APPLICATION_JSON);
    }

    public MappingJackson2HttpMessageConverter jacksonJsonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();
//        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        mapper.setDateFormat(df);

        Hibernate4Module module = new Hibernate4Module();
        module.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);
        //Registering Hibernate4Module to support lazy objects
        mapper.registerModule(module);

        messageConverter.setObjectMapper(mapper);
        return messageConverter;
    }

    public MappingJackson2HttpMessageConverter jacksonXmlMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new XmlMapper();

        //Registering Hibernate4Module to support lazy objects
        Hibernate4Module module = new Hibernate4Module();
        module.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);
        mapper.registerModule(module);

        // Cambiar AnnotationIntrospector para usar anotaciones de JAXB
        AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
        mapper.setAnnotationIntrospector(introspector);

        List<MediaType> MediaTypes = new ArrayList<>();
        MediaTypes.add(MediaType.APPLICATION_XML);
        messageConverter.setSupportedMediaTypes(MediaTypes);

        messageConverter.setObjectMapper(mapper);
        //log.debug("Listado de MediaTypes: [{}]", messageConverter.getSupportedMediaTypes().toString());

        return messageConverter;

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

        //Here we add our custom-configured HttpMessageConverter
        converters.add(jacksonJsonMessageConverter());
        converters.add(jacksonXmlMessageConverter());
        super.configureMessageConverters(converters);
    }

}
