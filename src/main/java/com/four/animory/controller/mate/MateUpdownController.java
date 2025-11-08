//package com.four.animory.controller.mate;
//
//import com.four.animory.dto.mate.MateBoardDTO;
//import com.four.animory.dto.mate.MateFileDTO;
//import com.four.animory.dto.mate.upload.MateUploadFileDTO;
//import com.four.animory.dto.spr.SprFileDTO;
//import net.coobird.thumbnailator.Thumbnailator;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MateUpdownController {
//
//
//
//
//    private List<MateFileDTO> fileUpload(MateUploadFileDTO mateUploadFileDTO){
//        String matePath = Paths.get(uploadPath, "mate").toString();
//        log.info("matePath:"+matePath);
//        List<MateFileDTO> list = new ArrayList<>();
//        if(mateUploadFileDTO.getFiles() != null){
//            mateUploadFileDTO.getFiles().forEach(multiFile -> {
//                String originalFileName = multiFile.getOriginalFilename();
//                log.info("originalFileName:"+originalFileName);
//                String uuid = UUID.randomUUID().toString();
//                Path savePath = Paths.get(matePath,uuid+"_"+originalFileName);
//                boolean image = false;
//                try{
//                    multiFile.transferTo(savePath);
//                    if(Files.probeContentType(savePath).startsWith("image")){
//                        image = true;
//                        File thumbnail = new File(matePath, "s_"+uuid+"_"+originalFileName);
//                        Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 200, 200);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                SprFileDTO sprFileDTO = SprFileDTO.builder()
//                        .uuid(uuid)
//                        .fileName(originalFileName)
//                        .image(image)
//                        .build();
//                list.add(mateFileDTO);
//            });
//        }
//        return list;
//    }
//
//    @GetMapping("/remove")
//    public String removePost(Long bno){
//        MateBoardDTO mateBoardDTO = mateService.findMateBoardById(bno, 2);
//        List<MateFileDTO> mateFileDTOS = mateBoardDTO.getMateFileDTOs();
//        if(mateFileDTOS != null && !mateFileDTOS.isEmpty()){
//            log.info("removeMateBoard");
//            removeFile(mateFileDTOS);
//        }
//        mateService.deleteMateBoardById(bno);
//        return "redirect:/mate/list";
//    }
//
//
//    private void removeFile(List<MateFileDTO> mateFileDTO){
//        String matePath = Paths.get(uploadPath, "mate").toString();
//        for(MateFileDTO mateFileDTO1 : mateFileDTO){
//            String fileName = mateFileDTO1.getUuid()+"_"+mateFileDTO1.getFileName();
//            log.info("removeFile: "+fileName);
//            Resource resource = new FileSystemResource(
//                    matePath+ File.separator+fileName);
//            String resourceName = resource.getFilename();
//            boolean removed = false;
//            try{
//                String contentType = Files.probeContentType(resource.getFile().toPath());
//                removed = resource.getFile().delete();
//                if(contentType.startsWith("image")){
//                    String fileName1 = "s_"+fileName;
//                    File originalFile = new File(matePath+File.separator+fileName1);
//                    originalFile.delete();
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//}
