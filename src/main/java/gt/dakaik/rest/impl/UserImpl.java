/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.rest.impl;

import gt.dakaik.common.Common;
import gt.dakaik.common.CommonEncripta;
import gt.dakaik.dto.DTOSession;
import gt.dakaik.exceptions.EntidadDuplicadaException;
import gt.dakaik.exceptions.EntidadNoEncontradaException;
import gt.dakaik.exceptions.GeneralException;
import gt.dakaik.rest.interfaces.WSUser;
import gt.dakaik.rest.repository.CityRepository;
import gt.dakaik.rest.repository.DocumentRepository;
import gt.dakaik.rest.repository.DocumentTypeRepository;
import gt.dakaik.rest.repository.LicenceRepository;
import gt.dakaik.rest.repository.MenuProfileRepository;
import gt.dakaik.rest.repository.PersonRepository;
import gt.dakaik.rest.repository.ProfileRepository;
import gt.dakaik.rest.repository.SchoolRepository;
import gt.dakaik.rest.repository.UserProfileRepository;
import gt.dakaik.rest.repository.UserRepository;
import gt.dakaik.rest.repository.UserSessionRepository;
import gt.entities.Address;
import gt.entities.City;
import gt.entities.Document;
import gt.entities.DocumentType;
import gt.entities.Licences;
import gt.entities.Person;
import gt.entities.Profile;
import gt.entities.School;
import gt.entities.Status;
import gt.entities.User;
import gt.entities.UserProfile;
import gt.entities.UserSession;
import gt.megapaca.fileprocessing.CompressImages;
import gt.megapaca.fileprocessing.SFTPinJava;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Dario Calderon
 */
@Component
public class UserImpl implements WSUser {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRepository repoU;
    @Autowired
    MenuProfileRepository repoMenu;
    @Autowired
    UserProfileRepository repoUProfile;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    LicenceRepository repoLic;
    @Autowired
    CityRepository repoCity;
    @Autowired
    ProfileRepository repoProfile;
    @Autowired
    UserSessionRepository rs;
    @Autowired
    PersonRepository repoPerson;
    @Autowired
    DocumentRepository repoDocument;
    @Autowired
    DocumentTypeRepository repoDocumentType;

    private Boolean valideSession(Long idUser, String token, Long idSession, Long idProfile, Long idMenu) {
        UserSession us = rs.findOne(idSession);
        if (us == null || !Objects.equals(us.getUser().getIdUser(), idUser) || !us.getToken().equals(token)) {
            return false;
        }

        Long ifExistP = repoMenu.findByAccessProfile(idMenu, idProfile);

        return ifExistP != null && ifExistP > 0;

    }

    @Override
    public ResponseEntity<User> findById(int idUsuario, String token, Long id) throws EntidadNoEncontradaException {
        User u = repoU.findOne(id);

        if (u != null) {
            return new ResponseEntity(u, HttpStatus.OK);
        } else {
            throw new EntidadNoEncontradaException("Entity User");
        }
    }

