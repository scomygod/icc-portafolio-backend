package ec.edu.ups.icc.portafolio_backend.admin.controller;

import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories")
    public List<AdvisoryReportItem> advisorySummary() {
        return reportService.advisorySummary();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects")
    public List<ProjectReportItem> projectSummary() {
        return reportService.projectSummary();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/advisories/pdf")
    public ResponseEntity<byte[]> advisoryPdf() {
        byte[] pdf = reportService.advisoryPdf();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=advisories.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects/excel")
    public ResponseEntity<byte[]> projectExcel() {
        byte[] excel = reportService.projectExcel();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=projects.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(excel);
    }
}