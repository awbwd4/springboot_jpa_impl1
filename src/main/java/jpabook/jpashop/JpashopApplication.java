package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.text.html.parser.Entity;

@SpringBootApplication
public class JpashopApplication {

	@PersistenceContext // 엔티티 매니저가 주입됨.
	private EntityManager em;

	public static void main(String[] args) {

		Hello hello = new Hello();
		hello.setData("hello");
		String data = hello.getData();

		System.out.println("data = " + data);


		SpringApplication.run(JpashopApplication.class, args);
	}

}
