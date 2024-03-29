package com.challenge.devchall.domain.member.service;

import com.challenge.devchall.domain.inventory.entity.Inventory;
import com.challenge.devchall.domain.inventory.service.InventoryService;
import com.challenge.devchall.domain.item.entity.Item;
import com.challenge.devchall.domain.item.service.ItemService;
import com.challenge.devchall.domain.member.repository.MemberRepository;
import com.challenge.devchall.domain.point.entity.Point;
import com.challenge.devchall.domain.point.service.PointService;
import com.challenge.devchall.domain.pointHistory.service.PointHistoryService;
import com.challenge.devchall.global.base.rsData.RsData;
import com.challenge.devchall.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 아래 메서드들이 전부 readonly 라는 것을 명시, 나중을 위해
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    private final ItemService itemService;

    private final PointService pointService;

    private final InventoryService inventoryService;

    private final PointHistoryService pointHistoryService;
    public Optional<Member> findByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID);
    }

    @Transactional
    public RsData<Member> join(String loginID, String password, String email, String nickname) {

        RsData<Member> rsData = validateMember(loginID, email);
        if (rsData.isFail()) return rsData;

        return join("DevChall",loginID,password,email,nickname);
    }
    private RsData<Member> join (String providerTypeCode, String loginID, String password, String email, String nickname){
        if (findByLoginID(loginID).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(loginID));
        }
        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);
        Member member = Member
                .builder()
                .providerTypeCode(providerTypeCode)
                .loginID(loginID)
                .email(email)
                .nickname(nickname)
                .password(password)
                .challengeLimit(0)
                .point(pointService.create())
                .build();

        memberRepository.save(member);

        inventoryService.create(member, itemService.getByName("basic").orElse(null), true);
        //inventoryService.create(member, itemService.getByName("cow.png").orElse(null), true);

        pointHistoryService.addPointHistory(member, +1000,"가입축하금");

        return RsData.of("S-1", "회원가입이 완료되었습니다.", member);
    }

    public RsData<Member> validateMember (String loginID, String email) {
        if (findByLoginID(loginID).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(loginID));
        }
        if (memberRepository.existsByEmail(email)){
            return RsData.of("F-2", "해당 이메일은 이미 사용중입니다.");
        }
        return RsData.of("S-1", "유효성 검사 완료");
    }

    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    // 소셜 로그인(카카오, 구글, 네이버) 로그인이 될 때 마다 실행되는 함수
    @Transactional
    public RsData<Member> whenSocialLogin(String providerTypeCode, String loginID, String email, String nickname) {
        Optional<Member> opMember = findByLoginID(loginID);

        if (opMember.isPresent()) return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        // 소셜 로그인를 통한 가입시 비번은 없음 "" 처리
        return join(providerTypeCode, loginID, "", email, nickname); // 최초 로그인 시 딱 한번 실행
    }

    public Member getByLoginId(String loginID){

        return memberRepository.findByLoginID(loginID).orElse(null);
    }

    public RsData<Member> checkChallengeLimit(Member member){

        int challengeLimit = member.getChallengeLimit();

        if(challengeLimit < 1){
            return RsData.of("S-1", "챌린지 개설이 가능합니다.");
        }else{
            return RsData.of("F-1", "이미 이번달에 챌린지를 생성하셨습니다.");
        }
    }

    public RsData<Member> canJoin(Member member, int joinCost){

        Point memberPoint = member.getPoint();
        Long currentPoint = memberPoint.getCurrentPoint();

        if(currentPoint >= joinCost){

            memberPoint.subtract(joinCost);

            return RsData.of("S-1", "참가 비용을 지불할 수 있습니다");
        }else{
            return RsData.of("F-3", "참가 비용이 부족합니다.");
        }
    }


    @Transactional
    public RsData<Inventory> buyItem(String itemId, Member member, boolean equip){

        Item buyItem = itemService.getById(Long.parseLong(itemId)).orElse(null);

        if(buyItem == null) {//아이템의 존재 유무
            return RsData.of("F-7", "아이템이 존재하지 않습니다.");
        }

        Point memberPoint = member.getPoint();

        long itemCost = buyItem.getPrice();

        if(memberPoint.getCurrentPoint() < itemCost){ //포인트 여부
            return RsData.of("F-6", "소지금이 부족합니다.");
        }

        RsData<Inventory> rs = inventoryService.create(member, buyItem, false);


        if(rs.isFail()) {//이미 구매한 아이템
            return rs;
        }

        member.getPoint().subtract(buyItem.getPrice());
        if(equip){
            if(buyItem.getType().equals("font")) {
                inventoryService.changeFontEquip(buyItem.getId(), member);
            } else if (buyItem.getType().equals("character")) {
                inventoryService.changeCharacterEquip(buyItem.getId(), member);

            }
        }


        if(buyItem.getType().equals("font")) {
            pointHistoryService.addPointHistory(member, -itemCost, "폰트 구입");
        }
        if(buyItem.getType().equals("character")) {
            pointHistoryService.addPointHistory(member, -itemCost, "캐릭터 구입");
        }
        return RsData.of("S-6", "구매에 성공하였습니다.");
    }

    public int getMemberPoint(String loginId) {
        Member member = getByLoginId(loginId);

        if (member != null) {
            Point point = member.getPoint();
            if (point != null) {
                return point.getCurrentPoint().intValue();
            }
        }
        return 0; // 멤버가 존재하지 않거나 포인트 정보가 없는 경우, 0을 반환
    }

    public List<Member> getAllMembers(){
        return memberRepository.findAll();
    }


    @Transactional
    public void changeNickname(String nickname, Member member) {
        member.changeNickname(nickname);
    }

}