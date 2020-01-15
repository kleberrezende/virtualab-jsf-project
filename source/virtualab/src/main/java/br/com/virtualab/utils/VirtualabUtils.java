package br.com.virtualab.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import javax.faces.context.FacesContext;
import javax.swing.text.MaskFormatter;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class VirtualabUtils {

    private static final long serialVersionUID = 1L;
    private static final Locale local = new Locale("pt", "BR");

    /**
     * Verifica se a string é nula ou vazia
     *
     * @param value:String
     * @return verdadeiro ou falso
     */
    public static boolean stringIsEmpty(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o valor é nulo ou menor igual a zero
     *
     * @param value:Long
     * @return verdadeiro ou falso
     */
    public static boolean longIsEmpty(Long value) {
        if (value == null || value <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o valor é nulo ou menor igual a zero
     *
     * @param value:Integer
     * @return verdadeiro ou falso
     */
    public static boolean integerIsEmpty(Integer value) {
        if (value == null || value <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica se o valor é nulo ou menor igual a zero
     *
     * @param value:BigDecimal
     * @return verdadeiro ou falso
     */
    public static boolean bigDecimalIsEmpty(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna string sem acentos
     *
     * @param str:String
     * @return String
     */
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Retorna string com apenas numeros
     *
     * @param str:String
     * @return String
     */
    public static String apenasNumeros(String str) {
        if (stringIsEmpty(str)) {
            return "0";
        } else {
            return str.replaceAll("[^0-9]", "");
        }
    }

    /**
     * Retorna long com apenas numeros
     *
     * @param str:String
     * @return Long
     */
    public static Long apenasNumerosToLong(String str) {
        if (stringIsEmpty(apenasNumeros(str))) {
            return (long) 0;
        } else {
            return Long.valueOf(apenasNumeros(str));
        }
    }

    /**
     * Retorna integer com apenas numeros
     *
     * @param str:String
     * @return Integer
     */
    public static Integer apenasNumerosToInteger(String str) {
        if (stringIsEmpty(apenasNumeros(str))) {
            return 0;
        } else {
            return Integer.valueOf(apenasNumeros(str));
        }
    }

    /**
     * Retorna string com apenas texto
     *
     * @param str:String
     * @return String
     */
    public static String apenasTexto(String str) {
        if (stringIsEmpty(str)) {
            return null;
        } else {
            return str.replaceAll("[^a-zA-Z0-9]", "");
        }
    }

    /**
     * Retorna string preenchida pelo filler a esquerda
     *
     * @param valueToPad:String
     * @param filler:String
     * @param size:int
     * @return String
     */
    public static String lpad(String valueToPad, String filler, int size) {
        while (valueToPad.length() < size) {
            valueToPad = filler + valueToPad;
        }
        return valueToPad;
    }

    /**
     * Retorna string preenchida pelo filler a direita
     *
     * @param valueToPad:String
     * @param filler:String
     * @param size:int
     * @return String
     */
    public static String rpad(String valueToPad, String filler, int size) {
        while (valueToPad.length() < size) {
            valueToPad = valueToPad + filler;
        }
        return valueToPad;
    }

    /**
     * Retorna string com telefone com mascara
     *
     * @param numero:String
     * @return String
     */
    public static String telefoneMask(String numero) {
        String parte1, parte2, codarea;
        //12 3456 7890
        if (numero.length() >= 3 && numero.length() <= 6) {
            codarea = numero.substring(0, 2);
            parte1 = numero.substring(2);
            numero = "(" + codarea + ") " + parte1;
        } else if (numero.length() >= 7 && numero.length() <= 10) {
            codarea = numero.substring(0, 2);
            parte1 = numero.substring(2, 6);
            parte2 = numero.substring(6);
            numero = "(" + codarea + ") " + parte1 + "-" + parte2;
        } else if (numero.length() == 11) {
            codarea = numero.substring(0, 2);
            parte1 = numero.substring(2, 7);
            parte2 = numero.substring(7);
            numero = "(" + codarea + ") " + parte1 + "-" + parte2;
        }

        return numero;
    }

    /**
     * Retorna string com cep com mascara
     *
     * @param cep:int
     * @return String
     */
    public static String cepMask(int cep) {
        String cepR = String.format("%08d", cep);
        cepR = cepR.substring(0, 2) + "." + cepR.substring(2, 5) + "-" + cepR.substring(5);
        return cepR;
    }

    /**
     * Retorna string com uma nova data formatada
     *
     * @param format:String
     * @return String
     */
    public static String formatDateTime(String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date dt = new Date(System.currentTimeMillis());
        return df.format(dt);
    }

    /**
     * Retorna string com data formatada
     *
     * @param format:String
     * @param data:Date
     * @return String
     */
    public static String formatDateTime(String format, Date data) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(data);
    }

    /**
     * Retorna data com horas zeradas
     *
     * @param data:Date
     * @return Date
     */
    public static Date zerarHorasData(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Retorna quantidade de anos entre a data inicial e data final
     *
     * @param dataInicial:Calendar
     * @param dataFinal:Calendar
     * @return int
     */
    public static int retornaAnosEntreDuasDatas(Calendar dataInicial, Calendar dataFinal) {
        int age = dataFinal.get(Calendar.YEAR) - dataInicial.get(Calendar.YEAR);

        dataInicial.add(Calendar.YEAR, age);

        if (dataFinal.before(dataInicial)) {
            age--;
        }
        return age;
    }

    /**
     * Retorna quantidade de dias entre a data inicial e data final
     *
     * @param dataInicial:Calendar
     * @param dataFinal:Calendar
     * @return int
     */
    public static int retornaDiasEntreDuasDatas(Calendar dataInicial, Calendar dataFinal) {
        return (int) ((dataFinal.getTimeInMillis() - dataInicial.getTimeInMillis()) / (24 * 60 * 60 * 1000));
    }

    /**
     * Retorna data acrescida de dias
     *
     * @param data:Date
     * @param dias:int
     * @return Date
     */
    public static Date retornaDataMaisDias(Date data, int dias) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + dias);

        return c.getTime();
    }

    /**
     * Retorna data acrescida de meses
     *
     * @param data:Date
     * @param meses:int
     * @return Date
     */
    public static Date retornaDataMaisMeses(Date data, int meses) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + meses);

        return c.getTime();
    }

    /**
     * Retorna data acrescida de anos
     *
     * @param data:Date
     * @param anos:int
     * @return Date
     */
    public static Date retornaDataMaisAnos(Date data, int anos) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + anos);

        return c.getTime();
    }

    /**
     * Retorna texto do dia da semana de uma data
     *
     * @param data:Date
     * @return String
     */
    public static String retornaDiaSemanaStr(Date data) {
        Calendar c = new GregorianCalendar();
        c.setTime(data);
        return new DateFormatSymbols().getWeekdays()[c.get(Calendar.DAY_OF_WEEK)];
    }

    /**
     * Retorna inteiro do dia da semana de uma data
     *
     * @param data:Date
     * @return int
     */
    public static int retornaDiaSemanaInteiro(Date data) {
        Calendar c = new GregorianCalendar();
        c.setTime(data);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Retorna string com valor formatado
     *
     * @param valor:BigDecimal
     * @return String
     */
    public static String decimalToStr(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(getLocal()));
        return df.format(valor);
    }

    /**
     * Retorna string com valor formatado com 3 casas decimais
     *
     * @param valor:BigDecimal
     * @return String
     */
    public static String decimalToStr3Decimais(BigDecimal valor) {
        DecimalFormat df = new DecimalFormat("#,##0.000", new DecimalFormatSymbols(getLocal()));
        return df.format(valor);
    }

    /**
     * Retorna string com valor formatado
     *
     * @param valor:Integer
     * @return String
     */
    public static String inteiroToStr(Integer valor) {
        if (valor == null) {
            valor = 0;
        }
        DecimalFormat nf = new DecimalFormat("#,##0", new DecimalFormatSymbols(getLocal()));
        return nf.format(valor);
    }

    /**
     * Retorna string com valor formatado
     *
     * @param valor:Long
     * @return String
     */
    public static String inteiroToStr(Long valor) {
        if (valor == null) {
            valor = Long.valueOf(0);
        }
        DecimalFormat nf = new DecimalFormat("#,##0", new DecimalFormatSymbols(getLocal()));
        return nf.format(valor);
    }

    /**
     * Retorna string com Texto formatado
     *
     * @param pattern:String
     * @param value:Object
     * @return String
     */
    public static String formatObjToStringMask(String pattern, Object value) {
        try {
            MaskFormatter mask;
            mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Retorna o local de internacionalização
     *
     * @return Locale
     */
    public static Locale getLocal() {
        return local;
    }

    /**
     * Retorna o contexto da aplicação
     *
     * @return String
     */
    public static String getContextName() {
        return FacesContext.getCurrentInstance().getExternalContext().getContextName();
    }

    /**
     * Retorna o caminho root da aplicação
     *
     * @return String
     */
    public static String getRootPath() {
        String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
        return realPath.substring(0, realPath.lastIndexOf("/" + getContextName()));
    }

    /**
     * Retorna o caminho do contexto da aplicação
     *
     * @return String
     */
    public static String getContextPath() {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath("");
    }

    /**
     * Retorna string do alfabeto mais numérico aleatório
     *
     * @param tamanho:int
     * @return String
     */
    public static String getAlfaNumericoAleatorio(int tamanho) {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVYWXZ0123456789";
        Random random = new Random();
        String codigo = "";
        int index;
        for (int i = 1; i <= tamanho; i++) {
            index = random.nextInt(letras.length());
            codigo += letras.substring(index, index + 1);
        }
        return codigo;
    }

    /**
     * Evniar e-mail
     *
     * @param hostName:String
     * @param porta:int
     * @param fromEmail:String
     * @param paraList:ArrayList
     * @param assunto:String
     * @param msg:String
     * @param username:String
     * @param senha:String
     * @param sslRequerido:boolean
     * @throws org.apache.commons.mail.EmailException
     */
    public static void sendEmail(String hostName, int porta, String fromEmail,
            ArrayList<String> paraList, String assunto, String msg, String username, String senha,
            boolean sslRequerido) throws EmailException {
        SimpleEmail email = new SimpleEmail();
        email.setFrom(fromEmail);
        for (String para : paraList) {
            email.addTo(para);
        }
        email.setSubject(assunto);
        email.setMsg(msg);

        if (sslRequerido) {
            email.setSslSmtpPort(String.valueOf(porta));
        } else {
            email.setSmtpPort(porta);
        }
        email.setHostName(hostName);
        email.setStartTLSEnabled(sslRequerido);
        email.setSSLOnConnect(sslRequerido);
        email.setSSLCheckServerIdentity(sslRequerido);
        email.setStartTLSRequired(sslRequerido);
        email.setAuthentication(username, senha);
        email.setDebug(false);
        email.send();
    }

    /**
     * Evniar e-mail de virtualab
     *
     * @param paraList:ArrayList
     * @param assunto:String
     * @param msg:String
     * @throws org.apache.commons.mail.EmailException
     */
    public static void sendEmailDeVirtualab(ArrayList<String> paraList, String assunto, String msg) throws EmailException {
        sendEmail("smtp.gmail.com", 465, "noreplay.virtualab@gmail.com", paraList, assunto, msg,
                "noreplay.virtualab@gmail.com", "VLg12l2015", true);
    }

    /*Calculos de porcentagem*/
    /**
     * Retorna int com a porcentagem inteira do calculo de dois inteiros,
     * Exemplo: (135, 31) = 23% | Cálculo: 31 / 135 * 100 = 23 | Próva: 135 *
     * 23% = 31
     *
     * @param total:int
     * @param atual:int
     * @return int
     */
    public static int calcularPorcentagemInteiro(int total, int atual) {
        BigDecimal totalBD = new BigDecimal(total);
        BigDecimal atualBD = new BigDecimal(atual);
        atualBD = atualBD.divide(totalBD, 8, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(100));
        return atualBD.intValue();
    }

    /**
     * Retorna BigDecimal com o valor do calculo entre valor e porcentagem,
     * Exemplo: (35, 5%) = 1,75 | Cálculo: 35 * 5% = 1,75
     *
     * @param valor:BigDecimal
     * @param porcentagem:BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal calcularValorDaPorcentagem(BigDecimal valor, BigDecimal porcentagem) {
        return valor.divide(BigDecimal.valueOf(100)).multiply(porcentagem);
    }

    /**
     * Retorna BigDecimal com valor mais o valor do calculo entre valor e
     * porcentagem, Exemplo: (35, 5%) = 36,75 | Cálculo: 35 * 5% = 1,75 + 35 =
     * 36,75 | Próva: 35 + 5% = 36,75
     *
     * @param valor:BigDecimal
     * @param porcentagem:BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal calcularValorAddPorcentagem(BigDecimal valor, BigDecimal porcentagem) {
        BigDecimal valorPorcentagem = calcularValorDaPorcentagem(valor, porcentagem);
        return valor.add(valorPorcentagem);
    }

    /**
     * Retorna BigDecimal com valor menos o valor do calculo entre valor e
     * porcentagem, Exemplo: (35, 5%) = 33,25 | Cálculo: 35 * 5% = 1,75 - 35 =
     * 33,25 | Próva: 35 - 5% = 33,25
     *
     * @param valor:BigDecimal
     * @param porcentagem:BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal calcularValorSubPorcentagem(BigDecimal valor, BigDecimal porcentagem) {
        BigDecimal valorPorcentagem = calcularValorDaPorcentagem(valor, porcentagem);
        return valor.subtract(valorPorcentagem);
    }

    /**
     * Retorna BigDecimal com a porcentagem do calculo do valor maior sobre o
     * valor menor, Exemplo: (31, 135) = 335% | Cálculo: 135 / 31 - 1 = 3,36 *
     * 100 = 336% | Próva: 31 * 336% = 135
     *
     * @param valorMenor:BigDecimal
     * @param valorMaior:BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal calcularPorcentagemDoMaiorSobreMenor(BigDecimal valorMenor, BigDecimal valorMaior) {
        BigDecimal valor = valorMaior.divide(valorMenor, 8, RoundingMode.HALF_DOWN);
        valor = valor.subtract(BigDecimal.ONE);
        return valor.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_DOWN);
    }

    /**
     * Retorna BigDecimal com a porcentagem de desconto entre dois valores,
     * Exemplo: (31, 135) = 77% | Cálculo: 31 / 135 * 100 = 23 - 100 = 77% |
     * Próva: 135 - 77% = 31
     *
     * @param valorMenor:BigDecimal
     * @param valorMaior:BigDecimal
     * @return BigDecimal
     */
    public static BigDecimal calcularPorcentagemDesconto(BigDecimal valorMenor, BigDecimal valorMaior) {
        BigDecimal valor = valorMenor.divide(valorMaior, 8, RoundingMode.HALF_UP);
        valor = valor.multiply(BigDecimal.valueOf(100));
        return BigDecimal.valueOf(100).subtract(valor).setScale(2, RoundingMode.HALF_UP);
    }

}
