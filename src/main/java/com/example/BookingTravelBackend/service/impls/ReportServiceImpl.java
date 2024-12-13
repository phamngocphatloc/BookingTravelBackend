package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.PostRepository;
import com.example.BookingTravelBackend.Repository.ReportRepository;
import com.example.BookingTravelBackend.entity.Post;
import com.example.BookingTravelBackend.entity.Report;
import com.example.BookingTravelBackend.payload.respone.ReportRespone;
import com.example.BookingTravelBackend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor

public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final PostRepository postRepository;

    @Override
    public List<ReportRespone> getReport() {
        List<Report> listReport = reportRepository.findAllPending();
        List<ReportRespone> reportResponeList = new ArrayList<>();
        if (listReport != null) {
            for (Report report : listReport) {
                ReportRespone reportRespone = new ReportRespone(report);
                reportResponeList.add(reportRespone);
            }
        }
        return reportResponeList;
    }

    @Override
    public ReportRespone ReportProcessing(int reportId, String status) {
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new RuntimeException("Report Not Found"));
        if(!report.getStatus().equalsIgnoreCase("pending")){
            throw new RuntimeException("Report Status is not pending");
        }
        report.setStatus(status);
        if (status.equalsIgnoreCase("success")) {
            Post post = report.getPost();
            if (post == null) {
                throw new RuntimeException("Post Not Found");
            }
            post.setDelete(true);
            postRepository.save(post);
        }
        Report reportSaved = reportRepository.save(report);
        return new ReportRespone(reportSaved);
    }


}
