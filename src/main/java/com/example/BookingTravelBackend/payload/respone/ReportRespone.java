package com.example.BookingTravelBackend.payload.respone;

import com.example.BookingTravelBackend.entity.Report;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class ReportRespone {
    private int id;
    private String reportType;
    private String description;
    private String status;
    private Timestamp reportedAt;
    private UserInfoResponse reportedBy;
    private UserInfoResponse reportedUser;
    private PostResponse postResponse;
    public ReportRespone(Report report) {
        this.id = report.getId();
        this.reportType = report.getReportType();
        this.description = report.getDescription();
        this.status = report.getStatus();
        this.reportedAt = report.getReportedAt();
        this.reportedBy = new UserInfoResponse(report.getReportedBy());
        this.reportedUser = new UserInfoResponse(report.getReportedUser());
        this.postResponse = new PostResponse(report.getPost());
    }
}
