package com.example.BookingTravelBackend.service.impls;

import com.example.BookingTravelBackend.Repository.ReportRepository;
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
}
