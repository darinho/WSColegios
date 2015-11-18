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
import gt.entities.Person;
import gt.entities.Profile;
import gt.entities.School;
import gt.entities.User;
import gt.entities.Resources;
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
    
    private boolean validaToken(String token, int idUsuario) throws GeneralException {
        boolean resp = false;
        tokenI.setRepo(repoU, rs);
        if (tokenI.validaTokenUsuarioOperaciones(token, idUsuario)) {
            resp = true;
        }
        return resp;
    }
    
    @Override
    public ResponseEntity<User> findAll(int idUsuario, String token) throws EntidadNoEncontradaException {
        return new ResponseEntity(repoU.findAll(), HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<User> doCreate(User user, int idUsuario, String token) throws EntidadDuplicadaException, EntidadNoEncontradaException {
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
                if (d == null || d.getTxtDocument().equals("")) {
                    throw new EntidadDuplicadaException("msj_document_required");
                }
                Document doc = repoDocument.findByTxtDocument(d.getTxtDocument());
                if (doc != null) {
                    throw new EntidadDuplicadaException("msj_document");
                }
                DocumentType dt = repoDocumentType.findOne(d.getDocumentType().getId());
                if (dt == null) {
                    return new ResponseEntity("msj_document_type_required", HttpStatus.CONFLICT);
                }
                
                doc = new Document();
                doc.setDocumentType(dt);
                doc.setTxtDocument(d.getTxtDocument());
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
            Address ad = new Address();
            ad.setIntZone(user.getPerson().getAddress().getIntZone());
            ad.setTxtColony(user.getPerson().getAddress().getTxtColony());
            ad.setTxtIndications(user.getPerson().getAddress().getTxtIndications());
            ad.setTxtNumberHouse(user.getPerson().getAddress().getTxtNumberHouse());
            
            City ct = repoCity.findOne(user.getPerson().getAddress().getCity().getId());
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
            String pwd = user.getTxtPwd() != null ? user.getTxtPwd() : "UsCollage" + person.getId();
            u.setLastDatePwd(new Date());
            u.setIntDaysChangePwd(user.getIntDaysChangePwd() == 0 ? 30 : user.getIntDaysChangePwd());
            u.setSnChangePwd(false);
            u.setId(user.getId());
            u.setTxtPwd(CommonEncripta.get_md5(pwd));
            u.setTxtUser(user.getTxtUser());
            u.setSnActive(true);
            u.setPerson(person);
            
            repoU.save(u);
            isUser = false;
        }
        
        if (user.getUserProfiles().isEmpty()) {
            mpResp.put("message", "msj_falta_perfil");
            mpResp.put("valor", "userProfile");
            return new ResponseEntity(mpResp, HttpStatus.BAD_REQUEST);
        }
        
        Resources pro = user.getUserProfiles().get(0);
        Resources up = new Resources();
        
        Profile profile = repoProfile.findOne(pro.getProfile().getId());
        
        if (profile == null) {
            mpResp.put("message", "msj_no_existe");
            mpResp.put("valor", "profile");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }
        up.setProfile(profile);
        
        School school = repoSchool.findOne(pro.getSchool().getId());
        
        if (school == null) {
            mpResp.put("message", "msj_no_existe");
            mpResp.put("valor", "school");
            return new ResponseEntity(mpResp, HttpStatus.NOT_FOUND);
        }
        up.setSchool(school);
        up.setIdResource(school.getId());
        
        if (isUser) {
            Resources upro = repoUProfile.findBySchoolAndUserAndProfile(school, u, profile);
            if (upro != null) {
                throw new EntidadDuplicadaException("User");
            }
        }
        
        up.setUser(u);
        repoUProfile.save(up);
        return new ResponseEntity(repoU.findOne(u.getId()), HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<User> doUpdate(int idUsuario, String token, User user) throws EntidadNoEncontradaException, EntidadDuplicadaException {
        User u = repoU.findOne(user.getId());
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
        
        Common.cleanSessionPrev(u.getId());

        /*List<UserSession> sessions = rs.getValidUsuarioSesions(u);
         List<UserSession> closeSessions = new ArrayList<>();
         sessions.stream().map((us) -> {
         us.setEndDate(new Date());
         return us;
         }).forEach((us) -> {
         closeSessions.add(us);
         });

         rs.save(closeSessions);*/
        String token = Common.getToken();
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(u.getLastDatePwd());
        
        long iDias = Common.daysBetween(cal2, cal1);
        
        if (iDias > (u.getIntDaysChangePwd() == 0 ? 60 : u.getIntDaysChangePwd())) {
            u.setSnChangePwd(false);
        }

        /*UserSession sesion = new UserSession(0, u, token, new Date(), new Date());

         rs.save(sesion);*/
        Common.setTokenLocal(token, u.getId());
        DTOSession sesion = new DTOSession();
        sesion.setUser(u);
        sesion.setToken(token);
        
        return new ResponseEntity(sesion, HttpStatus.OK);
    }
    
}
