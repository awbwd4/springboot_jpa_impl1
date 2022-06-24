package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpabook.jpashop.domain.Member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

	@Bean//빈으로 등록만 하면 프록시를 하이버네이트가 처리하지 않는다!
	Hibernate5Module hibernate5Module() {
		Hibernate5Module hibernate5Module =  new Hibernate5Module();
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);
		return hibernate5Module;
	}
	/**
	 * FetchType.LAZY : 지연로딩
	 * 지연로딩이 되면 Member객체의 정보는 일단 가져오지 않고
	 * 나머지 필드들의 정보만 가져온다
	 * Member객체에 null을 할 수는 없으므로
	 * proxy객체를 넣어서 가져온다.
	 * 그 proxy가 "ByteBuddyInterceptor"
	 *
	 * jackson 라이브러리에서는 이 프록시를 읽을 수 없다.
	 * 따라서 지연로딩인 경우에는
	 * jackson 라이브러리가  아무것도 하지 않도록 해야함.
	 * hibernate5module 빈 등록
	 *
	 * **///




}
