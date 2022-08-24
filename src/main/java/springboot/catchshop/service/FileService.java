package springboot.catchshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@Transactional
public class FileService {

    // 이미지 업로드 메서드
    public String uploadFile(MultipartFile imgFile) throws IOException {
        String originalName = imgFile.getOriginalFilename(); // 파일명

        // 파일 저장 경로
        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";

        // 랜덤 파일명 생성
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + originalName;

        // 파일 저장
        File file = new File(projectPath, fileName);
        imgFile.transferTo(file);

        return fileName;
    }
}
