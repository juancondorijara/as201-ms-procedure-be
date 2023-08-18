package com.ms_procedure.service.impl;

import com.ms_procedure.feignclient.*;
import com.ms_procedure.model.*;
import com.ms_procedure.repository.ProcedureRepository;
import com.ms_procedure.service.ProcedureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;
import lombok.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

//CAMBIO SPRING JAVA MAIL
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProcedureServiceImpl extends Conexion implements ProcedureService {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Autowired
    private DocumentsAttachmentsFeignClient documentsAttachmentsFeignClient;

    @Autowired
    private StudentFeignClient studentFeignClient;

    @Autowired
    private PersonFeignClient personFeignClient;

    @Override
    public Flux<Procedure> findAll() {
        log.info("Mostrando todas los procedimientos");
        //return procedureRepository.findAll();
        Flux<Procedure> list = procedureRepository.findAll().publishOn(Schedulers.boundedElastic());
        return findProcedureTransacction(list);
    }

    @Override
    public Mono<Procedure> findById(Integer id) {
        log.info("Procedimiento encontrado con el ID = " + id);
        return procedureRepository.findById(id);
    }

    @Override
    public Flux<Procedure> findByBatchNull() {
        log.info("Mostrando todos los procedimientos NULL");
        //return procedureRepository.findByBatchNull();
        Flux<Procedure> list = procedureRepository.findByBatchNull().publishOn(Schedulers.boundedElastic());
        return findProcedureTransacction(list);
    }

    @Override
    public Flux<Procedure> findByBatchNotNull() {
        log.info("Mostrando todas los procedimientos NOT NULL");
        //return procedureRepository.findByBatchNotNull();
        Flux<Procedure> list = procedureRepository.findByBatchNotNull().publishOn(Schedulers.boundedElastic());
        return findProcedureTransacction(list);
    }

    @Override
    public Flux<Procedure> findByPhaseId(Integer phaseId) {
        log.info("Procedimiento encontrado con el phase_id = " + phaseId);
        //return procedureRepository.findByPhaseId(phase_id);
        Flux<Procedure> list = procedureRepository.findByPhaseId(phaseId).publishOn(Schedulers.boundedElastic());
        return findProcedureTransacction(list);
    }

    @Override
    public Mono<Procedure> save(Procedure procedure) {
        log.info("Procedimiento creado = " + procedure.toString());
        return procedureRepository.save(procedure);
    }

    @Override
    public Mono<Procedure> update(Procedure procedure) {
        log.info("Procedimiento actualizado = " + procedure.toString());
        return procedureRepository.save(procedure);
    }

    @Override
    public Mono<ResponseEntity<Procedure>> consolidateEnUno(Integer id) {
        Integer batch = findLastBatch();
        log.info("Procedimiento consolidado = " + id + " con el lote = " + batch);
        return procedureRepository.findById(id).flatMap(newProcedure -> {
            newProcedure.setPhaseId(5);
            newProcedure.setBatch(batch);
            return procedureRepository.save(newProcedure);
        }).map(updatedProcedure -> new ResponseEntity<>(updatedProcedure, HttpStatus.OK)).defaultIfEmpty(new ResponseEntity<>(HttpStatus.OK));
    }

    @Override
    public void consolidatePhaseId4(Integer id1, Integer id2, Integer id3) {
        Integer batch = findLastBatch();
        String sql = "UPDATE procedure SET phase_id=?, batch=? WHERE id=? OR id=? OR id=?";
        try {
            PreparedStatement ps = Conexion.conectarMsProcedure().prepareStatement(sql);
            ps.setInt(1, 5);
            ps.setInt(2, batch);
            ps.setLong(3, id1);
            ps.setLong(4, id2);
            ps.setLong(5, id3);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en consolidatePhaseId4 " + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    @Override
    public void consolidatePhaseId5(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        String sql = "UPDATE procedure SET phase_id=? WHERE id=? OR id=? OR id=? OR id=? OR id=?";
        try {
            PreparedStatement ps = Conexion.conectarMsProcedure().prepareStatement(sql);
            ps.setInt(1, 6);
            ps.setLong(2, id1);
            ps.setLong(3, id2);
            ps.setLong(4, id3);
            ps.setLong(5, id4);
            ps.setLong(6, id5);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en consolidatePhaseId5 " + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    @Override
    public void consolidatePhaseId6(Integer id1, Integer id2, Integer id3, Integer id4, Integer id5) {
        String sql = "UPDATE procedure SET phase_id=? WHERE id=? OR id=? OR id=? OR id=? OR id=?";
        try {
            PreparedStatement ps = Conexion.conectarMsProcedure().prepareStatement(sql);
            ps.setInt(1, 7);
            ps.setLong(2, id1);
            ps.setLong(3, id2);
            ps.setLong(4, id3);
            ps.setLong(5, id4);
            ps.setLong(6, id5);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            System.out.println("Error en consolidatePhaseId6 " + e.getMessage());
        } finally {
            this.cerrar();
        }
    }

    @Override
    public Flux<Procedure> findDocumentsAttachmentsByProcedureId(Integer procedureId) {
        log.info("Documentos adjuntos encontrados con ID de la procedimiento = " + procedureId);
        return procedureRepository.findByProcedureId(procedureId);
    }

    public Flux<Procedure> findProcedureTransacction(Flux<Procedure> list) {
        return list.concatMap(Flux::just).publishOn(Schedulers.boundedElastic()).flatMap(dataProcedure -> {
            Flux<DocumentsAttachments> documentsAttachments = documentsAttachmentsFeignClient.findDocumentsAttachmentsByProcedureId(dataProcedure.getId());

            Mono<Student> student = studentFeignClient.findStudentById(dataProcedure.getStudentId());

            Mono<Person> person = personFeignClient.findPersonById(dataProcedure.getPersonId());

            return Flux.zip(documentsAttachments, student, person).flatMap(tuple -> {
                dataProcedure.setAttached1(tuple.getT1().getAttached());
                dataProcedure.setAttached2(tuple.getT1().getAttached());
                dataProcedure.setAttached3(tuple.getT1().getAttached());
                dataProcedure.setAttached4(tuple.getT1().getAttached());

                dataProcedure.setInstitutionalEmail(tuple.getT2().getInstitutional_email());

                dataProcedure.setPersonName(tuple.getT3().getName()+", "+ tuple.getT3().getLastname());
                return Flux.just(dataProcedure);
            });
        } );
    }


    //para jhianpol
    public Flux<Procedure> findProcedureTransacctionMono(Flux<Procedure> list) {
        return list.concatMap(Flux::just).publishOn(Schedulers.boundedElastic()).flatMap(dataProcedure -> {

            Mono<Student> student = studentFeignClient.findStudentById(dataProcedure.getStudentId());

            Mono<Person> person = personFeignClient.findPersonById(dataProcedure.getPersonId());

            return Flux.zip(student, person).flatMap(tuple -> {

                dataProcedure.setInstitutionalEmail(tuple.getT1().getInstitutional_email());

                dataProcedure.setPersonName(tuple.getT2().getName()+", "+ tuple.getT2().getLastname());
                return Flux.just(dataProcedure);
            });
        } );
    }


    public Integer findLastBatch() {
        Integer batch = 0;
        String sql = "SELECT MAX(batch) as batch FROM procedure";
        try {
            PreparedStatement ps = Conexion.conectarMsProcedure().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                batch = rs.getInt("batch");
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error en findLastBatch " + e.getMessage());
        } finally {
            this.cerrar();
        }
        return batch + 1;
    }

    //Metodo Principal de Uso Java Mail
    @Override
    public void notificationByEmail(String institutionalEmail) throws Exception {
        String remitente = "juangabrielcondorijara@gmail.com";
        String clave = "yyevveacqppbagud";
        String destinatario = institutionalEmail;

        String asunto = "Rechazo de Documento(s) Certidigital";
        String cuerpo = "Corregir, el DNI esta borroso";
        try {
            configurationEmail(remitente, clave, destinatario, asunto, cuerpo);
        } catch (MessagingException ex) {
            System.out.println("Error en enviarEmail_S " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void notificationByEmail2(Procedure procedure) throws Exception {
        String remitente = "juangabrielcondorijara@gmail.com";
        String clave = "yyevveacqppbagud";
        String destinatario = procedure.getInstitutionalEmail();

        String asunto = "Rechazo de Documento(s) Certidigital";
        String cuerpo = procedure.getMessage();
        try {
            configurationEmail(remitente, clave, destinatario, asunto, cuerpo);
        } catch (MessagingException ex) {
            System.out.println("Error en enviarEmail_S " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void notificationByEmail3(String institutionalEmail, String message) throws Exception {
        String remitente = "juangabrielcondorijara@gmail.com";
        String clave = "yyevveacqppbagud";
        String destinatario = institutionalEmail;

        String asunto = "Rechazo de Documento(s) Certidigital";
        String cuerpo = message;
        try {
            configurationEmail(remitente, clave, destinatario, asunto, cuerpo);
        } catch (MessagingException ex) {
            System.out.println("Error en enviarEmail_S " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void configurationEmail(String remitente, String clave, String destinatario, String asunto, String cuerpo) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(asunto);

            BodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo);
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);
            message.setContent(multiParte);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException ex) {
            System.out.println("Error en configurationEmail " + ex.getMessage());
        }
    }

    //YA NO
    public Flux<Procedure> findProcedureTransacction2(Flux<Procedure> list) {
        return list.concatMap(Flux::just).publishOn(Schedulers.boundedElastic()).map(dataProcedure -> {

            Flux<DocumentsAttachments> documentsAttachments = documentsAttachmentsFeignClient.findDocumentsAttachmentsByProcedureId(dataProcedure.getId());
            documentsAttachments.subscribe(getDocumentsAttachments -> dataProcedure.setAttached1(getDocumentsAttachments.getAttached()));
            documentsAttachments.subscribe(getDocumentsAttachments -> dataProcedure.setAttached2(getDocumentsAttachments.getAttached()));
            documentsAttachments.subscribe(getDocumentsAttachments -> dataProcedure.setAttached3(getDocumentsAttachments.getAttached()));
            documentsAttachments.subscribe(getDocumentsAttachments -> dataProcedure.setAttached4(getDocumentsAttachments.getAttached()));

            Mono<Student> student = studentFeignClient.findStudentById(dataProcedure.getStudentId());
            student.subscribe(getStudent -> dataProcedure.setInstitutionalEmail(getStudent.getInstitutional_email()));

            Mono<Person> person = personFeignClient.findPersonById(dataProcedure.getPersonId());
            person.subscribe(getPerson -> dataProcedure.setPersonName(getPerson.getName() + " " + getPerson.getLastname()));

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return dataProcedure;
        });
    }

    //CAMBIO SPRING JAVA MAIL
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void springNotificationByEmail(String institutionalEmail) {
        log.info("Notificación enviada a " + institutionalEmail);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date(System.currentTimeMillis());
        String dateSystem = dateFormat.format(currentDate);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(institutionalEmail);
            helper.setSubject("¡CERTIDIGITAL NOTIFICACIÓN!");
            helper.setText("text/html", "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div style=\"font-family: Arial, Helvetica, sans-serif; background-color: #C12E2E; height: 6%;\">\n" +
                    "            <h2 style=\"text-align: center; color: #FFFFFF; font-size: 160%;\">CERTIDIGITAL NOTIFICATION RECHAZO DE DOCUMENTO(S)</h2>\n" +
                    "        </div>\n" +
                    "        <div style=\"font-family: Arial, Helvetica, sans-serif; height: 20%; line-height: 2px;\">\n" +
                    "            <h3 style=\"text-align: center;\">Hola estimado usuario</h3>\n" +
                    "            <p style=\"text-align: center; color: #C12E2E; font-size: 120%;\">" + institutionalEmail + "</p>\n" +
                    "            <br>\n" +
                    "            <p style=\"text-align: center; font-size: 120%;\">Te informamos que hubo un rechazado de documento(s) en tu solicitud de Título Profesional en la etapa 4 Consolidación " + "</p>\n" +
                    "            <br>\n" +
                    "            <p style=\"text-align: center; font-size: 120%;\">Motivo y/o razón: Corregir el DNI, esta borroso</p>\n" +

                    "    <div class=\"container\">\n" +
                    "        <div style=\"font-family: Arial, Helvetica, sans-serif; height: 45%; text-align: center;\">\n" +
                    "            <img src=\"https://png.pngtree.com/png-clipart/20230427/original/pngtree-rejected-icon-png-image_9115832.png\"\n" +
                    "                alt=\"rejected\" width=\"300px\" height=\"300px\">\n" +
                    "        </div>\n" +
                    "    </div>\n" +

                    "            <p style=\"text-align: center; font-size: 120%;\">Fecha</p>\n" +
                    "            <p style=\"text-align: center\">" + dateSystem + "</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "    <hr>\n" +
                    "    <br>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div style=\"font-family: Arial, Helvetica, sans-serif; height: 45%; text-align: center;\">\n" +
                    "            <img src=\"https://upload.wikimedia.org/wikipedia/commons/2/21/Logo_del_Ministerio_de_Educaci%C3%B3n_del_Per%C3%BA_-_MINEDU.png\"\n" +
                    "                alt=\"logo-certidigital\" width=\"150px\">\n" +
                    "            <br>\n" +
                    "            <h3 style=\"line-height: 0.5px;\">CERTIDIGITAL</h3>\n" +
                    "            <P style=\"font-size: small;\">Sistema de Certificados</P>\n" +
                    "            <hr>\n" +
                    "            <br>\n" +
                    "            <div class=\"container\">\n" +
                    "                <a href=\"tel:949222062\" style=\"text-decoration: none;\">\n" +
                    "                    <p><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/phone-icon-2x.png\"\n" +
                    "                            width=\"11px\" style=\"background-color: #C12E2E;\"> <span style=\"color: #000000; font-size: 90%;\"> 949 222 062</span></p>\n" +
                    "                </a>\n" +
                    "                <a href=\"mailto:Certidigitaleducativo@gmail.com\" style=\"text-decoration: none;\">\n" +
                    "                    <p><img src=\"https://cdn2.hubspot.net/hubfs/53/tools/email-signature-generator/icons/email-icon-2x.png\"\n" +
                    "                        width=\"12px\" style=\"background-color: #C12E2E;\"><span style=\"color: #000000; font-size: 90%;\"> Certidigitaleducativo@gmail.com</span></p>\n" +
                    "                </a>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>");
            mailSender.send(mimeMessage);
        }catch (Exception e){
            System.out.println("Error en sm: " + e);
        }
    }



}
