package com.ejetool.videoai.event.consumer.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.springframework.util.StringUtils;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GoogleDriveHelper {

    public static final String APPLICATION_FOLDER = "application/vnd.google-apps.folder";
    
    /**
     * 패스로 폴더를 찾거나 생성
     * @param drive
     * @param rootId
     * @param filePath
     * @return
     * @throws IOException
     */
    public static File getFolderByPathOrCreate(Drive drive, String rootId, String filePath) throws IOException {
        String[] pathParts = Arrays.stream(filePath.split("\\/"))
            .filter(x->!x.isBlank())
            .toArray(String[]::new);

        File parent = new File()
            .setName("__root")
            .setId(rootId == null ? "root" : rootId)
            .setMimeType(APPLICATION_FOLDER);

        for (String folderName : pathParts) {
            if (folderName.isEmpty()) {
                continue;
            }
            Optional<File> parentOptional = getFolderByName(drive, parent.getId(), folderName);
            if(parentOptional.isPresent()){
                parent = parentOptional.get();
            } else {
                parentOptional = createFolder(drive, parent.getId(), folderName);
                if(parentOptional.isPresent()){
                    parent = parentOptional.get();
                } else {
                    throw new FileNotFoundException(String.format("folder create error. path=%s, target=%s", filePath, folderName));
                }
            }
            
            if(parent.getName() == null){
                throw new FileNotFoundException(String.format("folder id notfound. path=%s, target=%s", filePath, folderName));
            }
        }

        return parent;
    }

    /**
     * 이름으로 폴더 찾기
     * @param drive
     * @param parentId
     * @param folderName
     * @return
     * @throws IOException
     */
    public static Optional<File> getFolderByName(Drive drive, String parentId, String folderName) throws IOException {
        String query = String.format("mimeType = '%s' and name = '%s' and '%s' in parents and trashed=false", 
        APPLICATION_FOLDER, folderName, parentId);
            
        FileList result = drive.files().list()
            .setQ(query)
            .setPageSize(1)
            .setFields("files(id,name,webViewLink,webContentLink)")
            .execute();

        Optional<File> file = result.getFiles().stream().findFirst();
        return file;
    }

    /**
     * 이름으로 폴더 생성
     * @param drive
     * @param parentId
     * @param folderName
     * @return
     * @throws IOException
     */
    public static Optional<File> createFolder(Drive drive, String parentId, String folderName) throws IOException{
        File file = new File()
            .setName(folderName)
            .setMimeType(APPLICATION_FOLDER)
            .setParents(Collections.singletonList(parentId));

        file = drive.files().create(file).execute();
        return Optional.of(file);
    }

    /**
     * 파일 찾기
     * @param drive
     * @param folderId
     * @param fileName
     * @param mimeType
     * @return
     * @throws IOException
     */
    public static Optional<File> getFirstFileByName(Drive drive, String folderId, String fileName, String mimeType) throws IOException {
        String query = String.format("name = '%s' and mimeType = '%s' and '%s' in parents and trashed=false", 
            fileName, mimeType, folderId);
            
        FileList result = drive.files().list()
            .setQ(query)
            .setPageSize(1)
            .setFields("files(id, name,webViewLink,webContentLink)")
            .execute();

        Optional<File> file = result.getFiles().stream().findFirst();
        return file;
    }

    /**
     * 파일 업로드
     * @param drive
     * @param folderId
     * @param fileName
     * @param mimeType
     * @param input
     * @return
     * @throws IOException
     */
    public static File uploadFile(Drive drive, String folderId, String fileName, String mimeType, byte[] input) throws IOException{
        File file = new File();
        if(StringUtils.hasText(folderId)){
            file.setParents(Collections.singletonList(folderId));
        }
        file.setName(fileName);
        return uploadFile(drive, file, mimeType, input);
    }

    /**
     * 파일 업로드
     * @param drive
     * @param file
     * @param mimeType
     * @param input
     * @return
     * @throws IOException
     */
    public static File uploadFile(Drive drive, File file, String mimeType, byte[] input) throws IOException{
        ByteArrayContent content = new ByteArrayContent(mimeType, input);
        File uploadFile = drive.files().create(file, content)
            .setFields("id,name,webViewLink,webContentLink")
            .execute();
        return uploadFile;
    }


    /**
     * 누구나 볼수 있게 공유
     * @param drive
     * @param fileId
     * @throws IOException
     */
    public static void setWebShareAnyoneReader(Drive drive, String fileId) throws IOException {
        
        Permission permission = new Permission()
            .setType("anyone")
            .setRole("reader");

        drive.permissions().create(fileId, permission).execute();
    }
}
