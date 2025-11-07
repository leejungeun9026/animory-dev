package com.four.animory.controller.free;

import com.four.animory.config.auth.PrincipalDetails;
import com.four.animory.domain.user.Member;
import com.four.animory.dto.free.FreeBoardDTO;
import com.four.animory.dto.free.FreeFileDTO;
import com.four.animory.dto.free.upload.UploadFileDTO;
import com.four.animory.service.free.FreeService;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
@RequestMapping("/free")
public class FreeUpdownController {
    @Value("${com.four.animory.upload.path}")
    private String uploadPath;

    @Autowired
    private FreeService freeService;

    @PostMapping("/register") // 글등록
    public String registerPost(FreeBoardDTO freeBoardDTO, @AuthenticationPrincipal PrincipalDetails principalDetails, UploadFileDTO uploadFileDTO) {
        List<FreeFileDTO> fileDTOS=null;
        if(uploadFileDTO.getFiles()!=null
                && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){ // 파일 이름이 공백이 아닐 떄
            fileDTOS=fileUpload(uploadFileDTO);
        }
        freeBoardDTO.setFreeFileDTOS(fileDTOS);
        Member member = principalDetails.getMember();
        log.info(member);
        freeService.registerFreeBoard(freeBoardDTO, member); // 서비스에는 DTO만 전달
        return "redirect:/free/list";
    }

    // 파일 업로드
    private List<FreeFileDTO> fileUpload(UploadFileDTO uploadFileDTO) {
        String freeUploadPath = uploadPath + "\\free";
        List<FreeFileDTO> list = new ArrayList<>(); // 파일 업로드 결과를 보려고
        if (uploadFileDTO.getFiles() != null) { // 파일이 올라왔으면
            uploadFileDTO.getFiles().forEach(multiFile -> {
                String originalFileName = multiFile.getOriginalFilename();
                log.info("originalFileName:" + originalFileName);
                String uuid = UUID.randomUUID().toString(); // uuid+스트링 형태 -> 변수에 넣겠다.
                Path savePath = Paths.get(freeUploadPath, uuid + "_" + originalFileName); // 파일이 저장될 path가 만들어짐
                boolean image = false;
                try {
                    multiFile.transferTo(savePath); // 전체 파일 경로로 저장
                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbnail = new File(freeUploadPath, "s_" + uuid + "_" + originalFileName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200); // 가로세로 크기
                    } // 이미지라는 이름으로 시작하면
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FreeFileDTO freeFileDTO = FreeFileDTO.builder()
                        .uuid(uuid)
                        .filename(originalFileName)
                        .image(image)
                        .build();
                list.add(freeFileDTO); // 리스트에 계속 담아줌.
//                list.add(uuid+"_"+originalFileName);
            });
        }
        return list;
    }

    // 파일을 수정할 때 삭제하는 메서드라서 반환이 필요없다.
    public void removeFile(List<FreeFileDTO> fileDTOS) { // 이미지일 경우에는 썸네일이 옴
        String freeUploadPath = uploadPath + "\\free";
        for (FreeFileDTO freeFileDTO : fileDTOS) {
            String filename = freeFileDTO.getUuid() + "_" + freeFileDTO.getFilename();
            Resource resource = new FileSystemResource(freeUploadPath + File.separator + filename);
            String resourceName = resource.getFilename();
            boolean removed = false;

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                removed = resource.getFile().delete(); // 원본 파일 삭제

                if (contentType.startsWith("image")) {
                    String fileName1 = "s_" + filename; // 썸네일 파일
                    File thumFile = new File(freeUploadPath + File.separator + fileName1); // 원본 파일로 형태로 만들어짐
                    thumFile.delete(); // 원본 파일이 삭제 됨.
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 기존파일 삭제 후 새로운 파일 업로드
    @PostMapping("/modify")
    public String modifyfreeBoard(UploadFileDTO uploadFileDTO, FreeBoardDTO freeBoardDTO, RedirectAttributes redirectAttributes) {
        String freeUploadPath = uploadPath + "\\free";
        log.info("modify freeBoardDTO:" + freeBoardDTO);
        List<FreeFileDTO> fileDTOS= null;
        if(uploadFileDTO.getFiles()!=null && !uploadFileDTO.getFiles().get(0).getOriginalFilename().equals("")){
            FreeBoardDTO freeBoardDTO1 = freeService.findFreeBoardById(freeBoardDTO.getBno(),2);
            List<FreeFileDTO> fileDTOS1 = freeBoardDTO.getFreeFileDTOS();
            if(fileDTOS1!=null && !fileDTOS1.isEmpty()){
                removeFile(fileDTOS1); // 기존의 파일 삭제함.
            }
            fileDTOS=fileUpload(uploadFileDTO); // 새로 업로드한 파일 처리 (업로드, 정보 얻어옴)
        }
        freeBoardDTO.setFreeFileDTOS(fileDTOS);
        freeService.updateFreeBoard(freeBoardDTO);
        redirectAttributes.addAttribute("bno", freeBoardDTO.getBno());
        redirectAttributes.addAttribute("mode",1);
        return "redirect:/free/view";
    }



}


