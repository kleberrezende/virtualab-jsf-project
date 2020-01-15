package br.com.virtualab.utils;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class JasperJsfUtils {

    private static final long serialVersionUID = 1L;

    private static final String reportPath = "/relatorios_temp/";

    public static String getContextPathReport() {
        return VirtualabUtils.getContextPath() + reportPath;
    }

    private static String gerarIdRelatorio() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
        Date dt = new Date(System.currentTimeMillis());
        return df.format(dt);
    }

    public static String exportToPdf(JasperPrint print, String filename, String empresa) throws JRException, SQLException {
        removerArquivosRelatorios();
        filename = gerarIdRelatorio() + "_" + empresa + "_" + filename + ".pdf";
        JasperExportManager.exportReportToPdfFile(print, getContextPathReport() + filename);
        return reportPath + filename;
    }

    public static String exportToHtml(JasperPrint print, String filename, String empresa) throws JRException, SQLException {
        removerArquivosRelatorios();
        filename = gerarIdRelatorio() + "_" + empresa + "_" + filename + ".html";
        JasperExportManager.exportReportToHtmlFile(print, getContextPathReport() + filename);
        return reportPath + filename;
    }

    private static void removerArquivosRelatorios() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd_");

        Date dt = new Date();
        String maskDateHoje = df.format(dt);

        dt = VirtualabUtils.retornaDataMaisDias(new Date(), -1);
        String maskDateOntem = df.format(dt);

        File f = new File(getContextPathReport());
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (File file : files) {
                if (!file.getName().startsWith(maskDateHoje)
                        && !file.getName().startsWith(maskDateOntem)) {
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
    }

}
