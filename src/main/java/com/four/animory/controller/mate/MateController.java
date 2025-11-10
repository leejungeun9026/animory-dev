package com.four.animory.controller.mate;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.mate.MateBoardDTO;
import com.four.animory.service.mate.MateReplyService;
import com.four.animory.service.mate.MateService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Log4j2
@RequestMapping("/mate")

public class MateController {
    @Value("${com.four.animory.upload.path}")
    private String uploadPath;

    @Autowired
    private MateService mateService;

    @Autowired
    private UserService userService;


     //1차 list
//    @GetMapping("/list")
//    public String list(Model model) {
//        List<MateBoardDTO> mateboardList = mateService.findAllMateBoards();
//        log.info("LIST SIZE = {}", mateboardList.size());
//        model.addAttribute("mateboardList", mateboardList);
//        return "mate/list";
//    }

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<MateBoardDTO> responseDTO = mateService.getList(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
        return "mate/list";
    }

    @GetMapping("/view")
    public void view(Long bno, Integer mode, Model model) {
        if(mode == null) mode = 1;
        model.addAttribute("board", mateService.findMateBoardById(bno,mode));
    }

    @GetMapping("/register")
    public String registerGet(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        Member member = principalDetails.getMember();
        model.addAttribute("member", member);
        return "mate/register";
    }

    @PostMapping("/register")
    public String registerPost(MateBoardDTO mateBoardDTO, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        log.info(member);
        mateService.registerMateBoard(mateBoardDTO, member);
        return "redirect:/mate/list";
    }

    @GetMapping("/modify")
    public String modify(Long bno, Integer mode, Model model) {
        log.info("모드 확인:" +mode);
        MateBoardDTO mateBoardDTO = mateService.findMateBoardById(bno,mode);
        model.addAttribute("mateBoard", mateBoardDTO);
        return "mate/modify";
    }

    @PostMapping("/modify")
    public String modify(MateBoardDTO mateBoardDTO) {
        mateService.updateMateBoard(mateBoardDTO);
        return "redirect:/mate/view?bno=" + mateBoardDTO.getBno();
    }

    @GetMapping("remove")
    public String removeMateBoard(Long bno) {
        mateService.deleteMateBoardById(bno);
        return "redirect:/mate/list";
    }

    @ResponseBody
    @GetMapping("/like")
    public MateBoardDTO likePost(Long bno){
        return mateService.updateLikecount(bno);
    }
}





