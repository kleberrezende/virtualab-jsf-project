package br.com.virtualab.app.controller.Abstract;

import br.com.virtualab.utils.VirtualabUtils;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.util.Assert;

public class AbstractReportController extends AbstractController {

    private static final long serialVersionUID = 1L;

    private static final Map<String, JasperReport> jrCache = new HashMap<String, JasperReport>();

    protected JasperPrint createReport(final String fileName,
            final Collection<Map<String, ?>> values) {
        return createReport(fileName, new HashMap<String, Object>(), values);
    }

    protected JasperPrint createReport(final String fileName,
            final Map<String, Object> params,
            final Collection<Map<String, ?>> values) {

        String resourceName = getJasperPathReport() + fileName + ".jasper";
        JasperReport jasperReport = jrCache.get(resourceName);

        if (jasperReport == null) {
            InputStream is = AbstractReportController.class.getResourceAsStream(resourceName);

            if (is == null) {
                throw new IllegalArgumentException(String.format("Não foi possível localizar o relatório. (%s)", fileName));
            } else {
                try {
                    jasperReport = (JasperReport) JRLoader.loadObject(is);
                } catch (JRException e) {
                    throw new IllegalArgumentException(String.format("Erro ao carregar o relatório. (%s)", fileName));
                }
                jrCache.put(resourceName, jasperReport);
            }
        }

        Assert.notNull(jasperReport);
        Assert.notNull(params);
        Assert.notNull(values);

        params.put("DATA_HORA", VirtualabUtils.formatDateTime("dd/MM/yyyy 'as' HH:mm"));

        try {
            return JasperFillManager.fillReport(jasperReport, params,
                    new JRMapCollectionDataSource(values));
        } catch (Exception e) {
            logger.debug(e.getMessage());
            throw new IllegalArgumentException(String.format("Não foi possível preencher o relatório. (%s)", fileName));
        }
    }

    protected String getJasperPathReport() {
        return "/relatorios/";
    }

}
