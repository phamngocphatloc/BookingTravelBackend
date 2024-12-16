package com.example.BookingTravelBackend.util;

import com.example.BookingTravelBackend.Configuration.WebConfig;
import com.example.BookingTravelBackend.entity.RequesttoCreateHotel;
import com.example.BookingTravelBackend.entity.RequesttoCreatePartner;
import com.example.BookingTravelBackend.payload.respone.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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


    public static String getBookingDetailsEmail(BillResponse billResponse) {
        // Lấy thông tin từ BillResponse
        String firstName = billResponse.getFirstName();
        String lastName = billResponse.getLastName();
        int id = billResponse.getId();
        String phone = billResponse.getPhone();
        int price = billResponse.getPrice();
        Date createdAt = billResponse.getCreatedAt();
        Date checkIn = billResponse.getCheckIn();
        Date checkOut = billResponse.getCheckOut();
        String status = billResponse.getStatus();
        UserInfoResponse userCreate = billResponse.getUserCreate();
        List<BookingResponse> bookingDetails = billResponse.getBooking();

        // Định dạng lại ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String createdAtFormatted = dateFormat.format(createdAt);
        String checkInFormatted = dateFormat.format(checkIn);
        String checkOutFormatted = dateFormat.format(checkOut);

        // Tạo nội dung email
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
                .append("<html lang=\"vi\">")
                .append("<head>")
                .append("    <meta charset=\"UTF-8\">")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("    <title>Thông Tin Đặt Phòng</title>")
                .append("    <style>")
                .append("        body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append("        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                .append("        .header { background-color: #FF5733; color: white; text-align: center; padding: 20px; font-size: 24px; }")
                .append("        .content { padding: 20px; line-height: 1.6; color: #333333; }")
                .append("        .footer { background-color: #f1f1f1; text-align: center; padding: 10px; font-size: 14px; color: #666666; }")
                .append("        .info-table { width: 100%; margin: 20px 0; border-collapse: collapse; }")
                .append("        .info-table th, .info-table td { padding: 10px; border: 1px solid #ddd; text-align: left; }")
                .append("        .info-table th { background-color: #f2f2f2; }")
                .append("    </style>")
                .append("</head>")
                .append("<body>")
                .append("    <div class=\"email-container\">")
                .append("        <div class=\"header\">")
                .append("            Thông Tin Đặt Phòng")
                .append("        </div>")
                .append("        <div class=\"content\">")
                .append("            <p>Xin chào <strong>").append(firstName).append(" ").append(lastName).append("</strong>,</p>")
                .append("            <p>Chúng tôi xin gửi lời cảm ơn vì đã chọn dịch vụ của chúng tôi. Dưới đây là thông tin chi tiết về đặt phòng của bạn:</p>")
                .append("            <table class=\"info-table\">")
                .append("                <tr><th>Mã Đặt Phòng</th><td>").append(id).append("</td></tr>")
                .append("                <tr><th>Họ và Tên</th><td>").append(firstName).append(" ").append(lastName).append("</td></tr>")
                .append("                <tr><th>Số Điện Thoại</th><td>").append(phone).append("</td></tr>")
                .append("                <tr><th>Tổng Giá</th><td>").append(price).append(" VND</td></tr>")
                .append("                <tr><th>Ngày Đặt Phòng</th><td>").append(createdAtFormatted).append("</td></tr>")
                .append("            </table>")
                .append("            <h3>Chi Tiết Đặt Phòng:</h3>")
                .append("            <table class=\"info-table\">")
                .append("                <tr><th>Ngày Nhận Phòng (Check-in)</th><td>").append(checkInFormatted).append("</td></tr>")
                .append("                <tr><th>Ngày Trả Phòng (Check-out)</th><td>").append(checkOutFormatted).append("</td></tr>")
                .append("                <tr><th>Trạng Thái Đặt Phòng</th><td>").append(status).append("</td></tr>")
                .append("            </table>")
                .append("            <h3>Danh Sách Phòng Đã Đặt:</h3>")
                .append("            <ul>");

        // Duyệt qua danh sách các phòng đã đặt và thêm vào email
        for (BookingResponse booking : bookingDetails) {
            emailContent.append("                <li>Phòng: ").append(booking.getRoomBooking().getRoomName()).append("</li>");
        }

        emailContent.append("            </ul>")
                .append("            <h3>Thông Tin Người Đặt Phòng:</h3>")
                .append("            <table class=\"info-table\">")
                .append("                <tr><th>Người Đặt Phòng</th><td>").append(userCreate.getFullname()).append("</td></tr>")
                .append("                <tr><th>Số Điện Thoại Người Đặt</th><td>").append(userCreate.getPhone()).append("</td></tr>")
                .append("            </table>")
                .append("            <p>Chân thành cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi. Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ thêm, đừng ngần ngại liên hệ với chúng tôi.</p>")
                .append("            <p>Trân trọng,<br>Đội ngũ Booking</p>")
                .append("        </div>")
                .append("        <div class=\"footer\">")
                .append("            <p>© 2024 Booking Travel. Mọi quyền được bảo lưu.</p>")
                .append("        </div>")
                .append("    </div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }

    public static String getPartnerApprovalEmail(RequesttoCreatePartner partnerResponse) {
        // Lấy thông tin từ PartnerResponse
        String partnerName = partnerResponse.getHotelName();
        String email = partnerResponse.getEmail();
        String phone = "0352 207 847";
        Date approvedDate = new Date();

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String approvedDateFormatted = dateFormat.format(approvedDate);

        // Tạo nội dung email
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
                .append("<html lang=\"vi\">")
                .append("<head>")
                .append("    <meta charset=\"UTF-8\">")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("    <title>Chúc Mừng Đối Tác</title>")
                .append("    <style>")
                .append("        body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append("        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                .append("        .header { background-color: #28a745; color: white; text-align: center; padding: 20px; font-size: 24px; }")
                .append("        .content { padding: 20px; line-height: 1.6; color: #333333; }")
                .append("        .footer { background-color: #f1f1f1; text-align: center padding: 10px; font-size: 14px; color: #666666; }")
                .append("    </style>")
                .append("</head>")
                .append("<body>")
                .append("    <div class=\"email-container\">")
                .append("        <div class=\"header\">")
                .append("            Chúc Mừng Quý Đối Tác!")
                .append("        </div>")
                .append("        <div class=\"content\">")
                .append("            <p>Kính gửi <strong>").append(partnerName).append("</strong>,</p>")
                .append("            <p>Chúng tôi rất vui mừng thông báo rằng yêu cầu hợp tác của quý công ty đã được chấp thuận thành công vào ngày ").append(approvedDateFormatted).append(".</p>")
                .append("            <p>Quý công ty hiện đã trở thành đối tác chính thức của chúng tôi. Chúng tôi cam kết hỗ trợ quý công ty trong mọi hoạt động hợp tác để cùng nhau phát triển bền vững.</p>")
                .append("            <p>Vui lòng liên hệ qua số điện thoại <strong>").append(phone).append("</strong> hoặc email <strong>").append(email).append("</strong> nếu cần thêm thông tin.</p>")
                .append("            <p>Trân trọng,</p>")
                .append("            <p><strong>Đội ngũ quản lý đối tác</strong></p>")
                .append("        </div>")
                .append("        <div class=\"footer\">")
                .append("            <p>© 2024 Công Ty Của Bạn. Mọi quyền được bảo lưu.</p>")
                .append("        </div>")
                .append("    </div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }

    public static String getPartnerRejectionEmail(RequesttoCreatePartner partnerResponse) {
        // Lấy thông tin từ PartnerResponse
        String partnerName = partnerResponse.getHotelName();
        String email = partnerResponse.getEmail();
        String phone = "0352 207 847";
        Date requestDate = new Date();

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String requestDateFormatted = dateFormat.format(requestDate);

        // Tạo nội dung email
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
                .append("<html lang=\"vi\">")
                .append("<head>")
                .append("    <meta charset=\"UTF-8\">")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("    <title>Thông Báo Từ Chối Đối Tác</title>")
                .append("    <style>")
                .append("        body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append("        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                .append("        .header { background-color: #dc3545; color: white; text-align: center; padding: 20px; font-size: 24px; }")
                .append("        .content { padding: 20px; line-height: 1.6; color: #333333; }")
                .append("        .footer { background-color: #f1f1f1; text-align: center; padding: 10px; font-size: 14px; color: #666666; }")
                .append("    </style>")
                .append("</head>")
                .append("<body>")
                .append("    <div class=\"email-container\">")
                .append("        <div class=\"header\">")
                .append("            Thông Báo Từ Chối Yêu Cầu Hợp Tác")
                .append("        </div>")
                .append("        <div class=\"content\">")
                .append("            <p>Kính gửi <strong>").append(partnerName).append("</strong>,</p>")
                .append("            <p>Chúng tôi rất tiếc phải thông báo rằng yêu cầu hợp tác của quý công ty, gửi ngày ").append(requestDateFormatted).append(", không được chấp thuận.</p>")
                .append("            <p>Lý do từ chối: <em>").append("Chúng Tôi Chưa Đủ Cơ Sỡ Hợp Tác Với Bạn").append("</em></p>")
                .append("            <p>Chúng tôi hy vọng có thể hợp tác với quý công ty trong tương lai. Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ qua số điện thoại <strong>").append(phone).append("</strong> hoặc email <strong>").append(email).append("</strong>.</p>")
                .append("            <p>Trân trọng,</p>")
                .append("            <p><strong>Đội ngũ quản lý đối tác</strong></p>")
                .append("        </div>")
                .append("        <div class=\"footer\">")
                .append("            <p>© 2024 Công Ty Của Bạn. Mọi quyền được bảo lưu.</p>")
                .append("        </div>")
                .append("    </div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }

    public static String getHotelApprovalEmail(RequesttoCreateHotel partnerResponse) {
        // Lấy thông tin từ PartnerResponse
        String partnerName = partnerResponse.getPartner().getHotelName();
        String email = partnerResponse.getPartner().getEmail();
        String phone = "0352 207 847";
        Date approvedDate = new Date();

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String approvedDateFormatted = dateFormat.format(approvedDate);

        // Tạo nội dung email
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
                .append("<html lang=\"vi\">")
                .append("<head>")
                .append("    <meta charset=\"UTF-8\">")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("    <title>Chúc Mừng Đối Tác</title>")
                .append("    <style>")
                .append("        body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append("        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                .append("        .header { background-color: #28a745; color: white; text-align: center; padding: 20px; font-size: 24px; }")
                .append("        .content { padding: 20px; line-height: 1.6; color: #333333; }")
                .append("        .footer { background-color: #f1f1f1; text-align: center padding: 10px; font-size: 14px; color: #666666; }")
                .append("    </style>")
                .append("</head>")
                .append("<body>")
                .append("    <div class=\"email-container\">")
                .append("        <div class=\"header\">")
                .append("            Chúc Mừng Quý Đối Tác Thêm Khách Sạn Thành Công!")
                .append("        </div>")
                .append("        <div class=\"content\">")
                .append("            <p>Kính gửi <strong>").append(partnerName).append("</strong>,</p>")
                .append("            <p>Chúng tôi rất vui mừng thông báo rằng yêu cầu hợp tác của quý công ty đã được chấp thuận thành công vào ngày ").append(approvedDateFormatted).append(".</p>")
                .append("            <p>Quý công ty hiện đã trở thành đối tác chính thức của chúng tôi. Chúng tôi cam kết hỗ trợ quý công ty trong mọi hoạt động hợp tác để cùng nhau phát triển bền vững.</p>")
                .append("            <p>Vui lòng liên hệ qua số điện thoại <strong>").append(phone).append("</strong> hoặc email <strong>").append(email).append("</strong> nếu cần thêm thông tin.</p>")
                .append("            <p>Trân trọng,</p>")
                .append("            <p><strong>Đội ngũ quản lý đối tác</strong></p>")
                .append("        </div>")
                .append("        <div class=\"footer\">")
                .append("            <p>© 2024 Công Ty Của Bạn. Mọi quyền được bảo lưu.</p>")
                .append("        </div>")
                .append("    </div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }

    public static String getHotelRejectionEmail(RequesttoCreateHotel partnerResponse) {
        // Lấy thông tin từ PartnerResponse
        String partnerName = partnerResponse.getPartner().getHotelName();
        String email = partnerResponse.getPartner().getEmail();
        String phone = "0352 207 847";
        Date requestDate = new Date();

        // Định dạng ngày tháng
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String requestDateFormatted = dateFormat.format(requestDate);

        // Tạo nội dung email
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
                .append("<html lang=\"vi\">")
                .append("<head>")
                .append("    <meta charset=\"UTF-8\">")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
                .append("    <title>Thông Báo Từ Chối Thêm Khách Sạn</title>")
                .append("    <style>")
                .append("        body { font-family: Arial, sans-serif; background-color: #f9f9f9; margin: 0; padding: 0; }")
                .append("        .email-container { max-width: 600px; margin: 20px auto; background-color: #ffffff; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }")
                .append("        .header { background-color: #dc3545; color: white; text-align: center; padding: 20px; font-size: 24px; }")
                .append("        .content { padding: 20px; line-height: 1.6; color: #333333; }")
                .append("        .footer { background-color: #f1f1f1; text-align: center; padding: 10px; font-size: 14px; color: #666666; }")
                .append("    </style>")
                .append("</head>")
                .append("<body>")
                .append("    <div class=\"email-container\">")
                .append("        <div class=\"header\">")
                .append("            Thông Báo Từ Chối Yêu Cầu Thêm Khách Sạn")
                .append("        </div>")
                .append("        <div class=\"content\">")
                .append("            <p>Kính gửi <strong>").append(partnerName).append("</strong>,</p>")
                .append("            <p>Chúng tôi rất tiếc phải thông báo rằng yêu cầu hợp tác của quý công ty, gửi ngày ").append(requestDateFormatted).append(", không được chấp thuận.</p>")
                .append("            <p>Lý do từ chối: <em>").append("Chúng Tôi Chưa Đủ Cơ Sỡ Hợp Tác Với Bạn").append("</em></p>")
                .append("            <p>Chúng tôi hy vọng có thể hợp tác với quý công ty trong tương lai. Nếu có bất kỳ câu hỏi nào, vui lòng liên hệ qua số điện thoại <strong>").append(phone).append("</strong> hoặc email <strong>").append(email).append("</strong>.</p>")
                .append("            <p>Trân trọng,</p>")
                .append("            <p><strong>Đội ngũ quản lý đối tác</strong></p>")
                .append("        </div>")
                .append("        <div class=\"footer\">")
                .append("            <p>© 2024 Công Ty Của Bạn. Mọi quyền được bảo lưu.</p>")
                .append("        </div>")
                .append("    </div>")
                .append("</body>")
                .append("</html>");

        return emailContent.toString();
    }



}

