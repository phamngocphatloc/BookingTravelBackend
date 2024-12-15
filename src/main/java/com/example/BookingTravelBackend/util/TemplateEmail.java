package com.example.BookingTravelBackend.util;

import com.example.BookingTravelBackend.Configuration.WebConfig;

public class TemplateEmail {
    public static String getEmailUser(String email, String fullName, String link) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f9f9f9;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .email-container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            background-color: #ffffff;" +
                "            border: 1px solid #dddddd;" +
                "            border-radius: 8px;" +
                "            overflow: hidden;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            background-color: #007BFF;" +
                "            color: white;" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            font-size: 24px;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            line-height: 1.6;" +
                "            color: #333333;" +
                "        }" +
                "        .content a {" +
                "            color: white;" + // Đổi màu chữ của nút thành trắng
                "            text-decoration: none;" +
                "        }" +
                "        .footer {" +
                "            background-color: #f1f1f1;" +
                "            text-align: center;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            color: #666666;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 10px 20px;" +
                "            margin: 20px 0;" +
                "            background-color: #007BFF;" +
                "            color: white;" +
                "            text-decoration: none;" +
                "            border-radius: 4px;" +
                "            font-size: 16px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"email-container\">" +
                "        <div class=\"header\">" +
                "            Chào mừng đến với TravelBook!" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Xin chào <strong>" + fullName + "</strong>,</p>" +
                "            <p>Cảm ơn bạn đã đăng ký tài khoản! Để hoàn tất việc đăng ký, vui lòng xác minh địa chỉ email của bạn bằng cách nhấn vào nút dưới đây:</p>" +
                "            <p style=\"text-align: center;\">" +
                "                <a href=\"" + WebConfig.url + link + "\" class=\"button\">Xác minh email</a>" +
                "            </p>" +
                "            <p>Nếu bạn không tạo tài khoản, hãy bỏ qua email này.</p>" +
                "            <p>Trân trọng,<br>Đội ngũ TravelBook</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            &copy; 2024 TravelBook. Mọi quyền được bảo lưu." +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

    public static String getResetPasswordEmail(String fullName, String resetPasswordLink) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            background-color: #f9f9f9;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "        }" +
                "        .email-container {" +
                "            max-width: 600px;" +
                "            margin: 20px auto;" +
                "            background-color: #ffffff;" +
                "            border: 1px solid #dddddd;" +
                "            border-radius: 8px;" +
                "            overflow: hidden;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "        }" +
                "        .header {" +
                "            background-color: #FF5733;" + // Màu cam nổi bật cho email quên mật khẩu
                "            color: white;" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            font-size: 24px;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "            line-height: 1.6;" +
                "            color: #333333;" +
                "        }" +
                "        .content a {" +
                "            color: white;" +
                "            text-decoration: none;" +
                "        }" +
                "        .footer {" +
                "            background-color: #f1f1f1;" +
                "            text-align: center;" +
                "            padding: 10px;" +
                "            font-size: 14px;" +
                "            color: #666666;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 10px 20px;" +
                "            margin: 20px 0;" +
                "            background-color: #FF5733;" +
                "            color: white;" +
                "            text-decoration: none;" +
                "            border-radius: 4px;" +
                "            font-size: 16px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"email-container\">" +
                "        <div class=\"header\">" +
                "            Đặt Lại Mật Khẩu" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <p>Xin chào <strong>" + fullName + "</strong>,</p>" +
                "            <p>Bạn vừa yêu cầu đặt lại mật khẩu. Để tiếp tục, vui lòng nhấn vào nút dưới đây:</p>" +
                "            <p style=\"text-align: center;\">" +
                "                <a href=\"" + resetPasswordLink + "\" class=\"button\">Đặt Lại Mật Khẩu</a>" +
                "            </p>" +
                "            <p>Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này. Liên kết này sẽ hết hạn sau 24 giờ.</p>" +
                "            <p>Trân trọng,<br>Đội ngũ hỗ trợ</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            &copy; 2024 [Travelbook]. Mọi quyền được bảo lưu." +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }


}

