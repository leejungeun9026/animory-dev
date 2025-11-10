package com.four.animory.controller.spr;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.spr.SprReply;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.common.PageRequestDTO;
import com.four.animory.dto.common.PageResponseDTO;
import com.four.animory.dto.spr.SprBoardDTO;
import com.four.animory.dto.spr.SprFileDTO;
import com.four.animory.dto.spr.SprReplyDTO;
import com.four.animory.dto.spr.upload.SprUploadFileDTO;
import com.four.animory.dto.user.MemberDTO;
import com.four.animory.service.spr.SprBoardService;
import com.four.animory.service.spr.SprReplyService;
import com.four.animory.service.user.UserService;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
@RequestMapping("/spr")
public class SprBoardController {
    @Value("${com.four.animory.upload.path}")
    private String uploadPath;
    @Autowired
    private SprBoardService sprService;
    @Autowired
    private UserService userService;
    @Autowired
    private SprBoardService sprBoardService;


    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        PageResponseDTO<SprBoardDTO> responseDTO = sprService.getList(pageRequestDTO);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("pageRequestDTO",pageRequestDTO);
    }

    @GetMapping("/list/category")
    @ResponseBody
    public PageResponseDTO<SprBoardDTO> listCategory(PageRequestDTO pageRequestDTO, @RequestParam(value = "category", required = false)String category){
        return sprService.getList(pageRequestDTO);
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
    public String registerPost(SprBoardDTO sprBoardDTO, SprUploadFileDTO sprUploadFileDTO, RedirectAttributes redirectAttributes) {
        List<SprFileDTO> sprFileDTOS = null;
        if(sprUploadFileDTO.getFiles() != null && !sprUploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
            sprFileDTOS = fileUpload(sprUploadFileDTO);
        }
        sprBoardDTO.setSprFileDTOs(sprFileDTOS);
        sprService.registerSprBoard(sprBoardDTO);
        return "redirect:/spr/list";
    }


    @GetMapping("/remove")
    public String removePost(Long bno){
        SprBoardDTO sprBoardDTO = sprBoardService.findBoardById(bno, 2);
        List<SprFileDTO> sprFileDTOS = sprBoardDTO.getSprFileDTOs();
        if(sprFileDTOS != null && !sprFileDTOS.isEmpty()){
            removeFile(sprFileDTOS);
        }
        sprService.deleteBoardById(bno);
        return "redirect:/spr/list";
    }

    @ResponseBody
    @GetMapping("/like")
    public SprBoardDTO likePost(Long bno){
        return sprService.updateRecommend(bno);
    }

    @GetMapping({"/view","/modify"})
    public void view_modifyBoard(Long bno, int mode, PageRequestDTO pageRequestDTO, Model model){
        model.addAttribute("pageRequestDTO",pageRequestDTO);
        model.addAttribute("board",sprService.findBoardById(bno, mode));
    }
    @PostMapping("/modify")
    public String modifyPost(SprBoardDTO sprBoardDTO, RedirectAttributes redirectAttributes, SprUploadFileDTO sprUploadFileDTO) {
        List<SprFileDTO> sprFileDTOS = null;
        if(sprUploadFileDTO.getFiles() != null && !sprUploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
            SprBoardDTO sprBoardDTO1 = sprBoardService.findBoardById(sprBoardDTO.getBno(), 2);
            List<SprFileDTO> sprFileDTOS1 = sprBoardDTO1.getSprFileDTOs();
            if(sprFileDTOS1 != null && !sprFileDTOS1.isEmpty()){
                removeFile(sprFileDTOS1);
            }
            sprFileDTOS = fileUpload(sprUploadFileDTO);
        }
        sprBoardDTO.setSprFileDTOs(sprFileDTOS);
        sprService.updateBoard(sprBoardDTO);
        redirectAttributes.addAttribute("bno",sprBoardDTO.getBno());
        redirectAttributes.addAttribute("mode",2);
        return "redirect:/spr/view";
    }

    private List<SprFileDTO> fileUpload(SprUploadFileDTO sprUploadFileDTO){
        String sprPath = Paths.get(uploadPath, "spr").toString();
        log.info("sprPath:"+sprPath);
        List<SprFileDTO> list = new ArrayList<>();
        if(sprUploadFileDTO.getFiles() != null){
            sprUploadFileDTO.getFiles().forEach(multiFile -> {
                String originalFileName = multiFile.getOriginalFilename();
                log.info("originalFileName:"+originalFileName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(sprPath,uuid+"_"+originalFileName);
                boolean image = false;
                try{
                    multiFile.transferTo(savePath);
                    if(Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        File thumbnail = new File(sprPath, "s_"+uuid+"_"+originalFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                SprFileDTO sprFileDTO = SprFileDTO.builder()
                        .uuid(uuid)
                        .fileName(originalFileName)
                        .image(image)
                        .build();
                list.add(sprFileDTO);
            });
        }
        return list;
    }

    private void removeFile(List<SprFileDTO> sprFileDTOS){
        String sprPath = Paths.get(uploadPath, "spr").toString();
        for(SprFileDTO sprFileDTO : sprFileDTOS){
            String fileName = sprFileDTO.getUuid()+"_"+sprFileDTO.getFileName();
            Resource resource = new FileSystemResource(
                    sprPath+File.separator+fileName);
            String resourceName = resource.getFilename();
            boolean removed = false;
            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete();
                if(contentType.startsWith("image")){
                    String fileName1 = "s_"+fileName;
                    File originalFile = new File(sprPath+File.separator+fileName1);
                    originalFile.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


//    @GetMapping("/top10")
//    public String showTop10Boards(PageRequestDTO pageRequestDTO, Model model){
//        List<SprBoardDTO> sprBoardDTOS = sprService.getTop10SprBoards();
//        PageResponseDTO<SprBoardDTO> responseDTO = sprService.getList(pageRequestDTO);
//        model.addAttribute("responseDTO",responseDTO);
//        model.addAttribute("pageRequestDTO",pageRequestDTO);
//        model.addAttribute("board",sprBoardDTOS);
//        return "spr/top10";
//    }
    

}
