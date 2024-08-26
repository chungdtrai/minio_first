package com.chung.first_project;

import com.chung.first_project.entity.*;
import com.chung.first_project.repository.*;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Slf4j
@SpringBootApplication
public class FirstProjectApplication implements CommandLineRunner {
    @Autowired
    FileDownUpRepository fileDownUpRepository;
    @Autowired
    LogSignatureImageRepository logSignatureImageRepository;
    @Autowired
    LogOrObjectAttachmentRepository logOrObjectAttachmentRepository;
    @Autowired
    LogOrIndAttachmentRepsoritory logOrIndAttachmentRepsoritory;
    @Autowired
    LogAdmActivityProcessRepository logAdmActivityProcessRepository;
    @Autowired
    LogParentRepository logParentRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Value("${minio_url}")
    private String minioUrl;
    @Value("${minio_user}")
    private String minioUser;
    @Value("${minio_pass}")
    private String minioPass;
    @Value("${bucket_name_to}")
    private String bucketNameTo;
    @Value("${bucket_name_from}")
    private String bucketNameFrom;

    public static void main(String[] args) {
        SpringApplication.run(FirstProjectApplication.class, args);
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//        dataSource.setUrl("jdbc:oracle:thin:@10.159.10.90:1521/pdblris_test");
//        dataSource.setUsername("ioc");
//        dataSource.setPassword("ioc#1368");
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        try {
//            MinioClient minioClient = new MinioClient("storage-egov-bcqg-test.vnpt.vn", "minio", "bWluaW8tZWdvdi1iY3FnLXRlc3Q=");
////            minioClient.copyObject("87654321","kysoTM_734808_10614475_20240703_zodsq6781_2rd3f8lx2.pdf", "lris", "56/kysoTM_734808_10614475_20240703_zodsq6781_2rd3f8lx2.pdf");
//            String sql = "select TENANT_ID,SIGN_FILE from ioc_data01.sig_signature where sign_file is not null and TENANT_ID=56 and rownum = 1 order by SIG_ID DESC";
//            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
//
//            for(Map<String, Object> map : data) {
//                String fileName = map.get("SIGN_FILE").toString();
//                String folderName = map.get("TENANT_ID").toString();
//                System.out.println(fileName);
//                System.out.println("---------------------");
//                try {
//                    ObjectStat objectStat = minioClient.statObject("87654321", fileName);
//                    minioClient.copyObject("87654321",fileName, "lris", folderName+"/"+fileName);
//                    fileDownUpRepository.save(
//
//                    )
//                } catch (Exception e) {
//
//                }
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void run(String... args) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//        dataSource.setUrl("jdbc:oracle:thin:@10.159.10.90:1521/pdblris_test");
//        dataSource.setUsername("ioc");
//        dataSource.setPassword("ioc#1368");
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        try {
            MinioClient minioClient = new MinioClient(minioUrl, minioUser, minioPass);
            String sql_SIG_SIGNATURE = "select TENANT_ID, SIGN_FILE from ioc_data01.SIG_SIGNATURE where SIGN_FILE is not null";
            moveObject(sql_SIG_SIGNATURE, minioClient, new FileDownUpLog(), fileDownUpRepository);

            String sql_SIG_SIGNATURE_IMAGE = "select TENANT_ID, IMG_NAME from ioc_Data01.SIG_SIGNATURE_IMAGE where IMG_NAME is not null";
            moveObject(sql_SIG_SIGNATURE_IMAGE, minioClient, new LogSignatureImage(),logSignatureImageRepository);

            String sql_OR_OBJECT_ATTACHMENT= "select TENANT_ID, ATTACHMENT_NAME from ioc_data01.OR_OBJECT_ATTACHMENT where ATTACHMENT_NAME is not null";
            moveObject(sql_OR_OBJECT_ATTACHMENT, minioClient, new LogOrObjectAttachment(),logOrObjectAttachmentRepository);

            String sql_OR_IND_ATTACHMENT = "select TENANT_ID, ATTACHMENT_NAME from ioc_data01.OR_IND_ATTACHMENT where ATTACHMENT_NAME is not null";
            moveObject(sql_OR_IND_ATTACHMENT, minioClient, new LogOrIndAttachment(), logOrIndAttachmentRepsoritory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends LogParent>void moveObject(String sql, MinioClient minioClient, LogParent logParent ,LogParentRepository logParentRepository) {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        Set<String> keys = data.get(0).keySet();
        String columeName = null;
        for(String item:keys){
            if(!item.equals("TENANT_ID")) columeName = item;
        }
        for(Map<String, Object> map : data) {
            logParent = returnNewLogObject(logParent);
            String fileName = map.get(columeName).toString();
            String folderName = map.get("TENANT_ID").toString();
            System.out.println(fileName);
            System.out.println("---------------------");
            try {
                minioClient.statObject(bucketNameFrom, fileName);
                minioClient.copyObject(bucketNameFrom,fileName, bucketNameTo, folderName+"/"+fileName);
                logParent.setTenant_id(Long.valueOf(folderName));
                logParent.setFile_name(fileName);
                logParent.setStatus(1);
                logParent.setMessage("successfull");
                logParentRepository.save(logParent);
            } catch (Exception e) {
                logParent.setTenant_id(Long.valueOf(folderName));
                logParent.setFile_name(fileName);
                logParent.setStatus(0);
                logParent.setMessage("error " + e.getMessage());
                logParentRepository.save(logParent);
            }
        }
    }
    public LogParent returnNewLogObject(Object object){
        if(object instanceof FileDownUpLog){
            return new FileDownUpLog();
        }else if(object instanceof LogAdmActivityProcess){
            return new LogAdmActivityProcess();
        }else if(object instanceof LogOrIndAttachment){
            return new LogOrIndAttachment();
        }else if(object instanceof LogOrObjectAttachment){
            return new LogOrObjectAttachment();
        }else {
            return new LogSignatureImage();
        }
    }
//    public void saveCheckUpDown(String fileName, String folderName, Integer check) {
//        try{
//            fileDownUpRepository.save(FileDownUpLog.builder()
//                    .tenant_id(Long.valueOf(folderName))
//                    .sign_file(fileName)
//                    .status(check)
//                    .build());
//            System.out.println("cập nhật bảng đánh dấu thành công");
//        }catch (Exception e){
//            System.out.println("không cập nhật được file đã tải");
//        }
//    }
//
//    public void deleteFile(String file){
//        File files = new File(file);
//        if (files.exists()) {
//            files.delete();
//            System.out.println("File deleted successfully.");
//        } else {
//            System.out.println("File not found.");
//        }
//    };
//
//    public String downloadFile(String fileName){
//        OkHttpClient client = new OkHttpClient();
//        String url =url_dowload + fileName;;
//        String url_save = "D:/downloadfile/" + fileName;
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                // Lấy ResponseBody
//                ResponseBody responseBody = response.body();
//
//                if (responseBody != null) {
//                    // Đọc nội dung vào file
//                    File file = new File(url_save);
//                    try (InputStream inputStream = responseBody.byteStream();
//                         FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//
//                        // Đọc từ InputStream và ghi vào FileOutputStream
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            fileOutputStream.write(buffer, 0, bytesRead);
//                        }
//                        System.out.println("File downloaded successfully: " + file.getAbsolutePath());
//                    }
//                }
//            } else {
//                System.out.println("Failed to download file: " + response.message());
//            }
//
//        } catch (IOException e) {
//            System.out.println("File không tồn tại");
//            return "error";
//        }
//        return url_save;
//    }
//    public void uploadFile(String url_upload,String folderName) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        // Đường dẫn tới tệp cần tải lên
//        File file = new File(url_upload);
//
//        // Xây dựng RequestBody dưới dạng Multipart
//        RequestBody requestBody = new MultipartBuilder()
//                .type(MultipartBuilder.FORM)
//                .addFormDataPart("file", file.getName(),
//                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
//                .build();
//
//        // Tạo yêu cầu POST với Multipart body
//        Request request = new Request.Builder()
//                .url("http://10.159.10.29:8087/uploadFile?bucketName=lris/" + folderName)
//                .post(requestBody)
//                .build();
//
//        // Thực hiện yêu cầu
//        Response response = null;
//        try {
//            // Thực hiện yêu cầu và nhận phản hồi
//            response = client.newCall(request).execute();
//
//            if (response.isSuccessful()) {
//                System.out.println("File uploaded successfully: " + response.body().string());
//            } else {
//                System.out.println("Upload failed: " + response.code());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            // Đảm bảo rằng Response được đóng để giải phóng tài nguyên
//            if (response != null) {
//                response.body().close(); // Đóng body để giải phóng tài nguyên
//            }
//        }
//    }
}
