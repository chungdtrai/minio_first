package com.chung.first_project;

import com.chung.first_project.entity.*;
import com.chung.first_project.repository.*;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@SpringBootApplication
public class FirstProjectApplication implements CommandLineRunner {
    @Autowired
//    FileDownUpRepository fileDownUpRepository;
//    LogSignatureImageRepository logSignatureImageRepository;
//    LogOrObjectAttachmentRepository logOrObjectAttachmentRepository;
//    LogOrIndAttachmentRepsoritory logOrIndAttachmentRepsoritory;
    LogAdmActivityProcessRepository logAdmActivityProcessRepository;
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
//                    minioClient.copyObject(bucketNameFrom,fileName, bucketNameTo, folderName+"/"+fileName);
            String sql = "select TENANT_ID, FILE_PATH from ADM_ACTIVITY_PROCESS where FILE_PATH is not null";
            List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);

            for(Map<String, Object> map : data) {
                String fileName = map.get("FILE_PATH").toString();
                String folderName = map.get("TENANT_ID").toString();
                System.out.println(fileName);
                System.out.println("---------------------");
                try {
                    ObjectStat objectStat = minioClient.statObject(bucketNameFrom, fileName);
                    minioClient.copyObject(bucketNameFrom,fileName, bucketNameTo, folderName+"/"+fileName);
//                    ObjectStat objectStat = minioClient.statObject(bucketNameFrom, "1609331059953_HDSD_Bo_Tinh_Huyen(5).docx");
//                    minioClient.copyObject("87654321","1609331059953_HDSD_Bo_Tinh_Huyen(5).docx", "lris", "56/1609331059953_HDSD_Bo_Tinh_Huyen(5).docx");
                    logAdmActivityProcessRepository.save(LogAdmActivityProcess.builder()
                                    .tenant_id(Long.valueOf(folderName))
                                    .file_path(fileName)
                                    .status(1)
                                    .message("successfull")
                            .build());
                } catch (Exception e) {
                    logAdmActivityProcessRepository.save(LogAdmActivityProcess.builder()
                            .tenant_id(Long.valueOf(folderName))
                            .file_path(fileName)
                            .status(0)
                            .message("error " + e.getMessage())
                            .build());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void moveObject(String sql, MinioClient minioClient, LogParent logParent, JpaRepository jpaRepository) {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql);
        Set<String> keys = new HashSet<>();
        String columeName = null;
        for(String item:keys){
            if(!item.equals("TENANT_ID")) columeName = item;
        }
        for(Map<String, Object> map : data) {
            String fileName = map.get(columeName).toString();
            String folderName = map.get("TENANT_ID").toString();
            System.out.println(fileName);
            System.out.println("---------------------");
            try {
                ObjectStat objectStat = minioClient.statObject(bucketNameFrom, fileName);
                minioClient.copyObject(bucketNameFrom,fileName, bucketNameTo, folderName+"/"+fileName);
//                    ObjectStat objectStat = minioClient.statObject(bucketNameFrom, "1609331059953_HDSD_Bo_Tinh_Huyen(5).docx");
//                    minioClient.copyObject("87654321","1609331059953_HDSD_Bo_Tinh_Huyen(5).docx", "lris", "56/1609331059953_HDSD_Bo_Tinh_Huyen(5).docx");
                logAdmActivityProcessRepository.save(LogAdmActivityProcess.builder()
                        .tenant_id(Long.valueOf(folderName))
                        .file_path(fileName)
                        .status(1)
                        .message("successfull")
                        .build());
            } catch (Exception e) {
                logAdmActivityProcessRepository.save(LogAdmActivityProcess.builder()
                        .tenant_id(Long.valueOf(folderName))
                        .file_path(fileName)
                        .status(0)
                        .message("error " + e.getMessage())
                        .build());
            }
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
