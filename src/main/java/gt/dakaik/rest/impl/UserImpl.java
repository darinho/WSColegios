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
import gt.dakaik.rest.repository.LicenceTypeRepository;
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
import gt.entities.LicenceType;
import gt.entities.Licences;
import gt.entities.Person;
import gt.entities.Profile;
import gt.entities.School;
import gt.entities.Status;
import gt.entities.User;
import gt.entities.UserProfile;
import gt.entities.UserSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dario Calderon
 */
@Component
public class UserImpl implements WSUser {

    Logger eLog = LoggerFactory.getLogger(this.getClass());
    TokenImpl tokenI = new TokenImpl();
    @Autowired
    UserRepository repoU;
    @Autowired
    UserProfileRepository repoUProfile;
    @Autowired
    SchoolRepository repoSchool;
    @Autowired
    LicenceRepository repoLic;
    @Autowired
    LicenceTypeRepository repoLicType;
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
            String pwd = user.getTxtPwd() != null ? user.getTxtPwd() : "UsCollage" + person.getIdPerson();
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
        LicenceType licType = repoLicType.findOne(pro.getLicence().getLicenceType().getId());
        if (licType == null) {
            mpResp.put("message", "msj_no_existe");
            mpResp.put("valor", "LicenceType");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }
        Licences licence = repoLic.findTopBySchoolAndLicenceTypeAndStatusOrderByDatesAsc(school, licType, Status.A);

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
        repoUProfile.save(up);
        licence.setStatus(Status.U);
        repoLic.save(licence);
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

        UserSession sesion = new UserSession(null, u, token, new Date(), new Date());

        rs.save(sesion);
        sesion.setUser(u);
        sesion.setToken(token);

        return new ResponseEntity(sesion, HttpStatus.OK);
    }

}
