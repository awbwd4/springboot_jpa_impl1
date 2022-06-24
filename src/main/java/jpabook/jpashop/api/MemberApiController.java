package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 조회
     **/
    //회원 조회 API v.1
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    //회원 조회 API v.1
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        int count = collect.size();

        return new Result(count, collect);
    }

    /**회원 등록**/
    //회원 등록 API v.2
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        //@RequestBody : json으로 온 body를 Member로 바로 매핑해줌.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    //회원 등록 API v.2
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**회원 수정**/
    //회원 수정 API v.2
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
              @PathVariable("id") Long id
            , @RequestBody @Valid UpdateMemberRequest request) {
        //UpdateMemberResponse : update용 응답 dto
        //UpdateMemberRequeset : update용 요청 Dto

//      업데이트 로직
//        Member updatedMember = memberService.update(id, request.getName());
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);


        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }




    /***********DTO : 엔티티를 컨트롤러에서 직접 호출하지 않고 DTO를 통한다.************/

    /***회원 생성***/

    // 회원 생성(POST) 용 요청 DTO
    @Data
    static class CreateMemberRequest{
        private String name;
//        private Address address;
    }

    // 회원 생성(POST) 용 응답 DTO
    @Data
    static class CreateMemberResponse {
        private Long id;
        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    /***회원 수정용***/
    // 회원 수정용(PUT) 요청 DTO
    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    // 회원 수정용(PUT) 응답 DTO
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    /***회원 조회용***/
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }



}
