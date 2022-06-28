package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 화면으로 이동
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 1) Controller에 "members/createMemberForm" 요청이 들어오면
     * 2) Controller는 createMemberForm.html로 MemberForm.class(폼 객체)를 전달함
     * 3) createMemberForm.html은 이 MemberForm 객체에 값을 채워 넣고
     * 4) 이 채워진 form객체와 함께 "members/new"요청을 Controller로 전달
     * 5) Controller는 이 "members/new" 요청을 받으면 함께 받은 MemberForm 객체의 데이터들을 가지고 MemberService를 호출해서 회원 가입 처리
     * 6) 처리가 끝나면 home으로 리다이렉트 "redirect:/"
     */

    // 회원 가입 Form으로 회원 가입 수행
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }


    //회원 목록 조회
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