    @Override
    public ResponseEntity<User> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoU.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> doCreate(UserProfile pro, int idUsuario, String token) throws EntidadDuplicadaException, EntidadNoEncontradaException {
        User user = pro.getUser();
        User u = repoU.findByTxtUser(user.getTxtUser());
        Boolean isUser = true;
        String pwd = "";
        Map<String, String> mpResp = new HashMap<>();
        if (u == null) {
            u = new User();

            if (user.getPerson() == null) {
                return new ResponseEntity("msj_person_required", HttpStatus.CONFLICT);
            }

            if (user.getPerson().getDocuments().isEmpty()) {
                throw new EntidadDuplicadaException("msj_document_required");
            }
            List<Document> doctos = new ArrayList<>();
            for (Document d : user.getPerson().getDocuments()) {
//            for (Document d : doctos) {
                if (d == null || d.getTxtDocument().equals("")) {
                    throw new EntidadDuplicadaException("msj_document_required");
                }
                Document doc = repoDocument.findByTxtDocument(d.getTxtDocument());
                if (doc != null) {
                    throw new EntidadDuplicadaException("msj_document");
                }
                DocumentType dt = repoDocumentType.findOne(d.getDocumentType().getIdDocumentType());
                if (dt == null) {
                    return new ResponseEntity("msj_document_type_required", HttpStatus.CONFLICT);
                }

                doc = new Document();
                doc.setDocumentType(dt);
                doc.setTxtDocument(d.getTxtDocument());
                doc.setSnActive(true);
                doctos.add(doc);
            }

            Person p = new Person();

            p.setTxtFullName(user.getPerson().getTxtFullName());
            p.setTxtFirstNameF(user.getPerson().getTxtFirstNameF());
            p.setTxtFirstNameS(user.getPerson().getTxtFirstNameS());
            p.setTxtFirstNameT(user.getPerson().getTxtFirstNameT());
            p.setTxtLastNameF(user.getPerson().getTxtLastNameF());
            p.setTxtLastNameS(user.getPerson().getTxtLastNameS());
            p.setTxtLastNameT(user.getPerson().getTxtLastNameT());
            p.setSnActive(true);
            Address ad = new Address();
            ad.setIntZone(user.getPerson().getAddress().getIntZone());
            ad.setTxtColony(user.getPerson().getAddress().getTxtColony());
            ad.setTxtIndications(user.getPerson().getAddress().getTxtIndications());
            ad.setTxtNumberHouse(user.getPerson().getAddress().getTxtNumberHouse());

            City ct = repoCity.findOne(user.getPerson().getAddress().getCity().getIdCity());
            if (ct == null) {
                throw new EntidadNoEncontradaException("Entity City");
            }

            ad.setCity(ct);
            p.setAddress(ad);
            Person person = repoPerson.save(p);

            doctos.stream().forEach((d) -> {
                d.setPerson(person);
            });

            repoDocument.save(doctos);
            pwd = user.getTxtPwd() != null ? user.getTxtPwd() : "UsCollage" + person.getIdPerson();
            u.setLastDatePwd(new Date());
            u.setIntDaysChangePwd(user.getIntDaysChangePwd() == 0 ? 30 : user.getIntDaysChangePwd());
            u.setSnChangePwd(false);
            u.setIdUser(user.getIdUser());
            u.setTxtPwd(CommonEncripta.get_md5(pwd));
            u.setTxtUser(user.getTxtUser());
            u.setSnActive(true);
            u.setPerson(person);

            repoU.save(u);
            isUser = false;
        }

        UserProfile up = new UserProfile();

        Profile profile = repoProfile.findOne(pro.getProfile().getIdProfile());

        if (profile == null) {
            mpResp.put("message", "msj_no_existe");
            mpResp.put("valor", "profile");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }
        up.setProfile(profile);

        School school = repoSchool.findOne(pro.getLicence().getSchool().getIdSchool());
        if (school == null) {
            mpResp.put("message", "msj_no_existe");
            mpResp.put("valor", "school");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }
        Licences licence = repoLic.findTopBySchoolAndProfileAndStatusOrderByDatesAsc(school, profile, Status.A);

        if (licence == null) {
            mpResp.put("message", "msj_no_hay_licencias");
            mpResp.put("valor", "Licence");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }

        up.setLicence(licence);
        up.setIdUserProfile(school.getIdSchool());

        if (isUser) {
            UserProfile upro = repoUProfile.findByLicenceYUserYProfile(school, u, profile);
            if (upro != null) {
                throw new EntidadDuplicadaException("User");
            }
        }

        up.setUser(u);
        up.setSnActive(true);
        repoUProfile.save(up);
        licence.setStatus(Status.U);
        repoLic.save(licence);
        String contentEmail = Common.getTemplateSendEmailRegister(u.getTxtUser(), profile.getTxtDescription(), school.getTxtName(), licence.getTxtLicence(), pwd);
        /*try {
            MimeMessage mail = Common.createEmail(u.getTxtUser(), "NO-RESPONDER Bienvenido a Colegios GT", contentEmail);
            Common.sen
            Common.sendEmail("NO-RESPONDER Bienvenido a Colegios GT", contentEmail, u.getTxtUser());
        } catch (Exception e) {
        }*/
        return new ResponseEntity(repoU.findOne(u.getIdUser()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> doUpdate(int idUsuario, String token, User user) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        User u = repoU.findOne(user.getIdUser());
        if (u == null) {
            throw new EntidadNoEncontradaException("User");
        }
        Person p = u.getPerson();

        p.setTxtFullName(user.getPerson().getTxtFullName());
        u.setTxtPwd(CommonEncripta.get_md5(user.getTxtPwd()));

        return new ResponseEntity(repoU.save(u), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<User> onDelete(int idUsuario, String token, Long idUser) throws EntidadNoEncontradaException {
        User u = repoU.findOne(idUser);
        if (u == null) {
            throw new EntidadNoEncontradaException("User");
        }
        u.setSnActive(Boolean.FALSE);

        return new ResponseEntity(repoU.save(u), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DTOSession> login(String sUsuario, String pwds) throws GeneralException {
        User u = repoU.findByTxtUser(sUsuario);

        if (u == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        String pwd = CommonEncripta.get_md5(pwds);

        if (!u.getTxtPwd().equals(pwd)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        List<UserSession> sessions = rs.getValidUsuarioSesions(u);
        List<UserSession> closeSessions = new ArrayList<>();
        sessions.stream().map((us) -> {
            us.setEndDate(new Date());
            return us;
        }).forEach((us) -> {
            closeSessions.add(us);
        });

        rs.save(closeSessions);
        String token = Common.getToken();

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(u.getLastDatePwd());

        long iDias = Common.daysBetween(cal2, cal1);

        if (iDias > (u.getIntDaysChangePwd() == 0 ? 60 : u.getIntDaysChangePwd())) {
            u.setSnChangePwd(false);
        }

        UserSession sesion = new UserSession(null, u.getIdUser(), token, new Date(), new Date());
        rs.save(sesion);
        DTOSession ses = new DTOSession(sesion.getIdUserSession(), u, u.getIdUser(), sesion.getToken(), sesion.getStartDate(), sesion.getEndDate());

        List<UserProfile> uprof = repoUProfile.findByUserAndSnActiveTrue(u);

        if (uprof.isEmpty()) {
            throw new GeneralException("No Profile Available");
        } else if (uprof.size() == 1) {
            ses.setUserProfile(uprof.get(0));
        } else {
            ses.setUserProfiles(uprof);
        }

        return new ResponseEntity(ses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DTOSession> getByIdSesion(Long idUsuario, String token, Long idSession) throws GeneralException {

        UserSession sesion = rs.findOne(idSession);

        if (sesion == null || !Objects.equals(sesion.getUser().getIdUser(), idUsuario) || !sesion.getToken().equals(token)) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        User u = sesion.getUser();

        DTOSession ses = new DTOSession(sesion.getIdUserSession(), u, u.getIdUser(), sesion.getToken(), sesion.getStartDate(), sesion.getEndDate());

        List<UserProfile> uprof = repoUProfile.findByUserAndSnActiveTrue(u);

        if (uprof.isEmpty()) {
            throw new GeneralException("No Profile Available");
        } else if (uprof.size() == 1) {
            ses.setUserProfile(uprof.get(0));
        } else {
            ses.setUserProfiles(uprof);
        }

        return new ResponseEntity(ses, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> handleFileUpload(Long idUser, MultipartFile file) throws EntidadNoEncontradaException {
        User u = repoU.findOne(idUser);
        if (u != null) {
            if (!file.isEmpty()) {
                try {
                    File convFile = new File(file.getOriginalFilename());
                    convFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(convFile);
                    fos.write(file.getBytes());
                    fos.close();
                    CompressImages cImage = new CompressImages(convFile, "img");
                    List<File> files = cImage.getFamilyFixedProcessedFiles();
                    System.out.println(files.size() + " files to upload.");

                    //************
                    String SFTPHOST = "www.colegios.e.gt";
                    int SFTPPORT = 1157;
                    String SFTPUSER = "colegiose";
                    String SFTPPASS = "Dario2015.";
                    String SFTPWORKINGDIR = "/home/colegiose/public_html/images/u/";
                    String PARENT_PATH = "" + idUser;
                    SFTPinJava.saveFileToSftp(files, false, SFTPHOST, SFTPPORT, SFTPUSER, SFTPPASS, SFTPWORKINGDIR, PARENT_PATH);
                    //************
                    u.setTxtImageURI("http://www.colegios.e.gt/images/u/" + idUser + "/");
                    repoU.save(u);
                    return new ResponseEntity(u.getTxtImageURI(), HttpStatus.OK);
                } catch (Exception e) {
                    e.getMessage();
                    throw new EntidadNoEncontradaException();
                }
            } else {
                throw new EntidadNoEncontradaException();
            }
        } else {
            throw new EntidadNoEncontradaException("User");
        }
    }

}
