package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // final인 필드에 대해서만 인자값으로 받는 생성자를 만들어줌.
public class MemberService {

    private final MemberRepository memberRepository;

//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //회원 가입
    @Transactional(readOnly = false)
    public Long join(Member member){
        validateDuplicateMember(member);//회원 중복여부 체크

        memberRepository.save(member);
        return member.getId();
    }
    private void validateDuplicateMember(Member member) {
        //회원 중복여부 체크, 중복시 예외처리
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long memberId) {
//        return memberRepository.findOne(memberId); //JPA
        return memberRepository.findById(memberId).get(); //Spring JPA
    }

    @Transactional
    public void update(Long id, String name) {

//        Member findMember = memberRepository.findOne(id);//영속성 컨텍스트에서 가져옴0  jpa
        Member findMember = memberRepository.findById(id).get();//영속성 컨텍스트에서 가져옴0  springJpa
        findMember.setName(name);// dirty checking

    }


    //
//    @Transactional(readOnly = false)
//    public void delEntity(){
//        memberRepository.delEntity();
//    }

}
