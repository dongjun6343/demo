package com.example.demo.api;

import com.example.demo.dto.MemberSaveRequestDto;
import com.example.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 *  @RestController : 컨트롤러에서 JSON 반환되는 컨트롤러를 만들어준다.
 *                @RequestBody(required=false) String param ..
 *                (예전에는 @ResponseBody를 각 메소드마다 선언했던 것을 한번에 사용할 수 있도록 해준다고 생각.)
 */
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    // Member Entity 대신에 Dto 생성해서 사용한다.
    // Api 스펙이 변경된다해도 Dto만 수정하면 됨.
    /**
     * @GetMapping : 예전에는  @RequestMapping(method = RequestMethod.GET)으로 사용.
     */
    @PostMapping("/api/v1/members")
    public Long saveMember(@RequestBody MemberSaveRequestDto requestDto){
        return memberService.save(requestDto);
    }

    @PutMapping("/api/v1/members/{id}")
    public Long update(@PathVariable Long id, @RequestBody MemberSaveRequestDto requestDto){
        return memberService.update(id, requestDto);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
