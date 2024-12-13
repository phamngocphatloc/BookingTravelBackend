package com.example.BookingTravelBackend.service;

import com.example.BookingTravelBackend.entity.Report;
import com.example.BookingTravelBackend.payload.respone.ReportRespone;

import java.util.List;

public interface ReportService {
    public List<ReportRespone> getReport();
    public ReportRespone ReportProcessing(int reportId,String status);

}
