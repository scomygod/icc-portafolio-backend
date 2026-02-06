package ec.edu.ups.icc.portafolio_backend.programmer.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.AdvisoryReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.dto.ProjectReportItem;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.AdvisoryStatus;
import ec.edu.ups.icc.portafolio_backend.programmer.entity.Project;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.AdvisoryRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProgrammerProfileRepository;
import ec.edu.ups.icc.portafolio_backend.programmer.repository.ProjectRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    private final AdvisoryRepository advisoryRepository;
    private final ProgrammerProfileRepository profileRepository;
    private final ProjectRepository projectRepository;

    public ReportService(AdvisoryRepository advisoryRepository,
                         ProgrammerProfileRepository profileRepository,
                         ProjectRepository projectRepository) {
        this.advisoryRepository = advisoryRepository;
        this.profileRepository = profileRepository;
        this.projectRepository = projectRepository;
    }

    public List<AdvisoryReportItem> advisorySummary() {
        return profileRepository.findAll().stream().flatMap(p ->
            List.of(AdvisoryStatus.values()).stream().map(status -> {
                long total = advisoryRepository.findByProfileId(p.getId()).stream()
                    .filter(a -> a.getStatus() == status).count();
                return new AdvisoryReportItem(p.getUser().getName(), status.name(), total);
            })
        ).toList();
    }

    public List<ProjectReportItem> projectSummary() {
        return profileRepository.findAll().stream().map(p -> {
            long total = projectRepository.findByProfileId(p.getId()).stream()
                .filter(Project::isActive).count();
            return new ProjectReportItem(p.getUser().getName(), total);
        }).toList();
    }

    public byte[] advisoryPdf() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();
            doc.add(new Paragraph("Reporte de Asesorías"));

            PdfPTable table = new PdfPTable(3);
            table.addCell("Programador");
            table.addCell("Estado");
            table.addCell("Total");
            for (AdvisoryReportItem item : advisorySummary()) {
                table.addCell(item.programmerName());
                table.addCell(item.status());
                table.addCell(String.valueOf(item.total()));
            }
            doc.add(table);
            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public byte[] projectExcel() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Proyectos");
            var header = sheet.createRow(0);
            header.createCell(0).setCellValue("Programador");
            header.createCell(1).setCellValue("Activos");

            int row = 1;
            for (ProjectReportItem item : projectSummary()) {
                var r = sheet.createRow(row++);
                r.createCell(0).setCellValue(item.programmerName());
                r.createCell(1).setCellValue(item.totalActive());
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}