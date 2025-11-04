package com.four.animory.controller.spr;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.sitter.SitterBoardDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.spr.SprService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/spr")
public class SprController {
    @Autowired
    private SprService sprService;
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<SprBoardDTO> responseDTO = sprService.getList(pageRequestDTO);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageRequestDTO",pageRequestDTO);
    }

    @GetMapping("/register")
    public void registerGet(@AuthenticationPrincipal PrincipalDetails principal, Model model) {
        MemberDTO memberDTO = null;
        if (principal != null) { // 로그인한 사용자가 있는 경우
            Member member = principal.getMember();
            memberDTO = userService.getMemberByUsername(member.getUsername());
            log.info("로그인한 회원 정보: {}", memberDTO);
        } else {
            log.info("비로그인 상태에서 글쓰기 페이지 접근");
        }
        model.addAttribute("memberDTO", memberDTO);
    }

    @PostMapping("/register")
    public String registerPost(SprBoardDTO sprBoardDTO) {
        sprService.registerSprBoard(sprBoardDTO);
        return "redirect:/spr/list";
    }


    @GetMapping("/remove")
    public String removePost(Long bno){
        SprBoardDTO sprBoardDTO = sprService.findBoardById(bno,2 );
        sprService.deleteBoardById(bno);
        return "redirect:/spr/list";
    }

    @GetMapping({"/view","/modify"})
    public void view_modifyBoard(Long bno, int mode, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("pageRequestDTO",pageRequestDTO);
        model.addAttribute("board",sprService.findBoardById(bno, mode));
    }
    @PostMapping("modify")
    public String modifyPost(SprBoardDTO sprBoardDTO, RedirectAttributes redirectAttributes) {
        sprService.updateBoard(sprBoardDTO);
        redirectAttributes.addAttribute("bno",sprBoardDTO.getBno());
        redirectAttributes.addAttribute("mode",2);
        return "redirect:/spr/view";
    }
}
