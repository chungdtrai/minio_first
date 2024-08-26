package com.chung.first_project.upload_download;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class Download_Upload {
    public static void main(String[] args) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate();
//        String sql = "SELECT * FROM ioc_data01.sig_signature WHERE sign_file IS NOT NULL ORDER BY SIG_ID DESC";
//        Map<String, Object> data = jdbcTemplate.queryForMap(sql);
//        System.out.println(data.get("SIG_ID"));
    }

//
//    public static void downloadFile(){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://10.159.10.29:8087/files/kysoTM_734808_10614475_20240703_zodsq6781_2rd3f8lx2.pdf")
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            System.out.println(response.body().string());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void uploadFile() throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//        // Đường dẫn tới tệp cần tải lên
//        File file = new File("C:/Users/HP/Desktop/project/category_2.csv");
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
//                .url("http://10.159.10.29:8087/uploadFile?bucketName=lris/56")
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
