import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.apache.camel.component.jms.JmsComponent;

import javax.jms.ConnectionFactory;
import java.io.FileInputStream;
import java.io.InputStream;

public class CamelWithActiveMQ {
    public static void main(String[] args) throws Exception {
//        ApplicationContext appContext = new ClassPathXmlApplicationContext(
//                "SpringRouteContext.xml");
        CamelContext camelContext = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        camelContext.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        try {
//            file transfer
          /*  camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("file:C:/Projects/CamelLearning/CamelWithSpring/src/main/resources/inputFolder/")
                            .to("file:C:/Projects/CamelLearning/CamelWithSpring/src/main/resources/outputFolder");
                }
            });*/
//            active mq transfer
            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("file:C:/Projects/CamelLearning/CamelWithSpring/src/main/resources/inputFolder/input.txt").split().tokenize("\n").to("jms:queue:testQueue");
                }
            });
            System.out.println("Starting");
            camelContext.start();
            Thread.sleep(5 * 60 * 1000);
        } catch (NullPointerException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Stopping");
            camelContext.stop();
        }
    }
}